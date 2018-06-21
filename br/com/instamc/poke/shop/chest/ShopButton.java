package br.com.instamc.poke.shop.chest;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.logs.LogsDB;
import br.com.instamc.poke.logs.LogsDB.PokeAction;
import br.com.instamc.poke.shop.chest.ShopItem.ShopType;
import br.com.instamc.poke.utils.InvUtils;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.EconAPI;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.menu.menus.ConfirmarMenu;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;
import br.com.instamc.sponge.library.utils.encode.Base64;
import net.minecraft.nbt.NBTTagCompound;

public class ShopButton extends MenuButton {

	ShopItem item;
	ShopMenu menu;

	public ShopButton(SlotPos slot, ShopItem item, ShopMenu menu) {
		super(slot, item.getItem());
		this.item = item;
		this.menu = menu;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void click(Player p, Menu arg1, ClickType arg2) {

		if (menu.vip && !p.hasPermission("instamc.vip")) {
			p.sendMessage(Txt.f("§cVocê precisa ser vip usar este shop!"));
			playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);

			return;
		}
		if (menu.bad != null) {
			if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(menu.bad)) {
				p.sendMessage(Txt.f("§aVocê precisa da insignia " + menu.bad.name().replace("Badge", "") + " §apara usar este shop!"));
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);

				return;
			}

		}

		if (item instanceof ItemStackShop) {
			if (!fazItem(p, (ItemStackShop) item)) {
				close(p);
			}

		}
		if (item instanceof PokemonShop) {
			if (fazPoke(p, (PokemonShop) item, VendePokemon.SELECIONADO)) {
				close(p);
			}
		}
		if (item instanceof VendeShiny) {
			if (fazPoke(p, item, VendePokemon.SHINY)) {
				close(p);
			}
		}
		if (item instanceof VendeLendario) {
			if (fazPoke(p, item, VendePokemon.LENDARIO)) {
				close(p);
			}
		}

	}

	public boolean fazItem(Player p, ItemStackShop item) {
		if (item.type == ShopType.COMPRA) {
			int preco = item.preco;
			if (!EconAPI.hasMoney(p.getUniqueId(), preco + 0.0)) {
				InstaPokemon.sendMessage(p, "§cVocê não tem coins suficientes para comprar isso!");
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
				return false;
			}
			if (InventoryUtils.getEmpty(p) < 1) {
				InstaPokemon.sendMessage(p, "§cSeu inventário está cheio para comprar algo, esvazie-o!");
				return false;
			}
			EconAPI.removeMoney(p.getUniqueId(), BigDecimal.valueOf(preco));
			p.getInventory().offer(item.it.copy());
			InstaPokemon.sendMessage(p, "§fVocê comprou o item por §6" + preco + " §fcoins!");
			playSound(p, SoundTypes.BLOCK_NOTE_PLING);
			return true;
		}
		if (item.type == ShopType.VENDA) {

			if (InvUtils.remove(p, item.it.copy(), item.it.getQuantity())) {
				EconAPI.addMoney(p.getUniqueId(), item.preco);
				InstaPokemon.sendMessage(p, "§fVocê vendeu o item por §6" + item.preco + " §fcoins!");
				playSound(p, SoundTypes.BLOCK_NOTE_PLING);
				return true;
			} else {
				InstaPokemon.sendMessage(p, "§cVocê não tem o item para vender!");
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
				return false;

			}

		}
		return false;

	}

	public boolean fazPoke(Player p, ShopItem pok, VendePokemon tipo) {
		if (item.type == ShopType.COMPRA && pok instanceof PokemonShop) {
			PokemonShop poke = (PokemonShop) pok;
			int preco = item.preco;
			if (!EconAPI.hasMoney(p.getUniqueId(), preco + 0.0)) {
				InstaPokemon.sendMessage(p, "§cVocê não tem coins suficientes para comprar isso!");
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
				return true;
			}
			EconAPI.removeMoney(p.getUniqueId(), BigDecimal.valueOf(preco));
			PixelmonUtils.give(p, PixelmonUtils.build(p, poke.pokemon, EnumPokeballs.PokeBall, 1, false));
			InstaPokemon.sendMessage(p, "§fVocê comprou o pokemon §a" + poke.pokemon.name + " §fpor §6" + preco + " §fcoins!");
			playSound(p, SoundTypes.BLOCK_NOTE_PLING);
			return true;
		}
		EnumPokemon pokeenum = null;
		boolean shiny = false;
		if (pok instanceof PokemonShop) {
			pokeenum = ((PokemonShop) pok).pokemon;
			shiny = ((PokemonShop) pok).shiny;
		}
		if (item.type == ShopType.VENDA) {
			int tem = 0;
			int slot = -1;
			NBTTagCompound[] t = PixelmonUtils.getParty(p);
			for (int x = 0; x < t.length; x++) {
				if (t[x] == null) {
					continue;
				}
				PixelmonData po = new PixelmonData(t[x]);
				if (tipo.check(po, pokeenum) && !po.isEgg) {
					if ((shiny && po.isShiny) || !shiny) {
						tem++;

						slot = x;
					}
				}
			}
			if (tem == 0) {
				InstaPokemon.sendMessage(p, "§cVocê não tem o pokemon que deseja vender!");
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
				return true;
			}
			if (tem == 1) {
				if (pokeenum == null) {
					PixelmonData aaa = new PixelmonData(PixelmonUtils.getParty(p)[slot]);
					pokeenum = aaa.getSpecies();
					shiny = aaa.isShiny;
				}
				vende(pokeenum, slot, p, pok.preco, shiny, tipo);
				return false;
			}
			final EnumPokemon pokevenda = pokeenum;
			final boolean shinyvenda = shiny;
			if (tem > 1) {
				Menu m = new Menu("Selecionar", 1);

				for (int x = 0; x < t.length; x++) {
					int invslot = 1 + x;
					if (x >= 3) {
						invslot++;
					}
					if (t[x] == null) {
						m.addButton(new NothingButton(new SlotPos(invslot, 0), ItemStackBuilder.of(ItemTypes.BARRIER).withName("§cInválido").build()));
						continue;
					}
					PixelmonData po = new PixelmonData(t[x]);

					if ((tipo.check(po, pokeenum)) && !po.isEgg) {
						if ((shiny && po.isShiny) || !shiny) {
							ItemStack it = PixelmonUtils.getPixelmonIcon(po.getSpecies(), po.isShiny);
							ItemUtils.setItemName(it, Txt.f("§e§lParty §6§l" + (x + 1) + "§f: " + po.getSpecies().name));
							ItemUtils.addLore(it, Txt.f("§fClique aqui para vender!"));
							ItemUtils.addLore(it, Txt.f("§6por " + pok.preco + " coins!"));
							final int slotfinal = x;
							m.addButton(new MenuButton(new SlotPos(invslot, 0), it) {

								@Override
								public void click(Player pc, Menu arg1, ClickType arg2) {
									vende(po.getSpecies(), slotfinal, pc, pok.preco, po.isShiny, tipo);

								}
							});
						}
					} else {
						m.addButton(new NothingButton(new SlotPos(invslot, 0), ItemStackBuilder.of(ItemTypes.BARRIER).withName("§cInválido").build()));

					}
				}
				m.open(p);

				return false;
			}

			return false;

		}

		return false;
	}

	public static enum VendePokemon {
		SELECIONADO {
			@Override
			public boolean check(PixelmonData poke, EnumPokemon e) {
				return e == poke.getSpecies();
			}
		},
		SHINY {
			@Override
			public boolean check(PixelmonData poke, EnumPokemon e) {
				return poke.isShiny;
			}
		},
		LENDARIO {
			@Override
			public boolean check(PixelmonData poke, EnumPokemon e) {
				// TODO Auto-generated method stub
				return PixelmonUtils.isLegendery(poke.getSpecies());
			}
		};

		public abstract boolean check(PixelmonData poke, EnumPokemon e);

	}

	public boolean vende(EnumPokemon poke, int slot, Player p, int preco, boolean shiny, VendePokemon v) {
		ConfirmarMenu m = new ConfirmarMenu(PixelmonUtils.getPixelmonIcon(poke, shiny), "Vender Pokemon " + poke.name, "por " + preco + " coins! (Party " + (slot + 1) + ")") {

			@Override
			public void recusa(Player arg0) {

			}

			@Override
			public void confirma(Player p) {
				if (PixelmonUtils.getPokemonCount(p, false) <= 1) {
					InstaPokemon.sendMessage(p, "§bVocê não pode vender todos seus pokemons, você precisa ficar no mínimo com um.");
					playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
					return;
				}
				NBTTagCompound[] t = PixelmonUtils.getParty(p);
				if (t[slot] != null) {
					PixelmonData d = new PixelmonData(t[slot]);
					if (!d.isEgg) {
						if ((d.isShiny && shiny) || !shiny) {
							if (v.check(d, poke)) {
								PixelmonUtils.getPlayerStorage(p).recallAllPokemon();
								PixelmonUtils.getPlayerStorage(p).removeFromPartyPlayer(slot);
								LogsDB.addLog(d.getSpecies().name + "-" + (d.isShiny ? "-s" : ""), "" + preco, p.getUniqueId(), "", PokeAction.VENDE);
								PixelmonUtils.getPlayerStorage(p).sendUpdatedList();
								EconAPI.addMoney(p.getUniqueId(), BigDecimal.valueOf(preco));
								InstaPokemon.sendMessage(p, "§fVocê vendeu o pokemon §a" + poke.name + " §fpor §6" + preco + " §fcoins!");
								playSound(p, SoundTypes.BLOCK_NOTE_PLING);
								return;
							}
						}
					}
				}

				p.sendMessage(Txt.f("§cPokemon inválido!"));
				playSound(p, SoundTypes.BLOCK_FIRE_EXTINGUISH);
			}
		};
		m.open(p);
		return false;

	}

}
