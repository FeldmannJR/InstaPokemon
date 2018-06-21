package br.com.instamc.poke.ranks.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.poke.ranks.RankCache;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public abstract class RankMenu extends Menu {

	RankCache rank;
	String nome;

	ItemStack it;

	public abstract String processaInfo(int x);

	public RankMenu(String nome, ItemStack it) {
		super("Rank " + nome, 6);
		this.nome = nome;
		this.it = it;
		addButton(new NothingButton(new SlotPos(4, 0), it.copy()));
	}

	public void updateButtons(RankCache cache) {
		this.rank = cache;

		for (SlotPos pos : Menu.buildSquare(new SlotPos(0, 1), new SlotPos(8, 5))) {
			removeButton(pos);
		}
		List<UUID> uids = new ArrayList();
		uids.addAll(rank.top.keySet());
		Collections.sort(uids, new Comparator<UUID>() {

			@Override
			public int compare(UUID u1, UUID u2) {
				int q1 = rank.top.get(u1);
				int q2 = rank.top.get(u2);
				if (q1 > q2) {
					return -1;
				}
				if (q2 > q1) {
					return 1;
				}
				return 0;
			}
		});

		int pos = 1;
		for (final UUID uid : uids) {
			int quanto = rank.top.get(uid);

			ItemStack i = ItemStack.of(ItemTypes.SKULL, 1);

			String nome = UUIDUtils.getName(uid);
			if (nome != null) {
			//	i = LibUtils.getHeadOf(nome);
				if(i==null){
					 i = ItemStack.of(ItemTypes.SKULL, 1);
				}
			} else {
				nome = "Desconhecido";
			}

			ItemUtils.setItemName(i, Txt.f("§6§l" + pos + "º§7. §f" + nome));
			ItemUtils.addLore(i, Txt.f("§6" + processaInfo(quanto)));
			final String nomef = nome;
			addButtonToSquare(new SlotPos(0, 1), new SlotPos(8, 5), new MenuButton(new SlotPos(0, 0), i) {

				@Override
				public void click(Player p, Menu arg1, ClickType arg2) {
					if (!nomef.equalsIgnoreCase("Desconhecido")) {
						new VerStatsMenu(uid).open(p);
					}
				}
			});
			pos++;
		}
		while (pos <= 45) {
			S: for (SlotPos s : Menu.buildSquare(new SlotPos(0, 1), new SlotPos(8, 5))) {
				if (getButton(s) == null) {
					ItemStack i = ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1);
					i.offer(Keys.DYE_COLOR, DyeColors.GRAY);
					ItemUtils.setItemName(i, Txt.f("§6§l" + pos + "º§f§l. §7§lNinguém"));
					addButton(new NothingButton(s, i));
					break S;
				}
			}
			pos++;
		}

	}

}
