package br.com.instamc.poke.elites.cmds.subs;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import br.com.instamc.poke.elites.ConviteManager.Convite;
import br.com.instamc.poke.elites.ConviteManager;
import br.com.instamc.poke.elites.Elite;
import br.com.instamc.poke.elites.EliteDB;
import br.com.instamc.poke.elites.EliteHome;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.core.TerrenoInfo;
import br.instamc.protection.managers.ProtectionManager;

public class CmdSetHome extends CmdSubElite {

	public CmdSetHome() {
		super("sethome");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "";
	}

	@Override
	public String getHelp() {

		return "Seta a home(lugar de teleporte) da base";
	}

	@Override
	public boolean needClan() {
		return true;
	}

	@Override
	public boolean needToBeLeader() {
		return true;
	}

	@Override
	public void execute(Player p, ElitePlayer pl, Elite eli, String[] args) {

		TerrenoInfo info = ProtectionManager.getInfoAt(p.getLocation());
		if (!info.hasDono()) {
			p.sendMessage(Txt.f("§cVocê não pode setar home em terras sem dono!"));
			return;
		}
		if (!info.getDono().equals(p.getUniqueId()) && !info.getMembros().contains(p.getUniqueId())) {
			p.sendMessage(Txt.f("§cVocê não pode setar home em terrenos de outras pessoas!"));
			return;
		}

		EliteHome home = new EliteHome(p);
		eli.home = home;
		EliteManager.sendMessage(p, "§eVocê setou a home da sua elite! Use /elite home para teleportar.");
		eli.sendMessage("§eFoi setada uma nova home para sua elite, use §a/elite home§e para teleportar!");
		EliteManager.save(eli);
	}

}
