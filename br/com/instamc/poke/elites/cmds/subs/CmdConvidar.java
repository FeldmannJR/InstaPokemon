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
import br.com.instamc.poke.elites.EliteHome;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.elites.ElitePlayer;
import br.com.instamc.poke.elites.cmds.CmdSubElite;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdConvidar extends CmdSubElite {

	public CmdConvidar() {
		super("convidar");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArgs() {

		return "<jogador>";
	}

	@Override
	public String getHelp() {

		return "Convida alguem para sua elite";
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

		if (args.length != 1) {
			showUsage(p);
			return;
		}

		String jogadorname = args[0];
		Optional<Player> pOn = Sponge.getServer().getPlayer(jogadorname);
		if (!pOn.isPresent()) {
			p.sendMessage(Txt.f("§cJogador offline!"));
			return;
		}
		ElitePlayer convidado = EliteManager.getElitePlayer(pOn.get().getUniqueId());
		if (convidado.hasElite()) {
			p.sendMessage(Txt.f("§eJogador já possui uma elite!"));
			return;
		}
		Convite conv = EliteManager.getConvites().getConvite(eli.getId(), convidado.uid);
		if (conv != null) {
			p.sendMessage(Txt.f("§eVocê já convidou o jogador para sua elite, espere a resposta dele!"));
			return;
		}
		if (eli.getMembros().size() >= EliteManager.MAXIMO) {
			p.sendMessage(Txt.f("§cSua elite está lotada! Máximo de " + EliteManager.MAXIMO + " jogadores!"));
			return;

		}
		conv = new Convite(eli.getId(), convidado.uid);
		EliteManager.getConvites().addConvite(conv);

		Text t = Txt.f("§d§l[Elites] §fVocê foi convidado para a elite §6" + eli.getNome() + "§7(" + eli.tag + "§7),");
		pOn.get().sendMessage(t);
		Text aceitar = Txt.f("§a§l[Aceitar]");
		aceitar = aceitar.toBuilder().onClick(TextActions.runCommand("/elite aceitar " + eli.getTagDB())).onHover(TextActions.showText(Txt.f("§fClique para aceitar!"))).build();
		Text recusar = Txt.f("§c§l[Recusar]");
		recusar = recusar.toBuilder().onClick(TextActions.runCommand("/elite recusar " + eli.getTagDB())).onHover(TextActions.showText(Txt.f("§fClique para recusar!"))).build();
		t = Txt.f("§fvocê deseja:  ").toBuilder().append(aceitar).append(Txt.f("    ")).append(recusar).build();
		pOn.get().sendMessage(t);
		pOn.get().sendMessage(Txt.f("§8Clique na opção que deseja!"));
		p.sendMessage(Txt.f("§cVocê convidou " + pOn.get().getName() + "!"));

	}

}
