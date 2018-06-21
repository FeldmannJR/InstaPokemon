package br.com.instamc.poke.elites.cmds.subs;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import br.com.instamc.poke.elites.ConviteManager.Convite;
import br.com.instamc.poke.elites.ConviteManager.ConviteRelation;
import br.com.instamc.poke.elites.ConviteManager;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteDB;
import br.com.instamc.poke.elites.EliteHome;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.core.TerrenoInfo;
import br.instamc.protection.managers.ProtectionManager;

public class CmdAliado extends CmdSubElite {

	public CmdAliado() {
		super("aliado");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<tag>";
	}

	@Override
	public String getHelp() {

		return "Cria uma aliança com uma elite!";
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public boolean needToBeLeader() {
		return true;
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		if (args.length < 1) {
			showUsage(p);
			return;
		}
		String tag = args[0];
		boolean convite = false;
		if (args.length == 2) {
			String c = args[1];

			if (c.equalsIgnoreCase("convite")) {
				convite = true;
			}
		}
		if (!EliteManager.isTagValid(tag)) {
			p.sendMessage(Txt.f("§cTag inválida, use uma tag valida!"));
			return;
		}

		Elite e = EliteManager.getEliteByTag(tag);
		if (e == null) {
			p.sendMessage(Txt.f("§cNão existe elite com esta tag!"));
			return;
		}
		if (e == eli) {
			p.sendMessage(Txt.f("§cVocê não pode ser aliado de si mesmo!"));
			return;
		}
		if (eli.getRivais().contains(e.getId())) {
			p.sendMessage(Txt.f("§c" + e.getNome() + " já é sua aliada!"));
			return;
		}
		if (convite) {
			ConviteRelation cv = EliteManager.getConvites().getConviteRelation(e.getId(), eli.getId());

			if (cv != null && cv.aliado == true) {
				eli.getRivais().remove(Integer.valueOf(e.getId()));
				e.getRivais().remove(Integer.valueOf(eli.getId()));
				eli.getAliados().add(Integer.valueOf(e.getId()));
				e.getAliados().add(Integer.valueOf(eli.getId()));

				EliteManager.save(e);
				EliteManager.save(eli);
				p.sendMessage(Txt.f("§aVocê aceitou o convite de aliança de " + e.getNome() + "!"));
				e.sendMessage("§e" + eli.tag + " declarou uma aliança com sua elite!");
				eli.sendMessage("§eSua elite criou uma aliança com " + e.tag + "!");
				return;
			}
		} else {
			if (!e.hasLeaderOnline()) {
				p.sendMessage(Txt.f("§cNão há nenhum lider online da elite para responder seu convite! Espere alguem entrar e tente novamente!"));
				return;
			}
			ConviteRelation cv = EliteManager.getConvites().getConviteRelation(e.getId(), eli.getId());
			if (cv != null)
			{
				p.sendMessage(Txt.f("§eVocê já convidou " + e.tag + " §epara ser aliado, espere para convidar novamente!"));
				return;
			}
			p.sendMessage(Txt.f("§eVocê convidou " + e.tag + " §epara ser aliado!"));
			EliteManager.getConvites().addConvite(new ConviteRelation(eli.getId(), e.getId(), true));
			e.sendMessageToLeader(Txt.f("§eA elite " + eli.nome + "§7(" + eli.tag + "§7) §e está propondo uma aliança clique em aceitar para confirmar a aliança:"));
			Text aceitar = Txt.f("§a§l[ACEITAR]");
			aceitar = aceitar.toBuilder().onClick(TextActions.runCommand("/elite aliado " + eli.getTagDB() + " convite")).onHover(TextActions.showText(Txt.f("§aClique para aceitar a aliança!"))).build();
			e.sendMessageToLeader(aceitar);
		}
	}

}
