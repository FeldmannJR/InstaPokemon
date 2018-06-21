package br.com.instamc.poke.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.teleport.EntityTeleportCause;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent.ChunkLoad;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBox;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityEndTable;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFridge;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;

import br.com.instamc.poke.Config;
import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.dbs.PlayerDB;
import br.com.instamc.poke.dbs.PvPDB;
import br.com.instamc.poke.dbs.PlayerDB.Flag;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.events.PlayerWinBattleEvent;
import br.com.instamc.poke.seechests.SeeChestMenu;
import br.com.instamc.poke.shop.chest.ShopItem;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.library.ScoreboardManager;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.chat.ChatAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.managers.ProtectionManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;

public class GeralListener {

	List<BlockType> blocks = Arrays.asList(BlockTypes.HOPPER, BlockTypes.REDSTONE_WIRE, BlockTypes.PISTON, BlockTypes.STICKY_PISTON, BlockTypes.UNPOWERED_COMPARATOR, BlockTypes.POWERED_REPEATER, BlockTypes.UNPOWERED_REPEATER, BlockTypes.POWERED_COMPARATOR);

	@Listener
	public void redstone(ChangeBlockEvent.Place ev, @First Player p) {
		if (InstaPokemon.isOp(p)) {
			return;
		}
		for (Transaction<BlockSnapshot> v : ev.getTransactions()) {
			BlockType type = v.getFinal().getState().getType();
			if (blocks.contains(type)) {
				p.sendMessage(Txt.f("§cItem bloqueado no servidor!"));
				ev.setCancelled(true);
				return;
			}
		}
	}

	@Listener
	public void playerLoginEvent(ClientConnectionEvent.Login event) {
		if (event.isCancelled()) {
			if (Sponge.getServer().getOnlinePlayers().size() >= Sponge.getServer().getMaxPlayers()) {
				if (event.getTargetUser().hasPermission("instamc.lotado")) {
					event.setCancelled(false);

				}
			}
		}
	}

	@Listener
	public void spawn(SpawnEntityEvent ev) {
		for (Entity e : ev.getEntities()) {
			if (e instanceof Monster) {
				ev.setCancelled(true);
			}
		}
	}

	@Listener
	public void removeItems(LoadChunkEvent ev) {
		for (Entity e : ev.getTargetChunk().getEntities()) {
			if (e instanceof Item || e instanceof Monster) {
				e.remove();
			}
		}
	}

	List<String> bloqueado = Arrays.asList("warp", "spawn", "tpa", "tpaceitar", "tpanegar", "pokeheal", "home", "terreno", "pc", "enderchest", "shop", "minerar");

	@Listener
	public void onCommand(SendCommandEvent ev, @First Player p) {
		if (bloqueado.contains(ev.getCommand().toLowerCase())) {
			if (PixelmonUtils.isInBattle(p)) {
				p.sendMessage(Txt.f("§cComando bloqueado durante batalha!"));
				ev.setCancelled(true);
			}

		}
	}

	@Listener
	public void breakF(ChangeBlockEvent.Break ev, @First Player p) {
		for (Transaction<BlockSnapshot> t : ev.getTransactions()) {
			BlockSnapshot sn = t.getOriginal();
			BlockType type = sn.getState().getType();
			if (type.equals(PixelmonUtils.getBlockType("pixelmon:fossil"))) {
				if (!p.hasPermission("instamc.staff")) {
					
					InstaPokemon.sendMessage(p, "§cBloqueado!");
					ev.setCancelled(true);
				
				}
			}
		}
	}

	@Listener
	public void ganhaPvp(PlayerWinBattleEvent ev) {
		Player ganhador = ev.getWinner();
		Player perdedor = ev.getLoser();
		if (ev.fugiu())
			return;
		InstaPokemon.broadcast(LibAPI.getPrefix(ganhador).trim() + "§a " + ganhador.getName() + " §eganhou de " + LibAPI.getPrefix(perdedor).trim() + "§c " + perdedor.getName() + "§f!");

		if (!PvPDB.alreadyWon(ganhador.getUniqueId(), perdedor.getUniqueId())) {
			if (InventoryUtils.getEmpty(ganhador) < 1) {
				InstaPokemon.sendMessage(ganhador, "§cPara você ganhar a cabeça de seu oponente, precisa ter um espaço vazio no inventário!");
				return;
			}
			ItemStack it = LibUtils.getHeadOf(perdedor.getName());
			ItemUtils.setItemName(it, Txt.f("§fCabeça de §b" + perdedor.getName()));
			ganhador.getInventory().offer(it);
			InstaPokemon.sendMessage(ganhador, "§aVocê ganhou a cabeça de §f" + perdedor.getName() + "§a !");

			PvPDB.won(ganhador.getUniqueId(), perdedor.getUniqueId());

		}

	}

	@Listener
	public void vip(ClientConnectionEvent.Join ev) {
		GameModeData md = ev.getTargetEntity().getGameModeData();
		md.set(Keys.GAME_MODE, GameModes.SURVIVAL);
		ev.getTargetEntity().offer(md);
		Player p = ev.getTargetEntity();
		InstaPokemon.sendMessage(p, ChatAPI.fixLinks(Txt.f("§fBem vindo ao servidor Pixelmon do §5§lInsta§f§lMC!")));
		InstaPokemon.sendMessage(p, ChatAPI.fixLinks(Txt.f("§fCaso precise de ajuda digite §6/ajuda§f !")));
		if (PlayerDB.getFlag(p.getUniqueId(), Flag.GANHASHINY) != null) {
			if (PlayerDB.getFlag(p.getUniqueId(), Flag.GANHASHINY).equals("sim")) {
				PlayerDB.setFlag(p.getUniqueId(), Flag.GANHASHINY, "nao");
				p.getInventory().offer(EnumCustomItems.PokemonShinyAleatorio.getItem().toItemStack());
				InstaPokemon.sendMessage(p, "§b§lVocê ganhou um Pokemon Shiny Aleatório por §d§ljogar 7h na BETA! §f§lObrigado!");
			}
		}
		if (!SpongeGuard.getManager().getManager(p.getWorld()).getRegionsAt(p.getLocation()).isEmpty()) {
			InstaPokemon.sendMessage(p, "§cVocê deslogou em um lugar inadequado teleporado para o spawn!");

			p.setLocation(p.getWorld().getSpawnLocation());

		}
		p.getTabList().setFooter(Config.footer);
		p.getTabList().setHeader(Config.header);
	}

	public boolean temRegiao(Location<World> loc) {
		return !SpongeGuard.getManager().getManager(loc.getExtent()).getRegionsAt(loc, false).isEmpty();
	}

	@Listener(order = Order.FIRST, beforeModifications = true)
	public void interactItem(InteractItemEvent.Secondary ev, @First Player p) {
		ItemType itemInHand = ItemTypes.NONE;
		if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
			itemInHand = p.getItemInHand(HandTypes.MAIN_HAND).get().getItem();
		} else
			if (p.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
				itemInHand = p.getItemInHand(HandTypes.OFF_HAND).get().getItem();
			}
		if (itemInHand == ItemTypes.CHORUS_FRUIT) {
			ev.setCancelled(true);
		}
	}

	
	@Listener(order = Order.FIRST, beforeModifications = true)
	public void interactBlock(InteractBlockEvent.Secondary ev, @First Player p) {
		ItemType itemInHand = ItemTypes.NONE;
		if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
			itemInHand = p.getItemInHand(HandTypes.MAIN_HAND).get().getItem();
		} else
			if (p.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
				itemInHand = p.getItemInHand(HandTypes.OFF_HAND).get().getItem();
			}
		if (itemInHand == ItemTypes.CHORUS_FRUIT) {
			ev.setUseItemResult(Tristate.FALSE);
		}
	}

	@Listener
	@IsCancelled(Tristate.UNDEFINED)
	public void interactBlock(InteractBlockEvent ev, @First Player p) {
		if (!ev.getTargetBlock().getLocation().isPresent()) {
			return;
		}

		if (temRegiao(ev.getTargetBlock().getLocation().get())) {
			return;
		}
		if (br.instamc.protection.listeners.GeralListener.byPass(p)) {
			return;
		}
		int linhas = -1;
		HashMap<SlotPos, ItemStack> itens = new HashMap();
		if (ProtectionManager.getInfoAt(ev.getTargetBlock().getLocation().get()).hasDono()) {
			if (!ProtectionManager.hasPermissionAt(p, ev.getTargetBlock().getLocation().get())) {
				TileEntity tile = ev.getTargetBlock().getLocation().get().getTileEntity().orElse(null);
				if (tile != null) {
					if (tile instanceof Chest) {

						Chest c = (Chest) tile;
						if (c.getDoubleChestInventory().isPresent()) {

							// SPONGE É UMA MERDA
							linhas = 6;
							InventoryLargeChest i = (InventoryLargeChest) c.getDoubleChestInventory().get();
							for (int x = 0; x < i.getSizeInventory(); x++) {
								SlotPos slotPos = Menu.toSlotPos(x);
								net.minecraft.item.ItemStack it = i.getStackInSlot(x);
								if (it != null) {
									it = it.copy();
									ItemStack ita = ItemStackUtil.fromNative(it).copy();
									itens.put(slotPos, ita);
								}
							}

						} else {
							Inventory i = c.getInventory();
							linhas = 3;
							SLOT: for (SlotPos pos : Menu.buildSquare(new SlotPos(0, 0), new SlotPos(8, 2))) {
								Optional<ItemStack> ita = i.query(pos).peek();
								if (ita.isPresent()) {
									ItemStack it = ita.get().copy();

									if (it != null) {
										itens.put(pos, it);
									}
								}

							}
						}

					} else
						if (tile instanceof IInventory) {
							IInventory i = (IInventory) tile;
							linhas = i.getSizeInventory() / 9;
							for (int x = 0; x < i.getSizeInventory(); x++) {
								SlotPos slotPos = Menu.toSlotPos(x);
								net.minecraft.item.ItemStack it = i.getStackInSlot(x);
								if (it != null) {
									it = it.copy();
									ItemStack ita = ItemStackUtil.fromNative(it).copy();
									itens.put(slotPos, ita);
								}
							}
						}

				}

			}
		}
		if (linhas != -1) {
			if (p.hasPermission("instaprotection.bau")) {
				final int linhasf = linhas;
				SchedulerUtils.runSync(new Runnable() {

					@Override
					public void run() {
						new SeeChestMenu(linhasf, itens).open(p);

					}
				}, 2);

			}
		}

	}

	@Listener(order = Order.LATE)
	public void breakB(ChangeBlockEvent.Break ev, @First Player p) {
		if (ev.isCancelled()) {
			return;
		}
		for (Transaction<BlockSnapshot> t : ev.getTransactions()) {
			BlockSnapshot original = t.getOriginal();
			if (t.getFinal().getState().getType() == BlockTypes.AIR) {
				if (original.getState().getType() == PixelmonUtils.getBlockType("pixelmon:healer")) {
					ev.setCancelled(true);
					if (original.getLocation().isPresent()) {
						Location<World> loc = original.getLocation().get();
						SchedulerUtils.runSync(new Runnable() {

							@Override
							public void run() {
								if (loc.getBlock().getType() == PixelmonUtils.getBlockType("pixelmon:healer")) {
									loc.removeBlock(Cause.source(InstaPokemon.getPlugin()).build());
									ItemUtils.spawnItem(PixelmonUtils.getItemStack(PixelmonBlocks.healer), loc);
								}
							}
						}, 5);

					}
				}
			}
		}
	}

	@Listener
	public void interact(InteractEntityEvent.Secondary.MainHand ev, @First Player p) {
		if (ev.getTargetEntity().getType() != EntityTypes.PLAYER) {
			if (p.hasPermission("instamc.adm")) {
				if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {

					if (p.getItemInHand(HandTypes.MAIN_HAND).get().getItem() == ItemTypes.BONE) {

						ev.getTargetEntity().remove();
					}
				}
			}
		}
	}

}
