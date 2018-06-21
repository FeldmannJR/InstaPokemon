package br.com.instamc.poke.dbs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import br.com.instamc.poke.InstaPokemon;

public class PvPDB {

	public static void init() {
		try {
			InstaPokemon.getDB().createStatement().execute("CREATE TABLE IF NOT EXISTS vitorias (`id` INTEGER AUTO_INCREMENT PRIMARY KEY, `ganhador` VARCHAR(200),`perdedor` VARCHAR(200))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean alreadyWon(UUID winner, UUID loser) {
		try {
			ResultSet rs = InstaPokemon.getDB().createStatement().executeQuery("SELECT 1 FROM vitorias WHERE `ganhador` = '" + winner.toString() + "' and `perdedor` = '" + loser.toString() + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	public static void won(UUID winner,UUID loser){
		try {
			InstaPokemon.getDB().createStatement().execute("INSERT INTO vitorias (`ganhador`,`perdedor`) VALUES('"+winner.toString()+"','"+loser.toString()+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
