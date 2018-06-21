package br.com.instamc.poke.cmds;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;

import br.com.instamc.poke.chaves.ChaveManager;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.item.ItemStack;

public class CmdAddChave extends ComandoAPI {

	public CmdAddChave() {
		super(CommandType.OP, "addchave");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] args) {
		Player p = (Player) arg0;
		if(args.length==0){
			p.sendMessage(Txt.f("§/addchave NOMECHAVE"));
			return;
		}
		
		for (Entity t : p.getNearbyEntities(3)) {
			if (t instanceof NPCTrainer) {
				NPCTrainer npc = (NPCTrainer) t;
				ItemStack[] newi = new ItemStack[npc.getWinnings().length+1];
				for(int x =0;x<newi.length-1;x++){
					newi[x] = npc.getWinnings()[x];
				}
				newi[newi.length-1] = ItemStackUtil.toNative(ChaveManager.geraChave(args[0]));
				npc.updateDrops(newi);
				p.sendMessage(Txt.f("§aSetada chave em npc "+npc.getDisplayText()+" !"));
				return;
			}
		}
		p.sendMessage(Txt.f("§cNenhum trainer encontrado"));
	}

}
