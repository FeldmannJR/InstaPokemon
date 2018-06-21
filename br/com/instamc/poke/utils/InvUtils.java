package br.com.instamc.poke.utils;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class InvUtils {
	public static boolean remove(Player p, ItemStack it, int qtd) {
		if (tem(p, it) < qtd) {
			return false;
		}
		int falta = qtd;
		for (Inventory i : p.getInventory().slots()) {
			if (falta == 0)
				break;
			Optional<ItemStack> peek = i.peek();
			if (peek.isPresent()) {
				ItemStack temp = it.copy();

				if (compare(temp, peek.get())) {
					int iqtd = peek.get().getQuantity();
					if (falta < iqtd) {
						iqtd = iqtd - falta;
						falta = 0;
						ItemStack set = peek.get();
						set.setQuantity(iqtd);
						i.set(set);
						continue;
					}
					if (falta >= iqtd) {
						falta = falta - iqtd;
						i.clear();
						continue;
					}
				}
			}
		}

		return true;

	}

	public static int tem(Player p, ItemStack it) {
		int x = 0;
		it = it.copy();
		it.setQuantity(-1);

		int y = 0;
		for (Inventory i : p.getInventory().slots()) {

			Optional<ItemStack> peek = i.peek();
			if (peek.isPresent()) {
				if (compare(it, peek.get())) {
					ItemStack inv = peek.get();

					x += inv.getQuantity();
				}
			}
		}

		return x;

	}

	public static boolean compare(ItemStack it, ItemStack it1) {
		it = it.copy();
		ItemUtils.addLore(it, Txt.f(" "));
		it1 = it1.copy();
		ItemUtils.addLore(it1, Txt.f(" "));
		net.minecraft.item.ItemStack nit = ItemStackUtil.toNative(it);
		net.minecraft.item.ItemStack nit1 = ItemStackUtil.toNative(it1);

		return nit.isItemEqual(nit1) && nit.areItemStackTagsEqual(nit, nit1);

	}
}
