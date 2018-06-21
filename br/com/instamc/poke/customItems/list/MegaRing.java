package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.EconAPI;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.item.Item;

public class MegaRing extends CustomItem {

	public MegaRing() {
		super("Vale Mega Ring", ItemStackBuilder.of(ItemTypes.END_CRYSTAL).build(), "Ao usar ganha", "um mega ring!");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {
		boolean tem = PixelmonUtils.getPlayerStorage(player).megaData.canEquipMegaItem();
		if (tem) {
			player.sendMessage(Txt.f("§c§lVocê já tem o Mega Ring!"));
			return;
		}
		PixelmonUtils.giveBracelet(player);
		InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item §dVale Mega Ring §d!");

		consomeMain(player);
	
		
		
	}

}
