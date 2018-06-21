/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.insignias;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ranks.RankCache;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.utils.Txt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.title.Title;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

/**
 *
 * @author Carlos
 */
public class InsigniaDB {

	public static void init() {
		try {
			getConnection().createStatement()
					.execute("CREATE TABLE IF NOT EXISTS badges (uuid VARCHAR(200) primary key, badgesvalue TEXT, quantidade INTEGER)");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	private static HashMap<UUID, List<EnumBadges>> cache = new HashMap<UUID, List<EnumBadges>>();

	public static List<EnumBadges> getInsignias(UUID uid) {
		if (!cache.containsKey(uid)) {

			cache.put(uid, getInsigniasFromDB(uid));
		}
		return cache.get(uid);

	}

	public static void addInsignia(Player p, EnumBadges bad) {
		if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(bad)) {
			InsigniaDB.addInsignia(p.getUniqueId(), bad);
			String nome = bad.name().replace("Badge", "");
			int tem = getInsignias(p.getUniqueId()).size();
			Title t = Title.builder().title(Txt.f("§c§l" + nome)).subtitle(Txt.f("Você ganhou uma nova insignia!")).stay(60).fadeOut(20)
					.build();
			p.sendTitle(t);
			InstaPokemon.sendMessage(p, "§eVocê ganhou a insignia §c" + nome + " §e! §fAgora você §6" + tem
					+ "§f insignias! §7(Use /insignias)");
			for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
				if (pOn != p) {
					InstaPokemon.sendMessage(pOn, "§eO jogador §6" + p.getName() + " §eganhou a insignia §b" + nome
							+ "§e ! §fAgora tem §6" + tem + "§f insignias!");
				}
			}

		}
	}

	public static  RankCache getTop(int x){
		RankCache cache = new RankCache();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT quantidade,uuid FROM badges ORDER BY quantidade DESC LIMIT "+x);
			while(rs.next()){
				cache.top.put(UUID.fromString(rs.getString("uuid")), rs.getInt("quantidade"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache;
	}
	public static void addInsignia(UUID uid, EnumBadges bad) {
		List<EnumBadges> badges = getInsigniasFromDB(uid);
		if (!badges.contains(bad)) {
			badges.add(bad);
			String poe = "";
			for (int x = 0; x < badges.size(); x++) {
				if (x > 0) {
					poe += ",";
				}
				poe += badges.get(x);
			}
			try {
				getConnection().createStatement().execute("INSERT INTO badges (`uuid`,`badgesvalue`,`quantidade`) VALUES('"
						+ uid.toString() + "','" + poe + "',"+badges.size()+") on duplicate key update `badgesvalue` = '" + poe + "', `quantidade` = "+badges.size());
			} catch (SQLException ex) {
				Logger.getLogger(InsigniaDB.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		cache.put(uid, badges);

	}

	private static List<EnumBadges> getInsigniasFromDB(UUID uid) {
		List<EnumBadges> bad = new ArrayList();
		try {
			ResultSet rs = getConnection().createStatement()
					.executeQuery("SELECT badgesvalue FROM badges WHERE uuid ='" + uid.toString() + "'");
			if (rs.next()) {
				String badges = rs.getString("badgesvalue");
				String[] split = badges.split(",");
				for (String s : split) {
					for (EnumBadges e : EnumBadges.values()) {
						if (e.name().equalsIgnoreCase(s)) {
							bad.add(e);
						}
					}
				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(InsigniaDB.class.getName()).log(Level.SEVERE, null, ex);
		}

		return bad;
	}

	private static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}

}
