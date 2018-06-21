package br.com.instamc.poke.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

public class PlayerWinBattleEvent implements Event {

	Player winner;
	Player loser;
	private boolean draw;

	public PlayerWinBattleEvent(Player winner, Player loser,boolean draw) {
		this.winner = winner;
		this.loser = loser;
		this.draw =draw;
		
	}
	public boolean fugiu() {
		return draw;
	}

	public Player getWinner() {
		return winner;
	}

	public Player getLoser() {
		return loser;
	}

	@Override
	public Cause getCause() {
		return Cause.of(NamedCause.source("Plugin"));
	}
}
