package br.com.instamc.poke.pokeloot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.item.inventory.ItemStack;



import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;

public class PokeLootDB {

	public static void createTables() {
		try {
			InstaPokemon.getDB().createStatement()
					.execute("CREATE TABLE IF NOT EXISTS pokeloot (id INTEGER PRIMARY KEY,itens TEXT)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static List<ItemStack> cache = null;

	public static List<ItemStack> podeVir() {
		if (cache == null) {

			cache = new ArrayList();
			try {
				ResultSet rs = InstaPokemon.getDB().createStatement().executeQuery("SELECT * FROM pokeloot LIMIT 1");
				if (rs.next()) {

					String[] split = rs.getString("itens").split(",");
					for (String it : split) {
						ItemStack item = ItemStackSerializer.deserializeItemStack(it);
						if (item != null) {
							cache.add(item);
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return cache;
	}

	public static void set(List<ItemStack> itens) {
		String itenss = "";
		for (int x = 0; x < itens.size(); x++) {
			if (x > 0) {
				itenss += ",";
			}
			itenss += ItemStackSerializer.serializeItemStack(itens.get(x));
		}
		cache = itens;
		try {
			PreparedStatement pr = InstaPokemon.getDB().prepareStatement("INSERT INTO pokeloot (`id`,`itens`) VALUES(0,?) ON DUPLICATE KEY update `itens` = ?");
			pr.setString(1, itenss);
			pr.setString(2, itenss);
			pr.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
