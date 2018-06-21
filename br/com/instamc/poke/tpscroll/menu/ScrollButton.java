package br.com.instamc.poke.tpscroll.menu;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.tpscroll.TPScrollInfo;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;

public class ScrollButton extends MenuButton {

	TPScrollInfo info;

	public ScrollButton(TPScrollInfo inf) {
		super(inf.toItemStack());
		this.info = inf;
		// TODO Auto-generated constructor stub
	}

	public void updateItem() {
		setItemStack(info.toItemStack());

	}

	@Override
	public void click(Player p, Menu arg1, ClickType arg2) {

		if (info.isValid()) {
			p.setLocation(info.getLocation().copy());
			InstaPokemon.sendMessage(p, "§aTeleportado para §e" + info.getNome() + " §a!");
			close(p);
			
		}

	}

}
