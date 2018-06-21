/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop.menus;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.poke.shop.ShopManager;

import br.com.instamc.sponge.library.apis.ChatInputAPI;

import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;

import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class CompraPokemon extends Menu {

	String filtrar = null;

	public CompraPokemon(String filtrar, int page) {

		super("Comprar Pokemon", 6);
		this.filtrar = filtrar;

		for (final EnumPokemon pk : getPage(filtrar, page)) {
			ItemStack cv = PixelmonUtils.getPixelmonIcon(pk, false);
			ItemUtils.setItemName(cv, Txt.f("§e§l" + pk.name));
			ItemUtils.addLore(cv, Txt.f("§fClique aqui para comprar"));
			ItemUtils.addLore(cv, ShopManager.buildPreco( ShopManager.precoPokemon ));
			addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), cv) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new VendaMenu(SpongeVIP.moedashop, PixelmonUtils.getPixelmonIcon(pk, false), "Comprar Pokemon",
							ShopManager.precoPokemon, "Clique aqui para comprar um " + pk.name + ".") {

						@Override
						public void cancela(Player player) {
							close(player);
						}

						@Override
						public boolean compra(Player player) {
							InstaPokemon.sendMessage(player, "§aVocê comprou um §e" + pk.name + "§a.");
							PixelmonUtils.give(player,
									PixelmonUtils.build(player, pk, EnumPokeballs.PokeBall, 1, false));
							SpongeVIP.sendMessageComprou(player, "Um Pokemon Escolhido (" + pk.name + ")",
									ShopManager.precoPokemon);
							return true;
						}
					}.open(player);
				}
			});
		}
		if (page > 1) {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar")
					.withLore("§fClique aqui para voltar!").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new CompraPokemon(filtrar, page - 1).open(player);
				}
			});
		}
		if (!getPage(filtrar, page + 1).isEmpty()) {
			addButton(new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR)
					.withName("§e§lProxima").withLore("§fClique aqui para ir para", "§fa proxima pagina.").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new CompraPokemon(filtrar, page + 1).open(player);
				}
			});
		}
		ItemStack busca = ItemStackBuilder.of(ItemTypes.PAPER).withName("§e§lFiltrar Nomes").build();
		if (filtrar != null) {
			ItemUtils.addLore(busca, Txt.f("§e§lAtual: §f" + filtrar));
		}
		addButton(new MenuButton(new SlotPos(4, 5), busca) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				close(player);
				ChatInputAPI.inputPlayer(player, new ChatInputAPI.ChatAction(15) {

					@Override
					public void inputText(Player player, String string) {
						new CompraPokemon(string, 1).open(player);
					}
				}, "Nome do pokemon");
			}
		});

	}

	public static List<EnumPokemon> getPage(String oq, int page) {
		ArrayList<EnumPokemon> l = new ArrayList();
		int foi = 0;
		int comeca = (page - 1) * 45;
		int termina = page * 45;
		for (EnumPokemon n : EnumPokemon.values()) {

			if (foi >= termina) {
				break;
			}
			if (((oq != null && n.name.toLowerCase().contains(oq.toLowerCase())) || oq == null)
					&& !PixelmonUtils.isLegendery(n)) {

				if (foi >= comeca) {
					l.add(n);
				}

				foi++;
			}
		}
		return l;
	}

}
