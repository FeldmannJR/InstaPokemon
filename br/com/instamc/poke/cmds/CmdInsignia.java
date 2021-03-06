/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.cmds;

import br.com.instamc.poke.insignias.InsigniaMenu;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;
import java.util.UUID;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class CmdInsignia extends ComandoAPI {

    public CmdInsignia() {
        super(CommandType.PLAYER, "insignias");

    }

    @Override
    public void onCommand(CommandSource cs, String[] strings) {
        Player p = (Player) cs;
        if (strings.length == 0) {
            p.sendMessage(Txt.f("§c§l[!!!] §eUse /insignias ver ou /insignias ver {nome}"));
        }
        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("ver")) {
                new InsigniaMenu(p.getUniqueId()).open(p);
            }
        }
        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("ver")) {
                String nome = strings[1];
                UUID uid = UUIDUtils.getUUID(nome);
                if (uid == null) {
                    p.sendMessage(Txt.f("§c§lJogador não encontrado!"));
                    return;
                }
                new InsigniaMenu(uid).open(p);

            }
        }

    }

}
