/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds.subs.lider;

import br.com.instamc.poke.ginasios.Ginasio;
import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.ginasios.cmds.SubCmd;
import br.com.instamc.sponge.library.utils.Txt;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class FechaLider extends SubCmd {

	public FechaLider() {
		super("fecha");
	}

	@Override
	public void executa(Player p, String[] args) {

		Ginasio g = GinasioManager.getGinasioByLider(p.getUniqueId());
		if (g == null) {
			p.sendMessage(Txt.f("§4§l!!! §cVocê não é lider de um ginasio pokemon."));

			return;
		}
		if (!g.isAberto()) {
			p.sendMessage(Txt.f("§4§l!!! §cGinásio já está fechado."));

			return;
		}
		g.setAberto(false);	
		p.sendMessage(Txt.f("§eVocê fechou o ginasio!"));

	}

}
