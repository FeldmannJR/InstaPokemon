package br.com.instamc.poke.logs;

import java.sql.SQLException;
import java.util.UUID;

import org.spongepowered.api.event.block.TickBlockEvent.Scheduled;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.SchedulerUtils;

public class LogsDB {

	public static void init() {

		try {
			InstaPokemon.getDB().createStatement().execute(

					"CREATE TABLE IF NOT EXISTS pokemonlogs (" //
							+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"//
							+ "uuid varchar(200)," //
							+ "pokemon varchar(200), "//
							+ "rpokemon varchar(200)," + "action varchar(100)," //
							+ "participante varchar(200),"//
							+ "quando TIMESTAMP) ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void addLog(String poke, String rpoke, UUID uid, String participante, PokeAction action) {

		SchedulerUtils.runAsync(new Runnable() {

			@Override
			public void run() {
				try {
					InstaPokemon.getDB().createStatement().execute("INSERT INTO pokemonlogs (`uuid`,`action`,`participante`,`quando`,`pokemon`,`rpokemon`) VALUES('" + uid.toString() + "','" + action.toString() + "','" + participante + "',NOW(),'" + (poke != null ? poke : "") + "','" + (rpoke != null ? rpoke : "") + "')");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, 1);

	}

	public static enum PokeAction {
		DESTROY, GANHA, TROCA, CUSTOM, VENDE

	}

}
