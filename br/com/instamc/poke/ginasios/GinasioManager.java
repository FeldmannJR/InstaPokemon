/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ginasios.cmds.CmdGinasio;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.pixelmonmod.pixelmon.Pixelmon;

/**
 *
 * @author Carlos
 */
public class GinasioManager {

	public static List<Ginasio> ginasios = new ArrayList();

	public static List<Ginasio> getGinasios() {
		return ginasios;
	}

	private static GinasioMenu menu;

	public static void init() {
		GinasioDB.init();
		ginasios.addAll(GinasioDB.loadGinasios());

		menu = new GinasioMenu();
		menu.updateButtons();
		
		GinasioListeners l = new GinasioListeners();
		Sponge.getEventManager().registerListeners(InstaPokemon.instancia,l);
		Pixelmon.EVENT_BUS.register(l);
	
		ComandoAPI.enable(new CmdGinasio());
	}

	public static void openMenu(Player p) {
		menu.open(p);
	}

	public static Ginasio getGinasioByName(String name) {
		for (Ginasio g : getGinasios()) {
			if (g.getNome().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}

	public static Ginasio getGinasioByLider(UUID uid) {
		for (Ginasio g : getGinasios()) {
			if (g.getDono() != null) {
				if (g.getDono().toString().equalsIgnoreCase(uid.toString())) {
					return g;
				}
			}
		}
		return null;
	}

	public static void entra(Player p, Ginasio g) {
		if (!g.isAberto()) {
			p.sendMessage(Txt.f("§cO ginásio está fechado!"));
		}
		if (g.getOcupado() != null) {
			p.sendMessage(Txt.f("§cGinásio está ocupado!"));
			return;
		}
		if (g.getDono() == null) {
			p.sendMessage(Txt.f("§cGinásio sem dono!"));
			return;
		}
		if (g.getDono().toString().equals(p.getUniqueId().toString())) {
			p.sendMessage(Txt.f("§cNão entre em seu próprio ginasio! Use §a/ginasios tp §cpara ir para seu ginasio!"));
			return;
		}
		g.setOcupado(p);
		p.setLocation(g.getLocation().copy());
		InstaPokemon.sendMessage(p, "§eVocê entrou no ginásio " + g.getNome() + "!");
		Optional<Player> dono = Sponge.getServer().getPlayer(g.getDono());
		if(dono.isPresent()){
			InstaPokemon.sendMessage(dono.get(), "§c"+p.getName()+"§e entrou no seu ginásio!");
		}
		updateMenu();
	}

	public static Location<World> getExit() {
		return Sponge.getServer().getWorlds().iterator().next().getSpawnLocation();

	}

	public static void updateMenu() {
		menu.updateButtons();
	}

	public static void saveGinasio(Ginasio g) {
		for (Ginasio ga : new HashSet<Ginasio>(ginasios)) {
			if (ga.getNome().equalsIgnoreCase(g.getNome())) {
				ginasios.remove(ga);
			}
		}
		ginasios.add(g);
		GinasioDB.saveGinasion(g);
		updateMenu();
	}

	public static void delete(Ginasio g) {
		for (Ginasio ga : new HashSet<Ginasio>(ginasios)) {
			if (ga.getNome().equalsIgnoreCase(g.getNome())) {
				ginasios.remove(ga);
				GinasioDB.deletaGinasio(ga.getNome());
				updateMenu();
			}
		}

	}
}
