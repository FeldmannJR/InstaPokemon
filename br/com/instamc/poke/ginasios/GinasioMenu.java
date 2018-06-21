/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;
import com.flowpowered.math.vector.Vector2i;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 *
 * @author Carlos
 */
public class GinasioMenu extends Menu {

	public GinasioMenu() {
		super("Ginasios", 6);

	}

	public void updateButtons() {
		for (MenuButton but : new ArrayList<MenuButton>(getButtons())) {
			removeButton(but.getSlot());
		}
		for (Ginasio g : GinasioManager.getGinasios()) {
			if (g.getBadge() != null) {
				ItemStack i = PixelmonUtils.getBadge(g.getBadge());
				ItemUtils.setItemName(i, Txt.f("§d§l" + g.getNome()));
				String dono = "Ninguém";
				if (g.getDono() != null) {
					if (UUIDUtils.getName(g.getDono()) != null) {
						dono = UUIDUtils.getName(g.getDono());
					} else {
						dono = "Desconhecido";
					}
				}
				ItemUtils.addLore(i, Txt.f(" "));
				if (g.aberto) {
					ItemUtils.addLore(i, Txt.f("§a§lAberto"));
				} else {
					ItemUtils.addLore(i, Txt.f("§c§lFechado"));
					ItemUtils.addLore(i, Txt.f(" "));
					ItemUtils.addLore(i, Txt.f("§fUltima vez aberto: §a" + new SimpleDateFormat("dd/MM/yyyy").format(new Date(g.lastopen.getTime()))));

				}
				ItemUtils.addLore(i, Txt.f(" "));
				ItemUtils.addLore(i, Txt.f("§fDono: §6" + dono));
				if (g.getOcupado() != null) {
					ItemUtils.addLore(i, Txt.f(" "));
					ItemUtils.addLore(i, Txt.f("§fDesafiador: §d" + g.getOcupado().getName()));
				}
				addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), i) {

					@Override
					public void click(Player player, Menu menu, ClickType ct) {
						if (g.isAberto()) {
							GinasioManager.entra(player, g);
						} else {
							player.sendMessage(Txt.f("§cGinasio fechado"));
						}
						close(player);

					}
				});
			}

		}
	}

}
