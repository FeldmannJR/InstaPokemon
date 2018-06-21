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
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.item.Item;

public class TMAleatorio extends CustomItem {

	public TMAleatorio() {
		super("TM Aleatório", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar ganha um", "TM aleatório!");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {
		if(InventoryUtils.getEmpty(player)<1){
			player.sendMessage(Txt.f("§aSeu inventário está lotado! Libere espaço!"));
			return;
		}
		ArrayList<Item> tMs = new ArrayList<Item>(PixelmonItemsTMs.TMs);
		ItemTM it = (ItemTM) tMs.get(new Random().nextInt(tMs.size()));
		
		consomeMain(player);
		InstaPokemon.sendMessage(player, "§eVocê ganhou o TM §6"+it.attackName+" §e!");
		player.getInventory().offer(PixelmonUtils.getItemStack(it));
		
		
	}

}
