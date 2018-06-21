/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.customItems.menus;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItemManager;
import br.com.instamc.poke.customItems.EnumCustomItems;
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

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
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
public class EscolhePokemonMenu extends Menu {

	String filtrar = null;
	boolean shiny;

	public EscolhePokemonMenu(String filtrar, int page, boolean shiny) {

		super("Escolher Pokemon", 6);
		this.filtrar = filtrar;
		this.shiny = shiny;
		for (final EnumPokemon pk : getPage(filtrar, page)) {
			ItemStack cv = PixelmonUtils.getPixelmonIcon(pk, shiny);
			ItemUtils.setItemName(cv, Txt.f("§e§l" + pk.name + (shiny ? " §6§lShiny" : "")));
			ItemUtils.addLore(cv, Txt.f("§fClique aqui escolhe este pokemon!"));

			addButtonNextSlot(new MenuButton(new SlotPos(Vector2i.ZERO), cv) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {

					EnumCustomItems cu = CustomItemManager.getItem(player.getItemInHand(HandTypes.MAIN_HAND).orElse(null));
					if (!shiny) {
						if (cu == null || cu != EnumCustomItems.EscolhePokemon) {
							InstaPokemon.sendMessage(player, "§eItem inválido!");
							close(player);
							return;
						}
						InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item §9Escolhe Pokemon §ee escolheu §a" + pk.name + "§e!");

					} else {
						if (cu == null || cu != EnumCustomItems.EscolheShiny) {
							InstaPokemon.sendMessage(player, "§eItem inválido!");
							close(player);
							return;
						}
						InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item §9Escolhe Pokemon §6§lShiny §ee escolheu §a" + pk.name + "§e!");

					}
					CustomItemManager.consomeMain(player);
					playSound(player, SoundTypes.ENTITY_FIREWORK_LARGE_BLAST);
					InstaPokemon.sendMessage(player, "§eVocê escolheu §6" + pk.name + " §e!");

					// ESCOLHE
					PixelmonUtils.give(player, PixelmonUtils.build(player, pk, EnumPokeballs.PokeBall, 1, shiny));
					close(player);

				}
			});
		}
		if (page > 1) {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§fClique aqui para voltar!").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new EscolhePokemonMenu(filtrar, page - 1, shiny).open(player);
				}
			});
		} else {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.BARRIER).withName("§c§lFechar").withLore("§fClique aqui para fechar!").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					close(player);
				}
			});
		}
		if (!getPage(filtrar, page + 1).isEmpty()) {
			addButton(new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR).withName("§e§lProxima").withLore("§fClique aqui para ir para", "§fa proxima pagina.").build()) {

				@Override
				public void click(Player player, Menu menu, ClickType ct) {
					new EscolhePokemonMenu(filtrar, page + 1, shiny).open(player);
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
						new EscolhePokemonMenu(string, 1, shiny).open(player);
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
			if (((oq != null && n.name.toLowerCase().contains(oq.toLowerCase())) || oq == null) && !PixelmonUtils.isLegendery(n)) {

				if (foi >= comeca) {
					l.add(n);
				}

				foi++;
			}
		}
		return l;
	}

}
