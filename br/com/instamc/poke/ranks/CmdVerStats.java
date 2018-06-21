package br.com.instamc.poke.ranks;

import java.util.UUID;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.ranks.menus.VerStatsMenu;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Cooldown;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdVerStats extends ComandoAPI {
	public CmdVerStats() {
		super(CommandType.PLAYER, "perfil");
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		if (Cooldown.isCooldown(p, "verperfil")) {
			p.sendMessage(Txt.f("§eEspere um pouco para usar novamente."));
			return;
		}
		if (args.length != 1) {
			p.sendMessage(Txt.f("§eUso correto §c/perfil <nome> §e!"));
			return;
		}
		UUID uid = UUIDUtils.getUUID(args[0]);
		if (uid == null) {
			p.sendMessage(Txt.f("§eJogador não encontrado!"));
			return;
		}
		new VerStatsMenu(uid).open(p);
		Cooldown.addCoolDown(p, "verperfil", 2000);

	}

}
