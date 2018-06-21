package br.com.instamc.poke.shop.chest;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

public class ItemStackShop extends ShopItem {

	ItemStack it;

	public ItemStackShop(SlotPos pos, int preco, ShopType type, ItemStack it) {
		super(pos, preco, type);
		this.it = it;

	}

	@Override
	public ItemStack buildItem() {
		return it.copy();
	}

}
