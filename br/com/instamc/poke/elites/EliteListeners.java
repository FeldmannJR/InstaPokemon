package br.com.instamc.poke.elites;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.sponge.library.events.PlayerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EliteListeners {

	@Listener
	public void log(ClientConnectionEvent.Join ev) {
		ElitePlayer e = EliteManager.getElitePlayer(ev.getTargetEntity().getUniqueId());
		if (e.hasElite()) {
			for (Player p : e.getElite().getOnlinePlayers()) {
				if (p != ev.getTargetEntity()) {
					EliteManager.sendMessage(p, "§eO jogador §a" + ev.getTargetEntity().getName() + "§e entrou no jogo!");
				}
			}

		}

	}

	@Listener
	public void disc(ClientConnectionEvent.Disconnect ev) {
		ElitePlayer e = EliteManager.getElitePlayer(ev.getTargetEntity().getUniqueId());
		if (e.hasElite()) {
			for (Player p : e.getElite().getOnlinePlayers()) {
				if (p != ev.getTargetEntity()) {
					EliteManager.sendMessage(p, "§eO jogador §c" + ev.getTargetEntity().getName() + "§e saiu no jogo!");
				}
			}

		}

	}

	public void enu(EnumPokemon a) {

	}

	@Listener
	public void chat(PlayerChatEvent ev) {
		ElitePlayer e = EliteManager.getElitePlayer(ev.getPlayer().getUniqueId());

		if (e.hasElite()) {
			ev.setPrefix(e.getElite().tag + "§7.");
		}
	}

	@SubscribeEvent
	public void evolve(EvolveEvent.PostEvolve ev) throws Exception {
		String depois = ev.postEvo.name;
		String antes = ev.preEvo.getPokemonName();
		ElitePlayer e = EliteManager.getElitePlayer(ev.player.getUniqueID());
		if (e.hasElite()) {
			for (Player p : e.getElite().getOnlinePlayers()) {
				if (!p.getUniqueId().equals(ev.player.getUniqueID())) {

					EliteManager.sendMessage(p, "§eO jogador §b" + ev.player.getName() + "§e evoluiu seu §6" + antes + "§e para §a" + depois + "§f!");
				}
			}
		}
	}

	@SubscribeEvent
	public void capture(CaptureEvent.SuccessfulCapture ev) {
		ElitePlayer e = EliteManager.getElitePlayer(ev.player.getUniqueID());
		if (e.hasElite()) {
			for (Player p : e.getElite().getOnlinePlayers()) {
				if (!p.getUniqueId().equals(ev.player.getUniqueID())) {

					EliteManager.sendMessage(p, "§eO jogador §b" + ev.player.getName() + "§e capturou um §6" + ev.getPokemon().getPokemonName() + "§e!");
				}
			}
		}
	}

}
