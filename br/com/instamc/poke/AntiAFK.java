package br.com.instamc.poke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class AntiAFK {

	HashMap<UUID, Integer> moves = new HashMap();
	HashMap<UUID, Vector3d> last = new HashMap();

	public AntiAFK() {
		Task.builder().execute(new Runnable() {

			@Override
			public void run() {
				doCicle();
			}
		}).interval(1, TimeUnit.SECONDS).submit(InstaPokemon.getPlugin());
	}

	public void doCicle() {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			if (p.hasPermission("instamc.staff")) {
				continue;
			}
			if(PixelmonUtils.isInBattle(p)) {
				moves.remove(p.getUniqueId());
				continue;
			}
			if (last.containsKey(p.getUniqueId())) {
				Vector3d l = last.get(p.getUniqueId());
				if (l.getX() == p.getHeadRotation().getX() && l.getY() == p.getHeadRotation().getY()&&l.getZ()==p.getHeadRotation().getZ()) {
					addponto(p);
				} else {
					last.put(p.getUniqueId(), p.getHeadRotation());
					moves.remove(p.getUniqueId());
				}

			} else {
				last.put(p.getUniqueId(), p.getHeadRotation());
			}

		}

	}

	public void addponto(Player p) {
		int tem = 1;
		UUID uid = p.getUniqueId();
		if (moves.containsKey(uid)) {
			tem += moves.get(uid);
		}

		moves.put(uid, tem);
		if (tem >= (10 * 60)) {
			p.kick(Txt.f("§c§lANTIAFK §fVocê ficou afk por 10 minutos!"));
		}
	}

	@Listener
	public void quit(ClientConnectionEvent.Disconnect ev) {
		sai(ev.getTargetEntity());
	}

	public void sai(Player p) {
		moves.remove(p.getUniqueId());
		last.remove(p.getUniqueId());
	}

}
