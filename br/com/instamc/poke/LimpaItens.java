package br.com.instamc.poke;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.vehicle.Boat;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import br.com.instamc.sponge.library.utils.Txt;

public class LimpaItens {

	int limpaem = 10 * 60;
	int limpa = 60;

	public static void init(){
	LimpaItens itens = new LimpaItens();
	Task.builder().intervalTicks(20).execute(itens::run).submit(InstaPokemon.getPlugin());
	}
	
	public void broadcast(String msg) {
		msg = "§b§l[Limpa] §f" + msg;
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			p.sendMessage(Txt.f(msg));
		}
		Sponge.getServer().getConsole().sendMessage(Txt.f(msg));
	}

	public void run() {

		if (limpa == 0) {
			limpa = limpaem;
			limpa();
			return;
		}
		if (limpa == 3 || limpa == 10 || limpa == 30 || limpa == 60 || limpa == 180) {
			int minutos = limpa / 60;
			int segundos = limpa % 60;
			if (minutos > 0) {
				broadcast("Limpando itens do chão em §b" + minutos + " minuto" + (minutos > 1 ? "s" : "") + " §f!");
			} else {
				broadcast("Limpando itens do chão em §d" + segundos + " segundos§f ! ");
			}

		}
		limpa--;
	}

	public void limpa() {

		broadcast("§fLimpando itens do chão §6agora§f !!!");
		for (World w : Sponge.getServer().getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getType() == EntityTypes.ITEM || e.getType() == EntityTypes.EXPERIENCE_ORB || e.getType() instanceof Arrow|| (e instanceof Monster))
					
				{
					e.remove();
				}
				if(e.getType()==EntityTypes.BOAT){
					Boat b = (Boat)e;
					if(b.getPassengers().isEmpty()){
						b.remove();
					}
				}
			}
		}

	}

}
