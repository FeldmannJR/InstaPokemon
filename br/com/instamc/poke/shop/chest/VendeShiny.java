package br.com.instamc.poke.shop.chest;

import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class VendeShiny extends ShopItem {

	

	public VendeShiny(SlotPos pos, int preco) {
		super(pos, preco,ShopType.VENDA);

	
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack buildItem() {
		ItemStack it = ItemStack.of(ItemTypes.NETHER_STAR, 1);
		ItemUtils.setItemName(it, Txt.f("§6Vender Shiny"));
		return it;
	}

}
