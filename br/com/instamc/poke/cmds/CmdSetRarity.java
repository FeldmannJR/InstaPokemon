package br.com.instamc.poke.cmds;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import br.com.instamc.poke.raridade.RaridadeManager;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdSetRarity extends ComandoAPI {

	public CmdSetRarity() {
		super(CommandType.OP, "raridade");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] args) {
		Player p = (Player)arg0;
		
		if(args.length==2){
			if(args[0].equalsIgnoreCase("ver")){
				String poke = args[1];
				EnumPokemon pokee = null;
				for(EnumPokemon po : EnumPokemon.values()){
					if(po.name.equals(poke)){
						pokee = po;
					}
				}
				if(pokee==null){
					p.sendMessage(Txt.f("§cPoke não encontrado!"));
					return;
				}
				p.sendMessage(Txt.f("§eRaridade: "+RaridadeManager.getRarity(pokee)));
			return;
			}
		}
		if(args.length==3){
			if(args[0].equalsIgnoreCase("set")){
				String poke = args[2];
				EnumPokemon pokee = null;
				for(EnumPokemon po : EnumPokemon.values()){
					if(po.name.equals(poke)){
						pokee = po;
					}
				}
				if(pokee==null){
					p.sendMessage(Txt.f("§cPoke não encontrado!"));
					return;
				}
				int raridade = -1;
				try{
					raridade = Integer.valueOf(args[1]);
				}catch(NumberFormatException ex){
					p.sendMessage(Txt.f("§cValor inválido!"));
					
					return;
				}
				if(raridade<0||raridade>200){
					p.sendMessage(Txt.f("§cValor inválido(0-200)!"));
					return;
				}
				RaridadeManager.setRarity(pokee, raridade);
				p.sendMessage(Txt.f("§cSetado!"));
				Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "psyduck reload");
				
			}
		}
		
		p.sendMessage(Txt.f("§e/raridade set VALOR(0-200 Quanto mais alto, mais chance de spawnar) POKEMON"));
		p.sendMessage(Txt.f("§e/raridade ver POKEMON"));

	}

}
