package br.com.instamc.poke.elites.cmds.subs;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdTrocarTag extends CmdSubElite {

	public CmdTrocarTag() {
		super("trocartag");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {
		// TODO Auto-generated method stub
		return "<tag>";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Troca a tag da elite";
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
		String tag = args[0];
		tag = tag.replace("&", "§");
		
		String plain = Txt.f(tag).toPlain();
		for (String s : Arrays.asList("o", "m", "n", "k", "l")) {
			tag = tag.replace("§" + s, "");
			tag = tag.replace("§" + s.toUpperCase(), "");

		}
		
		if (plain.length() < 2) {
			p.sendMessage(Txt.f("§eTag da elite muito pequena!"));
			return;
		}
		if (plain.length() > 5) {
			p.sendMessage(Txt.f("§eTag da elite muito grande!"));
			return;
		}
		if (tag.length() > 15) {
			p.sendMessage(Txt.f("§eTag da elite muito grande!"));
			return;
		}

		if (EliteManager.existsTag(tag) && EliteManager.getEliteByTag(tag) != eli) {
			p.sendMessage(Txt.f("§eTag já existente!"));
			return;
		}

		if (!EliteManager.isTagValid(tag)) {
			p.sendMessage(Txt.f("§eTag inválida!"));
			return;
		}

		Date d = new Date(eli.lasttrocatag.getTime());
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, 10080);
		d.setTime(c.getTimeInMillis());
		if (d.after(new Date(System.currentTimeMillis()))) {
			long dif = c.getTimeInMillis() - System.currentTimeMillis();
			p.sendMessage(Txt.f("§eAguarde " + timeToString(dif) + " para trocar a tag novamente!"));
			return;
		}

		eli.tag = tag;
		EliteManager.save(eli);
		EliteManager.broadcast("§eA elite §a" + eli.nome + " §etrocou a tag para §f" + eli.tag + " §e!");
		EliteManager.sendMessage(p, "§eTag trocada!");
		for(Player pOn : eli.getOnlinePlayers()){
			PokeScore.updateTeam(pOn);
		}

	}

	public static String timeToString(long time) {
		if (time < 0L) {
			return "forever";
		}
		if (time > 86400000L) {
			return time / 86400000L + " dia(s)";
		}
		if (time > 3600000L) {
			return time / 3600000L + " hora(s)";
		}
		if (time > 60000L) {
			return time / 60000L + " minuto(s)";
		}
		if (time > 1000L) {
			return time / 1000L + " segundo(s)";
		}
		return time + " milisegundo(s)";
	}
}
