package br.com.instamc.poke.ginasios;

import java.util.HashSet;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.events.PlayerWinBattleEvent;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GinasioListeners {

	@Listener
	public void quit(ClientConnectionEvent.Disconnect ev) {
		Player p = ev.getTargetEntity();
		for (Ginasio g : GinasioManager.getGinasios()) {
			if (g.getOcupado() != null && g.getOcupado() == p) {
				g.kickOcupado(true);

			}
			if (g.getDono() != null && g.getDono().toString().equals(p.getUniqueId().toString()) && g.isAberto()) {
				g.setAberto(false);
			}

		}

	}

	@Listener
	public void ganha(PlayerWinBattleEvent ev) {
		Player ganhador = ev.getWinner();
		Player perdedor = ev.getLoser();
		if (ganhador != null && perdedor != null) {
			for (Ginasio g : GinasioManager.getGinasios()) {
				if (g.getDono() != null && g.getOcupado() != null) {
					boolean donoperdeu = false;
					if (g.getDono().toString().equals(perdedor.getUniqueId().toString())) {
						donoperdeu = true;
					} else
						if (g.getDono().toString().equals(ganhador.getUniqueId().toString())) {
							donoperdeu = false;
						} else {
							return;
						}
					if (donoperdeu) {
						if (g.getOcupado() != ganhador) {
							return;
						}
					} else {
						if (g.getOcupado() != perdedor) {
							return;
						}
					}
					if (!g.isInRange(ganhador) || !g.isInRange(perdedor)) {
						return;
					}
					if (donoperdeu) {
						InstaPokemon.broadcast("§a§l" + ganhador.getName() + " §ederrotou o lider do ginásio §6" + g.getNome() + ", §b" + perdedor.getName() + " §e!");

						InsigniaDB.addInsignia(ganhador, g.getBadge());

					} else {
						InstaPokemon.broadcast("§a§l" + ganhador.getName() + " §edefendeu seu ginásio de §c" + perdedor.getName() + " §e!");

					}
					g.kickOcupado(false);
					break;
				}
			}
		}
	}

}
