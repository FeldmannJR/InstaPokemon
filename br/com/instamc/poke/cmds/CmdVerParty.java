package br.com.instamc.poke.cmds;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;

import com.pixelmonmod.pixelmon.comm.PixelmonData;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.nbt.NBTTagCompound;

public class CmdVerParty extends ComandoAPI{

	public CmdVerParty() {
		super(CommandType.CONSOLEPERM, "verparty");
		// TODO Auto-generated constructor stub
		this.permission = "instamc.cmd.verparty";
	}
	public static Text getText(PixelmonData data){
		String item = "Nenhum";
		if(data.heldItem!=null){
			item =data.heldItem.getItem().getRegistryName().toString();
		}
		Builder t = Txt.f("§a§l["+data.getSpecies().name+(data.isShiny?" §6§lShiny§a§l":"")+"]").toBuilder();
		t.onHover(TextActions.showText(Txt.f(
				"§6LvL: §e"+data.lvl+"\n"+
				"§6Nature: §e"+data.nature.name()+"\n"+				
				"§6Original Trainer: §e"+data.OT+"\n"+
				"§6Habilidade: §e"+data.ability+"\n"+
				"§6Item: §e"+item
				
		)));
		return t.build();
		
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		if(args.length!=1){
			cs.sendMessage(Txt.f("§aUse §f/verparty <player>"));
			return;
			
		}
		Player p = LibAPI.getPlayer(args[0]);
		if(p!=null){
			cs.sendMessage(Txt.f("§f§l---> §aParty "+p.getName()));
			
			NBTTagCompound[] party = PixelmonUtils.getParty(p);
			for(int x =0;x<6;x++){
				if(party[x]!=null){
					PixelmonData data = new PixelmonData(party[x]);
					cs.sendMessage(Txt.f("§6"+(x+1)+"- ").toBuilder().append(getText(data)).build());
				}else{
					cs.sendMessage(Txt.f("§6"+(x+1)+"- §c[Nada]"));
					
				}
			}
			
		}else{
			cs.sendMessage(Txt.f("§cPlayer offline!"));
			
		}
	}

}
