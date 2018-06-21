package br.com.instamc.poke.kits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.ClickType.Click;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;

public class KitMenu extends Menu {

	Player pl;

	public KitMenu(Player pl) {
		super("Kits", 3);
		updateButtons();
		this.pl = pl;

	}

	public void updateButtons() {

		int[] pode = new int[] { 0, 2, 4, 6, 8 };
		List<Kit> kits = new ArrayList();
		kits.addAll(KitManager.getKits());
		Collections.sort(kits, new Comparator<Kit>() {

			
			@Override
			public int compare(Kit o1, Kit o2) {
				return o1.getPriority()-o2.getPriority();
			}
		});
		
		
		FORK: for (Kit k : kits) {
			for (int x = 0; x < 3; x++) {
				for (int p : pode) {
					if (getButton(new SlotPos(p, x)) == null) {
						addButton(new MenuButton(new SlotPos(p, x), k.buildItem(this.pl)) {

							@Override
							public void click(Player player, Menu menu, ClickType click) {
								if (click.getType() == Click.LEFT) {
									KitManager.pegaKit(k, player);
									close(player);
								} else {
									Menu m = new Menu("Kit " + k.getNome(), 4);
									for (ItemStack i : k.getItens()) {
										m.addButtonNextSlot(new NothingButton(new SlotPos(0, 0), i.copy()));
									}
									m.addButton(new MenuButton(new SlotPos(0, 3),
											ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar")
													.withLore("§fClique aqui para", "§fvoltar para os kits!").build()) {

										@Override
										public void click(Player player, Menu arg1, ClickType arg2) {
											KitManager.openKits(player);

										}
									});
									m.open(player);
								}
							}
						});
						continue FORK;
					}
				}

			}
		}
	}
}
