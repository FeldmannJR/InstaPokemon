
package br.com.instamc.poke.dbs;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PlayerDB {

	public static List<UUID> firstlogin = new ArrayList();

	public static void init() {
		alterTables();
	}

	public static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}

	public static void alterTables() {
		try {
			DB.getConnection(InstaPokemon.banco).createStatement()
					.executeUpdate("CREATE TABLE IF NOT EXISTS flags (uuid VARCHAR(36) PRIMARY KEY)");
			for (Flag eng : Flag.values()) {
				DatabaseMetaData md = getConnection().getMetaData();
				ResultSet rs = md.getColumns(null, null, "flags", eng.name());
				if (!rs.next()) {
					Statement stmt = getConnection().createStatement();
					stmt.executeUpdate("ALTER TABLE flags ADD " + eng.name() + " varchar(255) DEFAULT null");
					stmt.close();
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static HashMap<UUID, HashMap<Flag, String>> flagcache = new HashMap<UUID, HashMap<Flag, String>>();

	public static String getFlag(UUID uid, Flag f) {
		HashMap<Flag, String> flags = null;
		if (flagcache.containsKey(uid)) {
			flags = flagcache.get(uid);
			if (flags.containsKey(f)) {
				return flags.get(f);
			}

		} else {
			flags = new HashMap<Flag, String>();
		}
		String result = null;
		try {
			ResultSet rs = getConnection().createStatement()
					.executeQuery("SELECT " + f.name() + " FROM flags WHERE uuid='" + uid.toString() + "'");
			if (rs.next()) {
				String oq = rs.getString(f.name());
				if(oq==null){
					oq ="";
				}
				result = oq;
				

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		flags.put(f, result);
		flagcache.put(uid, flags);
		return result;
	}

	public static void setFlag(UUID uid, Flag f, String v) {

		HashMap<Flag, String> flags = null;
		if (flagcache.containsKey(uid)) {
			flags = flagcache.get(uid);

		} else {
			flags = new HashMap<Flag, String>();
		}
		try {
			String q = "INSERT INTO flags (uuid," + f.name() + ") VALUES('" + uid.toString() + "','" + v
					+ "') ON DUPLICATE KEY UPDATE " + f.name() + "='" + v + "'";
			getConnection().createStatement().executeUpdate(q);
			flags.put(f, v);
			flagcache.put(uid, flags);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static enum Flag {
		PEGOUPOKEMON,GANHASHINY;
	}
}
