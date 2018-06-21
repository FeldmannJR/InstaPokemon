package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdEntrar extends CmdSubElite {

	public CmdEntrar() {
		super("entrar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean needToBeStaff() {
		return true;
	}

	@Override
	public String getArgs() {
		return "<tag>";
	}

	@Override
	public String getHelp() {

		return "Entra em uma elite como founder";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length == 0) {
			showUsage(p);
			return;
		}

		String tag = args[0];
		if (pl.hasElite()) {
			p.sendMessage(Txt.f("§cSaia da elite primeiro!"));
			return;
		}

		Elite sel = EliteManager.getEliteByTag(tag);
		if (sel == null) {
			p.sendMessage(Txt.f("§cNão existe elite procurada!"));
			return;
		}
		pl.cargo = ElitePlayer.STAFF;
		pl.titulo = "STAFF";
		pl.elite = sel.getId();
		EliteManager.save(pl);
		sel.getMembros().add(p.getUniqueId());
		EliteManager.save(sel);
		p.sendMessage(Txt.f("§eEntrou!"));

	}

}
