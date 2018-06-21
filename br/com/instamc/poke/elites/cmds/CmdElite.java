package br.com.instamc.poke.elites.cmds;

import java.util.HashMap;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.subs.CmdAceitar;
import br.com.instamc.poke.elites.cmds.subs.CmdAliado;
import br.com.instamc.poke.elites.cmds.subs.CmdConvidar;
import br.com.instamc.poke.elites.cmds.subs.CmdCriar;
import br.com.instamc.poke.elites.cmds.subs.CmdDarLider;
import br.com.instamc.poke.elites.cmds.subs.CmdElites;
import br.com.instamc.poke.elites.cmds.subs.CmdEntrar;
import br.com.instamc.poke.elites.cmds.subs.CmdExcluir;
import br.com.instamc.poke.elites.cmds.subs.CmdExpulsar;
import br.com.instamc.poke.elites.cmds.subs.CmdHome;
import br.com.instamc.poke.elites.cmds.subs.CmdNeutro;
import br.com.instamc.poke.elites.cmds.subs.CmdRecusar;
import br.com.instamc.poke.elites.cmds.subs.CmdRemoverLider;
import br.com.instamc.poke.elites.cmds.subs.CmdRival;
import br.com.instamc.poke.elites.cmds.subs.CmdSair;
import br.com.instamc.poke.elites.cmds.subs.CmdSetHome;
import br.com.instamc.poke.elites.cmds.subs.CmdSetarIcone;
import br.com.instamc.poke.elites.cmds.subs.CmdSetarTitulo;
import br.com.instamc.poke.elites.cmds.subs.CmdTrocarTag;
import br.com.instamc.poke.elites.cmds.subs.CmdVer;
import br.com.instamc.poke.elites.cmds.subs.CmdVerJogador;
import br.com.instamc.sponge.library.apis.ComandoAPI;

import br.com.instamc.sponge.library.utils.Txt;

public class CmdElite extends ComandoAPI {

	HashMap<String, CmdSubElite> subs = new HashMap<>();

	public static String cmd = "elite";

	public CmdElite() {
		super(CommandType.PLAYER, cmd);
		add(new CmdCriar());
		add(new CmdRecusar());
		add(new CmdSetHome());
		add(new CmdConvidar());
		add(new CmdSetHome());
		add(new CmdHome());
		add(new CmdAceitar());
		add(new CmdExpulsar());
		add(new CmdEntrar());
		add(new CmdSetarTitulo());
		add(new CmdSetarIcone());
		add(new CmdSair());
		add(new CmdRemoverLider());
		add(new CmdDarLider());
		add(new CmdExcluir());
		add(new CmdVer());
		add(new CmdElites());
		add(new CmdTrocarTag());
		add(new CmdAliado());
		add(new CmdRival());
		add(new CmdNeutro());
		add(new CmdVerJogador());
	}

	public void add(CmdSubElite el) {
		subs.put(el.cmd.toLowerCase(), el);
	}

	Text separator = Txt.f("§7§l§m=======================================");

	public void showHelp(Player p) {
		// STAFF

		p.sendMessage(separator);
		Text.Builder builder = Text.builder();
		if (p.hasPermission("instamc.staff")) {
			Text t = Txt.f("§6§l[STAFF] ");
			t = t.toBuilder().onHover(TextActions.showText(Txt.f("§eClique para ver os comandos de staff"))).onClick(TextActions.runCommand("/elite help staff")).build();
			builder.append(t);

		}
		// PLAYERS SEM ELITE
		ElitePlayer pl = EliteManager.getElitePlayer(p.getUniqueId());

		for (CmdSubElite el : subs.values()) {
			if (!el.needClan() && !el.needToBeStaff()) {
				p.sendMessage(Txt.f("§f[§a§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));

			}
		}
		if (pl.hasElite()) {
			for (CmdSubElite el : subs.values()) {
				if (el.needClan() && !el.needToBeLeader() && !el.needToBeFounder()) {
					p.sendMessage(Txt.f("§f[§2§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));

				}
			}

			p.sendMessage(Txt.f("§f[§2§l!!!§f] §e/. <Mensagem> §8- §7Manda uma mensagem para os membros online."));
		}

		if (pl.hasElite() && pl.getCargo() >= ElitePlayer.LIDER) {
			Text t = Txt.f("§9§l[LIDER] ");
			t = t.toBuilder().onHover(TextActions.showText(Txt.f("§eClique para ver os comandos de lider"))).onClick(TextActions.runCommand("/elite help lider")).build();
			builder.append(t);
		}
		if (pl.hasElite() && pl.getCargo() >= ElitePlayer.FUNDADOR) {
			Text t = Txt.f("§5§l[FUNDADOR] ");
			t = t.toBuilder().onHover(TextActions.showText(Txt.f("§eClique para ver os comandos de fundador"))).onClick(TextActions.runCommand("/elite help fundador")).build();
			builder.append(t);

		}
		Text extra = builder.toText();
		if (!extra.toPlain().isEmpty()) {
			p.sendMessage(Txt.f("§ePara mais comandos clique no botão correspondente:"));
			p.sendMessage(extra);
		}
		p.sendMessage(separator);

	}

	public void showLiderHelp(Player p) {
		p.sendMessage(separator);
		ElitePlayer pl = EliteManager.getElitePlayer(p.getUniqueId());
		if (pl.hasElite() && pl.getCargo() >= ElitePlayer.LIDER) {

			for (CmdSubElite el : subs.values()) {
				if (el.needClan() && el.needToBeLeader()) {
					p.sendMessage(Txt.f("§f[§9§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));

				}
			}
		}
		p.sendMessage(separator);
	}

	public void showFundadorHelp(Player p) {
		p.sendMessage(separator);
		ElitePlayer pl = EliteManager.getElitePlayer(p.getUniqueId());
		if (pl.hasElite() && pl.getCargo() >= ElitePlayer.FUNDADOR) {
			for (CmdSubElite el : subs.values()) {
				if (el.needClan() && el.needToBeFounder()) {
					p.sendMessage(Txt.f("§f[§5§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));

				}
			}
		}
		p.sendMessage(separator);
	}

	public void showStaffHelp(Player p) {
		p.sendMessage(separator);
		if (p.hasPermission("instamc.staff")) {
			for (CmdSubElite el : subs.values()) {
				if (el.needToBeStaff()) {

					p.sendMessage(Txt.f("§f[§6§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));
				}
			}

		}
		p.sendMessage(separator);
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		if (args.length == 0) {
			showHelp(p);
			return;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("help")) {
				if (args[1].equalsIgnoreCase("fundador")) {
					showFundadorHelp(p);
				}
				if (args[1].equalsIgnoreCase("lider")) {
					showLiderHelp(p);
				}
				if (args[1].equalsIgnoreCase("staff")) {
					showStaffHelp(p);
				}
				return;
			}
		}
		String subcmd = args[0].toLowerCase();
		if (subs.containsKey(subcmd)) {
			CmdSubElite el = subs.get(subcmd);
			if (el.needToBeStaff()) {
				if (!p.hasPermission("instamc.staff")) {
					showHelp(p);
					return;
				}
			}
			ElitePlayer ep = EliteManager.getElitePlayer(p.getUniqueId());
			if (el.needClan()) {
				if (!ep.hasElite()) {
					p.sendMessage(Txt.f("§f[§c§l!!!§f] §7Você precisa de uma elite para executar este comando!"));
					return;
				}
				if (el.needToBeLeader() && ep.getCargo() < ElitePlayer.LIDER) {
					p.sendMessage(Txt.f("§f[§c§l!!!§f] §7Você não pode executar este comando, você não é líder!"));
					return;
				}
				if (el.needToBeFounder() && ep.getCargo() < ElitePlayer.FUNDADOR) {
					p.sendMessage(Txt.f("§f[§c§l!!!§f] §7Você não pode executar este comando, você não é dono!"));
					return;
				}
			}

			String[] newargs = new String[args.length - 1];
			for (int x = 1; x < args.length; x++) {
				newargs[x - 1] = args[x];
			}
			el.execute(p, ep, ep.getElite(), newargs);

			return;
		}

		showHelp(p);

	}

}
