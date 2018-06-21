package br.com.instamc.poke.customItems;

import br.com.instamc.poke.customItems.list.*;

public enum EnumCustomItems {
	Lendario(new LendarioAleatorio()),
	PokemonAleatorio(new br.com.instamc.poke.customItems.list.PokemonAleatorio()),
	PokemonShinyAleatorio(new br.com.instamc.poke.customItems.list.PokemonShinyAleatorio()),
	LuckyBlock(new br.com.instamc.poke.customItems.list.luckyblock.LuckyBlock()),
	EscolheLendario(new EscolheLendario()),
	EscolhePokemon(new EscolhePokemon()),
	EscolheShiny(new EscolhePokemonShiny()),
	EscolheLendarioShiny(new EscolheLendarioShiny()),
	Cheque(new Cheque()),
	TMAleatorio(new TMAleatorio()),
	FossilAleatorio(new FossilAleatorio()),
	PedraEvolutivaAleatoria(new PedraEvolutivaAleatoria()),
	ItemEvolutivoAleatorio(new ItemEvolutivoAleatorio()),
	ArmaduraCouro(new ArmaduraAleatoria()),
	MegaRing(new MegaRing()),
	TPScroll(new TpScrollItem());
	;

	CustomItem item;

	private EnumCustomItems(CustomItem ct) {
		this.item = ct;
	}

	public CustomItem getItem() {
		return item;
	}

}
