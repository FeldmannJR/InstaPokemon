package br.com.instamc.poke.minerar;

import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.Immutable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableStoneData;
import org.spongepowered.api.data.manipulator.mutable.block.StoneData;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.StoneTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;

import com.flowpowered.math.vector.Vector3i;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.minerar.config.CmdMina;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;

public class Minerar {

	public static HashSet<Mina> minas = new HashSet();

	public static BlockState getStoneType(StoneType type) {
		BlockState state = BlockTypes.STONE.getDefaultState();
		StoneData d = Sponge.getDataManager().getManipulatorBuilder(StoneData.class).get().create();
		d.set(d.type().set(type));
		state = state.with(d.asImmutable()).get();

		return state;
	}

	public static int minutos = 0;

	public static void init() {
		ComandoAPI.enable(new CmdMina());
		ComandoAPI.enable(new CmdMinerar());
		MinaMinerios m = new MinaMinerios(Sponge.getServer().getWorld("world").get(), new Vector3i(39973, 40, 39967), new Vector3i(40055, 62, 40031), //
				new MinaChance(BlockTypes.DIAMOND_ORE, 0.0006, 2, 3), //
				new MinaChance(BlockTypes.IRON_ORE, 0.0058, 2, 5), //
				new MinaChance(BlockTypes.COAL_ORE, 0.007, 3, 6), //
				new MinaChance(BlockTypes.GOLD_ORE, 0.0014, 2, 4), //
				new MinaChance(BlockTypes.LAPIS_ORE, 0.0014, 1, 2), //
				new MinaChance(BlockTypes.GRAVEL, 0.005, 10, 16), //
				new MinaChance(getStoneType(StoneTypes.GRANITE), 0.005, 5, 10), //
				new MinaChance(getStoneType(StoneTypes.DIORITE), 0.005, 5, 10), //
				new MinaChance(getStoneType(StoneTypes.ANDESITE), 0.005, 5, 10), //
				new MinaChance(PixelmonUtils.getBlockType("pixelmon:bauxite_ore").getDefaultState(), 0.004, 2, 5)

		//

		);
		minas.add(m);
		minas.add(new MinaArvore(Sponge.getServer().getWorld("world").get(), new Vector3i(40055, 62, 40103), new Vector3i(39973, 92, 40039)));
		resetar();
		doTicks();
		Sponge.getEventManager().registerListeners(InstaPokemon.getPlugin(), new Minerar());
	}

	@Listener
	public void blockBreak(ChangeBlockEvent.Break ev, @First Player p) {
		/*if (p.getGameModeData().get(Keys.GAME_MODE).get() == GameModes.CREATIVE && p.hasPermission("instamc.op")) {
			return;
		}*/
		
		
		FT: for (Transaction<BlockSnapshot> t : ev.getTransactions()) {
			for (Mina m : minas) {
				if (m instanceof MinaArvore) {

					BlockType ori = t.getOriginal().getState().getType();
					if (ori != BlockTypes.LOG && ori != BlockTypes.LOG2 && ori != BlockTypes.LEAVES && ori != BlockTypes.LEAVES2) {
						if (m.isIn(t.getOriginal().getPosition().getX(), t.getOriginal().getPosition().getY(), t.getOriginal().getPosition().getZ(), true)) {
							InstaPokemon.sendMessage(p, "§eVocê não pode construir aqui!");
							ev.setCancelled(true);
							break FT;

						}
					}
				}else{
					BlockType ori = t.getOriginal().getState().getType();
					if(ori==BlockTypes.STONE&&m.isIn(t.getOriginal().getPosition().getX(), t.getOriginal().getPosition().getY(), t.getOriginal().getPosition().getZ(), true)){
						MinaMinerios mi = (MinaMinerios)m;
						mi.showMinerios(t.getOriginal().getPosition());
					}
				}
				
			}

		}
	}



	public static int resetaem = 60;

	public static void doTicks() {
		Task.builder().interval(1, TimeUnit.MINUTES).execute(new Runnable() {

			@Override
			public void run() {
				minutos++;
				if (minutos == (resetaem - 5)) {
					InstaPokemon.broadcast("§eO §c/minerar §eirá regenerar em §c10 minutos§e!");
				}
				if (minutos == (resetaem - 3)) {
					InstaPokemon.broadcast("§eO §c/minerar §eirá regenerar em §c5 minutos§e!");
				}
				if (minutos == (resetaem - 1)) {
					InstaPokemon.broadcast("§eO §c/minerar §eirá regenerar em §c1 minuto§e!");
				}
				if (minutos == resetaem) {
					InstaPokemon.broadcast("§eO §c/minerar §ese regenerou§e!");

					resetar();
					minutos = 0;
				}
			}

		}).submit(InstaPokemon.getPlugin());

	}

	public static void resetar() {
		for (Mina m : minas) {
			m.enche();
		}
	}

}
