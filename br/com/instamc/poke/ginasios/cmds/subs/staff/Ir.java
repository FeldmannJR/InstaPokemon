/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds.subs.staff;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.ginasios.Ginasio;
import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.ginasios.cmds.SubCmd;
import br.com.instamc.sponge.library.utils.Txt;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class Ir extends SubCmd {

	public Ir() {
		super("ir");
	}

	@Override
	public void executa(Player p, String[] args) {
		if (args.length != 1) {
			p.sendMessage(Txt.f("§c!!! §eUse §a/ginasios ir NomeGinasio"));
			return;
		}
		String nome = args[0];
		if (GinasioManager.getGinasioByName(nome) == null) {
			p.sendMessage(Txt.f("§4§l!!! §cNão existe ginásio com este nome."));
			return;
		}
		Ginasio g = GinasioManager.getGinasioByName(nome);
		p.setLocation(g.getLocation().copy());
		InstaPokemon.sendMessage(p, "§eTeleportado para ginasio "+g.getNome()+"!");
		
	}

}
