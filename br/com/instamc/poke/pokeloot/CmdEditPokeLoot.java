package br.com.instamc.poke.pokeloot;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuCloseAction;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdEditPokeLoot extends ComandoAPI {

	protected CmdEditPokeLoot() {
		super(CommandType.OP, "editpokeloot");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] arg1) {
		Player p = (Player) arg0;
		Menu m = new Menu("Edit PokeLoot", 6);
		for(ItemStack it : PokeLootDB.podeVir()){
			m.addNonButton(it);
		}
		m.setMoveItens(true);
		m.addClose(new MenuCloseAction() {
			
			@Override
			public void closeMenu(Player arg0, Menu arg1) {
				if(arg0.hasPermission("instamc.op")){
					PokeLootDB.set(arg1.getNonButtons());
					arg0.sendMessage(Txt.f("§a§lItens Setados!"));
				
				}
				
			}
		});
		m.open(p);
	}

}
