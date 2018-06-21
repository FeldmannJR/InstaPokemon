package br.com.instamc.poke.elites.cmds;

import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.sponge.library.utils.Txt;

public abstract class CmdSubElite {

	String cmd;

	public CmdSubElite(String cmd) {
		this.cmd = cmd;

	}

	public boolean needClan() {
		return false;
	}

	public boolean needToBeLeader() {
		return false;
	}

	public boolean needToBeFounder() {
		return false;
	}

	public boolean needToBeStaff() {
		return false;
	}

	public abstract String getArgs();

	public abstract String getHelp();

	
	public void showUsage(Player p){
		p.sendMessage(Txt.f("§f[§4!!!§f] §cUso correto: §e/"+CmdElite.cmd+" "+cmd+" "+getArgs()));
	}
	
	public abstract void execute(Player p,ElitePlayer pl,Elite eli,String[] args);
}
