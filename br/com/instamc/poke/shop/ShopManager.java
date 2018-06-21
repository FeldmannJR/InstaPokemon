/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.poke.shop.menus.CompraLendario;
import br.com.instamc.poke.shop.menus.CompraPokemon;
import br.com.instamc.poke.shop.menus.FazShinePokemon;
import br.com.instamc.poke.shop.menus.TrocaNaturePoke;
import br.com.instamc.poke.shop.menus.TrocarPokeball;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class ShopManager {

	public static int precoFazShine = 30;
	public static int precoTrocaNature = 100;
	public static int precoLendario = 100;
	public static int precoPokemon = 30;
	public static int precoTrocaPokeball = 3;
	public static int precoLendarioRandom = 70;
	public static int precoPokemonRandom = 5;
	public static int precoPokemonRandomShiny = 15;
	public static int precoMegaring = 50;

	public static void init() {
		SchedulerUtils.runSync(new Runnable() {
			
			@Override
			public void run() {
				addShine();
				addNature();
				addLendario();
				addPokemon();
				addPokeball();
				addLendarioAleatorio();
				addPokemonAleatorio();
				addPokemonAleatorioShiny();
				addBracelet();	}
		}, 20);
		

	}

	private static void addBracelet() {

		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.END_CRYSTAL).withName("§b§lMega Ring").withLore(buildPrecoS(precoMegaring)).build()) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				close(player);

				boolean tem = PixelmonUtils.getPlayerStorage(player).megaData.canEquipMegaItem();
				if (tem) {
					player.sendMessage(Txt.f("§c§lVocê já tem o Mega Ring!"));
					return;
				}
				new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.END_CRYSTAL, 1), "Comprar Mega Ring", precoMegaring, "Clique aqui para comprar o mega ring!") {

					@Override
					public boolean compra(Player pl) {
						boolean tem = PixelmonUtils.getPlayerStorage(pl).megaData.canEquipMegaItem();
						if (tem) {
							pl.sendMessage(Txt.f("§c§lVocê já tem o Mega Ring!"));
							return false;
						}
						PixelmonUtils.giveBracelet(pl);
						SpongeVIP.sendMessageComprou(pl, "Mega Ring", precoMegaring);
						InstaPokemon.sendMessage(player, "§eVocê comprou um Mega Ring!");
						return true;
					}

					@Override
					public void cancela(Player arg0) {
						close(arg0);
					}
				}.open(player);
				;

			}
		});

	}

	private static void addShine() {
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(1, 5), ItemStackBuilder.of(ItemTypes.NETHER_STAR).withName("§b§lFaz Shine").withLore(buildPrecoS(precoFazShine)).build()) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new FazShinePokemon(player).open(player);
			}
		});
	}

	private static void addNature() {
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(2, 5), ItemStackBuilder.of(ItemTypes.SAPLING).withName("§b§lTroca Nature").withLore(buildPrecoS(precoTrocaNature)).build()) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new TrocaNaturePoke(player).open(player);
			}
		});
	}

	private static void addLendario() {
		ItemStack suicune = PixelmonUtils.getPixelmonIcon(EnumPokemon.Suicune, false);
		ItemUtils.setItemName(suicune, Txt.f("§b§lComprar Lendário"));
		ItemUtils.addLore(suicune, buildPreco(ShopManager.precoLendario));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(4, 5), suicune) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new CompraLendario().open(player);
			}
		});
	}

	private static void addPokemon() {
		ItemStack pikachu = PixelmonUtils.getPixelmonIcon(EnumPokemon.Pikachu, false);
		ItemUtils.setItemName(pikachu, Txt.f("§b§lComprar Pokemon"));
		ItemUtils.addLore(pikachu, buildPreco(ShopManager.precoPokemon));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(5, 5), pikachu) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new CompraPokemon(null, 1).open(player);
			}
		});

	}

	private static void addPokeball() {
		ItemStack pb = PixelmonUtils.getPokeball(EnumPokeballs.PokeBall);
		ItemUtils.setItemName(pb, Txt.f("§b§lTrocar Pokeball"));
		ItemUtils.addLore(pb, buildPreco(ShopManager.precoTrocaPokeball));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(3, 5), pb) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new TrocarPokeball(player).open(player);
			}
		});
	}

	private static void addLendarioAleatorio() {

		ItemStack suicune = PixelmonUtils.getPixelmonIcon(EnumPokemon.Mewtwo, false);
		ItemUtils.setItemName(suicune, Txt.f("§b§lComprar Lendário Aleatório"));
		ItemUtils.addLore(suicune, buildPreco(ShopManager.precoLendarioRandom));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(6, 5), suicune) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.WEB, 1), "Lendário Aleatório", precoLendarioRandom, "Compra um pokemon lendário aletório!") {

					@Override
					public void cancela(Player player) {
					}

					@Override
					public boolean compra(Player player) {
						List<EnumPokemon> lendarios = new ArrayList();
						for (EnumPokemon e : EnumPokemon.values()) {
							if (PixelmonUtils.isLegendery(e)) {
								lendarios.add(e);
							}
						}

						EnumPokemon lend = lendarios.get(new Random().nextInt(lendarios.size()));
						PixelmonUtils.give(player, PixelmonUtils.build(player, lend, EnumPokeballs.MasterBall, 1, false));
						InstaPokemon.sendMessage(player, "§fVocê ganhou um §6" + lend.name + " !");
						SpongeVIP.sendMessageComprou(player, "Um Lendário Aleatório (" + lend.name + ")", ShopManager.precoLendarioRandom);
						return true;
					}
				}.open(player);
			}
		});

	}

	private static void addPokemonAleatorio() {

		ItemStack suicune = PixelmonUtils.getPixelmonIcon(EnumPokemon.Bulbasaur, false);
		ItemUtils.setItemName(suicune, Txt.f("§b§lComprar Pokemon Aleatório"));
		ItemUtils.addLore(suicune, buildPreco(precoPokemonRandom));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(7, 5), suicune) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.WEB, 1), "Pokemon Aleatório", precoPokemonRandom, "Compra um pokemon aletório!") {

					@Override
					public void cancela(Player player) {
					}

					@Override
					public boolean compra(Player player) {
						List<EnumPokemon> lendarios = new ArrayList();
						for (EnumPokemon e : EnumPokemon.values()) {
							if (!PixelmonUtils.isLegendery(e)) {
								lendarios.add(e);
							}
						}

						EnumPokemon lend = lendarios.get(new Random().nextInt(lendarios.size()));
						PixelmonUtils.give(player, PixelmonUtils.build(player, lend, EnumPokeballs.PokeBall, 1, false));
						InstaPokemon.sendMessage(player, "§fVocê ganhou um §6" + lend.name + " !");
						SpongeVIP.sendMessageComprou(player, "Um Pokemon Aleatório (" + lend.name + ")", ShopManager.precoPokemonRandom);
						return true;
					}
				}.open(player);
			}
		});

	}

	public static Text buildPreco(int preco) {
		return Txt.f(buildPrecoS(preco));
	}

	public static Text buildPrecoPor(int preco) {
		return Txt.f("§fpor §a§l" + preco + " " + SpongeVIP.moedashop.getName(preco));
	}

	public static String buildPrecoS(int preco) {
		return "§f§lPreço: §a§l" + preco + " " + SpongeVIP.moedashop.getName(preco) + " !";
	}

	private static void addPokemonAleatorioShiny() {
		ItemStack suicune = PixelmonUtils.getPixelmonIcon(EnumPokemon.Bulbasaur, true);
		ItemUtils.setItemName(suicune, Txt.f("§b§lComprar Pokemon Aleatório Shiny"));
		ItemUtils.addLore(suicune, buildPreco(precoPokemonRandomShiny));
		SpongeVIP.addButtonToMenu(new MenuButton(new SlotPos(8, 5), suicune) {

			@Override
			public void click(Player player, Menu menu, ClickType ct) {
				new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.WEB, 1), "Pokemon Aleatório Shiny", precoPokemonRandomShiny, "Compra um pokemon aletório shiny!") {

					@Override
					public void cancela(Player player) {
					}

					@Override
					public boolean compra(Player player) {
						List<EnumPokemon> lendarios = new ArrayList();
						for (EnumPokemon e : EnumPokemon.values()) {
							if (!PixelmonUtils.isLegendery(e)) {
								lendarios.add(e);
							}
						}

						EnumPokemon lend = lendarios.get(new Random().nextInt(lendarios.size()));
						PixelmonUtils.give(player, PixelmonUtils.build(player, lend, EnumPokeballs.PokeBall, 1, true));
						InstaPokemon.sendMessage(player, "§fVocê ganhou um §6" + lend.name + " shiny !");
						SpongeVIP.sendMessageComprou(player, "Um Pokemon Aleatório Shiny (" + lend.name + ")", ShopManager.precoPokemonRandomShiny);
						return true;
					}
				}.open(player);
			}
		});
	}

}
