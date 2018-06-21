package br.com.instamc.poke.elites.cmds.subs;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.poke.elites.menus.MenuVerElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdVerJogador extends CmdSubElite {

	public CmdVerJogador() {
		super("verjogador");
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		Elite elite = null;
		if (args.length == 0) {
			return;
		} else {
			String nome = args[0];

			UUID uid = UUIDUtils.getUUID(nome);
			if (uid == null) {
				p.sendMessage(Txt.f("§cJogador inválido!"));
				return;
			}
			ElitePlayer el = EliteManager.getElitePlayer(uid);
			if (!el.hasElite()) {
				p.sendMessage(Txt.f("§cJogador não possuí elite!"));
				return;
			}

			if (el.getElite() != null) {
				new MenuVerElite(el.getElite()).open(p);
			}

		}

	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "<tag>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Para ver a elite de um jogador";
	}

}
