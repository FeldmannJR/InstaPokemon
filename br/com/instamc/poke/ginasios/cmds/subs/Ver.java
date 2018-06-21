/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds.subs;

import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.ginasios.GinasioMenu;
import br.com.instamc.poke.ginasios.cmds.SubCmd;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public class Ver extends SubCmd {
    
    public Ver() {
        super("ver");
    }
    
    @Override
    public boolean isStaff() {
        return false;
        
    }
    
    @Override
    public void executa(Player p, String[] args) {
        GinasioManager.openMenu(p);
    }
    
}
