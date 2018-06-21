package br.com.instamc.poke.elites;

import java.util.UUID;

public class ElitePlayer {

	public static int LIDER = 1;
	public static int MEMBRO = 0;
	public static int FUNDADOR = 2;
	public static int STAFF = 3;

	public String titulo = "";
	public int elite = -1;
	public int cargo = 0;
	public UUID uid;

	public Elite getElite() {
		if (elite == -1)
			return null;
		return EliteManager.getEliteById(elite);

	}

	public boolean hasElite() {
		return elite != -1;
	}

	public void resetTitulo() {
		titulo = "";
	}

	public int getCargo() {
		return cargo;
	}
}
