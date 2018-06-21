package br.com.instamc.poke.kits.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import br.com.instamc.poke.kits.Kit;
import br.com.instamc.poke.kits.KitManager;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuCloseAction;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdKit extends ComandoAPI {

	public CmdKit() {
		super(CommandType.PLAYER, "kit");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;

		KitManager.openKits(p);
	}

}
