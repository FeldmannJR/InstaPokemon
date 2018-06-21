/*
 *  _   __   _   _____   _____       ___       ___  ___   _____  
 * | | |  \ | | /  ___/ |_   _|     /   |     /   |/   | /  ___| 
 * | | |   \| | | |___    | |      / /| |    / /|   /| | | |     
 * | | | |\   | \___  \   | |     / / | |   / / |__/ | | | |     
 * | | | | \  |  ___| |   | |    / /  | |  / /       | | | |___  
 * |_| |_|  \_| /_____/   |_|   /_/   |_| /_/        |_| \_____| 
 * 
 * Projeto feito por Carlos Andre Feldmann Junior, Isaias Finger e Gabriel Augusto Souza
 */
package br.com.instamc.poke.cmds;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SendExtraData;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerExtraData;
import com.pixelmonmod.pixelmon.storage.PlayerExtras;
import com.pixelmonmod.pixelmon.storage.PlayerExtras.HatType;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Skype: junior.feldmann GitHub: https://github.com/feldmannjr Facebook:
 * https://www.facebook.com/carlosandre.feldmannjunior
 *
 * @author Feldmann
 */
public class Roupas implements CommandExecutor {

	public abstract class Seleciona {

		public abstract void selecionou(Player p, Colors c);
	}

	public void openMenuColors(Player p, Seleciona se) {
		Menu m = new Menu("Cor", 2);
		for (final Colors c : Colors.values()) {
			m.addButtonNextSlot(new MenuButton(new SlotPos(0, 0), ItemStack.builder().quantity(1).itemType(ItemTypes.APPLE).keyValue(Keys.DISPLAY_NAME, Text.builder(c.name()).color(TextColors.AQUA).build()).build()) {

				@Override
				public void click(Player p, Menu m, ClickType click) {

					se.selecionou(p, c);

					close(p);
				}
			});
		}
		m.open(p);
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (src instanceof Player) {

			Player p = (Player) src;
			if (!p.hasPermission("instamc.op")) {
				return CommandResult.empty();
			}
			Menu m = new Menu("Estilo", 2);

			for (final Hats h : Hats.values()) {
				m.addButtonToSquare(new SlotPos(0, 0), new SlotPos(8, 0), new MenuButton(new SlotPos(0, 0), ItemStack.builder().itemType(ItemTypes.BEEF).keyValue(Keys.DISPLAY_NAME, Text.of(h.name())).build()) {
					@Override
					public void click(Player p, Menu m, ClickType click) {
						if (h != Hats.TRAINERHAT) {
							colocaHat(p, h, null);

							close(p);
						} else {
							close(p);

							openMenuColors(p, new Seleciona() {

								@Override
								public void selecionou(Player p, Colors c) {
									colocaHat(p, Hats.TRAINERHAT, c);
								}
							});

						}
					}

				});

			}

			for (final Sash s : Sash.values()) {
				m.addButtonToSquare(new SlotPos(0, 1), new SlotPos(8, 1), new MenuButton(new SlotPos(2, 1), ItemStack.builder().itemType(ItemTypes.STICK).keyValue(Keys.DISPLAY_NAME, Text.of("Sash " + s.name())).build()) {

					@Override
					public void click(Player p, Menu m, ClickType click) {
						if (s != Sash.SASH) {
							colocaSash(p, s, null);
							close(p);
						} else {
							close(p);
							Task.builder().delay(20, TimeUnit.MILLISECONDS).execute(new Runnable() {

								@Override
								public void run() {
									openMenuColors(p, new Seleciona() {

										@Override
										public void selecionou(Player p, Colors c) {
											colocaSash(p, Sash.SASH, c);
										}
									});
								}
							}).submit(InstaPokemon.instancia);

						}
					}

				});
			}
			m.open(p);
		}

		return CommandResult.success();
	}

	public static enum Hats {

		FEDORA(HatType.FEDORA),
		TOPHAT(HatType.TOP_HAT),
		FEZ(HatType.FEZ),
		TRAINERHAT(HatType.TRAINER_HAT);

		HatType ty;

		private Hats(HatType h) {
			ty = h;
		}

		public HatType getType() {
			return ty;
		}

	}

	public void colocaHat(Player p, Hats h, Colors c) {

		PlayerExtraData data = PlayerExtras.getExtras((EntityPlayerMP) p);

		p.sendMessage(Text.of("" + h.name() + " equipado!"));
		PlayerExtras.checkDataOnline(data.id, data2 -> {
			Optional<PlayerStorage> optstorage;
			if (data.hasData() && (optstorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) p)).isPresent()) {
				PlayerStorage storage = optstorage.get();
				data2.hasCap = true;
				data2.isSupport = true;

				if (c != null) {

					data2.setCapColours(c.r, c.g, c.b);
				}
				data2.setHatType(h.getType());
				storage.getExtraData().setDataValues(data2);

				Pixelmon.network.sendToAll((IMessage) new SendExtraData(data));
				Pixelmon.network.sendToAll(new SendExtraData(data2));

			}

		});
		;

	}

	public void colocaSash(Player p, Sash s, Colors c) {
		PlayerExtraData data = PlayerExtras.getExtras((EntityPlayerMP) p);
		s.faz(data);
		p.sendMessage(Text.of("Sash " + s.name() + " equipado!"));
		if (s == Sash.SASH) {
			if (c != null) {
				data.colours = new int[] { c.getR(), c.getG(), c.getB() };
			} else {
				data.colours = new int[] { -1, -1, -1 };
			}
		} else {
			data.colours = new int[] { -1, -1, -1 };
		}
		PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) p).get().getExtraData().setDataValues(data);
		;

		Pixelmon.network.sendToAll((IMessage) new SendExtraData(data));

	}

	public static enum Sash {

		SASH {
			@Override
			public void faz(PlayerExtraData data) {
				data.isSupport = true;
				data.isModeller = false;
				data.sashEnabled = true;
				data.isAdmin = false;
				data.isDeveloper = false;
				data.hasSash = true;
				data.hasRainbowSash = false;

			}
		},
		RAINBOW {
			@Override
			public void faz(PlayerExtraData data) {
				data.isSupport = true;
				data.isModeller = false;
				data.sashEnabled = true;
				data.isAdmin = false;
				data.isDeveloper = false;
				data.hasSash = true;
				data.hasRainbowSash = true;
			}
		},
		MODDER {
			@Override
			public void faz(PlayerExtraData data) {
				data.isSupport = false;
				data.isModeller = true;
				data.sashEnabled = true;
				data.isAdmin = false;
				data.isDeveloper = false;
				data.hasSash = true;
				data.hasRainbowSash = false;
			}
		},
		ADMIN {
			@Override
			public void faz(PlayerExtraData data) {
				data.isSupport = false;
				data.isModeller = false;
				data.isAdmin = true;
				data.isDeveloper = false;
				data.sashEnabled = true;
				data.hasSash = true;
				data.hasRainbowSash = false;
			}
		},
		DEVELOPER {
			@Override
			public void faz(PlayerExtraData data) {
				data.isSupport = false;
				data.isModeller = false;
				data.isAdmin = false;
				data.isDeveloper = true;
				data.sashEnabled = true;
				data.hasSash = true;
				data.hasRainbowSash = false;
			}
		};

		public abstract void faz(PlayerExtraData data);
	}

	public static enum Colors {

		Vermelha(255, 0, 0),
		Verde(0, 102, 0),
		Azul(0, 0, 255),
		Rosa(255, 0, 102),
		Amarelo(255, 255, 0),
		Laranja(255, 102, 0),
		Branco(255, 255, 255),
		Preto(0, 0, 0),
		Marrom(153, 102, 0),
		Roxo(153, 0, 204),
		Aqua(0, 255, 255),
		Limão(0, 255, 0);
		int r;
		int g;
		int b;

		public int getR() {
			return r;
		}

		public int getG() {
			return g;
		}

		public int getB() {
			return b;
		}

		private Colors(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

	}
}
