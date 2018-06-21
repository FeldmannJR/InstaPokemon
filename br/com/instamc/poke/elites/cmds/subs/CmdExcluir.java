package br.com.instamc.poke.elites.cmds.subs;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdExcluir extends CmdSubElite {

	public CmdExcluir() {
		super("excluir");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public boolean needToBeFounder() {
		return true;
	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "EXCLUI sua elite, sem reembolso.";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length == 1 && args[0].equals("confirmardeletar")) {
			eli.sendMessage("§a§lSUA ELITE FOI EXCLUIDA PELO LIDER!");
			EliteManager.delete(eli);
			p.sendMessage(Txt.f("§aVocê excluiu sua elite!"));
			return;

		}
		EliteManager.sendMessage(p, "§eClique no botão abaixo para confirmar que você deseja EXCLUIR sua elite!");
		Text aceitar = Txt.f("§c§l[EXCLUIR]");
		aceitar = aceitar.toBuilder().onClick(TextActions.runCommand("/elite excluir confirmardeletar")).onHover(TextActions.showText(Txt.f("§fClique para EXCLUIR sua elite!"))).build();
		p.sendMessage(aceitar);

	}

}
