/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop.menus;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public abstract class MenuFazAlgoPoke extends Menu {

    public MenuFazAlgoPoke(Player p, String oq) {
        super(oq, 1);
        NBTTagCompound[] pokes = PixelmonUtils.getParty(p);
        int slot = 1;
        for (int x = 0; x < pokes.length; x++) {
            final int pokeslot = x;
            if (pokes[x] != null) {
                PixelmonData data = new PixelmonData(pokes[x]);

                if (!data.isEgg) {

                    addButton(new MenuButton(new SlotPos(slot, 0), buildPokemon(data)) {

                        @Override
                        public void click(Player player, Menu menu, ClickType ct) {
                            faz(player, pokeslot, this);

                        }
                    });
                } else {
                    addButton(new NothingButton(new SlotPos(slot, 0), PixelmonUtils.getEgg()));
                }
            } else {
                addButton(new NothingButton(new SlotPos(slot, 0), ItemStackBuilder.of(ItemTypes.BARRIER).withName("§cNenhum Pokemon").build()));
            }
            slot++;
            if (x == 2) {
                slot++;
            }
        }

    }

    public abstract boolean faz(Player p, int slot, MenuButton mb);

    public abstract void processaItemStack(ItemStack i, PixelmonData data);

    public ItemStack buildPokemon(PixelmonData data) {
        ItemStack it = PixelmonUtils.getPixelmonIcon(data.getSpecies(), data.isShiny);
        String nome = data.getSpecies().name;
        if (data.nickname != null && !data.nickname.isEmpty()) {
            nome = data.nickname;
        }
        ItemUtils.setItemName(it, Txt.f("§e" + nome + " §fLevel " + data.lvl));
        if (data.isShiny) {
            ItemUtils.addLore(it, Txt.f("§6§lSHINY"));
        }
        processaItemStack(it, data);
        return it;

    }

}
