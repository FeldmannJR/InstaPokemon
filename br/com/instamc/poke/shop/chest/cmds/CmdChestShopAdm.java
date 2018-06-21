package br.com.instamc.poke.shop.chest.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.shop.chest.ChestShop;
import br.com.instamc.poke.shop.chest.ShopItem.ShopType;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdChestShopAdm extends ComandoAPI {
	public CmdChestShopAdm() {
		super(CommandType.OP, "chestshopadm");
		// TODO Auto-generated constructor stub
	}

	public void onCommand(CommandSource src, String[] args) {
		Player p = (Player) src;
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("vendeitem")) {
				try {
					int preco = Integer.valueOf(args[1]);
					if (preco <= 0) {
						p.sendMessage(Txt.f("§cValor inválido!"));
						return;
					}
					if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
						p.sendMessage(Txt.f("§9Coloque um item na mão apra setar o valor de venda!"));
						return;
					}
					p.sendMessage(Txt.f("§a§lSetado!"));
					p.setItemInHand(HandTypes.MAIN_HAND, ChestShop.setPreco(p.getItemInHand(HandTypes.MAIN_HAND).get(), preco, ShopType.VENDA));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§cValor inválido!"));
					return;
				}
			}
			if (args[0].equalsIgnoreCase("vendeshiny") || args[0].equalsIgnoreCase("vendelendario")) {
				try {
					int preco = Integer.valueOf(args[1]);
					if (preco <= 0) {
						p.sendMessage(Txt.f("§cValor inválido!"));
						return;
					}
					if (InventoryUtils.getEmpty(p) < 1) {
						p.sendMessage(Txt.f("§9Sem espaço no inventário!!"));
						return;
					}
					if(args[0].equalsIgnoreCase("vendeshiny")){
						p.getInventory().offer(ChestShop.getVendeShiny(preco));
					}else{
						p.getInventory().offer(ChestShop.getVendeLendario(preco));
						
					}
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§cValor inválido!"));
					return;
				}

			}
			if (args[0].equalsIgnoreCase("compraitem")) {
				try {
					int preco = Integer.valueOf(args[1]);
					if (preco <= 0) {
						p.sendMessage(Txt.f("§cValor inválido!"));
						return;
					}
					if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
						p.sendMessage(Txt.f("§9Coloque um item na mão apra setar o valor de venda!"));
						return;
					}
					p.sendMessage(Txt.f("§a§lSetado!"));
					p.setItemInHand(HandTypes.MAIN_HAND, ChestShop.setPreco(p.getItemInHand(HandTypes.MAIN_HAND).get(), preco, ShopType.COMPRA));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§cValor inválido!"));
					return;
				}
			}

		}
		if (args.length >= 3) {
			if (args[0].equalsIgnoreCase("vendepokemon")) {
				try {
					String pokenome = args[1];
					if (!EnumPokemon.hasPokemon(pokenome)) {
						p.sendMessage(Txt.f("§cNão achei esse pokemon!"));

						return;
					}
					int preco = Integer.valueOf(args[2]);
					if (preco <= 0) {
						p.sendMessage(Txt.f("§cValor inválido!"));
						return;
					}
					if (InventoryUtils.getEmpty(p) < 1) {
						p.sendMessage(Txt.f("§9Sem espaço no inventário!!"));
						return;
					}
					boolean shiny = false;
					if (args.length == 4) {
						if (args[3].equalsIgnoreCase("s")) {
							shiny = true;
						}
					}
					p.sendMessage(Txt.f("§a§lAdded!"));
					p.getInventory().offer(ChestShop.getPokemonItem(preco, ShopType.VENDA, EnumPokemon.getFromName(pokenome).get(), shiny));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§cValor inválido!"));
					return;
				}
			}
			if (args[0].equalsIgnoreCase("comprapokemon")) {
				try {
					String pokenome = args[1];
					if (!EnumPokemon.hasPokemon(pokenome)) {
						p.sendMessage(Txt.f("§cNão achei esse pokemon!"));

						return;
					}
					int preco = Integer.valueOf(args[2]);
					if (preco <= 0) {
						p.sendMessage(Txt.f("§cValor inválido!"));
						return;
					}
					if (InventoryUtils.getEmpty(p) < 1) {
						p.sendMessage(Txt.f("§9Sem espaço no inventário!!"));
						return;
					}
					p.sendMessage(Txt.f("§a§lAdded!"));
					p.getInventory().offer(ChestShop.getPokemonItem(preco, ShopType.COMPRA, EnumPokemon.getFromName(pokenome).get(), false));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§cValor inválido!"));
					return;
				}
			}
		}
		sendHelp(p);
	};

	public void sendHelp(Player p) {
		p.sendMessage(Txt.f("§e/chestshopadm vendeshiny preco"));
		p.sendMessage(Txt.f("§e/chestshopadm vendelendario preco"));
		p.sendMessage(Txt.f("§e/chestshopadm vendepokemon Pokemon preco (s)"));
		p.sendMessage(Txt.f("§e/chestshopadm comprapokemon Pokemon preco"));
		p.sendMessage(Txt.f("§e/chestshopadm vendeitem preco"));
		p.sendMessage(Txt.f("§e/chestshopadm compraitem preco"));
		p.sendMessage(Txt.f("§bNOMES EM RELAÇÃO AO JOGADOR! JOGADOR COMPRA, JOGADOR VENDE !"));

	}
}
