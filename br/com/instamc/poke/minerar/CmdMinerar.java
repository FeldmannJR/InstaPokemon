package br.com.instamc.poke.minerar;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.cmds.warps.CmdWarp;
import br.com.instamc.sponge.library.cmds.warps.WarpObject;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdMinerar extends ComandoAPI {

	protected CmdMinerar() {
		super(CommandType.PLAYER, "minerar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] arg1) {
		Player p = (Player) arg0;
		CmdWarp.loadWarps();
		if (CmdWarp.getWarps() != null && CmdWarp.getWarps().containsKey("minerar")) {
			WarpObject ob = CmdWarp.getWarps().get("minerar");
			if (ob.toLocation() != null) {
				p.setLocation(ob.toLocation());
			}
			p.sendMessage(Txt.f("§aTeleportado!"));
		} else {
			p.sendMessage(Txt.f("§cMinerar desabilitado!"));

		}

	}

}
