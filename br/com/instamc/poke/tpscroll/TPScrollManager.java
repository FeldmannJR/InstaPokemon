package br.com.instamc.poke.tpscroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.PixelmonUpdateData;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.tpscroll.cmds.CmdCriarScroll;
import br.com.instamc.poke.tpscroll.cmds.CmdTps;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionManager;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.guard.region.Region;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import net.minecraft.entity.player.EntityPlayerMP;

public class TPScrollManager {

	private static HashMap<UUID, List<TPScrollInfo>> infos = new HashMap();

	private static TpScrollDB db;

	public static void init() {
		SpongeLib.getPlugin().doLog("Iniciande TpScroll");
		db = new TpScrollDB();
		ComandoAPI.enable(new CmdTps());
		ComandoAPI.enable(new CmdCriarScroll());
		SchedulerUtils.runSyncReapeat(new Runnable() {

			@Override
			public void run() {
				update();
			}
		}, 100);
	}

	public static List<TPScrollInfo> getInfo(UUID uid) {

		if (!infos.containsKey(uid)) {
			infos.put(uid, db.getScrolls(uid));
		} else {
			for (TPScrollInfo info : new ArrayList<TPScrollInfo>(infos.get(uid))) {
				if (!info.isValid()) {
					infos.get(uid).remove(info);
				}
			}
		}

		return infos.get(uid);

	}

	public static boolean hasTimeInRegion(UUID uid, String region) {
		for (TPScrollInfo infos : getInfo(uid)) {
			if (infos.regiao.equalsIgnoreCase(region)) {
				return true;
			}
		}
		return false;
	}

	public static boolean addScroll(UUID uid, TPScrollInfo info) {
		if (has(uid, info.getNome()))
			return false;

		getInfo(uid).add(info);
		db.addScroll(uid, info);
		return true;
	}

	private static void update() {

		for (Player p : Sponge.getServer().getOnlinePlayers()) {

			if (SpongeGuard.getManager().getManager(p.getWorld()).getFlag(EnumFlags.TPSCROLL, p.getLocation()) == FlagValue.ALLOW) {
				List<Region> regions = SpongeGuard.getManager().getManager(p.getWorld()).getRegionsAt(p.getLocation());
				R: for (Region r : regions) {
					if (r.getFlag(EnumFlags.TPSCROLL) == FlagValue.ALLOW) {

						if (!hasTimeInRegion(p.getUniqueId(), r.getName())) {
							if(PixelmonUtils.isInBattle(p)){
								BattleRegistry.getBattle((EntityPlayerMP) p).endBattle();
							}
							p.setLocation(p.getWorld().getSpawnLocation());
							InstaPokemon.sendMessage(p, "§cAcabou seu tempo no pergaminho! Teleportado para o spawn!");
						}

						break R;
					}
				}

			}

		}

	}

	public static boolean has(UUID uid, String nome) {
		for (TPScrollInfo t : getInfo(uid)) {
			if (t.getNome().equalsIgnoreCase(nome)) {
				return true;
			}
		}
		return false;
	}

}
