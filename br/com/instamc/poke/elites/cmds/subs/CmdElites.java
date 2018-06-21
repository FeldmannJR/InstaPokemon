package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.poke.elites.menus.MenuElites;
import br.com.instamc.poke.elites.menus.MenuVerElite;

public class CmdElites extends CmdSubElite {

	public CmdElites() {
		super("listar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Ver todas as elites do servidor";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		new MenuElites(1).open(p);

	}

}
