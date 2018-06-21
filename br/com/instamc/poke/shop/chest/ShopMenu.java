package br.com.instamc.poke.shop.chest;

import java.util.List;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;

public class ShopMenu extends Menu {

	public boolean vip = false;
	public EnumBadges bad;

	public ShopMenu(List<ShopItem> itens, boolean vip, EnumBadges badge,int linhas) {

		super(getNome(vip, badge), linhas);
		for (ShopItem it : itens) {

			if (it.slot == null) {
				addButtonNextSlot(new ShopButton(new SlotPos(0, 0), it, this));
			} else {
				addButton(new ShopButton(it.slot, it, this));
			}
		}
		this.vip = vip;
		this.bad = badge;

	}

	public static String getNome(boolean vip, EnumBadges ba) {
		String nome = "Shop";
		if (vip) {
			nome = "ShopVIP";
		}
		if (ba != null) {
			nome += " " + ba.name().replace("Badge","");
		}
		return nome;
	}

	public static int slots(List<ShopItem> itens) {
		int maiorlinha = 3;
		for (ShopItem it : itens) {
			if (it.slot != null) {
				int f = it.slot.getY() + 1;
				if (f > maiorlinha) {
					maiorlinha = f;
				}
			}
		}
		return maiorlinha;
	}

}
