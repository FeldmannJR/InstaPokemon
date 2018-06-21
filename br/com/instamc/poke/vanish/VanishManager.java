package br.com.instamc.poke.vanish;

import java.util.HashSet;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class VanishManager {

	public static void vanish(Player p, boolean vanished,boolean tab) {
	
			p.offer(Keys.VANISH, vanished);
			p.offer(Keys.VANISH_IGNORES_COLLISION, vanished);
			p.offer(Keys.VANISH_PREVENTS_TARGETING, vanished);
			p.offer(Keys.IS_SILENT, vanished);

			if(tab){
			if (vanished) {

				for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
					pOn.getTabList().removeEntry(p.getUniqueId());
				
				}
			} else {
				for (Player pOn : Sponge.getServer().getOnlinePlayers()) {
					pOn.getTabList().addEntry(TabListEntry.builder().profile(p.getProfile()).latency(0).list(pOn.getTabList()).gameMode(GameModes.SURVIVAL).build());
				}
			}
			}

		
	}
	
	@Listener
	public void join(ClientConnectionEvent.Join ev){
		VanishManager.vanish(ev.getTargetEntity(), false, true);
		for(Player p : Sponge.getServer().getOnlinePlayers()){
			if(p.get(Keys.VANISH).orElse(Boolean.FALSE)){
				ev.getTargetEntity().getTabList().removeEntry(p.getUniqueId());
			}
		}
	}

}
