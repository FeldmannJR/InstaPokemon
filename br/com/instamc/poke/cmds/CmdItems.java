/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.cmds;

import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.insignias.InsigniaMenu;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;
import java.util.UUID;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class CmdItems extends ComandoAPI {

    public CmdItems() {
        super(CommandType.OP, "items");

    }

    @Override
    public void onCommand(CommandSource cs, String[] strings) {
        Player p = (Player) cs;
        if(p.hasPermission("instamc.op")){
        	Menu m = new Menu("Items", 6);
        	for(EnumCustomItems ci : EnumCustomItems.values()){
        		m.addNonButton(ci.getItem().toItemStack());
        	}
        	m.setMoveItens(true);
        	m.open(p);
        }
    }

}
