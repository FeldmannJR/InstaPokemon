package br.com.instamc.poke.kits;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.poke.kits.cmds.CmdKit;
import br.com.instamc.poke.kits.cmds.CmdKitAdm;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class KitManager {

	private static List<Kit> kits = new ArrayList();

	public static void init() {
		KitDB.init();
		kits.addAll(KitDB.loadKits());
		ComandoAPI.enable(new CmdKitAdm());
		ComandoAPI.enable(new CmdKit());
	}

	public static List<Kit> getKits() {
		return kits;
	}

	public static Kit getKitByName(String nome) {
		for (Kit k : getKits()) {
			if (k.getNome().equalsIgnoreCase(nome)) {
				return k;
			}
		}
		return null;
	}

	public static void salvaKit(Kit k) {
		KitDB.saveKit(k);
		for (Kit kt : new ArrayList<Kit>(getKits())) {
			if (kt.getNome().equalsIgnoreCase(k.getNome())) {
				kits.remove(kt);
			}

		}
		kits.add(k);

	}

	public static String timeToString(long time) {
		if (time < 0L) {
			return "forever";
		}
		if (time > 86400000L) {
			return time / 86400000L + " dia(s)";
		}
		if (time > 3600000L) {
			return time / 3600000L + " hora(s)";
		}
		if (time > 60000L) {
			return time / 60000L + " minuto(s)";
		}
		if (time > 1000L) {
			return time / 1000L + " segundo(s)";
		}
		return time + " milisegundo(s)";
	}

	public static String Minutos(int time) {

		if (time > 1440L) {
			return time / 1440L + " dia(s)";
		}
		if (time > 60L) {
			return time / 60L + " hora(s)";
		}
		return time + " minuto(s)";
	}

	public static void pegaKit(Kit k, Player p) {
		if (!k.hasPermission(p)) {
			p.sendMessage(Txt.f("§cVocê não tem permissão para pegar este kit!"));
			return;
		}
		Timestamp termina = KitDB.getUsado(p.getUniqueId(), k.getNome().toLowerCase());
		if (termina != null) {
			if (k.getMinutos() == 0) {
				p.sendMessage(Txt.f("§4[!!!] §eVocê já pegou o kit! Só pode pegar uma vez!."));
				return;
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(termina.getTime());
			c.add(Calendar.MINUTE, k.getMinutos());
			Date podeusar = c.getTime();

			if (podeusar.after(new Date(System.currentTimeMillis()))) {

				long dif = podeusar.getTime() - System.currentTimeMillis();
				p.sendMessage(Txt.f("§4[!!!] §eVocê ainda não pode usar o kit novamente."));
				p.sendMessage(Txt.f("§e[!!!] §6Aguarde §f" + timeToString(dif)));
				return;
			}
		}
		if (InventoryUtils.getEmpty(p) < k.getItens().size()) {
			p.sendMessage(Txt.f("§bVocê não tem espaço no inventário para pegar os itens!"));
			return;
		}

		KitDB.setUsado(p.getUniqueId(), k.getNome().toLowerCase(), new Timestamp(System.currentTimeMillis()));
		p.sendMessage(Txt.f("§a§l[!!!] §fVocê pegou o kit §6" + k.getNome().replaceAll("_", " ") + " §f!"));
		for (ItemStack ite : k.getItens()) {
			p.getInventory().offer(ite.copy());
		}

	}

	public static void deletaKit(Kit k) {
		KitDB.deletaKit(k);

		for (Kit kt : new ArrayList<Kit>(getKits())) {
			if (kt.getNome().equalsIgnoreCase(k.getNome())) {
				kits.remove(kt);
			}

		}
	}

	public static void openKits(Player player) {
		KitMenu m = new KitMenu(player);
		m.open(player);

	}

}
