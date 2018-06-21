package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdHome extends CmdSubElite {

	public CmdHome() {
		super("home");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "";
	}

	@Override
	public String getHelp() {
		return "Teleporta para o home da elite";
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		if (!eli.home.loaded) {
			p.sendMessage(Txt.f("§cSua elite não tem home setada!"));
			return;
		}
		p.sendMessage(Txt.f("§aTeleportado."));
		eli.home.teleportPlayer(p);
		

	}

}
