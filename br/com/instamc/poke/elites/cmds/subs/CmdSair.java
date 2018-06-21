package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdSair extends CmdSubElite {

	public CmdSair() {
		super("sair");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {
		return "";
	}

	@Override
	public String getHelp() {
		return "Use para sair da sua elite";
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		boolean hasAnotherFounder = false;
		for (ElitePlayer player : eli.getElitePlayers()) {
			if (player.getCargo() == ElitePlayer.FUNDADOR) {
				if (!player.uid.equals(p.getUniqueId())) {
					hasAnotherFounder = true;
				}
			}

		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("confirmar")) {

				eli.getMembros().remove(pl.uid);
				pl.elite = -1;
				pl.resetTitulo();
				pl.cargo = 0;
				EliteManager.save(pl);

				if (!hasAnotherFounder) {
					eli.sendMessage("§d§lSUA ELITE FOI EXCLUIDA PELO LIDER!");
					EliteManager.delete(eli);
					p.sendMessage(Txt.f("§aVocê excluiu sua elite!"));

				} else {
					eli.sendMessage("§fO jogador §a"+p.getName()+"§f saiu da elite.");
					
					p.sendMessage(Txt.f("§aVocê saiu da elite!"));
				}
				return;
			}
		}
		String sair = "Sair";
		if (pl.getCargo() == ElitePlayer.FUNDADOR) {
			if (!hasAnotherFounder) {

				p.sendMessage(Txt.f("§aCaso você clique em excluir, você não terá seu dinheiro devolta e sua elite será excluida! Caso não queira ignore!"));

				sair = "EXCLUIR";
			}
		} else {
			p.sendMessage(Txt.f("§eClique no botão para sair da elite, caso troque de ideia ignore.!"));

		}

		Text aceitar = Txt.f("§c§l[" + sair + "]");
		aceitar = aceitar.toBuilder().onClick(TextActions.runCommand("/elite sair confirmar")).onHover(TextActions.showText(Txt.f("§fClique para " + sair + " sua elite!"))).build();
		p.sendMessage(aceitar);
	}

}
