package br.com.instamc.poke.shop.chest;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class PokemonShop extends ShopItem {

	EnumPokemon pokemon;
	boolean shiny = false;

	public PokemonShop(SlotPos pos, int preco, ShopType type, EnumPokemon poke,boolean shiny) {
		super(pos, preco, type);

		pokemon = poke;
		this.shiny = shiny;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack buildItem() {
		ItemStack it = PixelmonUtils.getPixelmonIcon(pokemon, false);
		ItemUtils.setItemName(it, Txt.f("§cPokemon§f: §e" + pokemon.name));
		return it;
	}

}
