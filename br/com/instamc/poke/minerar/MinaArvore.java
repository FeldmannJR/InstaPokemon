package br.com.instamc.poke.minerar;

import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.mutable.block.StoneData;
import org.spongepowered.api.data.manipulator.mutable.block.TreeData;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import br.com.instamc.poke.InstaPokemon;

public class MinaArvore extends Mina {

	public MinaArvore(World w, Vector3i pos1, Vector3i pos2) {
		super(w, pos1, pos2);
		ran = new Random();
	}

	@Override
	public void enche() {
		checkPlayers();
		int minX = Math.min(pos1.getX(), pos2.getX());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int minZ = Math.min(pos1.getZ(), pos2.getZ());
		int maxX = Math.max(pos1.getX(), pos2.getX());
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int maxZ = Math.max(pos1.getZ(), pos2.getZ());

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					w.getLocation(x, y, z).removeBlock(Cause.source(InstaPokemon.getPlugin()).build());
				}
			}
		}
		for (int y = minY; y < maxY; y += 10) {
			if ((y + 10) > maxY) {
				break;
			}

			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {

					setBlock(new Vector3i(x, y, z), BlockTypes.GRASS.getDefaultState());
				}
			}
		}
		for (int y = minY; y < maxY; y += 10) {
			if ((y + 10) > maxY) {
				break;
			}
			botaArvores(y);
		}
	}

	public void botaArvores(int layer) {

		int minX = Math.min(pos1.getX(), pos2.getX());
		int minY = Math.min(pos1.getY(), pos2.getY());
		int minZ = Math.min(pos1.getZ(), pos2.getZ());
		int maxX = Math.max(pos1.getX(), pos2.getX());
		int maxY = Math.max(pos1.getY(), pos2.getY());
		int maxZ = Math.max(pos1.getZ(), pos2.getZ());

		boolean torch = true;
		for (int x = minX + 3; x <= maxX; x += 6) {
			torch = !torch;

			for (int z = minZ + 3; z <= maxZ; z += 6) {
				

				if (x + 3 > maxX) {
					continue;
				}
				if (z + 3 > maxZ) {
					continue;
				}
				BlockState log = BlockTypes.LOG.getDefaultState();
				BlockState leaves = BlockTypes.LEAVES.getDefaultState();
				int r = ran.nextInt(50);
				if (r <= 5) {
					log = getLogType(TreeTypes.JUNGLE);
					leaves = getLeavesType(TreeTypes.JUNGLE);
				} else
					if (r <= 10) {
						log = getLogType(TreeTypes.BIRCH);
						leaves = getLeavesType(TreeTypes.BIRCH);

					} else
						if (r <= 15) {
							log = getLogType(TreeTypes.SPRUCE);
							leaves = getLeavesType(TreeTypes.SPRUCE);

						} else
							if (r <= 20) {
								log = getLogType(TreeTypes.ACACIA);
								leaves = getLeavesType(TreeTypes.ACACIA);

							} else
								if (r <= 25) {
									log = getLogType(TreeTypes.DARK_OAK);
									leaves = getLeavesType(TreeTypes.DARK_OAK);

								}

				botaArvore(w.getLocation(x, layer + 1, z), log, leaves, torch);

			}
		}

	}

	private static BlockState getLogType(TreeType type) {
		BlockType b = BlockTypes.LOG;
		if (type == TreeTypes.ACACIA || type == TreeTypes.DARK_OAK) {
			b = BlockTypes.LOG2;
		}
		BlockState state = b.getDefaultState();
		TreeData d = Sponge.getDataManager().getManipulatorBuilder(TreeData.class).get().create();
		d.set(d.type().set(type));
		state = state.with(d.asImmutable()).get();

		return state;
	}

	private static BlockState getLeavesType(TreeType type) {
		BlockType b = BlockTypes.LEAVES;
		if (type == TreeTypes.ACACIA || type == TreeTypes.DARK_OAK) {
			b = BlockTypes.LEAVES2;
		}
		BlockState state = b.getDefaultState();
		TreeData d = Sponge.getDataManager().getManipulatorBuilder(TreeData.class).get().create();
		d.set(d.type().set(type));
		state = state.with(d.asImmutable()).get();

		return state;
	}

	public Random ran;

	@Override
	public void setBlock(int x, int y, int z, BlockState t) {

		if (!isIn(x, y, z, true))
			return;
		super.setBlock(x, y, z, t);
	}

	public void botaArvore(Location<World> loc, BlockState m, BlockState f, boolean torch) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		for (int h = 0; h < 5; h++) {
			setBlock(x, y + h, z, m);

		}
		for (int lx = -2; lx <= 2; lx++) {
			for (int lz = -2; lz <= 2; lz++) {
				if (lx == 0 && lz == 0) {
					continue;
				}
				setBlock(x + lx, y + 2, z + lz, f);

			}
		}
		for (int lx = -2; lx <= 2; lx++) {
			for (int lz = -2; lz <= 2; lz++) {
				if (lx == 0 && lz == 0) {
					continue;
				}
				boolean canto = false;
				// z ==-2 x==-2
				// z==2 x==2
				// z==-2 x==2
				// z==-2 x==-2
				if ((lz == -2 || lz == 2) && (lx == 2 || lx == -2)) {
					canto = true;
				}
				if (ran.nextInt(4) != 0 || !canto) {
					setBlock(x + lx, y + 3, z + lz, f);
				}
			}
		}
		for (int lx = -1; lx <= 1; lx++) {
			for (int lz = -1; lz <= 1; lz++) {
				if (lx == 0 && lz == 0) {
					continue;
				}
				boolean canto = false;

				if ((lz == -1 || lz == 1) && (lx == 1 || lx == -1)) {
					canto = true;
				}
				if (ran.nextInt(4) != 0 || !canto) {
					setBlock(x + lx, y + 4, z + lz, f);
				}
			}
		}
		setBlock(x + 1, y + 5, z, f);
		setBlock(x - 1, y + 5, z, f);
		setBlock(x, y + 5, z, f);
		setBlock(x, y + 5, z + 1, f);
		setBlock(x, y + 5, z - 1, f);
		if (torch) {
			setBlock(x + 3, y - 1, z, BlockTypes.GLOWSTONE.getDefaultState());
			setBlock(x - 3, y - 1, z, BlockTypes.GLOWSTONE.getDefaultState());
		}
	}

}
