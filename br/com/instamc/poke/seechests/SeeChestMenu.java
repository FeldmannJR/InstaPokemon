package br.com.instamc.poke.seechests;

import java.util.HashMap;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;

public class SeeChestMenu extends Menu {

	public SeeChestMenu(int linhas, HashMap<SlotPos, ItemStack> itens) {
		super("Ver Bau", linhas);
		// TODO Auto-generated constructor stub
		for (SlotPos s : itens.keySet()) {
			addButton(new NothingButton(s, itens.get(s)));
		}
	}

}
