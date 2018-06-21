package br.com.instamc.poke.minerar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.BlockVolume;
import org.spongepowered.api.world.gen.type.BiomeTreeType;
import org.spongepowered.api.world.gen.type.BiomeTreeTypes;

import com.flowpowered.math.vector.Vector3i;
import com.pixelmonmod.pixelmon.commands.BlockSnapShot;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.cmds.warps.CmdWarp;
import br.com.instamc.sponge.library.cmds.warps.WarpObject;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.init.Blocks;

public abstract class Mina {

	Vector3i pos1;
	Vector3i pos2;
	World w;

	public Mina(World w, Vector3i pos1, Vector3i pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.w = w;

	}

	public void checkPlayers() {

		for (Player p : Sponge.getServer().getOnlinePlayers()) {

			if (isIn(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(), true)) {
				if (CmdWarp.getWarps().containsKey("minerar")) {
					WarpObject ob = CmdWarp.getWarps().get("minerar");
					if (ob.toLocation() != null) {
						p.setLocation(ob.toLocation());
					} else {
						p.setLocation(p.getWorld().getSpawnLocation());
					}
				} else {
					p.setLocation(p.getWorld().getSpawnLocation());
				}
				p.sendMessage(Txt.f("§aVocê foi teleportado, pois a mina restaurou!"));
			}
		}
	}

	public boolean isIn(int x, int y, int z, boolean incluirborda) {
		int minX = Math.min(pos1.getX(), pos2.getX());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int minZ = Math.min(pos1.getZ(), pos2.getZ());
		int maxX = Math.max(pos1.getX(), pos2.getX());
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int maxZ = Math.max(pos1.getZ(), pos2.getZ());
		if (incluirborda) {
			if (x >= minX && x <= maxX) {
				if (y >= minY && y <= maxY) {
					if (z >= minZ && z <= maxZ) {
						return true;
					}
				}
			}
		} else {
			if (x > minX && x < maxX) {
				if (y > minY && y < maxY) {
					if (z > minZ && z < maxZ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void setBlock(Vector3i vec, BlockState t) {
		w.setBlock(vec, t, Cause.source(InstaPokemon.getPlugin()).build());

	}

	public void setBlock(int x, int y, int z, BlockState t) {
		setBlock(new Vector3i(x, y, z), t);
	}

	public abstract void enche();

}
