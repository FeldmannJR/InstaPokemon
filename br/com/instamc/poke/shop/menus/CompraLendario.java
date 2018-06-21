/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop.menus;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.poke.shop.CompraPokeMenu;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.entity.player.EntityPlayerMP;
import scala.actors.threadpool.Arrays;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomNoticeOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.GraphicDisplayTypes;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomNoticePacket;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import java.util.ArrayList;

import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class CompraLendario extends CompraPokeMenu {

	public CompraLendario() {
		super("Comprar Lendário", 1);
		
	}

	@Override
	public boolean isValid(EnumPokemon poke) {
		return PixelmonUtils.isLegendery(poke);
	}

	@Override
	public void clickPoke(Player p, EnumPokemon e) {

		new VendaMenu(SpongeVIP.moedashop, PixelmonUtils.getPixelmonIcon(e, false), "Comprar Lendário",
				ShopManager.precoLendario, "Clique aqui para comprar um " + e.name + ".") {

			@Override
			public void cancela(Player player) {
				close(p);
			}

			@Override
			public boolean compra(Player player) {
				InstaPokemon.sendMessage(player, "§aVocê comprou um §e" + e.name + "§a.");
				PixelmonUtils.give(player,
						PixelmonUtils.build(player, e, EnumPokeballs.MasterBall, 1, false));
				SpongeVIP.sendMessageComprou(player, "Um Lendário Escolhido (" + e.name + ")",
						ShopManager.precoLendario);

				return true;
			}
		}.open(p);
		
	}

	@Override
	public ItemStack buildItem(ItemStack cv, EnumPokemon poke) {
		ItemUtils.addLore(cv, Txt.f("§fClique aqui para comprar"));
		ItemUtils.addLore(cv, ShopManager.buildPrecoPor(ShopManager.precoLendario));
		return cv;	
	}

	@Override
	public boolean isShiny() {
		
		return false;
	}




}
