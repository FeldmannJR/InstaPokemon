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

public class CmdRival extends CmdSubElite {

	public CmdRival() {
		super("rival");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<tag>";
	}

	@Override
	public String getHelp() {

		return "Cria uma rivalidade entre uma elite!";
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

		if (args.length != 1) {
			showUsage(p);
			return;
		}
		String tag = args[0];

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
			p.sendMessage(Txt.f("§cVocê não pode ser rival de si mesmo!"));
			return;
		}
		if (eli.getRivais().contains(e.getId())) {
			p.sendMessage(Txt.f("§c" + e.getNome() + " já é sua rival!"));
			return;
		}
		eli.getRivais().add(Integer.valueOf(e.getId()));
		e.getRivais().add(Integer.valueOf(eli.getId()));
		
		eli.getAliados().remove(Integer.valueOf(e.getId()));
		e.getAliados().remove(Integer.valueOf(eli.getId()));

		EliteManager.save(e);
		EliteManager.save(eli);
		e.sendMessage("§e" + eli.tag + " declarou rivalidade com sua elite!");
		eli.sendMessage("§eSua elite declarou rivalidade contra " + e.tag + "!");

	}

}
