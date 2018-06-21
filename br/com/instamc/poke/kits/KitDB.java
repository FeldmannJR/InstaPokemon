package br.com.instamc.poke.kits;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;

public class KitDB {

	public static void init() {
		try {
			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS kits (`nome` varchar(200) primary key, `itens` TEXT, `minutos` int,`icone` TEXT,`priority` integer)");
			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS kitsc (`uuid` varchar(200), `kit` varchar(200), `vence` TIMESTAMP)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setUsado(UUID uid, String kit, Timestamp t) {
		ResultSet rs;
		try {
			rs = getConnection().createStatement().executeQuery("SELECT * FROM kitsc WHERE `uuid` = '" + uid.toString() + "' and `kit` ='" + kit + "'");
			if (rs.next()) {
				PreparedStatement pre = getConnection().prepareStatement("UPDATE kitsc set `vence` = ? where `uuid`= ? and `kit` = ?");
				pre.setString(2, uid.toString());
				pre.setString(3, kit);
				pre.setTimestamp(1, t);
				pre.execute();
			} else {
				PreparedStatement pre = getConnection().prepareStatement("INSERT INTO kitsc (`uuid`,`kit`,`vence`) VALUES(?,?,?)");
				pre.setString(1, uid.toString());
				pre.setString(2, kit);
				pre.setTimestamp(3, t);
				pre.execute();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Timestamp getUsado(UUID uid, String kit) {
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM kitsc WHERE `uuid` = '" + uid.toString() + "' and `kit` ='" + kit + "'");
			if (rs.next()) {
				Timestamp t = rs.getTimestamp("vence");

				return t;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void saveKit(Kit k) {
		try {
			PreparedStatement pre = getConnection().prepareStatement("INSERT INTO kits (`nome`,`minutos`,`itens`,`icone`,`priority`) VALUES(?,?,?,?,?)ON DUPLICATE KEY update `itens` = ?,`minutos` = ?,`icone` = ?,`priority` = ? ");

			String itens = ItemStackSerializer.serializeListItemStack(k.getItens());
			
			pre.setString(1, k.getNome());
			pre.setInt(2, k.getMinutos());		
			pre.setString(3, itens);
			pre.setString(4, ItemStackSerializer.serializeItemStack(k.icone));
			pre.setInt(5,k.getPriority());
			
			pre.setString(6, itens);
			pre.setInt(7, k.getMinutos());
			pre.setString(8, ItemStackSerializer.serializeItemStack(k.icone));
			pre.setInt(9,k.getPriority());
			
			pre.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Kit> loadKits() {
		ArrayList<Kit> kits = new ArrayList();
		ResultSet rs;
		try {
			rs = getConnection().createStatement().executeQuery("SELECT * FROM kits");
			while (rs.next()) {
				Kit k = new Kit(rs.getString("nome"), rs.getInt("minutos"));

				k.getItens().addAll(ItemStackSerializer.deserializeListItemStack(rs.getString("itens")));
				k.icone = ItemStackSerializer.deserializeItemStack(rs.getString("icone"));
				k.setPriority(rs.getInt("priority"));

				kits.add(k);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kits;
	}

	public static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}

	public static void deletaKit(Kit k) {
		try {
			getConnection().createStatement().execute("DELETE FROM kitsc WHERE kit ='" + k.getNome().toLowerCase() + "'");
			getConnection().createStatement().execute("DELETE FROM kits WHERE nome ='" + k.getNome() + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
