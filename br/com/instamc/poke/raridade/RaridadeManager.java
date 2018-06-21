package br.com.instamc.poke.raridade;

import java.io.File;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.cmds.CmdSetRarity;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import net.minecraftforge.common.config.Configuration;

public class RaridadeManager {

	public static void init(){
		ComandoAPI.enable(new CmdSetRarity());
	}
	private static Configuration cfg = null;

	public static void setRarity(EnumPokemon poke, int rarity) {
		if (cfg == null) {
			File tr = new File("./config/spawnofpsy/spawnofpsy-names.cfg");
			cfg = new Configuration(tr);
		}

		String category = poke.name.toLowerCase();
		if (cfg.hasKey(category, "Rarity")) {
			cfg.get(category, "Rarity", rarity).set(rarity);
		}
		cfg.save();

	}

	public static int getRarity(EnumPokemon poke) {
		if (cfg == null) {
			File tr = new File("./config/spawnofpsy/spawnofpsy-names.cfg");
			cfg = new Configuration(tr);
		}
		String category = poke.name.toLowerCase();
		if (cfg.hasKey(category, "Rarity")) {
			return cfg.get(category, "Rarity", 200).getInt();
		}
		return -2;
	}

}
