package br.com.instamc.poke.sorteios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.placas.BlockClickSignEvent;
import br.com.instamc.poke.shop.chest.ShopMenu;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class SorteioManager {

	public static int preco = 20;

	public static List<Sorteio> getSorteioAtivos() {
		List<Sorteio> sorteios = new ArrayList<>();
		for (Sorteio s : SorteioDB.getSorteios()) {
			if (s.termina != null) {
				if (s.termina.after(new Date(System.currentTimeMillis()))) {
					sorteios.add(s);

				}
			}

		}
		Collections.sort(sorteios, new Comparator<Sorteio>() {

			@Override
			public int compare(Sorteio o1, Sorteio o2) {
				if (o1.termina.before(o2.termina)) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		return sorteios;

	}

	public static Sorteio getLast() {
		List<Sorteio> sorteios = new ArrayList<>();
		for (Sorteio s : SorteioDB.getSorteios()) {
			if (s.termina != null) {
				if (s.termina.before(new Date(System.currentTimeMillis()))) {
					sorteios.add(s);
				}
			}
		}
		Collections.sort(sorteios, new Comparator<Sorteio>() {

			@Override
			public int compare(Sorteio o1, Sorteio o2) {
				if (o1.termina.before(o2.termina)) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		if (sorteios.isEmpty()) {
			return null;
		}
		return sorteios.get(0);

	}

	public static Sorteio getNext() {
		List<Sorteio> ativos = getSorteioAtivos();
		if (ativos.isEmpty()) {
			return null;
		}
		return ativos.get(0);
	}

	public static Sorteio getSorteioById(int id) {
		for (Sorteio s : SorteioDB.getSorteios()) {
			if (s.id == id) {
				return s;
			}
		}
		return null;
	}

	public static void daPremio(Sorteio s, Player p) {
		if (InventoryUtils.getEmpty(p) < s.itens.size()) {
			sendMessage(p, "§eVocê não tem espaço no inventário para pegar o premio!");
			return;
		}

		s.premio = true;
		SorteioDB.salva(s);
		for (ItemStack it : s.itens) {
			p.getInventory().offer(it.copy());
		}
		broadCast("§eO jogador §6§l" + p.getName() + " §e pegou o premio da loteria " + s.id + " !");
		sendMessage(p, "§eVocê pegou o premio do sorteio!");
		clearItemInHand(p);

	}

	public static void clearItemInHand(Player p) {
		p.setItemInHand(HandTypes.MAIN_HAND, null);
	}

	@Listener
	public void clickSign(BlockClickSignEvent ev) {
		if (ev.getLinhas().get(0).equalsIgnoreCase("loteria")) {
			Bilhete b = null;
			if (ev.getTargetEntity().getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
				b = extractBilhete(ev.getTargetEntity().getItemInHand(HandTypes.MAIN_HAND).get());
			}

			if (b != null) {
				Sorteio so = getSorteioById(b.concurso);
				if (so != null) {
					if (so.termina != null && so.termina.before(new Date(System.currentTimeMillis()))) {
						if (so.sorteado != -1) {
							if (so.sorteado == b.bilhete) {
								if (so.premio) {

									sendMessage(ev.getTargetEntity(), "§eO premio deste concurso já foi retirado!");
									clearItemInHand(ev.getTargetEntity());
								} else {
									Calendar c = Calendar.getInstance();
									c.setTime(new Date(System.currentTimeMillis()));
									c.add(Calendar.DAY_OF_MONTH, 14);
									if (c.getTime().before(new Date(System.currentTimeMillis()))) {
										sendMessage(ev.getTargetEntity(), "§eTempo de retirada de premio expirado!");
										clearItemInHand(ev.getTargetEntity());
									} else {
										daPremio(so, ev.getTargetEntity());
									}
								}
							} else {
								sendMessage(ev.getTargetEntity(), "§eBilhete não é o ganhador!");
								clearItemInHand(ev.getTargetEntity());
							}

						} else {
							sendMessage(ev.getTargetEntity(), "§eSorteio do concurso será realizado §a" + so.getTerminaFormatado() + "§e!");

						}
					} else {
						sendMessage(ev.getTargetEntity(), "§eSorteio do concurso será realizado §a" + so.getTerminaFormatado() + "§e!");

					}

				} else {
					clearItemInHand(ev.getTargetEntity());
					sendMessage(ev.getTargetEntity(), "§eBilhete inválido!");
				}
			} else {
				if (getNext() != null) {
					final Player p = ev.getTargetEntity();
					SchedulerUtils.runSync(new Runnable() {

						@Override
						public void run() {
							if (p != null) {
								new MenuVerSorteios(getSorteioAtivos()).open(p);
							}
						}
					}, 2);

				} else {
					sendMessage(ev.getTargetEntity(), "§eNenhum sorteio futuro!");
				}
			}
		}

	}

	public static void init() {
		SorteioDB.createTables();
		iniciaTask();
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia, new SorteioManager());
		ComandoAPI.enable(new CmdCriarSorteio());

	}

	public static void sendMessageNext() {
		Sorteio last = getLast();
		Sorteio next = getNext();
		if (last == null) {
			if (next != null) {
				broadCast("§eO próximo sorteio da loteria irá acontecer §f" + next.getTerminaFormatado() + "§e aproveite e compre um bilhete no spawn!");
			}
		} else {
			long dif = System.currentTimeMillis() - last.termina.getTime();

			if (next != null) {
				if (dif < 1000 * 60 * 60 * 24 * 2) {

					broadCast("§eO resultado do concurso §f" + next.id + "§e foi o número §b[" + last.sorteado + "]§e! §bPróximo sorteio irá acontecer §c" + next.getTerminaFormatado() + "!");
				} else {
					broadCast("§eO próximo sorteio da loteria irá acontecer §f" + next.getTerminaFormatado() + "§e aproveite e compre um bilhete no spawn!");

				}
			} else {
				if (dif < 1000 * 60 * 60 * 24 * 2) {
					broadCast("§eO resultado do concurso §f" + last.id + "§e foi o número §b[" + last.sorteado + "]§e, §dconfiram seus bilhetes!");
				}
			}
		}
	}

	private static int minutos = 0;

	public static void iniciaTask() {
		if(LibUtils.getNomeServer().equalsIgnoreCase("poketeste"))return;
		Task.builder().intervalTicks(20 * 60).execute(new Runnable() {

			@Override
			public void run() {
				for (Sorteio s : new ArrayList<Sorteio>(SorteioDB.getSorteios())) {
					if (s.sorteado == -1) {
						if (s.termina != null) {
							if (s.termina.before(new Date(System.currentTimeMillis()))) {
								sorteia(s);
							}
						}
					}
				}
				minutos += 1;
				if (minutos > 30) {
					minutos = 0;
					sendMessageNext();
				}

			}
		}).submit(InstaPokemon.instancia);
	}

	public static void broadCast(String s) {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			p.sendMessage(Txt.f("§6§l[Loteria] §f" + s));
		}
	}

	public static void sendMessage(Player p, String msg) {
		p.sendMessage(Txt.f("§6§l[Loteria] §f" + msg));
	}

	public static void sorteia(Sorteio s) {
		if (s.sorteado == -1 && !s.premio) {
			if (s.bilhetes > 0) {
				s.sorteado = new Random().nextInt(s.bilhetes) + 1;
			} else {
				s.sorteado = 0;
			}

			SorteioDB.salva(s);
			broadCast("§bFoi realizado o sorteio do concurso §6" + s.id + "§f, o número sorteado foi §c§l" + s.sorteado + "! §dConfiram seus bilhetes!");

		}
	}

	public static ItemStack makeBilhete(Sorteio s) {
		ItemStack it = ItemStack.of(ItemTypes.PAPER, 1);
		int bilhete = s.bilhetes + 1;
		s.bilhetes++;
		SorteioDB.salva(s);

		ItemUtils.setItemName(it, Txt.f("§e§lBilhete de Loteria"));
		ItemUtils.addLore(it, Txt.f("§eConcurso§7: §f" + s.id));
		ItemUtils.addLore(it, Txt.f("§eBilhete§7: §b" + bilhete));
		ItemUtils.addLore(it, Txt.f(""));

		ItemUtils.addLore(it, Txt.f("§dData do sorteio"));
		ItemUtils.addLore(it, Txt.f("§f" + s.getTerminaFormatado()));
		return it;

	}

	public static Bilhete extractBilhete(ItemStack it) {
		List<Text> lore = it.getOrElse(Keys.ITEM_LORE, new ArrayList<Text>());
		Bilhete b = new Bilhete();

		for (Text t : lore) {

			String plain = t.toPlain().toLowerCase();
			if (plain.startsWith("concurso:")) {
				try {
					int id = Integer.valueOf(plain.split(":")[1].trim());
					b.concurso = id;
				} catch (NumberFormatException ex) {

				}
			}
			if (plain.startsWith("bilhete:")) {
				try {
					int id = Integer.valueOf(plain.split(":")[1].trim());
					b.bilhete = id;
				} catch (NumberFormatException ex) {

				}
			}
		}
		if (b.bilhete != -1 && b.concurso != -1) {
			return b;
		}
		return null;

	}

	public static class Bilhete {
		int concurso = -1;
		int bilhete = -1;

	}

}
