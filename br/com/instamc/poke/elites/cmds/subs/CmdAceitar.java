package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.ConviteManager.Convite;
import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdAceitar extends CmdSubElite {

	public CmdAceitar() {
		super("aceitar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<tag>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Aceitar convite de elite";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length != 1) {
			showUsage(p);
			return;
		}
		if (pl.hasElite()) {
			p.sendMessage(Txt.f("§cVocê já está em uma elite!"));
			return;
		}
		String tag = args[0];

		Elite convidou = EliteManager.getEliteByTag(tag.toLowerCase());
		if (convidou == null) {
			p.sendMessage(Txt.f("§cConvite inválido!"));
			return;
		}

		Convite c = EliteManager.getConvites().getConvite(convidou.getId(), p.getUniqueId());

		if (c == null) {
			p.sendMessage(Txt.f("§cVocê não recebeu um convite desta elite!"));
			return;

		}
		if (convidou.getMembros().size() > EliteManager.MAXIMO) {
			p.sendMessage(Txt.f("§cA elite que você foi convidada está lotada!"));
			return;
		}

		pl.elite = convidou.getId();
		pl.resetTitulo();
		pl.cargo = 0;
		convidou.sendMessage("O jogador §a" + p.getName() + "§f entrou na sua elite!");
		convidou.getMembros().add(p.getUniqueId());
		EliteManager.save(pl);
		EliteManager.save(convidou);
		EliteManager.sendMessage(p, "Você entrou na elite §b" + convidou.nome + "§f !");
		PokeScore.updateTeam(p);

	}

}
