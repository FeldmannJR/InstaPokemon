package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
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

public class PokemonShinyAleatorio extends CustomItem {

	public PokemonShinyAleatorio() {
		super("Pokemon Shiny Aleatório", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar ganha um", "pokemon shiny aleatório!");
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {

		List<EnumPokemon> lendarios = new ArrayList();
		for (EnumPokemon e : EnumPokemon.values()) {
			if (!PixelmonUtils.isLegendery(e)) {
				lendarios.add(e);
			}
		}

		EnumPokemon lend = lendarios.get(new Random().nextInt(lendarios.size()));
		PixelmonUtils.give(player, PixelmonUtils.build(player, lend, EnumPokeballs.PokeBall, 1, true));
		InstaPokemon.sendMessage(player, "§fVocê ganhou um §6" + lend.name + " !");
		InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item §9Pokemon §6§lShiny §7Aleatório §ee ganhou §a" + lend.name + "§e!");

		consomeMain(player);

	}

}
