package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemCoveredFossil;
import com.pixelmonmod.pixelmon.items.ItemEvolutionStone;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
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

public class ItemEvolutivoAleatorio extends CustomItem {

	public ItemEvolutivoAleatorio() {
		super("Item Evolutivo Aleatório", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar ganha um", "item evolutivo aleatório!");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {
		if (InventoryUtils.getEmpty(player) < 1) {
			player.sendMessage(Txt.f("§aSeu inventário está lotado! Libere espaço!"));
			return;
		}
		ArrayList<Item> evo = new ArrayList<>();

		evo.addAll(PixelmonItems.getEvostoneList());
		evo.add(PixelmonItemsHeld.electirizer);
		evo.add(PixelmonItemsHeld.magmarizer);
		evo.add(PixelmonItemsHeld.protector);
		evo.add(PixelmonItemsHeld.razorFang);
		evo.add(PixelmonItemsHeld.razorClaw);
		evo.add(PixelmonItemsHeld.deepSeaScale);
		evo.add(PixelmonItemsHeld.deepSeaTooth);
		evo.add(PixelmonItemsHeld.prismScale);
		evo.add(PixelmonItemsHeld.reaperCloth);
		evo.add(PixelmonItemsHeld.metalCoat);
		evo.add(PixelmonItemsHeld.dubiousDisc);
		evo.add(PixelmonItemsHeld.upGrade);
		evo.add(PixelmonItemsHeld.ovalStone);
		evo.add(PixelmonItemsHeld.dragonScale);
		evo.add(PixelmonItemsHeld.kingsRock);
		Item it = evo.get(new Random().nextInt(evo.size()));
		
		consomeMain(player);
		InstaPokemon.sendMessage(player, "§eVocê ganhou §6" + it.getUnlocalizedName().replace("item.","").replace("_", " ") + " §e!");
		player.getInventory().offer(PixelmonUtils.getItemStack(it));

	}

}
