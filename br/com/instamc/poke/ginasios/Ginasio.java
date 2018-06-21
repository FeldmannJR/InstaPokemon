/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.UUIDUtils;

/**
 *
 * @author Carlos
 */
public class Ginasio {

	UUID dono;
	EnumBadges badge;
	Location<World> loc;
	boolean aberto;
	Timestamp lastopen;
	Timestamp setado;
	String nome;

	Player ocupado;

	public Ginasio(UUID dono, EnumBadges badge, Timestamp lastopen, Timestamp setado, String nome, Location loc) {

		this.dono = dono;
		this.badge = badge;
		this.aberto = false;
		this.lastopen = lastopen;
		this.setado = setado;
		this.nome = nome;
		this.loc = loc;
	}

	public Player getOcupado() {
		return ocupado;
	}
	public void setLoc(Location<World> loc) {
		this.loc = loc;
	}

	public void kickOcupado(boolean tpemsg) {
		if (ocupado != null) {
			if(tpemsg){
			getOcupado().setLocation(GinasioManager.getExit());
			InstaPokemon.sendMessage(ocupado, "§eVocê foi expulso do ginásio!");
			}
			ocupado.setLocation(ocupado.getLocation().getExtent().getSpawnLocation().copy());
			
			setOcupado(null);
			GinasioManager.updateMenu();
		}

	}

	public Location<World> getLocation() {
		return loc;
	}

	public void setBadge(EnumBadges badge) {
		this.badge = badge;
	}

	public boolean isAberto() {
		return aberto;
	}

	public Timestamp getSetado() {
		return setado;
	}

	public void setOcupado(Player ocupado) {
		this.ocupado = ocupado;
	}

	public EnumBadges getBadge() {
		return badge;
	}

	public UUID getDono() {
		return dono;
	}

	public Timestamp getLastopen() {
		return lastopen;
	}

	public String getNome() {
		return nome;
	}

	public void setDono(UUID dono) {
		this.setado = new Timestamp(System.currentTimeMillis());
		this.dono = dono;
	}

	public String getNomeDono() {
		if (getDono() != null) {
			String nome = UUIDUtils.getName(getDono());
			if (nome != null) {
				return nome;
			}
		}
		return "Desconhecido";
	}

	public boolean isInRange(Player p) {
		if (p.getWorld() != loc.getExtent()) {
			return false;
		}
		if (p.getLocation().copy().getPosition().distance(p.getLocation().copy().getPosition()) < 20) {
			return true;
		}
		return false;
	}

	public void setAberto(boolean aberto) {
		this.aberto = aberto;
		if (aberto) {
			this.lastopen = new Timestamp(System.currentTimeMillis());
			InstaPokemon.broadcast("§fO ginasio §6" + getNome() + " §fde §a" + getNomeDono()
					+ " §festá aberto! §bUse '/ginasios ver' para entrar!");

		} else {

			InstaPokemon.broadcast("§cO ginasio §6" + getNome() + " §cde §a" + getNomeDono() + " §cfoi fechado!! ");

			if (getOcupado() != null) {
				kickOcupado(true);
			}
		}

		GinasioManager.updateMenu();
	}

}
