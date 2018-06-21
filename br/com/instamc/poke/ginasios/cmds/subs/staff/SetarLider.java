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
import br.com.instamc.sponge.library.utils.UUIDUtils;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import java.sql.Timestamp;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class SetarLider extends SubCmd {

    public SetarLider() {
        super("setarlider");
    }

    @Override
    public void executa(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage(Txt.f("§c!!! §eUse §a/ginasios setarlider NomeGinasio NomeJogador"));
            return;
        }
        String nome = args[0];
        if (GinasioManager.getGinasioByName(nome) == null) {
            p.sendMessage(Txt.f("§4§l!!! §cNão existe ginásio com este nome."));
            return;
        }
        Ginasio g = GinasioManager.getGinasioByName(nome);
        String nomeplayer = args[1];
        UUID uid = UUIDUtils.getUUID(nomeplayer);
        if (uid == null) {
            p.sendMessage(Txt.f("§4§l!!! §cNão achei jogador com este nome."));
            return;
        }
        for (Ginasio gfor : GinasioManager.getGinasios()) {
            if (gfor.getDono() != null && gfor.getDono().toString().equals(uid.toString())) {
                p.sendMessage(Txt.f("§4§l!!! §cEste jogador já tem um ginasio!."));

                return;
            }
        }

        g.setDono(uid);
        GinasioManager.saveGinasio(g);
        p.sendMessage(Txt.f("§4§l!!! §aSetado lider do ginasio."));
    }

}
