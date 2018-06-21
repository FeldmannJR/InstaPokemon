/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.camera;

import br.com.instamc.poke.camera.Foto.Forma;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.menus.MenuElites;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import com.flowpowered.math.vector.Vector2i;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

/**
 *
 * @author Carlos
 */
public class AlbumMenu extends Menu {

	public AlbumMenu(UUID uid, int page) {
		super("Album " + CameraDB.getFotos(uid).qtd + " de " + fotos.size(), 6);

		for (Foto f : getPage(uid, page)) {
			ItemStack i = f.toItemStack(CameraDB.getFotos(uid).fotos.contains(f));
			addButtonNextSlot(new NothingButton(new SlotPos(Vector2i.ZERO), i));
		}
		if (page > 1) {
			addButton(new MenuButton(new SlotPos(0, 5), ItemStackBuilder.of(ItemTypes.REPEATER).withName("§e§lVoltar").withLore("§7Clique para voltar", "§7para a pagina " + (page - 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new AlbumMenu(uid, page - 1).open(arg0);
					;

				}
			});
		}
		if (page < getPages()) {
			addButton(new MenuButton(new SlotPos(8, 5), ItemStackBuilder.of(ItemTypes.COMPARATOR).withName("§e§lAvançar").withLore("§7Clique para avançar", "§7para a pagina " + (page + 1)).build()) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new AlbumMenu(uid, page + 1).open(arg0);
					;

				}
			});
		}

	}

	private static List<Foto> fotos = new ArrayList();

	public static void populate() {
		if (fotos.isEmpty()) {
			List<EnumPokemon> hasdefault = new ArrayList();
			for (Forma f : Forma.values()) {
				for (EnumPokemon poke : f.pokes) {
					if(f.hasdefault){
						hasdefault.add(poke);
					}
				}
			}
			for (EnumPokemon poke : EnumPokemon.values()) {
				
				if (!hasdefault.contains(poke)) {
					fotos.add(new Foto(poke, true, null));
					fotos.add(new Foto(poke, false, null));
				}
			}
			for (Forma f : Forma.values()) {
				for (EnumPokemon poke : f.pokes) {
					fotos.add(new Foto(poke, false, f));
					fotos.add(new Foto(poke, true, f));
					
				}
			}

			Collections.sort(fotos,new Comparator<Foto>() {

				@Override
				public int compare(Foto o1, Foto o2) {
					return o1.poke.name.compareTo(o2.poke.name);
					
				}
			});
		}
		

	}

	public static int getPages() {
		int size = fotos.size();

		return 1 + ((size - 1) / 45);
	}

	public static List<Foto> getPage(UUID uid, int page) {
		page = page - 1;
		ArrayList<Foto> infos = new ArrayList();
		ArrayList<Foto> doretorno = new ArrayList();
		infos.addAll(fotos);
		Collections.sort(infos, new Comparator<Foto>() {

			@Override
			public int compare(Foto o1, Foto o2) {
				boolean tem1 = CameraDB.getFotos(uid).fotos.contains(o1);
				boolean tem2 = CameraDB.getFotos(uid).fotos.contains(o2);
				if (tem1 && !tem2) {
					return -1;
				}
				if (tem2 && !tem1) {
					return 1;
				}
				return 0;

			}
		});
		int foi = 0;
		int start = page * 45;

		for (Foto info : infos) {
			if (foi >= (start + 45)) {
				break;
			}
			if (foi >= start) {
				doretorno.add(info);
			}
			foi++;
		}
		return doretorno;

	}

}
