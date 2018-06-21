package br.com.instamc.poke.customItems.list.luckyblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.customItems.list.luckyblock.LuckyDrop.Rarity;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.item.Item;

public class LuckyBlock extends CustomItem {

	public LuckyBlock() {
		super("Bloco da Sorte", ItemStackBuilder.of(ItemTypes.SPONGE).build(), "Ao clicar com o direito", "held item aleatorio!");
		// TODO Auto-generated constructor stub
	}

	public ArrayList<LuckyDrop> drop = new ArrayList<>();

	public void populate() {
		if (drop.isEmpty()) {
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(EnumApricorns.Green.apricorn(), 64), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.PokeBall.getItem(), 64), Rarity.COMUM));

			drop.add(new LuckyDrop(ItemStack.of(ItemTypes.STONE, 64), Rarity.COMUM));
			drop.add(new LuckyDrop(ItemStack.of(ItemTypes.DIRT, 64), Rarity.COMUM));
			drop.add(new LuckyDrop(ItemStack.of(ItemTypes.SAND, 64), Rarity.COMUM));
			drop.add(new LuckyDrop(ItemStack.of(ItemTypes.LOG, 64), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.fireStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.sunStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.moonStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.leafStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.shinyStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.duskStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.dawnStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.thunderStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.waterStone), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.machoBrace), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.revive), Rarity.COMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.potion, 16), Rarity.COMUM));

			//

			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.UltraBall.getItem(), 64), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.DuskBall.getItem(), 64), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.maxRevive), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.hyperPotion, 16), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.dubiousDisc), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.electirizer), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.kingsRock), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.magmarizer), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.metalCoat), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.protector), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.upGrade), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.prismScale), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.razorClaw), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.razorFang), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.sootheBell), Rarity.INCOMUM));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.goodRod), Rarity.INCOMUM));
			drop.add(new LuckyDrop(EnumCustomItems.PokemonAleatorio.getItem().toItemStack(), Rarity.INCOMUM));
			//

			drop.add(new LuckyDrop(EnumCustomItems.PokemonShinyAleatorio.getItem().toItemStack(), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.luckyEgg), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.everStone), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.destinyKnot), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.expShare), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.rareCandy, 10), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.MasterBall.getItem(), 1), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.superRod), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.wailmerPail), Rarity.RARO));
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.newRunningShoes), Rarity.RARO));
			for (Item it : PixelmonItemsTMs.TMs) {
				drop.add(new LuckyDrop(PixelmonUtils.getItemStack(it), Rarity.RARO));
			}
			for (Item it : PixelmonItemsTMs.HMs) {
				drop.add(new LuckyDrop(PixelmonUtils.getItemStack(it), Rarity.RARO));
			}

			//
			drop.add(new LuckyDrop(PixelmonUtils.getItemStack(PixelmonBlocks.mechanicalAnvil), Rarity.EPICO));
			drop.add(new LuckyDrop(EnumCustomItems.Lendario.getItem().toItemStack(), Rarity.EPICO));

		}
	}

	/*
	 * 
	 * 
	 * public void populate() { if (drop.isEmpty()) { drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.DuskBall.getItem(),
	 * 10), Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.UltraBall.getItem(),
	 * 10), Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.fireStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.sunStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.moonStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.leafStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.shinyStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.duskStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.dawnStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.thunderStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.waterStone),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.magnet),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.metronome),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.machoBrace),
	 * Rarity.COMUM)); drop.add(new
	 * LuckyDrop(EnumCustomItems.PokemonAleatorio.getItem().toItemStack(),
	 * Rarity.COMUM)); // drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.dubiousDisc),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.electirizer),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.kingsRock),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.magmarizer),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.metalCoat),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.protector),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.upGrade),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.prismScale),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.razorClaw),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.razorFang),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.sootheBell),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.revive),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.hyperPotion),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.goodRod),
	 * Rarity.INCOMUM)); drop.add(new
	 * LuckyDrop(EnumCustomItems.PokemonShinyAleatorio.getItem().toItemStack(),
	 * Rarity.INCOMUM)); // drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.luckyEgg),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.everStone),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.destinyKnot),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItemsHeld.expShare),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.rareCandy, 5),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.maxRevive),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.maxPotion),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(EnumPokeballs.MasterBall.getItem(),
	 * 3), Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.superRod),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.wailmerPail),
	 * Rarity.RARO)); drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonItems.newRunningShoes),
	 * Rarity.RARO)); for (Item it : PixelmonItemsTMs.TMs) { drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(it), Rarity.RARO)); } for (Item it :
	 * PixelmonItemsTMs.HMs) { drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(it), Rarity.RARO)); }
	 * 
	 * // drop.add(new
	 * LuckyDrop(PixelmonUtils.getItemStack(PixelmonBlocks.mechanicalAnvil),
	 * Rarity.EPICO)); drop.add(new
	 * LuckyDrop(EnumCustomItems.Lendario.getItem().toItemStack(),
	 * Rarity.EPICO));
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * 
	 */

	@Override
	public void interact(Player p, InteractType type, ClickInteract click) {

		if(InventoryUtils.getEmpty(p)<1){
			p.sendMessage(Txt.f("§aVocê não tem espaço no inventário!"));
			return;
		}
		int x = new Random().nextInt(100);
		Rarity r = Rarity.COMUM;
		if (x == 0) {
			r = Rarity.EPICO;
		}
		if (x >= 1 && x <= 30) {
			r = Rarity.INCOMUM;

		}
		if (x > 30 && x <= 40) {
			r = Rarity.RARO;
		}

		List<LuckyDrop> pode = new ArrayList<>();
		populate();
		for(LuckyDrop l : drop){
			if(l.r==r){
			pode.add(l);
			}
		}
		LuckyDrop sorteado = pode.get(new Random().nextInt(pode.size()));
		p.getInventory().offer(sorteado.stack.copy());
		InstaPokemon.sendMessage(p, "§aVocê abriu uma Lucky Block!");
		p.playSound(SoundTypes.ENTITY_PLAYER_LEVELUP,p.getLocation().getPosition(), 1);
		consomeMain(p);

	}

}
