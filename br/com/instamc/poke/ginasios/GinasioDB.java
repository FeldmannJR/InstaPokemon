/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.DB;
import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class GinasioDB {

	public static void init() {
		try {
			getConnection().createStatement().execute(
					"CREATE TABLE IF NOT EXISTS ginasios ( nome VARCHAR(100) PRIMARY KEY, dono VARCHAR(200), badge VARCHAR(100), lastopen Timestamp, setado TIMESTAMP,location VARCHAR(200))");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static String locationToString(Location<World> l) {
		if (l == null) {
			return "null";
		}
		String g = l.getExtent().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ();

		return g;

	}

	public static void saveGinasion(Ginasio g) {
		try {
			PreparedStatement pre = getConnection().prepareStatement(
					"INSERT INTO ginasios (`nome`,`dono`,`badge`,`lastopen`,`setado`,`location`) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `dono` = ?, `badge` = ?,`lastopen` = ?, `setado` = ?,`location` = ?");
			pre.setString(1, g.getNome());
			if (g.getDono() != null) {
				pre.setString(2, g.getDono().toString());
				pre.setString(7, g.getDono().toString());
			} else {
				pre.setString(2, null);
				pre.setString(7, null);
			}
			pre.setString(3, g.getBadge().name());
			pre.setString(8, g.getBadge().name());

			pre.setTimestamp(4, g.getLastopen());
			pre.setTimestamp(9, g.getLastopen());

			pre.setTimestamp(5, g.getSetado());
			pre.setTimestamp(10, g.getSetado());

			pre.setString(6, locationToString(g.getLocation()));
			pre.setString(11, locationToString(g.getLocation()));
			pre.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void deletaGinasio(String nome) {
		try {
			getConnection().createStatement().execute("DELETE FROM `ginasios` WHERE nome ='"+nome+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Location<World> stringToLocation(String s) {
		String[] p = s.split(";");
		if (p.length != 4) {
			return null;
		}
		double x, y, z = 0;
		try {
			x = Double.valueOf(p[1]);

			y = Double.valueOf(p[2]);

			z = Double.valueOf(p[3]);
		} catch (NumberFormatException ex) {
			return null;
		}
		if (!Sponge.getServer().getWorld(p[0]).isPresent()) {
			return null;
		}

		Location<World> l = Sponge.getServer().getWorld(p[0]).get().getLocation(new Vector3d(x, y, z));
		return l;
	}

	public static List<Ginasio> loadGinasios() {
		List<Ginasio> gr = new ArrayList();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM ginasios");
			while (rs.next()) {
				UUID dono = null;
				if (rs.getString("dono") != null) {
					dono = UUID.fromString(rs.getString("dono"));
				}
				EnumBadges ba = null;
				if (rs.getString("badge") != null) {
					for (EnumBadges e : EnumBadges.values()) {
						if (e.name().equals(rs.getString("badge"))) {
							ba = e;
						}
					}
				}

				gr.add(new Ginasio(dono, ba, rs.getTimestamp("lastopen"), rs.getTimestamp("setado"),
						rs.getString("nome"), stringToLocation(rs.getString("location"))));
			}
		} catch (SQLException ex) {
			Logger.getLogger(GinasioDB.class.getName()).log(Level.SEVERE, null, ex);
		}

		return gr;

	}

	public static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}
}
