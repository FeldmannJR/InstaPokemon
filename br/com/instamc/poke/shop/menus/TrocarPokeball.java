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
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;

import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
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
public class TrocarPokeball extends MenuFazAlgoPoke {

	public TrocarPokeball(Player p) {
		super(p, "Trocar Pokeball");
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
			InstaPokemon.sendMessage(p, "§cPokemon inválido!");
			return false;
		}

		Menu bolas = new Menu("Pokeballs", 3);

		for (EnumPokeballs pk : EnumPokeballs.values()) {
			ItemStack i = PixelmonUtils.getPokeball(pk);
			ItemUtils.setItemName(i, Txt.f("§e§l" + pk.getItem().getLocalizedName()));
			ItemUtils.addLore(i, ShopManager.buildPreco(ShopManager.precoTrocaPokeball));
			bolas.addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), i) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new VendaMenu(SpongeVIP.moedashop, i, "Trocar Pokeball", ShopManager.precoTrocaPokeball,
							"Troca a pokeboll para " + pk.getItem().getLocalizedName() + ".") {

						@Override
						public void cancela(Player player) {
						}

						@Override
						public boolean compra(Player player) {
							if (!can(player, slot)) {
								InstaPokemon.sendMessage(player, "§cPokemon inválido!");
								return false;
							}
							setPokeball(player, slot, pk);
							InstaPokemon.sendMessage(player, "§aModificada pokeball do seu pokemon!!");
							SpongeVIP.sendMessageComprou(player, "Troca de Pokeball (" + pk.name() + ")",
									ShopManager.precoTrocaPokeball);
							return true;
						}
					}.open(p);
				}
			});

		}
		bolas.open(p);
		return true;
	}

	public boolean can(Player p, int slot) {
		NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
		if (nbt == null) {
			return false;
		}
		PixelmonData data = new PixelmonData(nbt);
		if (data.isEgg) {
			return false;
		}
		return true;
	}

	@Override
	public void processaItemStack(ItemStack i, PixelmonData data) {

		ItemUtils.addLore(i, Txt.f("§bClique para trocar a pokebola!"));
		ItemUtils.addLore(i, ShopManager.buildPreco(ShopManager.precoTrocaPokeball));

	}

	public static boolean setPokeball(Player p, int slot, EnumPokeballs pb) {
		NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
		PlayerStorage sto = PixelmonUtils.getPlayerStorage(p);
		sto.recallAllPokemon();
		EntityPixelmon po = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt, (World) p.getWorld());
		po.caughtBall = pb;

		sto.replace(po, po);
		sto.sendUpdatedList();
		return true;
	}
}
