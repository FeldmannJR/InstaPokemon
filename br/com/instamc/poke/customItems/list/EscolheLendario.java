package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.CustomItemManager;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.customItems.list.menu.EscolhePokeUsaItem;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.Cooldown;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.item.Item;

public class EscolheLendario extends CustomItem {

	public EscolheLendario() {
		super("Escolhe Pokemon Lendário", ItemStackBuilder.of(ItemTypes.PAPER).build(), "Ao usar você pode escolher", "um pokemon lendário!");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {
		
		if (Cooldown.isCooldown(player, "escolhepokelitem")) {
			player.sendMessage(Txt.f("§eAguarde para usar o item novamente"));
			return;
		}
		Cooldown.addCoolDown(player, "escolhepokelitem", 1000);
		
		new EscolhePokeUsaItem(EnumCustomItems.EscolheLendario,"Escolhe Lendário") {
			
			@Override
			public boolean isValid(EnumPokemon poke) {
				return PixelmonUtils.isLegendery(poke);
				
			}
			
			@Override
			public boolean isShiny() {
				// TODO Auto-generated method stub
				return false;
			}
		}.open(player);

	}

}
