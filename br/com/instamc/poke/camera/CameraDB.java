/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.camera;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ranks.RankCache;
import br.com.instamc.poke.ranks.Stats.Stat;
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
public class CameraDB {

	public static void init() {
		try {
			getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS fotos (uuid VARCHAR(200) primary key, fotosvalue TEXT,quantidade INTEGER)");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	private static HashMap<UUID, CacheFotos> cache = new HashMap<UUID, CacheFotos>();

	public static CacheFotos getFotos(UUID uid) {
		if (!cache.containsKey(uid)) {

			cache.put(uid, getFotosFromDB(uid));
		}
		return cache.get(uid);

	}

	public static RankCache getTop(int x){
		RankCache cache = new RankCache();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT quantidade,uuid FROM fotos ORDER BY quantidade DESC LIMIT "+x);
			while(rs.next()){
				cache.top.put(UUID.fromString(rs.getString("uuid")), rs.getInt("quantidade"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache;
	}

	
	public static void addFoto(UUID uid, Foto bad) {
		CacheFotos fotos= getFotosFromDB(uid);
		if (!fotos.fotos.contains(bad)) {
			fotos.fotos.add(bad);
			String poe = "";
			for (int x = 0; x < fotos.fotos.size(); x++) {
				if (x > 0) {
					poe += ",";
				}
				poe += fotos.fotos.get(x).toString();
			}
			fotos.qtd = fotos.fotos.size();
			try {
				getConnection().createStatement().execute("INSERT INTO fotos (`uuid`,`fotosvalue`,`quantidade`) VALUES('" + uid.toString() + "','" + poe + "',"+fotos.qtd+") on duplicate key update `fotosvalue` = '" + poe + "',`quantidade` = "+fotos.qtd);
				
			} catch (SQLException ex) {
				Logger.getLogger(CameraDB.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		cache.put(uid, fotos);

	}

	private static CacheFotos getFotosFromDB(UUID uid) {
		
		CacheFotos ca = new CacheFotos();
		try {
			ResultSet rs = getConnection().createStatement().executeQuery("SELECT fotosvalue,quantidade FROM fotos WHERE uuid ='" + uid.toString() + "'");
			if (rs.next()) {
				String badges = rs.getString("fotosvalue");
				ca.qtd =rs.getInt("quantidade");
				String[] split = badges.split(",");
				for (String s : split) {
					Foto f = Foto.fromString(s);
					if (f != null) {
						ca.fotos.add(f);
					}

				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(CameraDB.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return ca;
	}

	private static Connection getConnection() {
		return DB.getConnection(InstaPokemon.banco);
	}

}
