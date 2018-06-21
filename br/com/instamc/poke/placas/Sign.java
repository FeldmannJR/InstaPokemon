package br.com.instamc.poke.placas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.type.Include;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.Cooldown;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;

public class Sign {

	@Listener
	public void sign(ChangeSignEvent ev, @First Player p) {
		if (!InstaPokemon.isOp(p)) {
			Optional<Text> linha = ev.getText().get(0);

			if (linha.isPresent()) {
				if (linha.get().toPlain().equalsIgnoreCase("[instamc]")) {
					ev.getText().setElement(0, Txt.f("§c§lErro!"));
				}
			}
		} else {
			for (int x = 0; x < 4; x++) {
				Optional<Text> linha = ev.getText().get(x);

				if (linha.isPresent()) {
					ev.getText().setElement(x, Txt.f(linha.get().toPlain().replaceAll("&", "§")));
				}
			}
		}

	}

	@Listener
	public void interact(InteractBlockEvent.Secondary.MainHand ev, @First Player p) {

		Optional<Location<World>> l = ev.getTargetBlock().getLocation();

		BlockState blo = null;
		Location loc = null;
		for (int x = 0; x < 8; x++) {
			if (l.isPresent()) {
				if (loc == null) {
					loc = l.get().getRelative(Direction.DOWN);
				} else {
					loc = loc.getRelative(Direction.DOWN);

				}
				blo = loc.getBlock();
				if (loc.getTileEntity().isPresent()) {
					TileEntity t = (TileEntity) loc.getTileEntity().get();
					Optional<SignData> datao = t.getOrCreate(SignData.class);
					if (datao.isPresent()) {
						SignData data = datao.get();
						if (data.get(0).isPresent() && data.get(1).isPresent()) {
							String l1 = data.get(1).get().toPlain();
							List<Integer> blocos = new ArrayList();

							A: for (String s : l1.split(",")) {
								try {
									blocos.add(Integer.valueOf(s));
								} catch (NumberFormatException ex) {
									continue A;
								}

							}
							if (blocos.isEmpty()) {
								continue;
							}

							int dif = l.get().getBlockY() - loc.getBlockY();
							boolean temdif = false;
							for (Integer b : blocos) {
								if (b.intValue() == dif) {
									temdif = true;
								}
							}
							if (!temdif) {

								continue;
							}

							if (data.get(0).get().toPlain().equalsIgnoreCase("[instamc]")) {

								List<String> resto = new ArrayList();
								for (int y = 2; y < 4; y++) {
									if (data.get(y).isPresent()) {
										resto.add(data.get(y).get().toPlain());
									} else {
										resto.add("");
									}
								}
								if (resto != null) {
									boolean cancel = true;

									if (!Cooldown.isCooldown(p, "usapraca")) {
										Cooldown.addCoolDown(p, "usapraca", 500);

										if (!doSomething(p, resto, ev.getTargetBlock())) {
											cancel = false;
										}
									}
									if (cancel) {
										ev.setCancelled(true);
									}
									break;
								}
							}
						}
					}
				}

			}
		}
	}

	public static boolean doSomething(Player p, List<String> placa, BlockSnapshot b) {

		BlockClickSignEvent ev = new BlockClickSignEvent(p, Cause.source(InstaPokemon.instancia).build(), b, placa);
		Sponge.getEventManager().post(ev);
		return ev.cancelblockinteract;
	}

}
