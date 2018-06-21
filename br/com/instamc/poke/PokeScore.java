package br.com.instamc.poke;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.ScoreboardManager;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.EconAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.events.ChangedGemEvent;
import br.com.instamc.sponge.library.gemas.GemasAPI;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;
import br.com.instamc.sponge.vip.data.PacoteDB;
import br.com.instamc.sponge.vip.data.PacoteDB.Vencimento;
import br.com.instamc.sponge.vip.pacotes.PacoteVIP;

public class PokeScore {

	public static void updateTeam(Player praquem, Player quem) {

		String cor = "§f";
		if (quem.hasPermission("instamc.staff")) {
			cor = "§6";
		} else
			if (quem.hasPermission("instamc.vip")) {
				cor = "§b";
			}
		String tag = LibAPI.getPrefix(quem, "", cor + " ");
		if (LibAPI.getPrefix(quem).isEmpty()) {
			tag = "";
		}

		ElitePlayer el = EliteManager.getElitePlayer(quem.getUniqueId());
		String elite = "";
		if (el.getCargo() != ElitePlayer.STAFF) {
			if (el.hasElite()) {
				elite = " §e" + el.getElite().tag;
			}
		}

		ScoreboardManager.setTeam(praquem, quem, quem.getName(), tag, elite);

	}

	public static void updateTeam(Player quem) {
		for (Player pl : Sponge.getServer().getOnlinePlayers()) {
			updateTeam(pl, quem);
		}
	}

	public static void updateCount() {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			ScoreboardManager.setLine(p, online, "§9§f" + getOnline()+ "  ");
		}
	}

	public PokeScore() {
		SchedulerUtils.runSyncReapeat(new Runnable() {

			@Override
			public void run() {
				updateCount();
			}
		}, 20 * 10);
	}

	@Listener
	public void a(ClientConnectionEvent.Join ev) {
		final Player p = ev.getTargetEntity();
		SchedulerUtils.runSync(new Runnable() {

			@Override
			public void run() {

				if (p == null) {
					return;
				}
				PixelmonUtils.getPlayerStorage(p).setCurrency(EconAPI.getMoney(p.getUniqueId()).intValue());

				ScoreboardManager.clear(p);
				PokeScore.updateTeam(p);
				for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
					if (pOn != p) {
						updateTeam(p, pOn);
					}
				}
				PokeScore.buildSide(p);

			}
		}, 20);
	}

	@Listener
	public void gemaAtualiza(ChangedGemEvent ev) {
		UUID u = ev.getUUID();
		Optional<Player> player = Sponge.getServer().getPlayer(u);
		if (player.isPresent() && player.get().isOnline()) {
			if (gem != -1) {
				ScoreboardManager.setLine(player.get(), gem, "§0§f" + ev.getTotalGemCount() + "    ");
			}
		}

	}

	@Listener
	public void coinATualiza(EconomyTransactionEvent ev) {

		if (ev.getTransactionResult().getResult() == ResultType.SUCCESS) {
			if (ev.getTransactionResult().getAccount() instanceof UniqueAccount) {
				UniqueAccount u = (UniqueAccount) ev.getTransactionResult().getAccount();
				Optional<Player> p = Sponge.getServer().getPlayer(u.getUniqueId());
				if (p.isPresent() && p.get().isOnline()) {
					if (coins != -1) {
						ScoreboardManager.setLine(p.get(), coins, "§2§f" + ev.getTransactionResult().getAccount().getBalance(ev.getTransactionResult().getCurrency()).toBigInteger().toString() + " ");
						PixelmonUtils.getPlayerStorage(p.get()).setCurrency(+ev.getTransactionResult().getAccount().getBalance(ev.getTransactionResult().getCurrency()).toBigInteger().intValue());
					}
				}
			}
		}
	}

	private static String sn = "§c§lPoké§f§lmon"; // §c§lPokémon §5§lInsta§f§lMC

	public static int gem = -1;
	public static int coins = -1;
	public static int online = -1;

	public static void buildSide(Player p) {
		int line = 15;
		ScoreboardManager.setDisplayName(p, sn);

		String rank = "§e§lSem Rank";
		String rankdesc = "Adquira no /shop";
		if (!LibAPI.getPrefix(p).isEmpty()) {
			rank = LibAPI.getPrefix(p);
			rankdesc = "§fVitálicio";
		}
		final int liner = line;
		ScoreboardManager.setLine(p, line--, rank);
		final int linerv = line;

		ScoreboardManager.setLine(p, line--, rankdesc);

		Task.builder().async().execute(new Runnable() {

			@Override
			public void run() {
				Vencimento v = PacoteDB.getAtivo(PacoteDB.getVencimentos(p.getUniqueId()));
				if (v != null) {
					if (v.getDias() != PacoteVIP.PERMANENTE) {
						SchedulerUtils.runSync(new Runnable() {

							@Override
							public void run() {
								ScoreboardManager.setLine(p, linerv, "§f" + v.getDiasRestantes());

							}
						}, 0);
					}
				}
			}
		}).submit(InstaPokemon.getPlugin());
		ScoreboardManager.setLine(p, line--, "§4§f ");
		ScoreboardManager.setLine(p, line--, "§e§lOnline");
		online = line;
		ScoreboardManager.setLine(p, line--, "§9§f" + getOnline() + "  ");
		ScoreboardManager.setLine(p, line--, "§6§f§a   ");
		ScoreboardManager.setLine(p, line--, "§a§l" + SpongeVIP.moedashop.getName(2));
		gem = line;
		ScoreboardManager.setLine(p, line--, "§0§f" + SpongeVIP.moedashop.get(p.getUniqueId()) + "   ");
		ScoreboardManager.setLine(p, line--, "§1§f    ");
		ScoreboardManager.setLine(p, line--, "§6§lCoins");
		coins = line;
		ScoreboardManager.setLine(p, line--, "§2§f" + ((int) EconAPI.getMoneyDouble(p.getUniqueId())) + " ");
		ScoreboardManager.setLine(p, line--, "§3§f      ");
		ScoreboardManager.setLine(p, line--, "§7§lSite");
		ScoreboardManager.setLine(p, line--, "§finstamc.com.br");
		ScoreboardManager.setLine(p, line--, "§f--------------");

	}

	public static int getOnline() {
		int x =0;
		for(Player p : Sponge.getServer().getOnlinePlayers()){
			if(!p.get(Keys.VANISH).orElse(Boolean.FALSE)){
				x++;
			}
		}
		return x;
	}

}
