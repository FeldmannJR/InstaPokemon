package br.com.instamc.poke.tpscroll.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.tpscroll.TPScrollManager;
import br.com.instamc.poke.tpscroll.menu.ScrollMenu;
import br.com.instamc.sponge.library.apis.ComandoAPI;

public class CmdTps extends ComandoAPI {

	public CmdTps() {
		super(CommandType.PLAYER, "tps");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] arg1) {
		Player p = (Player) cs;
		if(TPScrollManager.getInfo(p.getUniqueId()).isEmpty()){
			InstaPokemon.sendMessage(p,"§cVocê não tem nenhum teleporte!");
			return;
		}
		new ScrollMenu(p).open(p);
	}

}
