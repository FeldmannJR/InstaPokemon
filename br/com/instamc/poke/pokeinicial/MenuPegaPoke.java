package br.com.instamc.poke.pokeinicial;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import scala.actors.threadpool.Arrays;
import scala.util.Random;

public class MenuPegaPoke extends Menu {

	public MenuPegaPoke() {
		super("Pokemon Inicial", 3);
		for (EnumPokemon en : PokeInicialManager.pokes) {
			ItemStack it = PixelmonUtils.getPixelmonIcon(en, false);
			ItemUtils.setItemName(it, Txt.f("§e§lEscolher: §a§l" + en.name));
			ItemUtils.ClearLore(it);
			ItemUtils.addLore(it, Txt.f("§fClique aqui para escolher"));
			ItemUtils.addLore(it, Txt.f("§fum §6" + en.name + "§f para começar"));
			ItemUtils.addLore(it, Txt.f("§fsua jornada."));
			addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), it) {

				@Override
				public void click(Player p, Menu arg1, ClickType arg2) {
					PokeInicialManager.pegaPoke(p, en, false);
					close(p);
				}
			});
		}
		addButton(new MenuButton(new SlotPos(8, 2), ItemStackBuilder.of(ItemTypes.SPONGE).withName("§c§lAleatório").withLore("§fClique aqui para pegar", "§fum pokemon inicial aleatorio").build()) {

			@Override
			public void click(Player p, Menu arg1, ClickType arg2) {
				PokeInicialManager.pegaPoke(p, PokeInicialManager.pokes.get(new Random().nextInt(PokeInicialManager.pokes.size())),true);
				close(p);

			}
		});
	}

}
