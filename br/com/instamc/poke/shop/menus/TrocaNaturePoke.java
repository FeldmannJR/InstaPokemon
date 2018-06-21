/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop.menus;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.ConfirmarMenu;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.traducao.Traducao;
import br.com.instamc.sponge.vip.SpongeVIP;

import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class TrocaNaturePoke extends MenuFazAlgoPoke {

	public TrocaNaturePoke(Player p) {
		super(p, "Nature");
	}

	@Override
	public boolean faz(Player p, final int slot, MenuButton mb) {
		NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
		if (nbt == null) {
			mb.close(p);
			InstaPokemon.sendMessage(p, "§cPokemon inválido!");
			return false;
		}
		PixelmonData data = new PixelmonData(nbt);
		if (data.isEgg) {
			mb.close(p);
			InstaPokemon.sendMessage(p, "�Pokemon inválido!");
			return false;
		}

		Menu m = new Menu("Escolhe Nature", 4);
		for (final EnumNature na : EnumNature.values()) {

			ItemStack natureitem = ItemStackBuilder.of(ItemTypes.PAPER).withName("§e§l" + na.name()).build();
			String melhoraoq = "Nada";
			String pioraoq = "Nada";
			if (na.increasedStat != null && na.increasedStat != StatsType.None) {
				melhoraoq = na.increasedStat.getLocalizedName();
			}

			if (na.decreasedStat != null && na.decreasedStat != StatsType.None) {
				pioraoq = na.decreasedStat.getLocalizedName();
			}
			ItemUtils.addLore(natureitem, Txt.f("§fMelhora §6" + melhoraoq));
			ItemUtils.addLore(natureitem, Txt.f("§fReduz §c" + pioraoq));

			ItemUtils.addLore(natureitem, ShopManager.buildPreco(ShopManager.precoTrocaNature));

			m.addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), natureitem) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.SAPLING, 1), "Troca Nature",
							ShopManager.precoTrocaNature, "Troca a nature do seu pokemon para " + na.name() + ".") {

						@Override
						public void cancela(Player player) {
						}

						@Override
						public boolean compra(Player player) {
							if (!can(player, slot)) {
								InstaPokemon.sendMessage(player, "§cPokemon inválido!");
								return false;
							}
							setNature(player, slot, na);
							InstaPokemon.sendMessage(player, "§fNature trocada para §6" + na.name());
							SpongeVIP.sendMessageComprou(player, "Troca de Nature (" + na.name() + ")",
									ShopManager.precoTrocaNature);
							return true;
						}
					}.open(p);
				}
			});

		}
		m.open(p);
		return true;
	}

	public boolean can(Player p, int slot) {
		NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
		if (nbt == null) {
			return false;
		}

		return true;
	}

	@Override
	public void processaItemStack(ItemStack i, PixelmonData data) {

		if (data.nature != null) {
			ItemUtils.addLore(i, Txt.f("§eNature: §a" + data.nature.name()));

		}
		ItemUtils.addLore(i, Txt.f("§bClique para transformar em shiny."));
		ItemUtils.addLore(i, ShopManager.buildPreco(ShopManager.precoTrocaNature));

	}

	public static boolean setNature(Player p, int slot, EnumNature nature) {
		NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
		PlayerStorage sto = PixelmonUtils.getPlayerStorage(p);
		sto.recallAllPokemon();
		EntityPixelmon po = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt, (World) p.getWorld());
		po.setNature(nature);
		po.updateStats();

		sto.replace(po, po);
		sto.sendUpdatedList();
		return true;
	}
}
