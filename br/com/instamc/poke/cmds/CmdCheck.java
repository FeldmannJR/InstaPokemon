package br.com.instamc.poke.cmds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import org.spongepowered.api.command.CommandSource;

import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.ServerType;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.gemas.GemasAPI;
import br.com.instamc.sponge.library.gemas.LogManager;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdCheck extends ComandoAPI {

	public CmdCheck() {
		super(CommandType.CONSOLE, "check");
		// TODO Auto-generated constructor stub
	}

	boolean exec = false;

	@Override
	public void onCommand(CommandSource cs, String[] arg1) {
		/*
		 * if (exec) { return; } exec = true; HashMap<UUID, Integer> gastou =
		 * new HashMap(); HashMap<UUID, Integer> comprou = new HashMap();
		 * List<Integer> setre = new ArrayList(); try { ResultSet rs =
		 * DB.getConnection("new_gems").createStatement().
		 * executeQuery("SELECT * FROM logsnew WHERE server ='POKE' and moeda ='GEMA' and reembolsado = 0 "
		 * ); while (rs.next()) { int tem = 0; UUID uid =
		 * UUID.fromString(rs.getString("uuid")); if (gastou.containsKey(uid)) {
		 * tem = gastou.get(uid); } tem += rs.getInt("preco"); gastou.put(uid,
		 * tem); setre.add(rs.getInt("id"));
		 * 
		 * }
		 * 
		 * rs = DB.getConnection("instamc_central").createStatement().
		 * executeQuery("SELECT * FROM codigos"); while (rs.next()) { if
		 * (rs.getString("uuid") != null && !rs.getString("uuid").isEmpty()) {
		 * try { int quanto =
		 * Integer.valueOf(rs.getString("tipo").split("_")[1]); if (quanto < 1)
		 * { continue; } Timestamp gerado = rs.getTimestamp("usado"); if (gerado
		 * != null) { SimpleDateFormat dateFormat = new
		 * SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); try { Date d2 =
		 * dateFormat.parse("2015-07-31 23:59:59"); Date d1 =
		 * dateFormat.parse("2015-07-01 00:00:00"); if (gerado.compareTo(d1) >=
		 * 0 && gerado.compareTo(d2) <= 0) { quanto *= 2; } } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * } UUID uid = UUID.fromString(rs.getString("uuid"));
		 * 
		 * int tem = quanto; if (comprou.containsKey(uid)) { tem +=
		 * comprou.get(uid); } comprou.put(uid, tem); } catch
		 * (IllegalArgumentException ex) {
		 * 
		 * } } } String deficit = ""; List<UUID> g = new ArrayList();
		 * g.addAll(gastou.keySet()); Collections.sort(g, new Comparator<UUID>()
		 * {
		 * 
		 * @Override public int compare(UUID o1, UUID o2) { return
		 * gastou.get(o2) - gastou.get(o1);
		 * 
		 * } }); HashMap<UUID, Integer> naodevolvi = new HashMap(); for (UUID
		 * uuid : g) { int tem = 0; int gas = gastou.get(uuid); if
		 * (comprou.containsKey(uuid)) { tem = comprou.get(uuid); } String nome
		 * = uuid.toString(); if (UUIDUtils.getName(uuid) != null) { nome =
		 * UUIDUtils.getName(uuid); } int devolve = gas; if (devolve > tem) {
		 * naodevolvi.put(uuid, devolve - tem); devolve = tem; }
		 * 
		 * if (devolve > 0) { SpongeLib.getPlugin().doLog("Devolvi " + devolve +
		 * " para " + nome); LogManager.addLog(uuid, "Reembolso Poke", devolve,
		 * "GEMA", ServerType.POKE, true); GemasAPI.addGemas(uuid, devolve); } }
		 * 
		 * String d = ""; for (UUID uuid : naodevolvi.keySet()) { d +=
		 * uuid.toString() + ";" + naodevolvi.get(uuid) + ",";
		 * 
		 * }
		 * 
		 * SpongeLib.getPlugin().doLog(d);
		 * 
		 * } catch (
		 * 
		 * SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

}
