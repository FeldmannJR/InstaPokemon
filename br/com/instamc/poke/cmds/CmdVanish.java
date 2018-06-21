package br.com.instamc.poke.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import br.com.instamc.poke.vanish.VanishManager;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdVanish extends ComandoAPI {

	public CmdVanish() {
		super(CommandType.PERMISSION, "vanish");

		this.permission = "instamc.cmd.vanish";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		boolean vanish = p.get(Keys.VANISH).orElse(Boolean.FALSE).booleanValue();
		if (!vanish) {
			p.sendMessage(Txt.f("§aVocê agora está invisivel!"));
			VanishManager.vanish(p, true,true);
		} else {
			p.sendMessage(Txt.f("§cVocê agora está VISIVEL!"));
			VanishManager.vanish(p, false,true);

		}
	}

}
