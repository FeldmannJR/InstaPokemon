package br.com.instamc.poke.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.camera.Foto.Forma;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class Foto {

	EnumPokemon poke;
	Forma forma;
	public boolean shiny;

	public Foto(EnumPokemon poke, boolean shiny, Forma f) {
		this.poke = poke;
		this.forma = f;
		this.shiny = shiny;
	}

	public Forma getForma() {
		return forma;
	}

	public EnumPokemon getPoke() {
		return poke;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Foto) {
			Foto f = (Foto) obj;
			if (f.poke == getPoke() && f.forma == getForma() && f.shiny == shiny) {
				return true;
			}
		}
		return false;
	}

	public ItemStack toItemStack(boolean tem) {
		ItemStack i;
		String formas = "";
		if (shiny) {
			formas = " §6§lShiny";
		}
		if (forma != null) {
			formas += " §c§l" + forma.name() + "";
		}
		if (tem) {
			if (forma != null) {
				if (forma.suportshiny) {
					i = PixelmonUtils.getPixelmonIcon(poke, shiny, forma.numero);
				} else {
					i = PixelmonUtils.getPixelmonIcon(poke, false, forma.numero);

				}
			} else {
				i = PixelmonUtils.getPixelmonIcon(poke, shiny);

			}
			i.offer(Keys.DISPLAY_NAME, Txt.f("§f§l" + poke.name + "" + formas));
		} else {
			i = ItemStack.of(ItemTypes.STAINED_GLASS_PANE, 1);
			i.offer(Keys.DYE_COLOR, DyeColors.GRAY);
			i.offer(Keys.DISPLAY_NAME, Txt.f("§7" + poke.name + Txt.f(formas).toPlain()));

		}
		return i;
	}

	@Override
	public int hashCode() {
		return Objects.hash(poke, forma, shiny);
	}

	public static enum Forma {
		Plant(0, true, true, EnumPokemon.Burmy, EnumPokemon.Wormadam),
		Sandy(1, true, EnumPokemon.Burmy, EnumPokemon.Wormadam),
		Trash(2, true, EnumPokemon.Burmy, EnumPokemon.Wormadam),
		Ice(1, true, true, EnumPokemon.Castform),
		Rain(2, true, EnumPokemon.Castform),
		Sun(3, true, EnumPokemon.Castform),
		Attack(1, true, true, EnumPokemon.Deoxys),
		Defense(2, true, EnumPokemon.Deoxys),
		Speed(3, true, EnumPokemon.Deoxys),
		A(0, true, true, EnumPokemon.Unown),
		B(1, true, EnumPokemon.Unown),
		C(2, true, EnumPokemon.Unown),
		D(3, true, EnumPokemon.Unown),
		E(4, true, EnumPokemon.Unown),
		F(5, true, EnumPokemon.Unown),
		G(6, true, EnumPokemon.Unown),
		H(7, true, EnumPokemon.Unown),
		I(8, true, EnumPokemon.Unown),
		J(9, true, EnumPokemon.Unown),
		K(10, true, EnumPokemon.Unown),
		L(11, true, EnumPokemon.Unown),
		M(12, true, EnumPokemon.Unown),
		N(13, true, EnumPokemon.Unown),
		O(14, true, EnumPokemon.Unown),
		P(15, true, EnumPokemon.Unown),
		Q(16, true, EnumPokemon.Unown),
		R(17, true, EnumPokemon.Unown),
		S(18, true, EnumPokemon.Unown),
		T(19, true, EnumPokemon.Unown),
		U(20, true, EnumPokemon.Unown),
		V(21, true, EnumPokemon.Unown),
		W(22, true, EnumPokemon.Unown),
		X(23, true, EnumPokemon.Unown),
		Y(24, true, EnumPokemon.Unown),
		Z(25, true, EnumPokemon.Unown),
		Question(26, true, EnumPokemon.Unown),
		Exclamation(27, true, EnumPokemon.Unown),
		MegaX(1, false, EnumPokemon.Charizard),
		MegaY(2, false, EnumPokemon.Charizard),
		Mega(1, false, EnumPokemon.Blastoise, EnumPokemon.Venusaur),

		;

		int numero = 0;

		boolean suportshiny;
		boolean hasdefault;
		List<EnumPokemon> pokes = new ArrayList();

		private Forma(int forma, boolean suportshiny, EnumPokemon... pokes) {
			this(forma, suportshiny, false, pokes);
		}

		public boolean hasForma(EnumPokemon poke) {
			for (EnumPokemon p : pokes) {
				if (p == poke)
					return true;
			}
			return false;
		}

		private Forma(int forma, boolean suportshiny, boolean defaulte, EnumPokemon... pokes) {
			this.numero = forma;
			for (EnumPokemon po : pokes) {
				this.pokes.add(po);
			}
			this.suportshiny = suportshiny;
			this.hasdefault = defaulte;
		}

	}

	public static Foto fromString(String s) {
		EnumPokemon po = null;
		boolean shiny = false;
		String[] split = s.split(";");
		if (split.length != 2) {
			return null;
		}
		for (EnumPokemon poe : EnumPokemon.values()) {
			if (poe.name.equalsIgnoreCase(split[0])) {
				po = poe;
				break;
			}
		}
		if (split[1].startsWith("s")) {
			shiny = true;
		}
		Forma fo = null;
		if (split[1].contains(":")) {
			String forma = split[1].split(":")[1];
			for (Forma f : Forma.values()) {
				if (forma.equalsIgnoreCase(f.name())) {
					fo = f;
				}
			}
		}
		if (po == null)
			return null;
		return new Foto(po, shiny, fo);
	}

	public static Foto extractFromEntity(EntityPixelmon poke) {
		Forma forma = null;
		for (Forma f : Forma.values()) {
			if (f.hasForma(poke.baseStats.pokemon)) {
			if (f.numero == poke.getForm()) {

					forma = f;
					break;
				}
			}
		}

		return new Foto(poke.baseStats.pokemon, poke.getIsShiny(), forma);

	}

	@Override
	public String toString() {
		if (forma != null) {
			String extra = (shiny ? "s:" : "n:") + (forma != null ? forma.name() : "");
			return poke.name + ";" + extra;
		} else {
			return poke.name + ";" + (shiny ? "s" : "n");
		}
	}

}
