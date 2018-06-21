package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.poke.elites.menus.MenuVerElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdVer extends CmdSubElite {

	public CmdVer() {
		super("ver");
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		Elite elite = null;
		if (args.length == 0) {
			if (eli == null) {
				p.sendMessage(Txt.f("§cVocê não tem uma elite!"));
				showUsage(p);
				return;
			} else {
				elite = eli;
			}
		} else {
			String tag = args[0];
			if (!EliteManager.isTagValid(tag)) {
				p.sendMessage(Txt.f("§cTag inválida!"));
				return;
			}
			Elite target = EliteManager.getEliteByTag(tag);
			if (target == null) {
				p.sendMessage(Txt.f("§cElite não encontrada!"));
				return;
			}
			elite = target;

		}
		if (elite != null) {
			new MenuVerElite(elite).open(p);
		}

	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "[tag]";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Para ver informações de uma elite";
	}

}
