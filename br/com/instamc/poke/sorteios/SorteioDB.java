package br.com.instamc.poke.sorteios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;

public class SorteioDB {

	public static void createTables() {
		try {
			InstaPokemon.getDB().createStatement().execute("CREATE TABLE IF NOT EXISTS sorteios (id INTEGER AUTO_INCREMENT PRIMARY KEY,preco INTEGER, premios TEXT,termino TIMESTAMP, bilhetes INTEGER, sorteado INTEGER DEFAULT -1, pego BOOLEAN)");
			InstaPokemon.getDB().createStatement().execute("CREATE TABLE IF NOT EXISTS bilhetescomprados (uuid varchar(200), id INTEGER)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void salva(Sorteio s) {

		if (s.id == -1) {

			try {
				PreparedStatement ps = InstaPokemon.getDB().prepareStatement("INSERT INTO sorteios (`premios`,`termino`,`bilhetes`,`sorteado`,`pego`,`preco`) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, ItemStackSerializer.serializeListItemStack(s.itens));
				ps.setTimestamp(2, s.termina);
				ps.setInt(3, s.bilhetes);
				ps.setInt(4, s.sorteado);
				ps.setBoolean(5, s.premio);
				ps.setInt(6, s.preco);
				ps.execute();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					s.id = rs.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = InstaPokemon.getDB().prepareStatement("UPDATE sorteios set `premios` = ?,`termino` = ?, `bilhetes` = ?, `sorteado` = ?, `pego` = ?, `preco` = ? where id = ?");
				ps.setString(1, ItemStackSerializer.serializeListItemStack(s.itens));
				ps.setTimestamp(2, s.termina);
				ps.setInt(3, s.bilhetes);
				ps.setInt(4, s.sorteado);
				ps.setBoolean(5, s.premio);
				ps.setInt(6, s.preco);
				ps.setInt(7, s.id);

				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Sorteio sor : new ArrayList<Sorteio>(cache)) {
			if (sor.id == s.id) {
				cache.remove(sor);
			}
		}
		cache.add(s);

	}

	private static List<Sorteio> cache = null;

	public static boolean comprou(UUID uid, int id) {

		try {
			if (InstaPokemon.getDB().createStatement().executeQuery("SELECT 1 FROM bilhetescomprados WHERE uuid='" + uid.toString() + "' and id =" + id).next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static void setComprou(UUID uid, int id) {
		if (!comprou(uid, id)) {
			try {
				InstaPokemon.getDB().createStatement().execute("INSERT INTO bilhetescomprados (`uuid`,`id`) VALUES('"+uid.toString()+"',"+id+")");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static List<Sorteio> getSorteios() {
		if (cache == null) {

			List<Sorteio> sorteios = new ArrayList<Sorteio>();
			try {
				ResultSet rs = InstaPokemon.getDB().createStatement().executeQuery("SELECT * FROM sorteios");
				while (rs.next()) {
					Sorteio s = new Sorteio();
					s.bilhetes = rs.getInt("bilhetes");
					s.sorteado = rs.getInt("sorteado");
					s.termina = rs.getTimestamp("termino");
					s.id = rs.getInt("id");
					s.premio = rs.getBoolean("pego");
					s.preco = rs.getInt("preco");
					s.itens = ItemStackSerializer.deserializeListItemStack(rs.getString("premios"));

					sorteios.add(s);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cache = sorteios;
		}
		return cache;

	}

}
