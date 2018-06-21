package br.com.instamc.poke.tpscroll.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.list.TpScrollItem;
import br.com.instamc.poke.tpscroll.menu.ScrollMenu;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.ComandoAPI.CommandType;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdCriarScroll extends ComandoAPI {

	public CmdCriarScroll() {
		super(CommandType.OP, "criarscroll");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		if (args.length < 3) {
			InstaPokemon.sendMessage(p, "§aUse §f/criarscroll minutos regiao NOME §a!");
			return;
		}
		if (InventoryUtils.getEmpty(p) < 1) {
			p.sendMessage(Txt.f("§cInventário lotado"));
			return;
		}
		try {
			int minutos = Integer.valueOf(args[0]);
			String regiao = args[1];
			String nome = "";
			for (int x = 2; x < args.length; x++) {
				if (!nome.isEmpty()) {
					nome += " ";
				}
				nome += args[x];
			}
			p.getInventory().offer(TpScrollItem.generate(nome, regiao, p.getLocation().copy(), minutos));
			return;
		} catch (NumberFormatException ex) {

		}
		InstaPokemon.sendMessage(p, "§aUse §f/criarscroll minutos regiao NOME §a!");

		// criarscroll minutos regiao NOME
	}
}
