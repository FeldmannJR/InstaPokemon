package br.com.instamc.poke.customItems;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public abstract class CustomItem {

	String nome;
	String[] desc;
	ItemStack it;

	public CustomItem(String nome, ItemStack it, String... desc) {
		this.nome = nome;
		this.desc = desc;
		this.it = it;

	}

	public void consomeMain(Player p) {
		CustomItemManager.consomeMain(p);

	}

	public ItemStack toItemStack() {
		return toItemStack(1);
	}

	public ItemStack toItemStack(int qtd) {
		ItemStack item = it.copy();
		item.setQuantity(qtd);
		ItemUtils.setItemName(item, Txt.f("§6§l" + nome));
		if (desc != null) {
			for (String s : desc) {
				ItemUtils.addLore(item, Txt.f("§f" + s));
			}

		}
		ItemUtils.addLore(item, getLoreIdentifier());
		return item;
	}

	public Text getLoreIdentifier() {
		return Txt.f("§0cit:" + this.getClass().getSimpleName());

	}

	public abstract void interact(Player p, InteractType type, ClickInteract click);

	public static enum InteractType {
		AIR,
		BLOCK
	}

	public static enum ClickInteract {
		RIGHT,
		LEFT
	}
}
