package br.com.instamc.poke.shop.chest;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public abstract class ShopItem {
	int preco;
	ShopType type;
	SlotPos slot;

	public ShopItem(SlotPos slot, int preco, ShopType type) {
		this.slot = slot;
		this.preco = preco;
		this.type = type;
	}

	public ItemStack getItem() {
		ItemStack it = buildItem();
		ItemUtils.addLore(it,Txt.f(""));
		if (type == ShopType.COMPRA) {
			
			ItemUtils.addLore(it, Txt.f("§a§lCompre §epor §f"+preco+" §6coins!"));
		} else {
			ItemUtils.addLore(it, Txt.f("§c§lVenda §epor §f"+preco+" §6coins!"));
			
		}
		return it;

	}

	public abstract ItemStack buildItem();

	public static enum ShopType {
		VENDA, COMPRA;
	}
}
