package br.com.instamc.poke.elites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.data.util.DataQueries.Compatibility.Forge;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.PokeScore;
import br.com.instamc.poke.elites.cmds.CmdClanChat;
import br.com.instamc.poke.elites.cmds.CmdElite;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraftforge.common.MinecraftForge;

public class EliteManager {

	private static EliteDB db;
	private static ConviteManager convites;

	public static int MAXIMO = 9;

	public static void init() {
		db = new EliteDB();
		convites = new ConviteManager();
		//loadAll();
		ComandoAPI.enable(new CmdElite());
		ComandoAPI.enable(new CmdClanChat());
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, new EliteListeners());
		Pixelmon.EVENT_BUS.register(new EliteListeners());
	}

	public static ConviteManager getConvites() {
		return convites;
	}

	private static HashMap<Integer, Elite> elites = new HashMap();
	private static HashMap<UUID, ElitePlayer> eliteplayers = new HashMap();

	public static Elite getEliteById(int id) {
		if (elites.containsKey(id)) {
			return elites.get(id);
		}
		Elite eli = loadFromBanco("id = " + id);
		if (eli != null) {
			elites.put(id, eli);
		}
		return eli;

	}

	public static void loadAll() {
		for (Elite eli : db.loadEliteList("")) {
			elites.put(eli.id, eli);
			for (ElitePlayer pl : db.loadElitePlayer("eliteid=" + eli.id)) {
				eli.getMembros().add(pl.uid);
				if (!eliteplayers.containsKey(pl.uid)) {

					eliteplayers.put(pl.uid, pl);
				}
			}
		}
	}

	public static void sendMessage(Player p, String msg) {
		p.sendMessage(Txt.f("§d§l[Elite] §f" + msg));
	}

	public static void broadcast(String msg) {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			sendMessage(p, msg);
		}
	}

	public static Elite getEliteByTag(String tag) {
		for (Elite eli : elites.values()) {
			if (Txt.f(eli.tag).toPlain().equalsIgnoreCase(tag)) {
				return eli;
			}

		}
		Elite eli = loadFromBanco("tagsemcor = '" + tag.toLowerCase() + "'");
		if (eli != null) {
			elites.put(eli.id, eli);
		}
		return eli;

	}

	public static void delete(Elite eli) {
		for (Player p : eli.getOnlinePlayers()) {
			PokeScore.updateTeam(p);
		}
		elites.remove(eli.getId());

		for (UUID uid : new ArrayList<UUID>(eliteplayers.keySet())) {
			if (eliteplayers.get(uid).elite == eli.getId()) {
				eliteplayers.remove(uid);
			}
		}
		db.delete(eli.id);
		broadcast("§eA elite §a" + eli.nome + "§7(" + eli.tag + "§7) §efoi excluida!");

	}

	public static Elite getEliteByName(String name) {
		for (Elite eli : elites.values()) {
			if (name.equalsIgnoreCase(eli.nome)) {
				return eli;
			}

		}
		Elite eli = loadFromBanco("nome = '" + name + "'");
		if (eli != null) {
			elites.put(eli.id, eli);
		}
		return eli;

	}

	public static boolean existsName(String name) {
		return getEliteByName(name) != null;

	}

	public static boolean existsTag(String tag) {
		String plain = Txt.f(tag).toPlain().toLowerCase();
		if (getEliteByTag(tag) != null) {
			return true;
		}
		return false;
	}

	public static boolean isTagValid(String tag) {
		Text t = Txt.f(tag);
		String plain = t.toPlain();
		if (plain.length() < 2 || plain.length() > 5) {
			return false;
		}
		// DA PRA COLOCAR 1 COR PRA CADA LETRA
		if (tag.length() > 15) {
			return false;
		}
		List<String> safe = Arrays.asList("staff", "mod", "admin", "adm", "insta", "coord", "vip");
		if (safe.contains(plain.toLowerCase())) {
			return false;
		}
		Pattern pate = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = pate.matcher(plain);
		if (m.find()) {
			return false;
		}

		return true;
	}

	public static void save(Elite e) {
		db.save(e);
		// AQUI PODE VIR UM ELITE NOVO
		elites.put(e.getId(), e);

	}

	public static void save(ElitePlayer pl) {
		db.save(pl);
		// N PRECISA SALVAR NO CACHE PQ O OBJETO NAO É SOMENTE DE REFERENCIA

	}

	private static Elite loadFromBanco(String where) {
		Elite w = db.loadElite(where);
		if (w != null) {
			for (ElitePlayer pl : db.loadElitePlayer("eliteid=" + w.id)) {
				w.getMembros().add(pl.uid);
				if (!eliteplayers.containsKey(pl.uid)) {

					eliteplayers.put(pl.uid, pl);
				}
			}
		}
		return w;

	}

	public static ElitePlayer getElitePlayer(UUID uid) {
		if (eliteplayers.containsKey(uid)) {
			return eliteplayers.get(uid);
		}
		List<ElitePlayer> elis = db.loadElitePlayer("uuid='" + uid.toString() + "'");
		if (elis.isEmpty()) {
			ElitePlayer eli = new ElitePlayer();
			eli.uid = uid;
			eliteplayers.put(uid, eli);
			return eli;
		}
		ElitePlayer eli = elis.get(0);
		eliteplayers.put(uid, eli);

		return eli;

	}

	public static List<Elite> getCached() {
		return new ArrayList(elites.values());
	}

}
