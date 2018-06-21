package br.com.instamc.poke.customItems.list.menu;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.CustomItemManager;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.shop.CompraPokeMenu;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

public abstract class EscolhePokeUsaItem extends CompraPokeMenu{

	
	
	String nome;
	EnumCustomItems usa;
	
	public EscolhePokeUsaItem(EnumCustomItems usa,String nome) {
		super("Escolhe Pokemon",1);
		this.usa = usa;
		this.nome = nome;
	}
	
	
	@Override
	public void clickPoke(Player player, EnumPokemon e) {

		EnumCustomItems cu = CustomItemManager.getItem(player.getItemInHand(HandTypes.MAIN_HAND).orElse(null));
		if (cu == null || cu != usa) {
			InstaPokemon.sendMessage(player, "§eItem inválido!");
			close(player);
			return;
		}
		if(!getValidPokemons().contains(e)) {
			close(player);
			return;
		}
		
		player.playSound(SoundTypes.ENTITY_FIREWORK_LARGE_BLAST, player.getLocation().getPosition(), 2);
		PixelmonUtils.give(player, PixelmonUtils.build(player, e, EnumPokeballs.PokeBall, 1,this.isShiny()));
		InstaPokemon.sendMessage(player, "§eVocê escolheu §6"+e.name+" §e!");
		InstaPokemon.broadcast("§a" + player.getName() + " §eusou o item §6"+nome+" §ee escolheu §a" + e.name + "§e!");

		CustomItemManager.consomeMain(player);
		close(player);
	}

	@Override
	public ItemStack buildItem(ItemStack it, EnumPokemon poke) {
		it = ItemUtils.addLore(it, Txt.f("§fClique para escolher este pokemon!"));
		return it;
	}

	

}
