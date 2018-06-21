package br.com.instamc.poke.votecoins;

import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class VoteCoinItem {

	int preco;
	String nome;
	ItemStack[] premios;

	public VoteCoinItem(String nome, int preco, ItemStack... premios) {
		this.preco = preco;
		this.nome = nome;
		this.premios = premios;
	}

	public ItemStack getIcone(int tem) {
		ItemStack i = premios[0].copy();
		ItemUtils.setItemName(i, Txt.f("§e§l" + nome));
		ItemUtils.addLore(i, Txt.f(""));
		if (tem >= preco) {
			ItemUtils.addLore(i, Txt.f("§aPreço: §f" + preco + " §e" + MoedaType.VOTECOINS.m.getName(preco)));
		} else {
			ItemUtils.addLore(i, Txt.f("§cPreço: §f" + preco + " §e" + MoedaType.VOTECOINS.m.getName(preco)));
			ItemUtils.addLore(i, Txt.f("§cVocê não tem VoteCoins suficientes!"));
			
		}
		ItemUtils.addLore(i, Txt.f(""));
		ItemUtils.addLore(i, Txt.f("§fVocê tem: §b"+tem+" §f!"));
		
		
		

		return i;
	}
}
