package br.com.instamc.poke.elites.menus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class MenuVerElite extends Menu {

	Elite eli;
	public int retornapage = 0;

	public MenuVerElite(Elite eli) {

		super(eli.getTagDB().toUpperCase(), 2);
		this.eli = eli;
		ItemStack icone = eli.buildIcone(false);

		List<ElitePlayer> pls = new ArrayList(eli.getElitePlayers());
		Collections.sort(pls, new Comparator<ElitePlayer>() {

			@Override
			public int compare(ElitePlayer o1, ElitePlayer o2) {

				return o2.getCargo() - o1.getCargo();
			}
		});
		for (ElitePlayer el : pls) {
			if (el.getCargo() == ElitePlayer.STAFF) {
				continue;
			}
			String nick = UUIDUtils.getName(el.uid);

			if (nick == null) {
				nick = "Steve";
			}
			ItemStack it = LibUtils.getHeadOf(nick);
			String oq = "";
			if (el.getCargo() == ElitePlayer.FUNDADOR) {
				oq = "§5§lFUNDADOR ";
			}
			if (el.getCargo() == ElitePlayer.LIDER) {
				oq = "§6§lLIDER ";
			}
			ItemUtils.setItemName(it, Txt.f(oq + "§f" + nick));
			if (!el.titulo.isEmpty()) {
				ItemUtils.addLore(it, Txt.f("§eTitulo: §f" + el.titulo));
			}
			if (Sponge.getServer().getPlayer(el.uid).isPresent()) {
				if (Sponge.getServer().getPlayer(el.uid).get().isOnline()) {
					if (!Sponge.getServer().getPlayer(el.uid).get().hasPermission("instamc.elite.oculta")) {
						ItemUtils.addLore(it, Txt.f("§a§lOnline"));
					}else{
						ItemUtils.addLore(it, Txt.f("§c§lOffline"));
					}
				} else {
					ItemUtils.addLore(it, Txt.f("§c§lOffline"));

				}
			} else {
				ItemUtils.addLore(it, Txt.f("§c§lOffline"));
			}
			addButtonToSquare(new SlotPos(0, 1), new SlotPos(8, 5), new NothingButton(new SlotPos(0, 0), it));
		}
		ItemStack aliados = ItemStack.of(ItemTypes.STAINED_HARDENED_CLAY, 1);
		aliados.offer(Keys.DYE_COLOR, DyeColors.GREEN);
		ItemUtils.setItemName(aliados, Txt.f("§a§lAlianças:"));
		String add = "";
		int foi = 0;
		for (int x : eli.getAliados()) {
			Elite aliado = EliteManager.getEliteById(x);
			if (aliado != null) {
				add += "§f" + aliado.tag + " ";
				if (foi > 5) {
					ItemUtils.addLore(aliados, Txt.f(add));
					add = "";
					foi = 0;
				}
			}
		}
		if (!add.isEmpty()) {
			ItemUtils.addLore(aliados, Txt.f(add));
		}

		ItemStack rivais = ItemStack.of(ItemTypes.STAINED_HARDENED_CLAY, 1);
		rivais.offer(Keys.DYE_COLOR, DyeColors.RED);
		ItemUtils.setItemName(rivais, Txt.f("§c§lRivalidades:"));

		add = "";
		foi = 0;
		for (int x : eli.getRivais()) {
			Elite aliado = EliteManager.getEliteById(x);
			if (aliado != null) {
				add += "§f" + aliado.tag + " ";
				if (foi > 5) {
					ItemUtils.addLore(rivais, Txt.f(add));
					add = "";
					foi = 0;
				}
			}
		}
		if (!add.isEmpty()) {
			ItemUtils.addLore(rivais, Txt.f(add));
		}
		addButton(new NothingButton(new SlotPos(3, 0), rivais));
		addButton(new NothingButton(new SlotPos(5, 0), aliados));

		addButton(new NothingButton(new SlotPos(4, 0), icone));

	}

	public void addRetorna() {
		if (retornapage != 0) {
			addButton(new MenuButton(new SlotPos(0, 0), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§7Clique para voltar").build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new MenuElites(retornapage).open(arg0);
					;

				}
			});

		}
	}

}
