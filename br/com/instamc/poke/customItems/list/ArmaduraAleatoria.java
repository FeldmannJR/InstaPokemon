package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Color;

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

public class ArmaduraAleatoria extends CustomItem {

	public ArmaduraAleatoria() {
		super("Armadura de Couro", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar ganha uma", "armadura de couro!");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {
		if (InventoryUtils.getEmpty(player) < 4) {
			player.sendMessage(Txt.f("§aSeu inventário está lotado! Libere espaço!"));
			return;
		}
		populate();
		ArmorInfo i = armors.get(new Random().nextInt(armors.size()));
		player.getInventory().offer(get(ItemTypes.LEATHER_HELMET,i.helmet));
		player.getInventory().offer(get(ItemTypes.LEATHER_CHESTPLATE,i.chest));
		player.getInventory().offer(get(ItemTypes.LEATHER_LEGGINGS,i.leggings));
		player.getInventory().offer(get(ItemTypes.LEATHER_BOOTS,i.boots));
		consomeMain(player);

	}
	public ItemStack get(ItemType type, Color c){
			ItemStack i = ItemStack.of(type, 1);
			i.offer(Keys.DISPLAY_NAME,Txt.f("§5§lInsta§f§lMC §eArmadura Inicial"));
			i.offer(Keys.COLOR,c);
			return i;
	}

	public void populate(){
		if(!armors.isEmpty())return;
		//LARANJA
		ar(
				255,255,255,
				255,93,0,
				198,62,0,
				255,93,0
				);
		
		//ROXO
		ar(
				195, 66, 255,
				144,0,211,
				121, 3, 175,
				144,0,211
				);
		//VERDE
		ar(
				0,193,12,
				0,153,10,
				0,130,8,
				0,153,10
				);
		//VERMELHO
		ar(
				255,29,0,
				196, 22, 0,
				163,18,0,
				196, 22, 0
				);
		//AZUL
		ar(
				50,139,244,
				0, 94, 216,
				3, 67, 150,
				0, 94, 216
				);
		//ROSA
		ar(
				229, 41, 151,
				168, 0, 98,
				140, 2, 82,
				168, 0, 98
				);
		
		
		
	
	}

	public void ar(int r, int g, int b, int r1, int g1, int b1, int r2, int g2, int b2, int r3, int g3, int b3) {
		ArmorInfo i = new ArmorInfo();
		i.helmet = Color.ofRgb(r, g, b);
		i.chest = Color.ofRgb(r1, g1, b1);
		i.leggings = Color.ofRgb(r2, g2, b2);
		i.boots = Color.ofRgb(r3, g3, b3);
		armors.add(i);
	}

	private ArrayList<ArmorInfo> armors = new ArrayList();

	public static class ArmorInfo {
		Color helmet;
		Color chest;
		Color leggings;
		Color boots;

	}
}
