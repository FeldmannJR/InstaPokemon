package br.com.instamc.poke.sorteios;

import java.util.Date;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;

public class MenuVerSorteios extends Menu {

	public MenuVerSorteios(List<Sorteio> sort) {
		super("Sorteios", getSize(sort.size()) / 9);
		for (Sorteio s : sort) {
			final int id = s.id;
			ItemStack paper = ItemStackBuilder.of(ItemTypes.PAPER).withName("§e§lConcurso " + s.id).withLore("§eSorteio:§f " + s.getTerminaFormatado(),"§ePreço: §6"+s.preco+" coins.").build();
			addButtonNextSlot(new MenuButton(new SlotPos(0, 0), paper) {

				@Override
				public void click(Player p, Menu arg, ClickType click) {
					Sorteio s = SorteioManager.getSorteioById(id);
					if (s != null) {
						if (s.termina.after(new Date(System.currentTimeMillis()))) {
							new SorteioMenu(s).open(p);
							return;
						}

					}
					close(p);
				}
			});

		}

	}

	public static int getSize(int x) {
		while (x % 9 != 0) {
			x++;
		}
		return x;
	}

}
