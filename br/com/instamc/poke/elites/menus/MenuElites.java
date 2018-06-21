package br.com.instamc.poke.elites.menus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.block.BlockState;

import org.spongepowered.api.block.tileentity.TileEntityArchetype;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class MenuElites extends Menu {

	int page = 0;

	public MenuElites(int page) {

		super("Elites " + page, 6);
		this.page = page;

		for (Elite el : getPage(page)) {

			ItemStack it = el.buildIcone(true);
			ItemUtils.addLore(it, Txt.f("§7Clique para ver os membros!"));
			addButtonNextSlot(new MenuButton(new SlotPos(0, 0), it) {

				@Override
				public void click(Player p, Menu arg1, ClickType arg2) {
					Elite att = EliteManager.getEliteById(el.getId());
					if (att != null) {
						MenuVerElite m = new MenuVerElite(att);
						m.retornapage = page;
						m.addRetorna();
						m.open(p);
					}

				}
			});

		}
		if (page > 1) {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§7Clique para voltar", "§7para a pagina " + (page - 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new MenuElites(page - 1).open(arg0);
					;

				}
			});
		}
		if (page < getPages()) {
			addButton(new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR).withName("§e§lAvançar").withLore("§7Clique para avançar", "§7para a pagina " + (page + 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new MenuElites(page + 1).open(arg0);
					;

				}
			});
		}

	}

	public static int getPages() {
		int size = EliteManager.getCached().size();

		return 1 + ((size - 1) / 45);
	}

	public static List<Elite> getPage(int page) {
		page = page - 1;
		ArrayList<Elite> infos = new ArrayList();
		int foi = 0;
		int start = page * 45;
		List<Elite> bonita = EliteManager.getCached();
		Collections.sort(bonita, new Comparator<Elite>() {

			@Override
			public int compare(Elite o1, Elite o2) {
				if (o1.fundada.before(o2.fundada)) {
					return -1;
				}
				if (o2.fundada.before(o1.fundada)) {
					return 1;
				}
				return 0;
			}
		});

		for (Elite info : bonita) {
			if (foi >= (start + 45)) {
				break;
			}
			if (foi >= start) {
				infos.add(info);
			}
			foi++;
		}
		return infos;

	}

}
