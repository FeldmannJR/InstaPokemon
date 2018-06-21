package br.com.instamc.poke.tpscroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.instamc.poke.InstaPokemon;

public class TpScrollDB {

	public TpScrollDB() {

		try {
			getCon().createStatement().execute(

					"CREATE TABLE IF NOT EXISTS tps (" //
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"//
							+ " uuid varchar(200)," //
							+ "nome varchar(200), "//
							+ "regiao varchar(100)," //
							+ "mundo varchar(100), " //
							+ "x DOUBLE,"//
							+ " y DOUBLE, "//
							+ "z DOUBLE, "//
							+ "acaba TIMESTAMP) ");
			getCon().createStatement().execute("DELETE FROM tps WHERE acaba < NOW()");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addScroll(UUID uid, TPScrollInfo info) {

		try {
			PreparedStatement pre = getCon().prepareStatement("INSERT INTO tps (`uuid`,`nome`,`mundo`,`acaba`,`x`,`y`,`z`,`regiao`) VALUES(?,?,?,?,?,?,?,?) ");
			pre.setString(1, uid.toString());
			pre.setString(2, info.getNome());
			pre.setString(3, info.mundo);
			pre.setTimestamp(4, new Timestamp(info.acaba.getTime()));
			pre.setDouble(5, info.x);
			pre.setDouble(6, info.y);
			pre.setDouble(7, info.z);
			pre.setString(8, info.regiao);
			pre.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<TPScrollInfo> getScrolls(UUID uid) {
		List<TPScrollInfo> list = new ArrayList();
		try {
			ResultSet rs = getCon().createStatement().executeQuery("SELECT * FROM tps WHERE uuid ='" + uid + "' and acaba > NOW()");
			while (rs.next()) {
				TPScrollInfo info = new TPScrollInfo();
				info.acaba = rs.getTimestamp("acaba");
				info.x = rs.getDouble("x");
				info.y = rs.getDouble("y");
				info.z = rs.getDouble("z");
				info.mundo = rs.getString("mundo");
				info.nome = rs.getString("nome");
				info.regiao = rs.getString("regiao");
				list.add(info);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public Connection getCon() {
		return InstaPokemon.getDB();
	}

}
