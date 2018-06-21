package br.com.instamc.poke.shop.chest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.common.data.util.DataQueries;
import org.spongepowered.common.item.inventory.EmptyInventoryImpl;
import org.spongepowered.common.item.inventory.SpongeItemStackSnapshot;
import org.spongepowered.common.item.inventory.adapter.impl.slots.SlotAdapter;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import com.pixelmonmod.pixelmon.items.ItemBadge;
import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.placas.BlockClickSignEvent;
import br.com.instamc.poke.shop.chest.ShopItem.ShopType;
import br.com.instamc.poke.shop.chest.cmds.CmdChestShopAdm;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.nbt.NBTTagCompound;

public class ChestShop {
	public static void init() {
		ComandoAPI.enable(new CmdChestShopAdm());
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, new ChestShop());

	}

	public static int[] getRowAndColoum(int slot) {
		int row = slot / 9;
		int coloum = slot % 9;
		return new int[] { row, coloum };
	}

	@Listener
	public void listener(BlockClickSignEvent ev)

	{
		Player p = ev.getTargetEntity();
	
		if (!ev.getLinhas().get(0).equalsIgnoreCase("shop") && !ev.getLinhas().get(0).equalsIgnoreCase("shopvip")) {
			return;
		}
		boolean vip = ev.getLinhas().get(0).equalsIgnoreCase("shopvip");
		if (p.getGameModeData().get(Keys.GAME_MODE).isPresent()) {
			if (p.getGameModeData().get(Keys.GAME_MODE).get() == GameModes.CREATIVE && p.hasPermission("instamc.op")) {
				ev.setCancelblockinteract(false);
				return;
			}
		}
		EnumBadges badge = null;
		if (ev.getLinhas().size() == 2) {
			String badgen = ev.getLinhas().get(1);
			for (EnumBadges ba : EnumBadges.values()) {

				if (ba.name().replace("Badge", "").equalsIgnoreCase(badgen)) {
					badge = ba;

				}

			}

		}
		BlockSnapshot targetBlock = ev.getBlock();
		Location bau = targetBlock.getLocation().get();
		int linhas = 3;
		if (bau.getTileEntity().isPresent() && bau.getTileEntity().get() instanceof Chest) {
			Chest c = (Chest) bau.getTileEntity().get();
			final List<ShopItem> shops = new ArrayList<ShopItem>();

			if (c.getDoubleChestInventory().isPresent()) {
				// SPONGE É UMA MERDA
				InventoryLargeChest i = (InventoryLargeChest) c.getDoubleChestInventory().get();
				for (int x = 0; x < i.getSizeInventory(); x++) {
					SlotPos slotPos = Menu.toSlotPos(x);
					net.minecraft.item.ItemStack it = i.getStackInSlot(x);
					if (it != null) {
						it = it.copy();
						ItemStack ita = ItemStackUtil.fromNative(it).copy();
						ShopItem shopitem = tiraItem(ita, slotPos);
						if (shopitem != null) {
							shops.add(shopitem);

						}
					}
				}

				linhas = 6;

			} else {
				Inventory i = c.getInventory();

				SLOT: for (SlotPos pos : Menu.buildSquare(new SlotPos(0, 0), new SlotPos(8, linhas - 1))) {
					Optional<ItemStack> ita = i.query(pos).peek();
					if (ita.isPresent()) {
						ItemStack it = ita.get().copy();

						if (it != null) {
							ShopItem shopitem = tiraItem(it, pos);
							if (shopitem != null) {
								shops.add(shopitem);

							}
						}
					}

				}
			}

			if (shops.isEmpty()) {

				p.sendMessage(Txt.f("§cShop sem nenhum item!"));
				return;
			}
			final EnumBadges bb = badge;
			final int linhasf = linhas;
			SchedulerUtils.runSync(new Runnable() {

				@Override
				public void run() {

					new ShopMenu(shops, vip, bb, linhasf).open(p);
					;
				}
			}, 2);

		}
	}

	public static ItemStack setPreco(ItemStack it, int preco, ShopType type) {
		it = cleanItem(it.copy());
		if (type != null) {
			if (type == ShopType.COMPRA) {
				ItemUtils.addLore(it, Txt.f("§e§lCompre por§f: " + preco));
			}
			if (type == ShopType.VENDA) {
				ItemUtils.addLore(it, Txt.f("§e§lVenda por§f: " + preco));
			}
		}
		return it;

	}

	public static ItemStack getPokemonItem(int preco, ShopType type, EnumPokemon poke, boolean shiny) {
		ItemStack it = PixelmonUtils.getPixelmonIcon(poke, false);
		ItemUtils.setItemName(it, Txt.f("§a§l" + poke.name));
		it = setPreco(it, preco, type);
		ItemUtils.addLore(it, Txt.f("§e§lPokemon: §f" + poke.name));
		if (shiny) {
			ItemUtils.addLore(it, Txt.f("§e§isShiny"));

		}
		return it;
	}
	public static ItemStack getVendeLendario(int preco) {
		ItemStack it =ItemStack.of(ItemTypes.PAPER, 1);
		ItemUtils.setItemName(it, Txt.f("§aVendeLendario"));
		it = setPreco(it, preco,ShopType.VENDA);
		ItemUtils.addLore(it, Txt.f("§e§lVendeLendario"));
		
		return it;
	}
	public static ItemStack getVendeShiny(int preco) {
		ItemStack it =ItemStack.of(ItemTypes.PAPER, 1);
		ItemUtils.setItemName(it, Txt.f("§aVendeShiny"));
		it = setPreco(it, preco,ShopType.VENDA);
		ItemUtils.addLore(it, Txt.f("§e§lVendeShiny"));
		
		return it;
	}

	public ShopItem tiraItem(ItemStack it, SlotPos pos) {
		List<Text> lore = it.get(Keys.ITEM_LORE).orElse(new ArrayList());
		ShopType type = null;
		int preco = -1;
		EnumPokemon poke = null;
		boolean shiny = false;
		boolean lendario = false;
		boolean vendeshiny = false;
		for (Text t : lore) {
			String plain = t.toPlain();

			boolean checkpreco = false;
			if (plain.startsWith("Compre por:")) {
				type = ShopType.COMPRA;
				checkpreco = true;
			}
			if (plain.startsWith("Venda por:")) {
				type = ShopType.VENDA;
				checkpreco = true;
			}
			if (plain.startsWith("Pokemon:")) {
				String nomepoke = plain.split(":")[1].trim();
				if (EnumPokemon.hasPokemon(nomepoke) && EnumPokemon.getFromName(nomepoke).isPresent()) {
					poke = EnumPokemon.getFromName(nomepoke).get();
				}

			}
			if (plain.startsWith("isShiny")) {
				shiny = true;
			}
			if (plain.startsWith("VendeLendario")) {
				lendario = true;
			}
			if (plain.startsWith("VendeShiny")) {
				vendeshiny = true;
			}

			if (checkpreco) {
				try {
					preco = Integer.valueOf(plain.split(":")[1].trim());
					if (preco <= 0) {
						return null;
					}

				} catch (NumberFormatException ex) {
				}
			}
		}
		if (preco == -1) {
			return null;
		}
		if (type == null) {
			return null;
		}
		boolean pokeitem = it.getItem().getType().getId().equals(PixelmonUtils.spriteitem);
		if (lendario) {
			return new VendeLendario(pos, preco);
		}
		if (vendeshiny) {
			return new VendeShiny(pos, preco);
		}
		if (pokeitem && poke == null) {
			return null;
		}
		if (pokeitem) {
			return new PokemonShop(pos, preco, type, poke, shiny);
		} else {
			return new ItemStackShop(pos, preco, type, cleanItem(it));
		}

	}

	public static ItemStack cleanItem(ItemStack it) {
		List<Text> lore = new ArrayList();
		for (Text t : new ArrayList<Text>(it.getOrElse(Keys.ITEM_LORE, new ArrayList<>()))) {
			String plain = t.toPlain();
			if (plain.startsWith("Compre por:")) {
				continue;
			}
			if (plain.startsWith("Pokemon:")) {
				continue;
			}
			if (plain.startsWith("Venda por:")) {
				continue;
			}
			lore.add(t);

		}
		if (!lore.isEmpty()) {
			it.offer(Keys.ITEM_LORE, lore);
		} else {
			it.remove(Keys.ITEM_LORE);
		}

		SpongeItemStackSnapshot createSnapshot = new SpongeItemStackSnapshot(it);

		return createSnapshot.createStack();
	}
}
