/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds.subs.staff;

import br.com.instamc.poke.ginasios.Ginasio;
import br.com.instamc.poke.ginasios.GinasioDB;
import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.ginasios.cmds.SubCmd;
import br.com.instamc.sponge.library.utils.Txt;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import java.sql.Timestamp;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class Criar extends SubCmd {

    public Criar() {
        super("criar");
    }

    @Override
    public void executa(Player p, String[] args) {
        if (args.length != 1) {
            p.sendMessage(Txt.f("§c!!! §eUse §a/ginasios criar NomeGinasio"));
            return;
        }
        String nome = args[0];
        if (GinasioManager.getGinasioByName(nome) != null) {
            p.sendMessage(Txt.f("§c§l!!! §eJá existe ginásio com este nome."));
            return;
        }
        Ginasio g = new Ginasio(null, EnumBadges.CobbleBadge, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), nome, p.getLocation().copy());
        GinasioManager.saveGinasio(g);
        p.sendMessage(Txt.f("§6§l!!! §aGinasio criado. Não esqueça de setar a insignia!"));

    }

}
