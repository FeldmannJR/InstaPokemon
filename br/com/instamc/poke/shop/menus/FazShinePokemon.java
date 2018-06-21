/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.shop.menus;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.ConfirmarMenu;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.SpongeVIP;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class FazShinePokemon extends MenuFazAlgoPoke {

    public FazShinePokemon(Player p) {
        super(p, "Shiny");
    }

    @Override
    public boolean faz(Player p, final int slot, MenuButton mb) {
        NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
        if (nbt == null) {
            mb.close(p);
            InstaPokemon.sendMessage(p, "§cPokemon inválido!");
            return false;
        }
        PixelmonData data = new PixelmonData(nbt);
        if (data.isEgg) {
            mb.close(p);
            InstaPokemon.sendMessage(p, "§cPokemon inválido!");
            return false;
        }
        if (data.isShiny) {
            new ConfirmarMenu(PixelmonUtils.getPixelmonIcon(data.getSpecies(), true), "Remove Shiny", "Você tem certeza que deseja", "remover o shiny do pokemon?") {

                @Override
                public void confirma(Player player) {
                    if (can(player, slot)) {
                        fazShine(player, slot, false);
                        InstaPokemon.sendMessage(player, "§eVocê removeu o shiny do pokemon!");
                    }
                }

                @Override
                public void recusa(Player player) {

                }
            }.open(p);
        } else {
            new VendaMenu(SpongeVIP.moedashop, ItemStack.of(ItemTypes.NETHER_STAR, 1), "Faz Shiny", ShopManager.precoFazShine, "Transforma o pokemon em shiny!") {

                @Override
                public void cancela(Player player) {
                }

                @Override
                public boolean compra(Player player) {
                    if (!can(player, slot)) {
                        InstaPokemon.sendMessage(player, "§cPokemon inválido!");
                        return false;
                    }
                    fazShine(player, slot, true);
                    InstaPokemon.sendMessage(player, "§aPokemon agora é shiny!!");
                    SpongeVIP.sendMessageComprou(player, "Faz Shine",
							ShopManager.precoFazShine);
                    return true;
                }
            }.open(p);

        }

        return true;
    }

    public boolean can(Player p, int slot) {
        NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
        if (nbt == null) {
            return false;
        }
        PixelmonData data = new PixelmonData(nbt);
        if (data.isEgg) {
            return false;
        }
        return true;
    }

 

    @Override
    public void processaItemStack(ItemStack i, PixelmonData data) {
        if (data.isShiny) {
            ItemUtils.addLore(i, Txt.f("§dClique para remover o shiny"));
            ItemUtils.addLore(i, Txt.f("§dsem nenhum custo!"));
        } else {
            ItemUtils.addLore(i, Txt.f("§bClique para transformar em shiny"));
            ItemUtils.addLore(i, ShopManager.buildPrecoPor(ShopManager.precoFazShine));
        }
    }

    public static boolean fazShine(Player p, int slot, boolean b) {
        NBTTagCompound nbt = PixelmonUtils.getParty(p)[slot];
        PlayerStorage sto = PixelmonUtils.getPlayerStorage(p);
        sto.recallAllPokemon();
        EntityPixelmon po = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt, (World) p.getWorld());
        
       
        po.setIsShiny(b);

        sto.replace(po, po);
        sto.sendUpdatedList();
      
        return true;
    }
}
