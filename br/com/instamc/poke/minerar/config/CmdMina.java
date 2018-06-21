package br.com.instamc.poke.minerar.config;

import org.spongepowered.api.command.CommandSource;

import com.flowpowered.noise.module.combiner.Min;

import br.com.instamc.poke.minerar.Mina;
import br.com.instamc.poke.minerar.MinaChance;
import br.com.instamc.poke.minerar.Minerar;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdMina extends ComandoAPI {

	public CmdMina() {
		super(CommandType.OP, "mina");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] arg1) {
		for (Mina m : Minerar.minas) {
			m.enche();
			cs.sendMessage(Txt.f(":"));
		}

	}

}
