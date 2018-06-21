package br.com.instamc.poke.elites.cmds.subs;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdRemoverLider extends CmdSubElite {

	public CmdRemoverLider() {
		super("removerlider");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "<nome>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Rebaixa um líder a membro.";
	}

	@Override
	public boolean needToBeFounder() {
		return true;
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length != 1) {
			showUsage(p);
			return;
		}
		UUID uid = UUIDUtils.getUUID(args[0]);

		if (uid == null) {
			p.sendMessage(Txt.f("§cJogador não encontrado!"));
			return;

		}
		ElitePlayer alvo = EliteManager.getElitePlayer(uid);
		if (!alvo.hasElite() || alvo.getElite().getId() != eli.getId()) {
			p.sendMessage(Txt.f("§cJogador não faz parte de sua elite!!"));
			return;

		}
		if (alvo.getCargo() != ElitePlayer.LIDER) {
			p.sendMessage(Txt.f("§cJogador não é líder!!"));
			return;
		}
		alvo.cargo = ElitePlayer.MEMBRO;
		EliteManager.save(alvo);
		p.sendMessage(Txt.f("§aJogador setado para membro!"));
		eli.sendMessage("§eO jogador §a"+args[0]+"§e foi rebaixado a membro!");

	}

}
