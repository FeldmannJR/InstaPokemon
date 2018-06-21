/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds;

import br.com.instamc.poke.ginasios.cmds.subs.staff.SetarLider;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Tira;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Abrir;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Criar;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Deletar;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Fechar;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Ir;
import br.com.instamc.poke.ginasios.cmds.subs.staff.RemoverLider;
import br.com.instamc.poke.ginasios.cmds.subs.staff.Set;
import br.com.instamc.poke.ginasios.cmds.subs.staff.SetarInsgnia;
import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.ginasios.cmds.subs.*;
import br.com.instamc.poke.ginasios.cmds.subs.lider.AbrirLider;
import br.com.instamc.poke.ginasios.cmds.subs.lider.FechaLider;
import br.com.instamc.poke.ginasios.cmds.subs.lider.Kick;
import br.com.instamc.poke.ginasios.cmds.subs.lider.Tp;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import java.util.HashSet;

import org.h2.command.dml.Delete;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class CmdGinasio extends ComandoAPI {

	HashSet<SubCmd> subs = new HashSet();

	public CmdGinasio() {
		super(CommandType.PLAYER, "ginasios");
		subs.add(new Criar());
		subs.add(new Ver());
		subs.add(new SetarInsgnia());
		subs.add(new SetarLider());
		subs.add(new Ir());
		subs.add(new RemoverLider());
		subs.add(new Fechar());
		subs.add(new Abrir());
		subs.add(new Deletar());
		subs.add(new Tp());
		subs.add(new AbrirLider());
		subs.add(new FechaLider());
		subs.add(new Tira());
		subs.add(new Kick());
		subs.add(new Set());
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {

		Player p = (Player) cs;
		if (args.length == 0) {
			sendHelp(p);
			return;
		}
		String[] newargs = new String[args.length - 1];
		for (int x = 1; x < args.length; x++) {
			newargs[x - 1] = args[x];
		}
		for (SubCmd sub : subs) {
			if (sub.sub.equalsIgnoreCase(args[0])) {
				if (!sub.isStaff() || (sub.isStaff() && p.hasPermission("instamc.poke.ginasiosstaff"))) {
					sub.executa(p, newargs);
				}
				return;
			}
		}
		sendHelp(p);
	}

	public void sendHelp(Player p) {
		p.sendMessage(Txt.f("§c§l[!!!] §eUse §a/ginasios ver §epara ver os ginasios!"));

		if (p.hasPermission("instamc.poke.ginasiosstaff")) {
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios criar NomeGinasio §e- Cria um ginasio sem insignia!"));
			p.sendMessage(
					Txt.f("§6§l[!!!] §a/ginasios setarlider NomeGinasio NomeJogador §e- Seta um lider de ginasio!"));
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios removelider NomeGinasio §e- Remove ginasio!"));
			p.sendMessage(Txt.f(
					"§6§l[!!!] §a/ginasios setarinsignia NomeGinasio NomeInsignia §e- Seta a insignia de um ginasio!!"));
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios abrir NomeGinasio  §e- Abre um ginasio!!"));
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios fechar NomeGinasio  §e- Fecha um ginasio!!"));
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios ir NomeGinasio  §e-Teleporta em um ginasio!!"));

			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios deletar NomeGinasio  §e- Exclui um ginasio!!"));
			p.sendMessage(Txt.f("§6§l[!!!] §a/ginasios tira Ginasio  §e- Remove um player que está em um ginásio!!"));
		}
		if (GinasioManager.getGinasioByLider(p.getUniqueId()) != null) {

			p.sendMessage(Txt.f("§b§l[!!!] §a/ginasios abre  §e- Abre seu ginasio!!"));
			p.sendMessage(Txt.f("§b§l[!!!] §a/ginasios fecha  §e- Fecha seu ginasio!!"));
			p.sendMessage(Txt.f("§b§l[!!!] §a/ginasios tp  §e- Teleporta para seu ginásio!!"));
			p.sendMessage(Txt.f("§b§l[!!!] §a/ginasios kick  §e- Kicka quem está em seu ginásio!!"));


		}
	}
}
