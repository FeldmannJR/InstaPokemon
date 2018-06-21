package br.com.instamc.poke.pokeinicial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.GraphicDisplayTypes;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardLocation;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomNoticePacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardDisplayPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardUpdatePacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.dbs.PlayerDB;
import br.com.instamc.poke.dbs.PlayerDB.Flag;
import br.com.instamc.poke.placas.BlockClickSignEvent;
import br.com.instamc.poke.utils.NoticeManager;
import br.com.instamc.poke.utils.PixelmonUtils;
import net.minecraft.entity.player.EntityPlayerMP;

public class PokeInicialManager {

	public static ArrayList<EnumPokemon> pokes = new ArrayList<EnumPokemon>();

	public PokeInicialManager() {
		pokes.add(EnumPokemon.Pikachu);
		// PLANTA
		pokes.add(EnumPokemon.Bulbasaur);
		pokes.add(EnumPokemon.Chikorita);
		pokes.add(EnumPokemon.Treecko);
		pokes.add(EnumPokemon.Turtwig);
		pokes.add(EnumPokemon.Snivy);
		pokes.add(EnumPokemon.Chespin);
		pokes.add(EnumPokemon.Rowlet);
		
		// FOGO
		pokes.add(EnumPokemon.Charmander);
		pokes.add(EnumPokemon.Cyndaquil);
		pokes.add(EnumPokemon.Torchic);
		pokes.add(EnumPokemon.Chimchar);
		pokes.add(EnumPokemon.Tepig);
		pokes.add(EnumPokemon.Fennekin);
		pokes.add(EnumPokemon.Litten);
		
		// AGUA
		pokes.add(EnumPokemon.Squirtle);
		pokes.add(EnumPokemon.Totodile);
		pokes.add(EnumPokemon.Mudkip);
		pokes.add(EnumPokemon.Piplup);
		pokes.add(EnumPokemon.Oshawott);
		pokes.add(EnumPokemon.Froakie);
		pokes.add(EnumPokemon.Popplio);
		

	}

	@Listener
	public void join(ClientConnectionEvent.Join ev) {
		if (!pegouInicial(ev.getTargetEntity())) {
			InstaPokemon.sendMessage(ev.getTargetEntity(), "§aOlá aventureiro, vá para o laboratório para pegar seu primeiro pokémon!");

			NoticeManager.sendNotice(ev.getTargetEntity(), EnumPokemon.Pikachu, "Bem-vindo Aventureiro!", "Vá ao laboratório para pegar seu pokémon inicial!");

		}

	}

	@Listener
	public void interact(BlockClickSignEvent ev) {
		Player p = ev.getTargetEntity();
		if (ev.getLinhas().get(0).equalsIgnoreCase("pokeinicial")) {
			if (pegouInicial(p)) {
				InstaPokemon.sendMessage(p, "§eVocê já pegou seu pokemon inicial!");
				return;
			}
			new MenuPegaPoke().open(p);

		}

	}
	public static boolean pegouInicial(Player p){
		String flag = PlayerDB.getFlag(p.getUniqueId(), Flag.PEGOUPOKEMON);
		if (flag==null||flag.isEmpty())return false;
		return true;
	}

	public static void pegaPoke(Player p, EnumPokemon pk, boolean random) {
		if (pegouInicial(p)) {
			InstaPokemon.sendMessage(p, "§cVocê já pegou o pokemon inicial!");
			return;
		}

		EntityPixelmon epk = PixelmonUtils.build(p, pk, EnumPokeballs.PokeBall, 5, false);

		PixelmonUtils.give(p, epk);
		if (!random) {
			for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
				if (pOn != p) {
					InstaPokemon.sendMessage(pOn, "§eO jogador §c" + p.getName() + " §eescolheu um §a" + pk.name + "§e para começar sua jornada!");
				}
			}
			InstaPokemon.sendMessage(p, "§eVocê escolheu um §c" + pk.name + " §e! §6Boa sorte com sua aventura!");
		} else {
			for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
				if (pOn != p) {

					InstaPokemon.sendMessage(pOn, "§eO jogador §c" + p.getName() + " §eescolheu aleatóriamente e ganhou um§a " + pk.name + "§e para começar sua jornada!");

				}
			}

			InstaPokemon.sendMessage(p, "§eVocê ganhou um §c" + pk.name + " §e! §6Boa sorte com sua aventura!");

		}
		PlayerDB.setFlag(p.getUniqueId(), Flag.PEGOUPOKEMON, pk.name + ":" + random);
		NoticeManager.removeNotice(p);
	}

}
