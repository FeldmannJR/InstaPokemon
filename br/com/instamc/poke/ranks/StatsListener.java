package br.com.instamc.poke.ranks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.sql.ConnectionEvent;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;

import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.events.PlayerWinBattleEvent;
import br.com.instamc.poke.ranks.Stats.Stat;
import br.com.instamc.sponge.library.ScoreboardManager;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.UUIDUtils;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class StatsListener {

	HashMap<UUID, Long> last = new HashMap<>();

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void captureWild(com.pixelmonmod.pixelmon.api.events.CaptureEvent.SuccessfulCapture ev) {
		if (!ev.isCanceled()) {
			EntityPixelmon pokemon = ev.getPokemon();
			if (pokemon.battleController != null) {
				if (pokemon.battleController.wasFishing) {
					Stats.addStats(ev.player.getUniqueID(), Stat.POKESPESCADOS, 1);
				}
			}
			Stats.addStats(ev.player.getUniqueID(), Stat.CAPTUROU, 1);

		}

	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void colheapricorn(ApricornEvent.PickApricorn ev) {
		if (!ev.isCanceled()) {
			Stats.addStats(ev.player.getUniqueID(), Stat.APRICORNS, 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void evolve(EvolveEvent.PostEvolve ev) {
		if (!ev.isCanceled()) {
			Stats.addStats(ev.player.getUniqueID(), Stat.EVOLUCOES, 1);
		}
	}

	public void update(UUID uid, boolean add, boolean remove) {
		if (last.containsKey(uid)) {
			long dif = System.currentTimeMillis() - last.get(uid);
			long minutos = dif / 1000L / 60L;

			if (add) {
				long resto = dif - (minutos * 1000 * 60);
				last.put(uid, System.currentTimeMillis() - resto);
			}
			if (remove) {
				last.remove(uid);
			}
			if (minutos > 0) {
				final int minutosf = (int) minutos;
				final UUID uidf = (UUID) uid;
				int antes = Stats.geStats(uid, Stat.MINUTOSOLINE);
				int agora = antes + minutosf;
				antes = antes / 60;
				agora = agora / 60;

				if (antes < agora) {
					if (agora == 10 || agora == 50 || agora == 1) {
						avisa(UUIDUtils.getName(uid), agora);
					} else
						if (agora % 10== 0 && agora != 0) {
							avisa(UUIDUtils.getName(uid), agora);
						} else {
							if (Sponge.getServer().getPlayer(uid).isPresent()) {
								InstaPokemon.sendMessage(Sponge.getServer().getPlayer(uid).get(), "§fVocê alcançou mais uma hora de jogo agora tem §6" + agora + " §fhoras!");
							}
						}
				}

				Task.builder().async().execute(new Runnable() {

					@Override
					public void run() {
						Stats.addStats(uidf, Stat.MINUTOSOLINE, minutosf);
					
					}
				}).submit(InstaPokemon.getPlugin());

			}
		}

	}

	public void avisa(String name, int quanto) {
		if (name == null)
			return;
		InstaPokemon.broadcast("§fO jogador §b" + name + "§f alcançou §6" + quanto + " §fhoras de jogo no pokemon!");
	}

	@Listener
	public void login(ClientConnectionEvent.Join ev) {
		last.put(ev.getTargetEntity().getUniqueId(), System.currentTimeMillis());
	}

	@Listener
	public void logout(ClientConnectionEvent.Disconnect ev) {
		update(ev.getTargetEntity().getUniqueId(), false, true);
	}

	public StatsListener() {
		Task.builder().execute(new Runnable() {

			@Override
			public void run() {
				for (UUID uid : new ArrayList<UUID>(last.keySet())) {
					update(uid, true, false);
				}
			}
		}).interval(1, TimeUnit.MINUTES).submit(InstaPokemon.getPlugin());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void beatTrainer(BeatTrainerEvent ev) {
		if (!ev.isCanceled()) {
			Stats.addStats(ev.player.getUniqueID(), Stat.VITORIASTRAINER, 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void beatWild(BeatWildPixelmonEvent ev) {
		if (!ev.isCanceled()) {
			Stats.addStats(ev.player.getUniqueID(), Stat.VITORIASWILD, 1);
		}
	}

	@SubscribeEvent
	public void crafted(ItemCraftedEvent ev) {
		if (ev.crafting != null) {
			for (EnumPokeballs poke : EnumPokeballs.values()) {
				if (poke.getItem() == ev.crafting.getItem()) {

					Stats.addStats(ev.player.getUniqueID(), Stat.CRAFTPOKEBALL, 1);
					break;

				}
			}

		}

	}

	HashSet<Integer> japrocessadas = new HashSet();

	@SubscribeEvent
	public void ganha(com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent event) {
		EntityPlayerMP p = event.player;
		if (event.result != BattleResults.VICTORY) {
			return;
		}
		if (japrocessadas.contains(event.battleController.battleIndex)) {
			return;
		}

		japrocessadas.add(event.battleController.battleIndex);
		if (event.battleController.wasFishing) {
			Stats.addStats(p.getUniqueID(), Stat.POKESPESCADOS, 1);
		}

		

	}
	@Listener
	private void ganha(PlayerWinBattleEvent ev) {
		Player ganhador = ev.getWinner();
		Player perdedor = ev.getLoser();
		if (ganhador == null || perdedor == null) {
			return;
		}
		if(ev.fugiu())return;
		Stats.addStats(ganhador.getUniqueId(), Stat.VITORIASPVP, 1);
		Stats.addStats(perdedor.getUniqueId(), Stat.DERROTASPVP, 1);

	}
}
