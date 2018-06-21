package br.com.instamc.poke.elites.cmds.subs;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdSetarTitulo extends CmdSubElite {

	public CmdSetarTitulo() {
		super("setartitulo");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean needToBeLeader() {
		return true;
	}

	@Override
	public String getArgs() {
		return "<nome> <titulo>";
	}

	@Override
	public boolean needToBeStaff() {
		return false;
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Seta um titulo para um jogador.";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length != 2) {
			showUsage(p);
			return;
		}
		String alvo = args[0];
		String titulo = args[1];
		UUID uid = UUIDUtils.getUUID(alvo);
		if (uid == null) {
			p.sendMessage(Txt.f("§cJogador não existe!"));
			return;
		}
		if (titulo.length() > 15) {
			p.sendMessage(Txt.f("§cTitulo muito longo!"));
			return;
		}
		ElitePlayer el = EliteManager.getElitePlayer(uid);
		if (!el.hasElite() || el.getElite().getId() != eli.getId()) {
			p.sendMessage(Txt.f("§cO jogador não é da sua elite!"));
			return;
		}
		if (el.getCargo() == ElitePlayer.FUNDADOR) {
			if (pl.getCargo() != ElitePlayer.FUNDADOR) {
				p.sendMessage(Txt.f("§cVocê não pode trocar o titulo do fundador!"));
				return;

			}
		}

		el.titulo = titulo;
		EliteManager.save(el);
		eli.sendMessage("§fO titulo do jogador §a" + alvo + "§f foi setado para §a" + titulo + "§f !");

	}

}
