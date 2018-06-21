package br.com.instamc.poke.pokeloot;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.SpawnEntityEvent.ChunkLoad;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.TranslatableText;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokeLootClaimedEvent;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ranks.Stats;
import br.com.instamc.poke.ranks.Stats.Stat;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.managers.ProtectionManager;
import net.minecraft.util.math.BlockPos;

public class PokeLoot {

	public static void init() {
		PokeLoot listener = new PokeLoot();
		Pixelmon.EVENT_BUS.register(listener);
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, listener);
		PokeLootDB.createTables();
		ComandoAPI.enable(new CmdEditPokeLoot());
	}

	Random rand;

	public PokeLoot() {
		rand = new Random();
	}

	@Listener
	public void chunkgenerate(LoadChunkEvent ev) {
		if (rand.nextInt(3000) == 0) {

			if (ProtectionManager.getInfoAt(ev.getTargetChunk().getWorld(), ev.getTargetChunk().getPosition().getX(),
					ev.getTargetChunk().getPosition().getZ()).hasDono()) {
				return;
			}

			final int x = (ev.getTargetChunk().getPosition().getX() * 16) + new Random().nextInt(16);
			final int z = (ev.getTargetChunk().getPosition().getZ() * 16) + new Random().nextInt(16);
			final Location<World> l = getHighestBlock(ev.getTargetChunk().getWorld(), x, z);

			SchedulerUtils.runSync(new Runnable() {

				@Override
				public void run() {
					net.minecraft.world.World w = (net.minecraft.world.World) l.getExtent();
					BlockPos pos = new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ());
					w.setBlockState(pos, PixelmonBlocks.pokeChest.getDefaultState());
					net.minecraft.tileentity.TileEntity chestEntity = w.getTileEntity(pos);
					SpongeLib.getPlugin().doLog("Spawnou pokeloot em " + x + ":" + z);
					if (chestEntity == null)
						return;
					((TileEntityPokeChest) chestEntity).setVisibility(EnumPokechestVisibility.Hidden);

				}
			}, 20);

		}

	}

	public void drop(Location<World> w, ItemStack it) {

		Entity optional = w.getExtent().createEntity(EntityTypes.ITEM, w.getPosition());

		Item item = (Item) optional;
		item.offer(Keys.REPRESENTED_ITEM, it.createSnapshot());
		w.getExtent().spawnEntity(item, Cause.of(NamedCause.of("plugin", InstaPokemon.instancia)));

	}

	@Listener
	public void interact(InteractBlockEvent ev, @First Player p) {

		if (ev.getTargetBlock().getState().getType().getId().equalsIgnoreCase("pixelmon:poke_chest")) {
			ev.setCancelled(true);
			Optional<Location<World>> location = ev.getTargetBlock().getLocation();
			if (location.isPresent()) {
				net.minecraft.world.World w = (net.minecraft.world.World) location.get().getExtent();
		
		
		        location.get().removeBlock(Cause.source(InstaPokemon.getPlugin()).build());
				Stats.addStats(p.getUniqueId(), Stat.POKELOOTS, 1);
				playSound(p);

				ItemStack it = PixelmonUtils.getPokeball(EnumPokeballs.UltraBall);
				List<ItemStack> itens = PokeLootDB.podeVir();
				if (itens.size() > 0) {
					it = itens.get(new Random().nextInt(itens.size()));
				}

				drop(location.get(), it);

				String nome = InstaPokemon.getPrefixWithName(p);
				for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
					if (pOn != p) {
						InstaPokemon.sendMessage(pOn,nome+"§e achou um §b§lPokeLoot §e!");
					}
				}

				InstaPokemon.sendMessage(p, "§eVocê achou um §b§lPokeLoot§e!");

			}
		}

	}

	public void playSound(Player p) {
		p.playSound(SoundType.of("pixelmon:pixelmon.block.PokelootObtained"), p.getLocation().getPosition(), 1);
	}

	public static Location<World> getHighestBlock(World w, int x, int z) {
		for (int y = 150; y > 0; y--) {
			BlockState b = w.getBlock(x, y, z);
			if (w.getBlock(x, y, z) != null && w.getBlock(x, y, z).getType() != BlockTypes.AIR) {
				BlockType t = b.getType();
				return new Location<World>(w, x, y + 1, z);

			}
		}
		return null;
	}
}
