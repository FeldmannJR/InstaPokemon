package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.ConviteManager.Convite;
import br.com.instamc.poke.elites.ConviteManager;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdRecusar extends CmdSubElite {

	public CmdRecusar() {
		super("recusar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<tag>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Recusar convite de elite";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length != 1) {
			showUsage(p);
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
		
		EliteManager.getConvites().removeConvite(c.eliteidconvidou, c.convidado);
		convidou.sendMessage("O jogador §a" + p.getName() + "§f recusou o convite!");
	
		EliteManager.sendMessage(p, "Você recusou o convite da elite §b" + convidou.nome + "§f !");

	}

}
