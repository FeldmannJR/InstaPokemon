package br.com.instamc.poke.elites;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class EliteHome {
	double x;
	double y;
	double z;
	String mundo;
	double yaw;// X
	double pitch;// Y

	public boolean loaded = false;

	public String toString() {
		if (loaded) {
			return mundo + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
		} else {
			return "";
		}

	}

	public EliteHome(Player p) {
		x = p.getLocation().getX();
		y = p.getLocation().getY();
		z = p.getLocation().getZ();
		mundo = p.getLocation().getExtent().getName();
		yaw = p.getHeadRotation().getX();
		pitch = p.getHeadRotation().getY();
		loaded = true;
	}

	public Optional<Location<World>> getLocation() {
		Optional<World> w = Sponge.getServer().getWorld(mundo);
		if (!w.isPresent()) {
			return Optional.empty();
		}
		return Optional.of(w.get().getLocation(x, y, z));

	}

	public void teleportPlayer(Player p) {
		if (getLocation().isPresent()) {
		
			p.setLocationAndRotationSafely(getLocation().get(), new Vector3d(yaw,pitch,0));
		}
	}

	public EliteHome(String s) {
		if (s == null || s.isEmpty()) {
			return;
		}
		String[] split = s.split(";");
		if (split.length != 6) {
			return;
		}

		try {
			x = Double.valueOf(split[1]);
			y = Double.valueOf(split[2]);
			z = Double.valueOf(split[3]);
			yaw = Double.valueOf(split[4]);
			pitch = Double.valueOf(split[5]);
			mundo = split[0];
			loaded = true;
		} catch (NumberFormatException ex) {

		}

	}
}
