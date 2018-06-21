package br.com.instamc.poke.customItems;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.living.humanoid.HandInteractEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import br.com.instamc.poke.customItems.CustomItem.ClickInteract;
import br.com.instamc.poke.customItems.CustomItem.InteractType;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class CustomItemManager {

	public static EnumCustomItems getItem(ItemStack it) {
		if (it == null) {
			return null;
		}
		List<Text> lore = it.getOrElse(Keys.ITEM_LORE, new ArrayList<Text>());
		for (Text t : lore) {
			if (t.toPlain().startsWith("cit:")) {
				String classe = t.toPlain().split("cit:")[1];
				for (EnumCustomItems e : EnumCustomItems.values()) {
					if (e.getItem().getClass().getSimpleName().equalsIgnoreCase(classe)) {
						return e;
					}
				}
			}

		}
		return null;

	}

	public void chama(CustomItem item,CustomItem.ClickInteract click, CustomItem.InteractType type, Player p) {
	
		item.interact(p, type, click);
	}

	@Listener
	public void interactPrimary(InteractItemEvent.Secondary.MainHand ev, @First Player p) {
		if (ev.getItemStack() != null) {
			if (getItem(ev.getItemStack().createStack()) != null) {
				CustomItem it = getItem(ev.getItemStack().createStack()).getItem();
				chama(it,ClickInteract.RIGHT, InteractType.AIR, p);

			}

		}
	}

	public static void consomeMain(Player p){
		if(p.getItemInHand(HandTypes.MAIN_HAND).isPresent()){
			ItemStack it = p.getItemInHand(HandTypes.MAIN_HAND).get();
			if(it.getQuantity()>1){
				it = it.copy();
				it.setQuantity(it.getQuantity()-1);
				p.setItemInHand(HandTypes.MAIN_HAND, it);
			}else{
				p.setItemInHand(HandTypes.MAIN_HAND, null);
			}
		}
		
	}
	@Listener
	public void interactPrimary(InteractItemEvent.Primary.MainHand ev, @First Player p) {
		if (ev.getItemStack() != null) {
			if (getItem(ev.getItemStack().createStack()) != null) {
				CustomItem it = getItem(ev.getItemStack().createStack()).getItem();
				chama(it,ClickInteract.LEFT, InteractType.AIR, p);

			}

		}
	}

	@Listener
	public void interactitem(InteractItemEvent ev) {
		if (ev.getItemStack() != null) {
			if (getItem(ev.getItemStack().createStack()) != null) {
				CustomItem it = getItem(ev.getItemStack().createStack()).getItem();
				ev.setCancelled(true);

			}

		}

	}

	
}
