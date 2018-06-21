package br.com.instamc.poke.tpscroll.menu;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.tpscroll.TPScrollInfo;
import br.com.instamc.poke.tpscroll.TPScrollManager;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;

public class ScrollMenu extends Menu {

	UUID uid;
	int update = 0;

	public ScrollMenu(Player p) {
		super("Teleportes", 3);
		this.uid = p.getUniqueId();

		List<TPScrollInfo> infos = TPScrollManager.getInfo(uid);
		for (TPScrollInfo info : infos) {
			addButtonNextSlot(new ScrollButton(info));
		}
	}

	@Override
	public void update() {
		update++;
		if (update == 20) {
			for (MenuButton botao : getButtons()) {
				if (botao instanceof ScrollButton) {
					ScrollButton sb = (ScrollButton) botao;
					sb.updateItem();
				}
			}
			update = 0;
		}

	}

}
