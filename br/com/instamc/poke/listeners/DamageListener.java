package br.com.instamc.poke.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Tristate;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.sponge.library.utils.Txt;

public class DamageListener {
	HashMap<UUID, Integer> agua = new HashMap();
	HashMap<UUID, Long> lastagua = new HashMap();

	public void tomaAgua(Player p) {
		if(p.hasPermission("instamc.staff")) return;
		if (lastagua.containsKey(p.getUniqueId())) {
			long last = lastagua.get(p.getUniqueId());
			if (last < (System.currentTimeMillis() - 1000 * 10)) {
				agua.remove(p.getUniqueId());
				lastagua.remove(p.getUniqueId());

			}
			if (last > (System.currentTimeMillis() - 1000)) {
				return;
			}

		}

		lastagua.put(p.getUniqueId(), System.currentTimeMillis());
		int aguap = 1;
		if (agua.containsKey(p.getUniqueId())) {
			aguap += agua.get(p.getUniqueId());
		}
		if (aguap == 10) {
			p.sendTitle(Title.builder().title(Txt.f("§cCUIDADO!!!")).subtitle(Txt.f("§bVocê irá se afogar!")).stay(20 * 5).build());

			InstaPokemon.sendMessage(p, "§bVocê irá se afogar cuidado!");
		}
		if (aguap > 20) {

			if (p.getVehicle().isPresent()) {
				p.getVehicle().get().clearPassengers();
			}
			InstaPokemon.sendMessage(p, "§cVocê se afogou, perdeu a consciência e não sabe como foi parar no spawn!");
			p.setLocation(p.getWorld().getSpawnLocation());
			p.sendTitle(Title.builder().title(Txt.f("§cOnde é que eu tô.")).subtitle(Txt.f("§7Será que estou na alagoinha?")).stay(60 * 20).build());
			List<PotionEffect> potions = p.get(Keys.POTION_EFFECTS).orElse(new ArrayList<PotionEffect>());
			potions.add(PotionEffect.builder().particles(false).potionType(PotionEffectTypes.BLINDNESS).amplifier(10).duration(60 * 20).build());
			potions.add(PotionEffect.builder().particles(false).potionType(PotionEffectTypes.SLOWNESS).amplifier(10).duration(60 * 20).build());
			potions.add(PotionEffect.builder().particles(false).potionType(PotionEffectTypes.JUMP_BOOST).amplifier(150).duration(60 * 20).build());
			
			p.offer(Keys.POTION_EFFECTS, potions);
			lastagua.remove(p.getUniqueId());
			agua.remove(p.getUniqueId());
		} else {
			agua.put(p.getUniqueId(), aguap);
		}

	}

	public void quebraPerna(Player p, double dano) {
		if(p.hasPermission("instamc.staff")) return;
		int tempo = ((int) dano) ;
		int efeito = 0;
		if (dano > 20) {
			efeito = 5;
		} else {
			efeito = 4;
		}
		if (tempo > 30) {
			tempo = 30;
		}
		if(tempo<=0)return;
		List<PotionEffect> potions = p.get(Keys.POTION_EFFECTS).orElse(new ArrayList<PotionEffect>());
		potions.add(PotionEffect.builder().particles(false).potionType(PotionEffectTypes.SLOWNESS).amplifier(efeito).duration(tempo * 20).build());
		potions.add(PotionEffect.builder().particles(false).potionType(PotionEffectTypes.JUMP_BOOST).amplifier(150).duration(tempo * 20).build());
		
		p.offer(Keys.POTION_EFFECTS, potions);

		p.getWorld().playSound(SoundTypes.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, p.getLocation().getPosition(), 4d, 1, 1d);
		p.sendTitle(Title.builder().title(Txt.f("§c§lAI!")).subtitle(Txt.f("§aVocê quebrou as pernas!!!")).stay(tempo * 20).build());

	}

	@Listener
	@IsCancelled(Tristate.UNDEFINED)
	public void damage(DamageEntityEvent ev) {
		if (ev.getTargetEntity() instanceof Player) {
			if (ev.getCause().first(DamageSource.class).isPresent()) {
				DamageSource d = ev.getCause().first(DamageSource.class).get();
				if (d.getType() == DamageTypes.VOID) {

					if (ev.getTargetEntity().getVehicle().isPresent()) {
						ev.getTargetEntity().getVehicle().get().clearPassengers();
					}
					ev.getTargetEntity().setLocation(ev.getTargetEntity().getLocation().getExtent().getSpawnLocation());
				}
				if (d.getType() == DamageTypes.DROWN) {
					tomaAgua((Player) ev.getTargetEntity());
				}
				if (d.getType() == DamageTypes.FALL) {
					quebraPerna((Player) ev.getTargetEntity(), ev.getFinalDamage());
				}
			}
			ev.setCancelled(true);

		}
	}
}
