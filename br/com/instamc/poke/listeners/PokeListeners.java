package br.com.instamc.poke.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent.ApricornPlanted;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import com.pixelmonmod.pixelmon.items.ItemBadge;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.chaves.ChaveManager;
import br.com.instamc.poke.chaves.ChavesDB;
import br.com.instamc.poke.events.PlayerWinBattleEvent;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.logs.LogsDB;
import br.com.instamc.poke.logs.LogsDB.PokeAction;
import br.com.instamc.poke.utils.InvUtils;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.library.apis.EconAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.utils.LibUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.managers.ProtectionManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import scala.reflect.api.FlagSets.FlagValues;

public class PokeListeners {

	@SubscribeEvent
	public void captureWild(com.pixelmonmod.pixelmon.api.events.CaptureEvent.SuccessfulCapture ev) {
		/*
		 * int insignias = InsigniaDB.getInsignias(ev.player.getUniqueID()).size(); int
		 * levelMaxCaptura = insignias * 3; int lvl = ev.pokemon.getLvl().getLevel(); if
		 * (levelMaxCaptura < 5) { levelMaxCaptura = 5; } if (levelMaxCaptura > 100) {
		 * levelMaxCaptura = 100; } if (lvl < levelMaxCaptura) { return; }
		 * ev.pokemon.getLvl().setLevel(levelMaxCaptura); ev.pokemon.getLvl().setExp(0);
		 * ev.pokemon.stats.setLevelStats(ev.pokemon.getNature(), ev.pokemon.baseStats,
		 * levelMaxCaptura); ev.pokemon.updateStats();
		 */
	}

	@SubscribeEvent
	public void dropItem(com.pixelmonmod.pixelmon.api.events.DropEvent ev) {
		List<DroppedItem> toremove = new ArrayList();
		for (int x = 0; x < ev.getDrops().size(); x++) {
			DroppedItem it = ev.getDrops().get(x);
			String chave = ChaveManager.getChaveFromItem(ItemStackUtil.fromNative(it.itemStack));
			if (chave != null) {
				ChavesDB.addChave(ev.player.getUniqueID(), chave);
				toremove.add(it);
				continue;
			}
			if (it.itemStack.getItem() instanceof ItemBadge) {
				EnumBadges bad = ((ItemBadge) it.itemStack.getItem()).badge;

				Player p = Sponge.getServer().getPlayer(ev.player.getUniqueID()).orElse(null);
				if (p != null) {
					InsigniaDB.addInsignia(p, bad);
				} else {
					InsigniaDB.addInsignia(ev.player.getUniqueID(), bad);
				}
				toremove.add(it);

			}

		}
		for (DroppedItem it : toremove) {
			ev.removeDrop(it);
		}
		if (ev.getDrops().isEmpty()) {
			ev.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void receive(com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent ev) {
		if (ev.receiveType == ReceiveType.Trade) {
			return;
		}
		LogsDB.PokeAction action = PokeAction.GANHA;
		if (ev.receiveType == ReceiveType.Command) {
			action = PokeAction.CUSTOM;
		}
		LogsDB.addLog(ev.pokemon.getSpecies().name + (ev.pokemon.getIsShiny() ? "-s" : ""), "", ev.player.getUniqueID(), "", action);

	}

	@SubscribeEvent
	public void destroy(com.pixelmonmod.pixelmon.api.events.PixelmonDeletedEvent ev) {

		PixelmonData data = new PixelmonData(ev.pokemon);
		LogsDB.addLog(data.getSpecies().name + (data.isShiny ? "-s" : ""), "", ev.player.getUniqueID(), "", LogsDB.PokeAction.DESTROY);
	}

	@SubscribeEvent
	public void winTrainer(com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent ev) {
		NPCTrainer trainer = ev.trainer;

		EnumBossMode boss = trainer.getBossMode();
		EnumEncounterMode co = trainer.getEncounterMode();
		if (co == EnumEncounterMode.Unlimited) {
			return;
		}
		int money = trainer.getLvl() / 5;
		String derrotou = null;
		if (boss == EnumBossMode.Ultimate) {
			money += 150;
			derrotou = "§6Ultimate";
		} else if (boss == EnumBossMode.Legendary) {
			money += 100;
			derrotou = "§4Lendário";
		} else if (boss == EnumBossMode.Rare) {
			money += 80;
		} else if (boss == EnumBossMode.Uncommon) {
			money += 50;
		} else if (boss == EnumBossMode.Equal) {
			money += 30;
		}
		if (ev.trainer.getWinMoney() > 0) {
			money = ev.trainer.getWinMoney();
		} else {
			if (money == 0) {
				money = 1;
			}
		}
		EconAPI.addMoney(ev.player.getUniqueID(), money + 0.0);

		if (derrotou != null) {
			InstaPokemon.broadcast("" + InstaPokemon.getPrefixWithName(ev.player) + " §ederrotou um §9Trainer " + derrotou + " §ee ganhou §6" + money + " coins§e.");
		}
		InstaPokemon.sendMessage(ev.player, "§eVocê ganhou §6" + money + " coins §epor derrotar o trainer!");
	}

	/*
	 * @SubscribeEvent public void apricorn(ApricornPlanted ev) { for (int x = -2; x
	 * <= 2; x++) { for (int y = -2; y <= 2; y++) { for (int z = -2; z <= 2; z++) {
	 * IBlockState blockState = ev.player.worldObj.getBlockState(new
	 * BlockPos(ev.pos.getX() + x, ev.pos.getY() + y, ev.pos.getZ() + z)); if
	 * (blockState.getBlock() instanceof BlockApricornTree) { ev.setCanceled(true);
	 * InstaPokemon.sendMessage(ev.player.getUniqueID(),
	 * "§cVocê não pode plantar apricorns perto de outras apricorns!"); return; } }
	 * } }
	 * 
	 * }
	 */

	HashSet<Integer> japrocessadas = new HashSet();

	@SubscribeEvent
	public void ganha(com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent event) {
		EntityPlayerMP p = event.player;

		if (japrocessadas.contains(event.battleController.battleIndex)) {
			return;
		}

		japrocessadas.add(event.battleController.battleIndex);

		int participando = event.battleController.participants.size();
		if (participando == 2 && event.battleController.isPvP()) {

			PlayerParticipant p1 = (PlayerParticipant) event.battleController.participants.get(0);
			PlayerParticipant p2 = (PlayerParticipant) event.battleController.participants.get(1);
			boolean draw = false;
			if (event.result == BattleResults.FLEE) {
				draw = true;
			}

			if (p2.isDefeated) {

				ganha(Sponge.getServer().getPlayer(p1.getEntity().getUniqueID()).orElse(null), Sponge.getServer().getPlayer(p2.getEntity().getUniqueID()).orElse(null), draw);
			} else if (p1.isDefeated) {
				ganha(Sponge.getServer().getPlayer(p2.getEntity().getUniqueID()).orElse(null), Sponge.getServer().getPlayer(p1.getEntity().getUniqueID()).orElse(null), draw);
			}
		}

	}

	private void ganha(Player ganhador, Player perdedor, boolean draw) {
		if (ganhador == null || perdedor == null) {
			return;
		}
		PlayerWinBattleEvent event = new PlayerWinBattleEvent(ganhador, perdedor, draw);
		Sponge.getEventManager().post(event);

	}

	@SubscribeEvent
	public void trade(com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent ev) {
		PixelmonData pk1 = new PixelmonData(ev.pokemon1);
		PixelmonData pk2 = new PixelmonData(ev.pokemon2);
		Player p1 = Sponge.getServer().getPlayer(ev.player1.getUniqueID()).get();

		Player p2 = Sponge.getServer().getPlayer(ev.player2.getUniqueID()).get();
		if (pk1.OT != null && pk1.OT.equalsIgnoreCase("CannotTrade")) {
			ev.setCanceled(true);
			InstaPokemon.sendMessage(p2, "§cO jogador está tentando trocar um pokemon que não pode ser trocado!");
			InstaPokemon.sendMessage(p1, "§cVocê não pode trocar este pokemon!");
			return;
		}
		if (pk2.OT != null && pk2.OT.equalsIgnoreCase("CannotTrade")) {
			ev.setCanceled(true);
			InstaPokemon.sendMessage(p1, "§cO jogador está tentando trocar um pokemon que não pode ser trocado!");
			InstaPokemon.sendMessage(p2, "§cVocê não pode trocar este pokemon!");
			return;
		}

		LogsDB.addLog(pk1.getSpecies().name + (pk1.isShiny ? "-s" : ""), pk2.getSpecies().name + (pk2.isShiny ? "-s" : ""), p1.getUniqueId(), p2.getUniqueId().toString(), PokeAction.TROCA);
		LogsDB.addLog(pk2.getSpecies().name + (pk2.isShiny ? "-s" : ""), pk1.getSpecies().name + (pk1.isShiny ? "-s" : ""), p2.getUniqueId(), p1.getUniqueId().toString(), PokeAction.TROCA);

	}

	@SubscribeEvent
	public void berry(BerryEvent.PickBerry ev) {
	
		Player p = Sponge.getServer().getPlayer(ev.player.getUniqueID()).orElse(null);
		Location<World> l = p.getWorld().getLocation(new Vector3i(ev.pos.getX(), ev.pos.getY(), ev.pos.getZ()));
		if (l != null && p != null) {
			if (br.instamc.protection.listeners.GeralListener.byPass(p)) {
				return;
			}
			if (ProtectionManager.getInfoAt(l).hasDono()) {
				if(!ProtectionManager.hasPermissionAt(p, l)) {
					ev.setCanceled(true);
					InstaPokemon.sendMessage(p, "Você não pode colher aqui!");
				}
				
			}
		}

	}
}
