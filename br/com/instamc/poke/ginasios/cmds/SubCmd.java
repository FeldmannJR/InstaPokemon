/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.ginasios.cmds;

import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public abstract class SubCmd {

    String sub;

    public SubCmd(String sub) {
        this.sub = sub;
    }

    public boolean isStaff() {
        return true;
    }

    public abstract void executa(Player p, String[] args);
}
