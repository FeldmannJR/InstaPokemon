/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.insignias;

import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import java.util.UUID;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

/**
 *
 * @author Carlos
 */
public class InsigniaMenu extends Menu {

    public InsigniaMenu(UUID uid) {
        super("Insignias", 6);
        for (EnumBadges ba : InsigniaDB.getInsignias(uid)) {
            String nome = ba.name().replace("Badge", "");
            ItemStack i = PixelmonUtils.getBadge(ba);
            ItemUtils.setItemName(i, Txt.f("§e§l" + nome));
            addButtonNextSlot(new NothingButton(new SlotPos(Vector2i.ZERO), i));
        }
    }

    
   
}
