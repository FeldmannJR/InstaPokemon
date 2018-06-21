package br.com.instamc.poke.placas;

import java.util.List;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

public class BlockClickSignEvent extends AbstractEvent implements TargetPlayerEvent {

	private final Player player;
	private final Cause cause;
	private final List<String> linhas;
	private final BlockSnapshot block;
	boolean cancelblockinteract = true;

	public BlockClickSignEvent(Player p, Cause c, BlockSnapshot block, List<String> linhas) {
		this.player = p;
		this.cause = c;
		this.block = block;
		this.linhas = linhas;
	}

	@Override
	public Cause getCause() {
		return cause;
	}

	public boolean isCancelblockinteract() {
		return cancelblockinteract;
	}

	public void setCancelblockinteract(boolean cancelblockinteract) {
		this.cancelblockinteract = cancelblockinteract;
	}

	public List<String> getLinhas() {
		return linhas;
	}

	public BlockSnapshot getBlock() {
		return block;
	}

	@Override
	public Player getTargetEntity() {
		return player;
	}

}
