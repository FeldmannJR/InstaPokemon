package br.com.instamc.poke.blockcrafting;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.sponge.library.apis.ComandoAPI;

public class CmdBlockCrafting extends ComandoAPI {

	protected CmdBlockCrafting() {
		super(CommandType.OP,"blockcrafting");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] arg1) {

		new BlockCraftingMenu(1).open((Player) arg0);

	}

}
