package br.com.instamc.poke.fixes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.block.tileentity.carrier.Furnace;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.type.CarriedInventory;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.flowpowered.math.vector.Vector3d;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.ComputerBox;
import com.pixelmonmod.pixelmon.storage.PCClient;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerComputerStorage;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.CustomItemManager;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.listeners.DamageListener;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;
import br.instamc.protection.core.TerrenoInfo;
import br.instamc.protection.managers.ProtectionManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.nbt.NBTTagCompound;

public class FindChests extends ComandoAPI {

	public FindChests() {
		super(CommandType.OP, "baus");
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, this);
		createTables();
	}

	public void createTables() {
		try {

			InstaPokemon.getDB().createStatement().execute("CREATE TABLE IF NOT EXISTS bugadores (`id` int auto_increment primary key, `uuid` varchar(100), quando timestamp)");
			InstaPokemon.getDB().createStatement().execute("CREATE TABLE IF NOT EXISTS rlendarios (`id` int auto_increment primary key, `uuid` varchar(100),`quantos` INTEGER DEFAULT -1)");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insert(UUID uid) {
		try {
			ResultSet rs = InstaPokemon.getDB().createStatement().executeQuery("SELECT 1 FROM bugadores WHERE uuid = '" + uid.toString() + "'");
			if (rs.next()) {
				return;
			}
			InstaPokemon.getDB().createStatement().execute("INSERT INTO bugadores (`uuid`,`quando`) VALUES('" + uid.toString() + "',NOW())");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void checkRemove(Player p) {
		int removido = 0;
		try {

			ResultSet rs = InstaPokemon.getDB().createStatement().executeQuery("SELECT 1 FROM rlendarios WHERE uuid = '" + p.getUniqueId().toString() + "' and `quantos` = -1");
			if (!rs.next()) {
				return;
			}
			PlayerComputerStorage box = PixelmonStorage.computerManager.getPlayerStorage((EntityPlayerMP) p);

			for (int b = 0; b < PlayerComputerStorage.boxCount; b++) {
				for (int x = 0; x < box.getBox(b).getStoredPokemon().length; x++) {
					NBTTagCompound poke = box.getBox(b).getStoredPokemon()[x];
					if (poke != null) {
						PixelmonData d = new PixelmonData(poke);
						if (PixelmonUtils.isLegendery(d.getSpecies())) {
							PCServer.deletePokemon((EntityPlayerMP) p, b, x);
							removido++;
						}

					}
				}

			}
			for (int x = 0; x < 6; x++) {
				NBTTagCompound nbt = PixelmonUtils.getParty(p)[x];
				if (nbt == null)
					continue;
				PixelmonData d = new PixelmonData(nbt);
				if (PixelmonUtils.isLegendery(d.getSpecies())) {
					PixelmonUtils.getPlayerStorage(p).removeFromPartyPlayer(x);
					removido++;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.sendMessage(Txt.f("§aRemovi " + removido + " lendários! Não gostou? Reclama pra mahzinha!"));
		try {
			InstaPokemon.getDB().createStatement().execute("UPDATE rlendarios set `quantos` = " + removido + " WHERE uuid ='" + p.getUniqueId().toString() + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void fazChunk(Chunk c, CommandSource cs) {

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 255; y++) {
				for (int z = 0; z < 16; z++) {
					int xw = (c.getPosition().getX() * 16) + x;
					int zw = (c.getPosition().getZ() * 16) + z;
					BlockState block = c.getWorld().getBlock(xw, y, zw);
					List<ItemStack> stacks = getItemStacks(block, c.getWorld().getLocation(new Vector3d(xw, y, zw)));
					if (stacks != null) {
						for (ItemStack it : stacks) {
							if (isBug(it)) {
								Text t = Txt.f(xw + " " + y + " " + zw);
								t = t.toBuilder().onClick(TextActions.runCommand("/tppos " + xw + " " + y + " " + z)).build();
								cs.sendMessage(t);
								break;
							}
						}

					}

				}
			}
		}
	}

	public void findPlayers(CommandSource cs) {
		Optional<UserStorageService> provide = Sponge.getServiceManager().provide(UserStorageService.class);
		if (provide.isPresent()) {
			UserStorageService um = provide.get();
			for (GameProfile gp : um.getAll()) {
				User us = um.get(gp).orElse(null);
				if (us != null) {
					CarriedInventory<? extends Carrier> i = us.getInventory();
					for (Inventory slot : i.slots()) {
						if (slot != null) {
							Optional<ItemStack> it = slot.peek();
							if (it != null) {
								if (isBug(it.get())) {
									cs.sendMessage(Txt.f(gp.getName().orElse("BUG")));
								}
							}
						}
					}
				}

			}
		}

	}

	@Listener
	public void join(ClientConnectionEvent.Join ev) {
		if (isFdp(ev.getTargetEntity())) {
			insert(ev.getTargetEntity().getUniqueId());
		}
		checkRemove(ev.getTargetEntity());

	}

	public List<ItemStack> getItemStacks(BlockState state, Location<World> loc) {
		List<ItemStack> itens = new ArrayList();
		TileEntity tile = loc.getTileEntity().orElse(null);
		if (tile != null) {
			if (tile instanceof Chest) {

				Chest c = (Chest) tile;
				if (c.getDoubleChestInventory().isPresent()) {

					InventoryLargeChest i = (InventoryLargeChest) c.getDoubleChestInventory().get();
					for (int x = 0; x < i.getSizeInventory(); x++) {
						SlotPos slotPos = Menu.toSlotPos(x);
						net.minecraft.item.ItemStack it = i.getStackInSlot(x);
						if (it != null) {
							it = it.copy();
							ItemStack ita = ItemStackUtil.fromNative(it).copy();
							itens.add(ita);
						}
					}

				} else {
					Inventory i = c.getInventory();

					SLOT: for (SlotPos pos : Menu.buildSquare(new SlotPos(0, 0), new SlotPos(8, 2))) {
						Optional<ItemStack> ita = i.query(pos).peek();
						if (ita.isPresent()) {
							ItemStack it = ita.get().copy();

							if (it != null) {
								itens.add(it);
							}
						}

					}
				}

			} else if (tile instanceof Furnace) {
				Furnace f = (Furnace) tile;
				for (Inventory i : f.getInventory().slots()) {
					ItemStack it = i.peek().orElse(null);
					if (it != null) {
						itens.add(it);
					}

				}
			} else if (tile instanceof IInventory) {
				IInventory i = (IInventory) tile;

				for (int x = 0; x < i.getSizeInventory(); x++) {
					SlotPos slotPos = Menu.toSlotPos(x);
					net.minecraft.item.ItemStack it = i.getStackInSlot(x);
					if (it != null) {
						it = it.copy();
						ItemStack ita = ItemStackUtil.fromNative(it).copy();
						itens.add(ita);
					}
				}
			}

		}

		return itens;

	}

	public boolean isFdp(Player p) {

		List<ItemStack> it = new ArrayList();

		for (Inventory i : p.getEnderChestInventory().slots()) {
			ItemStack item = i.peek().orElse(null);
			if (item != null) {
				it.add(item);
			}
		}
		for (Inventory i : p.getInventory().slots()) {
			ItemStack item = i.peek().orElse(null);
			if (item != null) {
				it.add(item);
			}
		}
		for (ItemStack item : it) {
			if (isBug(item)) {
				return true;
			}

		}

		return false;
	}

	public boolean isBug(ItemStack it) {

		EnumCustomItems custom = CustomItemManager.getItem(it);
		if (custom != null) {
			if (it.getQuantity() >= 10) {
				return true;
			}

		}

		return false;
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		if (cs instanceof Player) {
			Player pl = (Player) cs;
			Chunk c = pl.getWorld().getChunk(pl.getLocation().getChunkPosition()).orElse(null);
			if (c == null) {
				return;
			}
			List<Integer[]> list = new ArrayList();
			list.add(new Integer[] { 1, 0 });
			list.add(new Integer[] { -1, 0 });
			list.add(new Integer[] { 0, 1 });
			list.add(new Integer[] { 0, -1 });
			list.add(new Integer[] { 1, -1 });
			list.add(new Integer[] { -1, 1 });
			list.add(new Integer[] { -1, -1 });
			list.add(new Integer[] { 1, 1 });
			list.add(new Integer[] { 0, 0 });
			for (Integer[] a : list) {
				Chunk ch = c.getWorld().getChunk(c.getPosition().getX() + a[0], 0, c.getPosition().getZ() + a[1]).orElse(null);
				if (ch == null) {
					continue;
				}
				fazChunk(ch, cs);

			}

		}

	}
}
