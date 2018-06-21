package br.com.instamc.poke.elites.cmds.subs;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdExpulsar extends CmdSubElite {

	public CmdExpulsar() {
		super("expulsar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean needToBeLeader() {
		return true;
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public String getArgs() {
		return "<nome>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Expulsa um jogador da sua elite.";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length == 0) {
			showUsage(p);
			return;
		}
		String alvo = args[0];
		UUID uid = UUIDUtils.getUUID(alvo);
		if (uid == null) {
			p.sendMessage(Txt.f("§cJogador não existe!"));
			return;
		}
		ElitePlayer el = EliteManager.getElitePlayer(uid);
		if (!el.hasElite() || el.getElite().getId() != eli.getId()) {
			p.sendMessage(Txt.f("§cO jogador não é da sua elite!"));
			return;
		}
		if (el.getCargo() >= ElitePlayer.FUNDADOR) {
			if (pl.getCargo() != ElitePlayer.STAFF) {
				p.sendMessage(Txt.f("§cVocê não pode expulsar o fundador!"));
				return;

			}
		}
		eli.getMembros().remove(uid);
		el.elite = -1;
		el.resetTitulo();
		el.cargo = 0;
		EliteManager.save(eli);
		EliteManager.save(el);
		eli.sendMessage("O jogador §a" + alvo + " §ffoi expulso da elite!")
		;
		if(Sponge.getServer().getPlayer(alvo).isPresent()){
			PokeScore.updateTeam(Sponge.getServer().getPlayer(alvo).get());
			
		}

	}

}
