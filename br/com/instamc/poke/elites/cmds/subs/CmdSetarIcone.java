package br.com.instamc.poke.elites.cmds.subs;

import java.text.Normalizer.Form;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import br.com.instamc.poke.camera.CameraDB;
import br.com.instamc.poke.camera.Foto;
import br.com.instamc.poke.camera.Foto.Forma;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.UUIDUtils;

public class CmdSetarIcone extends CmdSubElite {

	public CmdSetarIcone() {
		super("setaricone");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean needToBeFounder() {
		return true;
	}

	@Override
	public String getArgs() {
		return "<Insignia|Pokemon> [shiny]";
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Seta um icone para a elite. Precisa ter a insignia ou a foto do pokemon";
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {
		if (args.length < 1) {
			showUsage(p);
			return;
		}
		String badgename = args[0];

		EnumBadges badge = null;

		for (EnumBadges b : EnumBadges.values()) {
			String nome = b.name().replace("Badge", "");
			if (nome.equalsIgnoreCase(badgename)) {
				badge = b;
			}
		}
		boolean shiny = false;
		if (args.length >= 2) {
			if (args[1].equalsIgnoreCase("shiny")) {
				shiny = true;
			}
		}
		Foto foto = null;
		for (EnumPokemon poke : EnumPokemon.values()) {
			if (poke.name.equalsIgnoreCase(args[0])) {
				foto = new Foto(poke, shiny,null);
			}
		}

		if (badge == null && foto == null) {
			p.sendMessage(Txt.f("§cInsignia ou pokemon não encontrado!"));

			return;
		}

		if (badge != null) {
			if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(badge)) {
				p.sendMessage(Txt.f("§cVocê precisa ter a insignia para colocar ela!"));
				return;
			}
			eli.icone = PixelmonUtils.getBadge(badge);
			p.sendMessage(Txt.f("§aIcone setado para  a insignia " + badgename + " !"));
		}
		if (foto != null) {
			if (!CameraDB.getFotos(p.getUniqueId()).fotos.contains(foto)) {
				p.sendMessage(Txt.f("§cVocê precisa ter a foto do pokemon para isso!"));
				return;
			}
			eli.icone = PixelmonUtils.getPixelmonIcon(foto.getPoke(), foto.shiny);
			p.sendMessage(Txt.f("§aIcone setado para foto de "+foto.getPoke().name+"!"));
			
		}
		EliteManager.save(eli);

	}

}
