package br.com.instamc.poke.shop;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.shop.menus.CompraPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ChatInputAPI;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public abstract class CompraPokeMenu extends Menu {

	String pesquisa = null;
	int page = 1;

	public CompraPokeMenu(String nome, int page) {
		super(nome, 6);
		openPage(page);
		//System.out.println("Disponiveis: " + getValidPokemons().size());
		// TODO Auto-generated constructor stub
	}

	public abstract boolean isValid(EnumPokemon poke);

	public abstract void clickPoke(Player p, EnumPokemon poke);

	public abstract ItemStack buildItem(ItemStack it, EnumPokemon poke);

	public abstract boolean isShiny();

	public void openPage(int page) {
		this.page = page;
		addItems();

	}

	public void addItems() {
		for (MenuButton b : new ArrayList<MenuButton>(getButtons())) {
			this.removeButton(b.getSlot());
		}
		MenuButton next = getNextButton();
		MenuButton previous = getPreviousButton();

		if (next != null)
			addButton(next);
		if (previous != null)
			addButton(previous);
		addButton(getPesquisaButton());

		for (EnumPokemon poke : getPage(page)) {
			ItemStack cv = PixelmonUtils.getPixelmonIcon(poke, isShiny());
			ItemUtils.setItemName(cv, Txt.f("§e§l" + poke.name + (isShiny() ? " §6§lSHINY" : "")));
			cv = buildItem(cv, poke);
			this.addButtonNextSlot(new MenuButton(new SlotPos(0, 0), cv) {

				@Override
				public void click(Player pl, Menu arg1, ClickType arg2) {
					clickPoke(pl, poke);
				}
			});
		}

	}

	public MenuButton getPesquisaButton() {
		ItemStack busca = ItemStackBuilder.of(ItemTypes.PAPER).withName("§e§lFiltrar Nomes").build();
		if (pesquisa != null) {
			ItemUtils.addLore(busca, Txt.f("§e§lAtual: §f" + pesquisa));
		}
		return new MenuButton(new SlotPos(4, 5), busca) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				close(player);
				ChatInputAPI.inputPlayer(player, new ChatInputAPI.ChatAction(15) {

					@Override
					public void inputText(Player player, String string) {
						pesquisa = string;
						openPage(1);
						open(player);
					}
				}, "Nome do pokemon");
			}
		};

	}

	public MenuButton getNextButton() {

		if (!getPage(page + 1).isEmpty()) {
			return new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR).withName("§e§lProxima").withLore("§fClique aqui para ir para", "§fa proxima pagina.").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					openPage(page + 1);
				}
			};
		}
		return null;
	}

	public MenuButton getPreviousButton() {

		if (page > 1) {
			return new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§fClique aqui para voltar!").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					openPage(page - 1);
				}
			};
		}
		return null;
	}

	public List<EnumPokemon> getValidPokemons() {
		ArrayList<EnumPokemon> pokes = new ArrayList();
		for (EnumPokemon poke : EnumPokemon.values()) {
			if (isValid(poke)) {
				if (pesquisa == null || poke.name.toLowerCase().contains(pesquisa.toLowerCase())) {
					pokes.add(poke);
				}
			}
		}
		return pokes;

	}

	public List<EnumPokemon> getPage(int page) {
		ArrayList<EnumPokemon> l = new ArrayList();
		int foi = 0;
		int comeca = (page - 1) * 45;
		int termina = page * 45;
		for (EnumPokemon n : getValidPokemons()) {

			if (foi >= termina) {
				break;
			}

			if (foi >= comeca) {
				l.add(n);
			}

			foi++;

		}
		return l;
	}

}
