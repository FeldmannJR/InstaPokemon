package br.com.instamc.poke.minerar;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

public class MinaChance {
	BlockState block;
	double pct = 0.0001;

	int inicia = 0;
	int termina = 0;

	int minvein = 1;
	int maxvein = 2;

	public int getInicia() {
		return inicia;
	}

	public int getTermina() {
		return termina;
	}

	public BlockState getBlock() {
		return block;
	}

	public MinaChance(BlockType type, double chance, int minvein, int maxvein) {
		this(type.getDefaultState(), chance, minvein, maxvein);
	}

	public MinaChance(BlockState type, double chance, int minvein, int maxvein) {

		this.maxvein = maxvein;
		this.minvein = minvein;
		this.block = type;
		this.block = type;
		this.pct = chance;
	}
}
