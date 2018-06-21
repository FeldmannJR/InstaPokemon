package br.com.instamc.poke.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.comm.PixelmonData;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdGeracao extends ComandoAPI {

	public CmdGeracao() {
		super(CommandType.PLAYER, "geracao");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {

		int slot = -1;
		try {
			if (args.length == 1) {
				slot = Integer.valueOf(args[0]);
			}
		} catch (NumberFormatException ex) {

		}
		if (slot == -1 || slot < 1 || slot > 6) {
			cs.sendMessage(Txt.f("§aSlot inválido use §f/geracao 1-6"));
			return;
		}
		Player p = (Player) cs;
       PixelmonData poke = PixelmonUtils.getPokemon(p, slot-1);
       if(poke==null){
    	   cs.sendMessage(Txt.f("§aNenhum pokemon no slot!"));
   		
    	   return ;
       }
      InstaPokemon.sendMessage(p, "§aO pokemon pertence a §f§l"+PixelmonUtils.getGeneration(poke.getNationalPokedexNumber())+"ª §ageração!");
	}

}
