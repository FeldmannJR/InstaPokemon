package br.com.instamc.poke.elites.cmds;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import akka.io.Tcp.Command;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdClanChat extends ComandoAPI {

	public CmdClanChat() {
		super(CommandType.PLAYER, ".", "elitechat");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		ElitePlayer el = EliteManager.getElitePlayer(p.getUniqueId());
		if (!el.hasElite()) {
			p.sendMessage(Txt.f("§c§lVocê não tem uma elite para usar este comando!"));
			return;
		}
		String msg = "";
		for (int x = 0; x < args.length; x++) {
			if (x != 0) {
				msg += " ";
			}
			msg += args[x];
		}
		el.getElite().sendChatMessage(el, p, msg);
		for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
			if (pOn.hasPermission("instamc.elite.vechat")) {
				pOn.sendMessage(Txt.f("§7[" + el.getElite().tag + "§7]" + p.getName() + ": §8" + msg));
			}
		}

	}

}
