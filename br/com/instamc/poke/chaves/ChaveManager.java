package br.com.instamc.poke.chaves;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class ChaveManager {

	public static ItemStack geraChave(String key) {

		ItemStack chave = ItemStack.of(ItemTypes.PAPER, 1);
		ItemUtils.setItemName(chave, Txt.f("§eChave:" + key));
		return chave;

	}

	public static String getChaveFromItem(ItemStack it) {
		String nome = it.get(Keys.DISPLAY_NAME).orElse(Txt.f("")).toPlain();
		if (nome.startsWith("Chave:")) {
			return nome.split(":")[1];
		}

		return null;

	}

}
