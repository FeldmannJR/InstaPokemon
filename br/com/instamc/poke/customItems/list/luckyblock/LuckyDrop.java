package br.com.instamc.poke.customItems.list.luckyblock;

import org.spongepowered.api.item.inventory.ItemStack;

public class LuckyDrop {
	ItemStack stack;
	Rarity r;
	
	public static enum Rarity {
		COMUM,
		INCOMUM,
		RARO,
		EPICO;
	}
	public LuckyDrop(ItemStack it,Rarity r) {
		this.stack = it;
		this.r = r;
		// TODO Auto-generated constructor stub
	}

}
