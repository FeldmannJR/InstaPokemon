package br.com.instamc.poke.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlagsListener {
	@SubscribeEvent
	public void spawna(com.pixelmonmod.pixelmon.api.events.PixelmonSpawnEvent event) {
		String nome = event.world.getWorldInfo().getWorldName();
		RegionWorldManager ma = SpongeGuard.getManager().getManager(nome);

		if (ma == null) {
			return;
		}
		if (ma.getFlag(EnumFlags.POKEMONS, new Location<World>(Sponge.getServer().getWorld(nome).get(), new Vector3d(event.x, event.y, event.z))) == FlagValue.DENY) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void send(com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent ev){
		Player p = PixelmonUtils.getPlayer(ev.player);
		if(p!=null){
			Location<World> loc = p.getLocation();
			RegionWorldManager ma = SpongeGuard.getManager().getManager(p.getWorld());
			if(ma.getFlag(EnumFlags.SENDPOKES,loc)==FlagValue.DENY){
				ev.setCanceled(true);
				InstaPokemon.sendMessage(p, "§cVocê não pode usar pokemons aqui!");
			}
		}
	}
	public FlagsListener(){
		SchedulerUtils.runSyncReapeat(new Runnable() {
			
			@Override
			public void run() {
				for(Player p : Sponge.getServer().getOnlinePlayers()){
					Location<World> loc = p.getLocation();
					RegionWorldManager ma = SpongeGuard.getManager().getManager(p.getWorld());
					if(ma.getFlag(EnumFlags.SENDPOKES,loc)==FlagValue.DENY){
					if(PixelmonUtils.hasPokemonsOutside(p)){
						PixelmonUtils.getPlayerStorage(p).recallAllPokemon();
					}
					}
					
				}
				
			}
		}, 5);
		
	}
	
}
