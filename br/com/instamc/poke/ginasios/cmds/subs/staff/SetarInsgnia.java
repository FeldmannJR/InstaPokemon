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
public class SetarInsgnia extends SubCmd {

    public SetarInsgnia() {
        super("setarinsignia");
    }

    @Override
    public void executa(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage(Txt.f("§c!!! §eUse §a/ginasios setarinsignia NomeGinasio NomeInsignia"));
            return;
        }
        String nome = args[0];
        if (GinasioManager.getGinasioByName(nome) == null) {
            p.sendMessage(Txt.f("§4§l!!! §cNão existe ginásio com este nome."));
            return;
        }
        Ginasio g = GinasioManager.getGinasioByName(nome);
        String badge = args[1];
        for (EnumBadges forbadge : EnumBadges.values()) {
            if (forbadge.name().replace("Badge", "").equalsIgnoreCase(badge)) {
                g.setBadge(forbadge);
                p.sendMessage(Txt.f("§aInsignia setada para " + forbadge.name() + "§e!"));

                GinasioManager.saveGinasio(g);
                return;
            }
        }
        p.sendMessage(Txt.f("§4§l!!! §cInsignia não encontrada! §7Nome em ingles!"));

    }

}
