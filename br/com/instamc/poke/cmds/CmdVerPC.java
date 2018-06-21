package br.com.instamc.poke.cmds;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.ComputerBox;
import com.pixelmonmod.pixelmon.storage.PlayerComputerStorage;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.nbt.NBTTagCompound;

public class CmdVerPC extends ComandoAPI {

	public CmdVerPC() {
		super(CommandType.CONSOLEPERM, "verpc");
		// TODO Auto-generated constructor stub
		this.permission = "instamc.cmd.verpc";
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		if (args.length != 2) {
			cs.sendMessage(Txt.f("§aUse §f/verpc <player> <box>"));
			return;
		}
		int x = -1;
		try {

			x = Integer.valueOf(args[1]);
			if (x < 0 || x >= PlayerComputerStorage.boxCount) {
				x = -1;
			}
		} catch (NumberFormatException ex) {

		}
		if (x == -1) {
			cs.sendMessage(Txt.f("§cBox inválida 0-" + (PlayerComputerStorage.boxCount - 1)));
			return;
		}

		Player p = LibAPI.getPlayer(args[0]);

		if (p != null) {

			ComputerBox box = PixelmonUtils.getBox(p, x);
			List<Text> pokes = new ArrayList();
			for (NBTTagCompound nbt : box.getStoredPokemon()) {
				if (nbt != null) {
					pokes.add(CmdVerParty.getText(new PixelmonData(nbt)));
				}
			}
			Builder guia = Txt.f("§b§l<<<").toBuilder().onClick(TextActions.runCommand("/verpc " + p.getName() + " " + (x - 1)));
			guia.append(Txt.f("   §f" + p.getName() + " §e[" + x + "]   "));
			guia.append(Txt.f("§b§l>>>").toBuilder().onClick(TextActions.runCommand("/verpc " + p.getName() + " " + (x + 1))).build());
			cs.sendMessage(Txt.f("§f§l§m================="));
			Text t = Txt.f("");
			for (Text po : pokes) {
				t = t.toBuilder().append(po).append(Txt.f("§f | ")).build();
			}
			cs.sendMessage(t);
			cs.sendMessage(guia.build());

		} else {
			cs.sendMessage(Txt.f("§cPlayer offline!"));

		}
	}

}
