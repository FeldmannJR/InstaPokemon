package br.com.instamc.poke.ranks.menus;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.poke.camera.CameraDB;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.ranks.RankManager;
import br.com.instamc.poke.ranks.Stats;
import br.com.instamc.poke.ranks.Stats.Stat;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class VerStatsMenu extends Menu {

	UUID uid;

	public VerStatsMenu(UUID uid) {
		super(UUIDUtils.getName(uid) == null ? "Perfil" : UUIDUtils.getName(uid), 3);
		// TODO Auto-generated constructor stub

		this.uid = uid;

		String nome = UUIDUtils.getName(uid);
		ItemStack it = LibUtils.getHeadOf(nome);
		ItemUtils.setItemName(it, Txt.f("§e§l" + nome));
		addButton(new NothingButton(new SlotPos(4, 0), it));

		String on = "";
		int minutos = Stats.geStats(uid, Stat.MINUTOSOLINE);
		if (minutos >= 60) {
			int horas = minutos / 60;
			int minutosr = minutos % 60;
			if (horas != 1) {
				on += horas + " horas";
			} else {
				on += horas + " hora";
			}
			if (minutosr != 1) {
				on += " e " + minutosr + " minutos";
			} else {
				on += " e " + minutosr + " minuto";

			}
		} else {
			if (minutos != 1) {
				on += "" + minutos + " minutos";
			} else {
				on += "" + minutos + " minuto";

			}
		}
		// ONLINE

		ItemStack online = build(Stat.MINUTOSOLINE.getIcone().copy(), "Tempo Online", on);

		addButton(new MenuButton(new SlotPos(0, 1), online){
			@Override
			public void click(Player p, Menu arg1, ClickType arg2) {
				RankManager.manager.menus.get(Stat.MINUTOSOLINE).open(p);
			}
		});

		// BADGES
		ItemStack ba = build(PixelmonUtils.getBadge(EnumBadges.BasicBadge), "Insignias", InsigniaDB.getInsignias(uid).size() + "");

		addButton(new MenuButton(new SlotPos(2, 1), ba) {
			@Override
			public void click(Player p, Menu arg1, ClickType arg2) {
				RankManager.manager.insigniamenu.open(p);
			}
		});

		// FOTOS
		ItemStack fo = build(PixelmonUtils.getItemStack(PixelmonItems.cameraItem), "Fotografias", CameraDB.getFotos(uid).fotos.size() + "");
		addButton(new MenuButton(new SlotPos(3, 1), fo) {

			@Override
			public void click(Player p, Menu arg1, ClickType arg2) {
				RankManager.manager.fotosmenu.open(p);
			}
		});
		for (Stat s : Stat.values()) {
			if (s == Stat.MINUTOSOLINE) {
				continue;
			}
			ItemStack si = build(s.getIcone().copy(), s.getNome(), "" + Stats.geStats(uid, s));

			addButtonToSquare(new SlotPos(0, 1), new SlotPos(8, 2), new MenuButton(new SlotPos(0, 0), si){
				@Override
				public void click(Player p, Menu arg1, ClickType arg2) {
					RankManager.manager.menus.get(s).open(p);
				}
			});
		}

	}

	public ItemStack build(ItemStack it, String oq, String valor) {
		ItemStack si = it.copy();
		ItemUtils.setItemName(si, Txt.f("§e§l" + oq));
		ItemUtils.addLore(si, Txt.f("§6" + valor));
		ItemUtils.addLore(si, Txt.f(""));
		ItemUtils.addLore(si, Txt.f("§bClique para ver o rank!"));
		
		return si;

	}
}
