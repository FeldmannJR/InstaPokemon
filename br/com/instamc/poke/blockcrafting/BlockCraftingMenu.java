package br.com.instamc.poke.blockcrafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.MenuCloseAction;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class BlockCraftingMenu extends Menu {

	public BlockCraftingMenu(int page) {
		super("BlockCrafting " + page, 6);
		addButton(new MenuButton(new SlotPos(4, 5), ItemStackBuilder.of(ItemTypes.EMERALD).withName("§a§lAdicionar").withLore("§7Clique para bloquear crafting!").build()) {

			@Override
			public void click(Player arg0, Menu arg1, ClickType arg2) {
				Menu m = new Menu("Adicionar Block", 3);
				m.setMoveItens(true);
				m.addClose(new MenuCloseAction() {

					@Override
					public void closeMenu(Player p, Menu m) {
						List<ItemStack> it = m.getNonButtons();
						int add = 0;
						for (ItemStack i : it) {
							if (!BlockCrafting.instance.isBloqueado(i)) {
								BlockCrafting.instance.itens.add(i);
								add++;
							}
						}
						p.sendMessage(Txt.f("§aBloqueado " + add + " itens!"));
						BlockCrafting.instance.save();
						BlockCrafting.instance.reloadCraftings();

					}
				});
				m.open(arg0);

			}
		});

		for (ItemStack i : getPage(page)) {
			ItemStack copy = i.copy();
			ItemUtils.addLore(copy, Txt.f("§cClique para permitir o craft!"));
			addButtonNextSlot(new MenuButton(new SlotPos(0, 0), copy) {

				@Override
				public void click(Player pp, Menu arg1, ClickType arg2) {
					if (BlockCrafting.instance.itens.contains(i)) {
						BlockCrafting.instance.itens.remove(i);
						BlockCrafting.instance.save();
						BlockCrafting.instance.reloadCraftings();
						new BlockCraftingMenu(page).open(pp);
						pp.sendMessage(Txt.f("§aCrafting permitido!"));
						

					} else {
						pp.sendMessage(Txt.f("§cUé!"));
					}

				}
			});
		}

		if (page > 1) {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§7Clique para voltar", "§7para a pagina " + (page - 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new BlockCraftingMenu(page - 1).open(arg0);
					;

				}
			});
		}
		if (page < getPages()) {
			addButton(new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR).withName("§e§lAvançar").withLore("§7Clique para avançar", "§7para a pagina " + (page + 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new BlockCraftingMenu(page + 1).open(arg0);
					;

				}
			});
		}

		// TODO Auto-generated constructor stub
	}

	public static int getPages() {
		int size = BlockCrafting.instance.itens.size();

		return 1 + ((size - 1) / 45);
	}

	public static List<ItemStack> getPage(int page) {
		page = page - 1;
		ArrayList<ItemStack> infos = new ArrayList();
		int foi = 0;
		int start = page * 45;

		for (ItemStack info : BlockCrafting.instance.itens) {
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
