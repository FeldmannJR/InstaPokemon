package br.com.instamc.poke.ranks;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import com.pixelmonmod.pixelmon.comm.PixelmonUpdateData;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import akka.actor.Scheduler;
import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.camera.CameraDB;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.ranks.Stats.Stat;
import br.com.instamc.poke.ranks.menus.RankMenu;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class RankManager {

	public static RankManager manager = null;

	public static void init() {
		manager = new RankManager();
		Stats.init();

		Task.builder().execute(new Runnable() {

			@Override
			public void run() {
			
				manager.update();
		
			}
		}).delayTicks(1).intervalTicks(20*60*20).submit(InstaPokemon.getPlugin());
	}

	boolean update = false;
	ConcurrentHashMap<Stat, RankCache> ranks = new ConcurrentHashMap();
	RankCache insignias = new RankCache();
	RankCache fotos = new RankCache();

	public ConcurrentHashMap<Stat, RankMenu> menus = new ConcurrentHashMap();
	public RankMenu insigniamenu = null;
	public RankMenu fotosmenu = null;

	public RankManager() {

		for (Stat s : Stat.values()) {
			if (s != Stat.MINUTOSOLINE) {
				RankMenu r = new RankMenu(s.nome, build(s.icone.copy(), s.nome)) {

					@Override
					public String processaInfo(int x) {
						return x + "";
					}
				};

				menus.put(s, r);
			} else {
				RankMenu r = new RankMenu(s.nome, build(s.icone.copy(), s.nome)) {

					@Override
					public String processaInfo(int x) {
						String on = "";
						int minutos = x;
						if (minutos >= 60) {
							int horas = minutos / 60;
							int minutosr = minutos % 60;
							if (horas != 1) {
								on += horas + " horas";
							} else {
								on += horas + " hora";
							}
							if (minutosr != 1) {
								on += " e " + minutosr + " minutos";
							} else {
								on += " e " + minutosr + " minuto";

							}
						} else {
							if (minutos != 1) {
								on += "" + minutos + " minutos";
							} else {
								on += "" + minutos + " minuto";

							}
						}
						return on;
					}
				};

				menus.put(s, r);
			}

		}
		insigniamenu = new RankMenu("Insignias", build(PixelmonUtils.getBadge(EnumBadges.BasicBadge), "Insignias")) {

			@Override
			public String processaInfo(int x) {
				if (x != 1) {
					return x + " insignias";
				} else {
					return x + " insignia";
				}
			}
		};
		fotosmenu = new RankMenu("Fotos", build(PixelmonUtils.getItemStack(PixelmonItems.cameraItem), "Fotos")) {

			@Override
			public String processaInfo(int x) {
				if (x != 1) {
					return x + " fotos";
				} else {
					return x + " foto";
				}
			}
		};

	}

	public void updateMenus() {
		SpongeLib.getPlugin().doLog("Iniciando update ranks pixelmon");
		for (Stat s : menus.keySet()) {
			menus.get(s).updateButtons(ranks.get(s));
		}
		insigniamenu.updateButtons(insignias);
		fotosmenu.updateButtons(fotos);
		SpongeLib.getPlugin().doLog("Terminando update ranks pixelmon");
	}

	public static ItemStack build(ItemStack it, String oq) {
		ItemStack si = it.copy();
		ItemUtils.setItemName(si, Txt.f("§e§l" + oq));
		return si;

	}

	public void update() {
		SchedulerUtils.runAsync(new Runnable() {

			@Override
			public void run() {
				for (Stat s : Stat.values()) {
					ranks.put(s, Stats.getTop(s, 45));
				}
				insignias = InsigniaDB.getTop(45);
				fotos = CameraDB.getTop(45);
				SchedulerUtils.runSync(new Runnable() {

					@Override
					public void run() {
						updateMenus();
					}
				}, 1);
			}
		}, 1);

	}

}
