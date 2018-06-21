package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.effect.sound.SoundTypes;
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

public class PokemonAleatorio extends CustomItem {

	public PokemonAleatorio() {
		super("Pokemon Aleatório", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar ganha um", "pokemon aleatório!");
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
		PixelmonUtils.give(player, PixelmonUtils.build(player, lend, EnumPokeballs.PokeBall, 1, false));
		InstaPokemon.sendMessage(player, "§fVocê ganhou um §6" + lend.name + " !");
		InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item Pokemon Aleatório e ganhou §a" + lend.name + "§e!");
		player.playSound(SoundTypes.ENTITY_FIREWORK_BLAST, player.getLocation().getPosition().clone(), 1);
		
		consomeMain(player);

	}

}
