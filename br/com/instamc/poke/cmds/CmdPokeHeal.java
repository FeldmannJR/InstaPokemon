package br.com.instamc.poke.cmds;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Cooldown;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.world.World;

public class CmdPokeHeal extends ComandoAPI {

	public CmdPokeHeal() {
		super(CommandType.PERMISSION, "pokeheal");
		this.permission = "instamc.cmd.pokeheal.use";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {

		Player p = (Player) cs;
		if (args.length == 0) {
			if (Cooldown.isCooldown(p, "pokeheal")) {
				p.sendMessage(Txt.f("§aAguarde para usar novamente!"));
				return;
			}
			Cooldown.addCoolDown(p, "pokeheal", 60000 * 2);
			PixelmonUtils.getPlayerStorage(p).healAllPokemon((World) p.getWorld());
			InstaPokemon.sendMessage(p, "§aVocê curou seus Pokemons!");
		}
		if(args.length==1){
			if(!p.hasPermission("instamc.cmd.pokeheal.others")){
				p.sendMessage(Txt.f("§cVocê não tem permissão para curar outros jogadores!"));
				
				return;
			}
	
			Player pon = Sponge.getServer().getPlayer(args[0]).orElse(null);
			if(pon==null){
				p.sendMessage(Txt.f("§cJogador offline!"));
				
				return;
			}
			PixelmonUtils.getPlayerStorage(pon).healAllPokemon((World) pon.getWorld());
			InstaPokemon.sendMessage(p, "§aJogador curado!");
		}
	}

}
