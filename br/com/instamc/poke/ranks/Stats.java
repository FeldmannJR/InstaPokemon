/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ranks;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.PixelmonItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

/**
 *
 * @author Carlos
 */
public class Stats {

	public static enum Stat {

		CAPTUROU("Pokemons Capturados", PixelmonUtils.getPokeball(EnumPokeballs.DuskBall)),
		VITORIASPVP("Jogadores Derrotados", ItemStack.of(ItemTypes.DIAMOND, 1)),
		DERROTASPVP("Derrotas por Jogadores", ItemStack.of(ItemTypes.BARRIER, 1)),
		VITORIASWILD("Pokemons Wild Derrotados", ItemStack.of(ItemTypes.LEAVES, 1)),
		VITORIASTRAINER("Trainers Derrotados", ItemStack.of(ItemTypes.ARMOR_STAND, 1)),
		APRICORNS("Apricorns Colhidas", PixelmonUtils.getItemStack(EnumApricorns.Blue.apricorn())),
		CRAFTPOKEBALL("Pokebolas Feitas", PixelmonUtils.getPokeball(EnumPokeballs.PokeBall)),
		POKELOOTS("Pokeloots Coletados", PixelmonUtils.getItemStack(PixelmonBlocks.pokeChest)),
		POKESPESCADOS("Pokemon Pescados", PixelmonUtils.getItemStack(PixelmonItems.goodRod)),
		EVOLUCOES("Pokemons Evoluidos", PixelmonUtils.getItemStack(PixelmonItems.fireStone)),
		MINUTOSOLINE("Minutos Online",PixelmonUtils.getItemStack(PixelmonBlocks.blueClockBlock));

		;

		String nome;
		ItemStack icone;

		public String getNome() {
			return nome;
		}

		public ItemStack getIcone() {
			return icone;
		}

		private Stat(String nome, ItemStack icone) {
			this.nome = nome;
			this.icone = icone;

		}

	}
	
	public static RankCache getTop(Stat s,int x){
		RankCache cache = new RankCache();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT stat_"+s.name()+",uuid FROM stats ORDER BY stat_"+s.name()+" DESC LIMIT "+x);
			while(rs.next()){
				cache.top.put(UUID.fromString(rs.getString("uuid")), rs.getInt("stat_"+s.name()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache;
	}

	public static void init() {
		try {
			getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS stats (`id` INT NOT NULL AUTO_INCREMENT,uuid VARCHAR(200) UNIQUE,PRIMARY KEY (`id`));");
			alterTables();
		} catch (SQLException ex) {
			DB.ErroMySQL(ex);
		}
		StatsListener listener = new StatsListener();
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, listener);
		Pixelmon.EVENT_BUS.register(listener);
		MinecraftForge.EVENT_BUS.register(listener);
		ComandoAPI.enable(new CmdVerStats());
	}

	@Listener
	public void quit(ClientConnectionEvent.Disconnect ev) {
		removeCache(ev.getTargetEntity().getUniqueId());
	}

	private static Connection getConnection() {

		return DB.getConnection(InstaPokemon.banco);

	}

	private static void alterTables() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = getConnection();
			for (Stat cf : Stat.values()) {
				DatabaseMetaData md = connection.getMetaData();
				ResultSet rs = md.getColumns(null, null, "stats", "stat_" + cf.name());
				if (!rs.next()) {
					Statement stmt = connection.createStatement();
					stmt.executeUpdate("ALTER TABLE stats ADD `stat_" + cf.name() + "` INTEGER DEFAULT 0");
					stmt.close();
				}
			}
			connection.close();
		} catch (SQLException ex) {
			DB.ErroMySQL(ex);
		} catch (Exception ex) {

			DB.ErroMySQL(ex);
		}
	}

	private static HashMap<UUID, StatCache> configs = new HashMap<>();

	public static int geStats(UUID uid, Stat conf) {
		if (!configs.containsKey(uid)) {
			configs.put(uid, getStatDB(uid));
		}
		return configs.get(uid).configs.get(conf);

	}

	public static void setStats(UUID uid, Stat cfg, int value) {
		if (!configs.containsKey(uid)) {
			configs.put(uid, getStatDB(uid));
		}
		configs.get(uid).configs.put(cfg, value);
		new Thread(new Runnable() {
			@Override
			public void run() {
				setStatDB(uid, cfg, value);

			}
		}).start();

	}

	public static void addStats(UUID uid, Stats.Stat stat, int add) {
		int tem = geStats(uid, stat);
		Stats.setStats(uid, stat, add + tem);
	}

	public void addStats(UUID uid, Stats.Stat stat) {
		addStats(uid, stat, 1);
	}

	private static void setStatDB(UUID uid, Stat cfg, int value) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("INSERT INTO stats (`uuid`,`stat_" + cfg.name() + "`) " + "VALUES(?,?)" + "ON DUPLICATE KEY UPDATE stat_" + cfg.name() + " = ?");
			statement.setString(1, uid.toString());
			statement.setInt(2, value);
			statement.setInt(3, value);

			statement.executeUpdate();
			return;
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public static void removeCache(UUID uid) {
		configs.remove(uid);
	}

	private static StatCache getStatDB(UUID uid) {
		StatCache cache = new StatCache();
		for (Stat cf : Stat.values()) {
			cache.configs.put(cf, 0);
		}
		try {

			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM stats WHERE uuid='" + uid.toString() + "'");
			if (rs.next()) {
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					String NomeColuna = rs.getMetaData().getColumnName(i);
					if (!NomeColuna.contains("_")) {
						continue;
					}
					String[] split = NomeColuna.split("_");
					String TagNome = NomeColuna.split("_")[0];

					String ValueTag = "";
					for (int x = 1; x < split.length; x++) {
						ValueTag += split[x];

						if (x != (split.length - 1)) {
							ValueTag += "_";
						}
					}
					if (TagNome.equals("stat")) {
						for (Stat cf : Stat.values()) {
							if (cf.name().equals(ValueTag)) {
								int valor = rs.getInt(NomeColuna);

								cache.configs.put(cf, valor);
							}
						}

					}
				}
			}
		} catch (SQLException ex) {
			DB.ErroMySQL(ex);
		}
		return cache;
	}

	public static class StatCache {

		HashMap<Stat, Integer> configs = new HashMap();

	}

}
