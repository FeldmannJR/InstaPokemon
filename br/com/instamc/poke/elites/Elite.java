package br.com.instamc.poke.elites;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class Elite {

	int id = -1;
	public String nome;
	public ItemStack icone;
	public String tag;
	public Timestamp fundada;
	public UUID fundador;
	public Timestamp lasttrocatag;

	List<Integer> aliados = new ArrayList<>();
	List<Integer> rivais = new ArrayList<>();

	// HOME
	public EliteHome home;

	public int getId() {
		return id;
	}

	public List<UUID> getMembros() {
		return membros;
	}

	public List<Integer> getAliados() {
		return aliados;
	}

	public List<Integer> getRivais() {
		return rivais;
	}

	public void sendMessageToLeader(Text t) {
		for (ElitePlayer e : getElitePlayers()) {
			if (e.getCargo() < ElitePlayer.LIDER) {
				continue;
			}
			Optional<org.spongepowered.api.entity.living.player.Player> pOn = Sponge.getServer().getPlayer(e.uid);
			if (pOn.isPresent()) {
				if (pOn.get().isOnline()) {
					pOn.get().sendMessage(t);
				}
			}

		}
	}

	public List<Player> getOnlinePlayers() {
		List<Player> pl = new ArrayList();
		for (UUID uid : getMembros()) {
			Optional<org.spongepowered.api.entity.living.player.Player> pOn = Sponge.getServer().getPlayer(uid);
			if (pOn.isPresent()) {
				pl.add(pOn.get());
			}
		}
		return pl;
	}

	public boolean hasLeaderOnline() {
		for (ElitePlayer e : getElitePlayers()) {
			if (e.getCargo() < ElitePlayer.LIDER) {
				continue;
			}
			Optional<org.spongepowered.api.entity.living.player.Player> pOn = Sponge.getServer().getPlayer(e.uid);
			if (pOn.isPresent()) {
				if (pOn.get().isOnline()) {
					return true;
				}
			}

		}
		return false;

	}

	public List<ElitePlayer> getElitePlayers() {
		List<ElitePlayer> pl = new ArrayList<>();
		for (UUID uid : getMembros()) {
			pl.add(EliteManager.getElitePlayer(uid));
		}
		return pl;
	}

	List<UUID> membros = new ArrayList();

	public String getNome() {
		return nome;
	}

	public ItemStack getIcone() {
		if (icone != null)
			return icone;
		return PixelmonUtils.getPokeball(EnumPokeballs.PokeBall);
	}

	public void sendChatMessage(ElitePlayer p, Player pl, String msg) {

		for (UUID uid : getMembros()) {
			Optional<org.spongepowered.api.entity.living.player.Player> pOn = Sponge.getServer().getPlayer(uid);
			if (pOn.isPresent()) {
				String format = "";
				if (p.cargo == ElitePlayer.FUNDADOR) {
					format = "§l";
				}
				if (p.cargo == ElitePlayer.LIDER) {
					format = "§n";
				}

				String titulo = p.titulo;
				if (!titulo.isEmpty()) {
					titulo = titulo + "§7.";
				}
				pOn.get().sendMessage(Txt.f("§d§l[E] §a" + titulo + "§f" + format + pl.getName() + "§8: §7" + msg));
			}

		}
	}

	public void sendMessage(String msg) {
		for (UUID uid : getMembros()) {
			Optional<org.spongepowered.api.entity.living.player.Player> pOn = Sponge.getServer().getPlayer(uid);
			if (pOn.isPresent()) {
				pOn.get().sendMessage(Txt.f("§d§l[Elite] §f" + msg));
			}

		}

	}

	public String getAliadosString() {
		String oq = "";
		for (int x = 0; x < aliados.size(); x++) {
			if (x != 0) {
				oq += ",";
			}
			oq += aliados.get(x);
		}
		return oq;
	}

	public String getRivaisString() {
		String oq = "";
		for (int x = 0; x < rivais.size(); x++) {
			if (x != 0) {
				oq += ",";
			}
			oq += rivais.get(x);
		}
		return oq;
	}

	public String getTagDB() {
		return Txt.f(tag).toPlain().toLowerCase().trim();
	}

	public ItemStack buildIcone(boolean fundador) {
		ItemStack it = getIcone().copy();
		ItemUtils.setItemName(it, Txt.f("§b§l" + nome));
		String fundado = "Desconhecido";
		if (fundada != null) {
			SimpleDateFormat si = new SimpleDateFormat("dd/MM/yyyy");
			fundado = si.format(fundada);
		}

		ItemUtils.addLore(it, Txt.f("§eTAG: §f" + tag));
		ItemUtils.addLore(it, Txt.f("§eFundada em: §f" + fundado));
		ItemUtils.addLore(it, Txt.f("§eMembros§f: " + getMembros().size()));
		if (fundador) {
			for (ElitePlayer pl : getElitePlayers()) {
				if (pl.getCargo() == ElitePlayer.FUNDADOR) {
					if (UUIDUtils.getName(pl.uid) != null) {
						ItemUtils.addLore(it, Txt.f("§eFundador: §f" + UUIDUtils.getName(pl.uid)));
						break;
					}
				}
			}
		}
		return it;
	}

}
