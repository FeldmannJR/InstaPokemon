/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.chaves;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ranks.RankCache;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.SpongeLib;
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
public class ChavesDB {

	public static void init() {
		try {
			SpongeLib.getPlugin().doLog("Iniciande ChavesDB");
			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS chaves (uuid VARCHAR(200) primary key, chavesvalue TEXT)");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	private static HashMap<UUID, List<String>> cache = new HashMap<UUID, List<String>>();

	public static List<String> getChaves(UUID uid) {
		if (!cache.containsKey(uid)) {

			cache.put(uid, getChavesFromDB(uid));
		}
		return cache.get(uid);

	}

	public static void addChave(UUID uid, String chave) {
		List<String> chaves =getChavesFromDB(uid);
		if (!chaves.contains(chave)) {
			chaves.add(chave);
			String poe = "";
			for (int x = 0; x < chaves.size(); x++) {
				if (x > 0) {
					poe += ",";
				}
				poe += chaves.get(x);
			}
			try {
				getConnection().createStatement().execute("INSERT INTO chaves (`uuid`,`chavesvalue`) VALUES('" + uid.toString() + "','" + poe + "') on duplicate key update `chavesvalue` = '" + poe + "'");
			} catch (SQLException ex) {
				Logger.getLogger(ChavesDB.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		cache.put(uid, chaves);

	}

	private static List<String> getChavesFromDB(UUID uid) {
		List<String> bad = new ArrayList();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT chavesvalue FROM chaves WHERE uuid ='" + uid.toString() + "'");
			if (rs.next()) {
				String badges = rs.getString("chavesvalue");
				String[] split = badges.split(",");
				for (String s : split) {
					if (!s.isEmpty()) {
						bad.add(s);
					}
				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(ChavesDB.class.getName()).log(Level.SEVERE, null, ex);
		}

		return bad;
	}

	private static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}

}
