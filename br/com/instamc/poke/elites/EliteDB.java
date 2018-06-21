package br.com.instamc.poke.elites;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;

public class EliteDB {

	public EliteDB() {
		createTables();
	}

	public void createTables() {
		try {
			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS elites ("//
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"//
					+ "nome VARCHAR(200),"//
					+ "icone TEXT,"//
					+ "tag VARCHAR(50),"//
					+ "aliados TEXT,"//
					+ "rivais TEXT,"//
					+ "tagsemcor VARCHAR(50),"//
					+ "created TIMESTAMP," //
					+ "lasttrocatag TIMESTAMP,"//
					+ "fundador VARCHAR(200),"//
					+ "home TEXT" + ")");//

			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS eliteplayers " //
					+ "(" //
					+ "uuid VARCHAR(200) PRIMARY KEY,"//
					+ "eliteid INTEGER," //
					+ "cargo INTEGER,"//
					+ "titulo VARCHAR(200)"//
					+ ")");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Elite loadElite(String where) {
		List<Elite> list = loadEliteList("WHERE " + where);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public List<Elite> loadEliteList(String where) {

		List<Elite> els = new ArrayList<>();

		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM elites " + where);
			while (rs.next()) {
				Elite eli = new Elite();
				eli.id = rs.getInt("id");
				eli.nome = rs.getString("nome");
				eli.tag = rs.getString("tag");
				if (rs.getString("icone") != null) {

					eli.icone = ItemStackSerializer.deserializeItemStack(rs.getString("icone"));
				}
				for (String s : rs.getString("rivais").split(",")) {
					try {
						int x = Integer.valueOf(s);
						eli.rivais.add(x);
					} catch (NumberFormatException ex) {

					}
				}
				for (String s : rs.getString("aliados").split(",")) {
					try {
						int x = Integer.valueOf(s);
						eli.aliados.add(x);
					} catch (NumberFormatException ex) {

					}
				}
				EliteHome home = new EliteHome(rs.getString("home"));
				eli.home = home;
				eli.fundada = rs.getTimestamp("created");
				eli.fundador = UUID.fromString(rs.getString("fundador"));
				eli.lasttrocatag = rs.getTimestamp("lasttrocatag");
				els.add(eli);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return els;

	}

	public List<ElitePlayer> loadElitePlayer(String where) {
		ArrayList<ElitePlayer> players = new ArrayList();

		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM eliteplayers WHERE " + where);
			while (rs.next()) {
				ElitePlayer pl = new ElitePlayer();
				pl.cargo = rs.getInt("cargo");
				pl.titulo = rs.getString("titulo");
				pl.uid = UUID.fromString(rs.getString("uuid"));
				pl.elite = rs.getInt("eliteid");
				players.add(pl);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return players;
	}

	public Connection getConnection() {
		return InstaPokemon.getDB();

	}

	public void save(ElitePlayer e) {
		try {
			PreparedStatement st = getConnection().prepareStatement("INSERT INTO eliteplayers (uuid,cargo,titulo,eliteid) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE cargo = ?,titulo=?,eliteid=?");
			st.setString(1, e.uid.toString());
			st.setInt(2, e.cargo);
			st.setInt(5, e.cargo);
			st.setString(3, e.titulo);
			st.setString(6, e.titulo);
			st.setInt(4, e.elite);
			st.setInt(7, e.elite);
			st.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void save(Elite e) {
		if (e.id == -1) {
			/*
			 * "id INTEGER AUTO_INCREMENT PRIMARY KEY," "nome VARCHAR(200),
			 * "icone TEXT," "tag VARCHAR(50)," "aliados TEXT," "rivais TEXT,"
			 * "tagsemcor VARCHAR(50)," + "created TIMESTAMP,"
			 * "fundador VARCHAR(200)," "home TEXT," + ")
			 */

			try {
				PreparedStatement st = getConnection().prepareStatement("INSERT INTO elites (nome,tag,tagsemcor,aliados,rivais,created,fundador,home,lasttrocatag) VALUES(?,?,?,?,?,?,?,?,?)");
				st.setString(1, e.nome);
				st.setString(2, e.tag);
				st.setString(3, e.getTagDB());
				st.setString(4, e.getAliadosString());
				st.setString(5, e.getRivaisString());
				st.setTimestamp(6, e.fundada);
				st.setString(7, e.fundador.toString());
				st.setString(8, e.home.toString());
				st.setTimestamp(9, e.lasttrocatag);
				st.execute();
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					e.id = rs.getInt(1);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			try {
				PreparedStatement st = getConnection().prepareStatement("UPDATE elites set nome = ?,tag = ?,tagsemcor = ?,aliados = ?,rivais = ?,created = ?,fundador = ?,home= ?,icone = ?,lasttrocatag = ? where id = ?");
				st.setString(1, e.nome);
				st.setString(2, e.tag);
				st.setString(3, e.getTagDB());
				st.setString(4, e.getAliadosString());
				st.setString(5, e.getRivaisString());
				st.setTimestamp(6, e.fundada);
				st.setString(7, e.fundador.toString());
				st.setString(8, e.home.toString());
				if (e.icone != null) {
					st.setString(9, ItemStackSerializer.serializeItemStack(e.icone));
				} else {
					st.setString(9, null);

				}
				st.setTimestamp(10, e.lasttrocatag);

				st.setInt(11, e.id);
				st.execute();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void delete(int id) {
		try {
			getConnection().createStatement().execute("DELETE FROM elites WHERE id = " + id);
			getConnection().createStatement().execute("DELETE FROM eliteplayers WHERE eliteid = " + id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

}
