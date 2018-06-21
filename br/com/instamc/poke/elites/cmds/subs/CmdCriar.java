package br.com.instamc.poke.elites.cmds.subs;

import java.sql.Timestamp;
import java.util.Arrays;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteHome;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.ranks.Stats;
import br.com.instamc.poke.ranks.Stats.Stat;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdCriar extends CmdSubElite {

	public CmdCriar() {
		super("criar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<tag> <nome>";
	}

	@Override
	public String getHelp() {

		return "Criar uma elite";
	}

	int horaspara = 30;
	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		if (pl.hasElite()) {
			p.sendMessage(Txt.f("§eVocê não pode criar uma elite, pois você já está em uma, caso queira criar saia."));
			return;
		}
		int horas = Stats.geStats(p.getUniqueId(), Stat.MINUTOSOLINE) / 60;
		
		
		if(horas<horaspara&&!InsigniaDB.getInsignias(p.getUniqueId()).contains(EnumBadges.TrioBadge)){
			p.sendMessage(Txt.f("§cVocê precisa jogar "+horaspara+"h para criar uma elite!§a Ou possuir a insignia Trio!"));
			return;
		}
		if (args.length < 2) {
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
		String nome = "";
		for (int x = 1; x < args.length; x++) {
			if (x > 1) {
				nome += " ";
			}
			nome += args[x];
		}
		if (EliteManager.existsName(nome)) {
			p.sendMessage(Txt.f("§eNome já existente!"));
			return;
		}
		if (EliteManager.existsTag(tag)) {
			p.sendMessage(Txt.f("§eTag já existente!"));
			return;
		}

		if (!EliteManager.isTagValid(tag)) {
			p.sendMessage(Txt.f("§eTag inválida!"));
			return;
		}

		Elite e = new Elite();
		e.fundada = new Timestamp(System.currentTimeMillis());
		e.fundador = p.getUniqueId();
		e.nome = nome;
		e.tag = tag;
		e.home = new EliteHome("");
		e.lasttrocatag = new Timestamp(System.currentTimeMillis());

		e.getMembros().add(p.getUniqueId());
		EliteManager.save(e);
		pl.cargo = ElitePlayer.FUNDADOR;
		pl.elite = e.getId();
		pl.resetTitulo();
		EliteManager.save(pl);
		EliteManager.sendMessage(p, "Elite criada!");
		EliteManager.broadcast("§eO jogador §a" + p.getName() + " §ecriou uma elite chamada §a" + e.nome + " §ecom a tag §f" + e.tag + "!");
		PokeScore.updateTeam(p);
	}

}
