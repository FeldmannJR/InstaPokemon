package br.com.instamc.poke.minerar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.text.Position;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.StoneTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;


import br.com.instamc.sponge.library.utils.Txt;

public class MinaMinerios extends Mina {

	HashSet<MinaChance> chances = new HashSet();

	public MinaMinerios(World w, Vector3i pos1, Vector3i pos2, MinaChance... minachance) {
		super(w, pos1, pos2);
		// TODO Auto-generated constructor stub
		int r = new Random().nextInt(10000);

		int last = -1;

		if (minachance != null) {
			for (MinaChance mc : minachance) {
				int x = (int) (mc.pct * 10000D);
				if (last == -1) {
					mc.inicia = 0;
					mc.termina = x;
					last = mc.termina;
				} else {
					mc.inicia = last + 1;
					mc.termina = (last + 1) + x;
					last = mc.termina;
				}
				chances.add(mc);
			}
		}

	}

	public String getKey(Vector3i v) {
		return v.getX() + ";" + v.getY() + ";" + v.getZ();
	}

	public void showMinerios(Vector3i pos) {
		for (LocatableBlock b : getNear(w.getLocatableBlock(pos))) {
			String k = getKey(b.getPosition());
			if (minerios.containsKey(k)) {
				setBlock(b.getPosition(), minerios.get(k));
				minerios.remove(k);
			}
		}

	}

	HashMap<String, BlockState> minerios = new HashMap<>();

	BlockState stone = null;
	@Override
	public void enche() {
		if(stone==null){
			stone = Minerar.getStoneType(StoneTypes.STONE);
		}
		minerios.clear();
		int minX = Math.min(pos1.getX(), pos2.getX());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int minZ = Math.min(pos1.getZ(), pos2.getZ());
		int maxX = Math.max(pos1.getX(), pos2.getX());
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int maxZ = Math.max(pos1.getZ(), pos2.getZ());
		checkPlayers();

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {

					boolean borda = false;
					if (x == minX || x == maxX || y == maxY || y == minY || z == maxZ || z == minZ) {
						borda = true;
					}
					MinaChance sor = sorteia();
					LocatableBlock vl = w.getLocatableBlock(x, y, z);
					if (vl.getBlockState().getType() != BlockTypes.STONE) {
						setBlock(new Vector3i(x, y, z),stone);
					}
					if (sor != null && !borda) {

						minerios.put(getKey(new Vector3i(x, y, z)), sor.block);
						int dif = sor.maxvein - sor.minvein;
						int vein = sor.minvein;
						if (dif > 0) {
							vein = sor.minvein + (new Random().nextInt(dif)) - 1;
						}
						if (vein <= 0) {
							continue;
						}
						geraVeioDeMinerio(vl, sor.block, vein);

					}

				}
			}
		}

	}

	public MinaChance sorteia() {
		if (!chances.isEmpty()) {
			int r = new Random().nextInt(10000);
			for (MinaChance chance : chances) {
				if (r >= chance.inicia && r <= chance.termina) {
					return chance;
				}
			}

		}

		return null;
	}

	public HashSet<MinaChance> getChances() {
		return chances;
	}

	public List<LocatableBlock> getNear(LocatableBlock bl) {
		List<LocatableBlock> near = new ArrayList<LocatableBlock>();
		int x = bl.getPosition().getX();
		int y = bl.getPosition().getY();
		int z = bl.getPosition().getZ();

		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x + 1, y, z)));
		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x - 1, y, z)));
		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x, y + 1, z)));
		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x, y - 1, z)));
		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x, y, z + 1)));
		near.add(bl.getWorld().getLocatableBlock(new Vector3i(x, y, z - 1)));
		Collections.shuffle(near);
		return near;
	}

	public void geraVeioDeMinerio(LocatableBlock b, BlockState ore, int count) {
		List<LocatableBlock> near = getNear(b);
		for (LocatableBlock block : near) {
			if (canSet(block)) {
				minerios.put(getKey(block.getPosition()), ore);

				count--;
				if (count > 0) {
					geraVeioDeMinerio(block, ore, count);
				}
				break;
			}
		}
	}

	public boolean canSet(LocatableBlock bl) {
		return isIn(bl.getPosition().getX(), bl.getPosition().getY(), bl.getPosition().getZ(), false);

	}

}
