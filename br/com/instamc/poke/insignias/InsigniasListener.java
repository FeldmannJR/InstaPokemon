/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.poke.insignias;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.chaves.ChavesDB;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.library.utils.Cooldown;
import br.com.instamc.sponge.library.utils.Txt;
import br.instamc.protection.SpongeProtection;
import br.instamc.protection.managers.ProtectionManager;
import net.minecraft.entity.EntityLivingBase;

import scala.util.Random;

import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import com.pixelmonmod.pixelmon.items.ItemBadge;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 *
 * @author Carlos
 */
public class InsigniasListener {

	@Listener
	public void interact(InteractEvent ev, @First Player p) {
		if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
			ItemStack item = p.getItemInHand(HandTypes.MAIN_HAND).get();

			if (ItemStackUtil.toNative(item).getItem() instanceof ItemBadge) {
				EnumBadges bad = ((ItemBadge) ItemStackUtil.toNative(item).getItem()).badge;
				if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(bad)) {
					InsigniaDB.addInsignia(p, bad);

				}
				p.setItemInHand(HandTypes.MAIN_HAND, null);
			}

		}

	}

	@Listener
	public void interactEntity(InteractEntityEvent.Secondary.MainHand ev, @First Player p) {
		Entity target = ev.getTargetEntity();
		if (target instanceof EntityPixelmon) {
			EntityPixelmon et = (EntityPixelmon) target;
			EntityLivingBase owner = et.getOwner();

			if (owner != null) {
				if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {

					if (InsigniaDB.getInsignias(p.getUniqueId()).contains(EnumBadges.JetBadge)) {

						if (owner.getUniqueID().toString().equals(p.getUniqueId().toString())) {

							if (SpongeGuard.getManager().getManager(p.getWorld()).getFlag(EnumFlags.JET, p.getWorld().getLocation(et.getPositionVector().xCoord, et.getPositionVector().yCoord, et.getPositionVector().zCoord)) != FlagValue.DENY) {
								if (!et.isInRanchBlock) {
									p.setVehicle(target);
								} else {
									InstaPokemon.sendMessage(p, "§cVocê não pode montar em pokemons da Ranch Block!");
								}
							} else {
								p.sendMessage(Txt.f("§cJet bloqueada nesta região!"));
							}

						} else {
							InstaPokemon.sendMessage(p, "§eVocê não pode montar no pokemon dos outros!");
						}
					} else {
						InstaPokemon.sendMessage(p, "§eVocê precisa da insignia jet para montar nos pokemons!");
					}
				} else {
					if (!Cooldown.isCooldown(p, "msgjet")) {
						Cooldown.addCoolDown(p, "msgjet", 60 * 1000 * 60);
						InstaPokemon.sendMessage(p, "§bCaso você queira montar no pokemon, tire o item da sua mão!");
					}
				}
			}

		}

	}

	public boolean isSame(Vector3d pos1, Vector3d pos2) {

		if (pos1.getFloorX() == pos2.getFloorX() && pos1.getFloorY() == pos2.getFloorY() && pos1.getFloorZ() == pos2.getFloorZ()) {
			return true;
		}
		return false;
	}

	@Listener
	public void move(MoveEntityEvent ev, @First Player p) {
		if (!isSame(ev.getFromTransform().getPosition(), ev.getToTransform().getPosition())) {
			BlockState block = ev.getToTransform().getLocation().getRelative(Direction.DOWN).getBlock();
			if (block.getType() == BlockTypes.DIAMOND_BLOCK) {

				Location bau = ev.getToTransform().getLocation().getRelative(Direction.DOWN).getRelative(Direction.DOWN);
				if (bau.getTileEntity().isPresent() && bau.getTileEntity().get() instanceof Chest) {

					Chest c = (Chest) bau.getTileEntity().get();
					Optional<ItemStack> item = c.getInventory().query(new SlotPos(0, 0)).peek();

					if (item.isPresent()) {
						if (ItemStackUtil.toNative(item.get()).getItem() instanceof ItemBadge) {
							EnumBadges bad = ((ItemBadge) ItemStackUtil.toNative(item.get()).getItem()).badge;
							if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(bad)) {
								ev.setCancelled(true);
								InstaPokemon.sendMessage(p, "§eVocê precisa da insignia §c" + bad.name().replace("Badge", "") + " §epara entrar aqui!");

							}
						}

					}
				}
			}

		}

	}

	List<BiomeType> invalidBiome = Arrays.asList(BiomeTypes.DEEP_OCEAN, BiomeTypes.OCEAN, BiomeTypes.FROZEN_OCEAN);

	public boolean tp(Player p) {
		int x = new Random().nextInt(20000) - 10000;
		int z = new Random().nextInt(20000) - 10000;
		Location<World> highestBlock = getHighestBlock(p.getWorld(), x, z);
		highestBlock = highestBlock.add(new Vector3d(0.5, 1.5, 0.5));
		if (invalidBiome.contains(highestBlock.getBiome())) {
			return false;
		}
		if (ProtectionManager.getInfoAt(highestBlock).hasDono()) {
			return false;
		}
		p.getWorld().playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, p.getLocation().getPosition(), 1);
		p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.ENDER_TELEPORT).quantity(1).build(), p.getLocation().getPosition());
		p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.PORTAL).quantity(10).offset(new Vector3d(0.2, 0.5, 0.2)).build(), p.getLocation().getPosition());

		p.setLocation(highestBlock);
		p.getWorld().playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, p.getLocation().getPosition(), 1);
		p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.ENDER_TELEPORT).quantity(1).build(), p.getLocation().getPosition());
		p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.PORTAL).quantity(10).offset(new Vector3d(0.2, 0.5, 0.2)).build(), p.getLocation().getPosition());

		return true;
	}

	public void randomTp(Player p) {
		if (Cooldown.isCooldown(p, "userandomtp")) {
			if (!Cooldown.isCooldown(p, "sendmsgrandom")) {
				Cooldown.addCoolDown(p,"sendmsgrandom", 2000);
				p.sendMessage(Txt.f("§cEspere para usar o teleporte novamente!"));
			}

			return;
		}
		for (int x = 0; x < 10; x++) {
			if (tp(p)) {
				InstaPokemon.sendMessage(p, Txt.f("§a§lTeleportado! §6Use §e/terreno §6para comprar um!"));
				Cooldown.addCoolDown(p, "userandomtp", 2 * 60 * 1000);

				return;
			}
		}
		Cooldown.addCoolDown(p, "userandomtp", 10000);
		Cooldown.addCoolDown(p,"sendmsgrandom", 2000);
		p.sendMessage(Txt.f("§cTeleporte falhou tente novamente!"));

	}

	public static Location<World> getHighestBlock(World w, int x, int z) {
		for (int y = 255; y > 0; y--) {
			BlockState b = w.getBlock(x, y, z);
			if (w.getBlock(x, y, z) != null && w.getBlock(x, y, z).getType() != BlockTypes.AIR) {
				BlockType t = b.getType();
				if (w.getBlock(x, y, z).getType() != BlockTypes.TALLGRASS && t != BlockTypes.DOUBLE_PLANT && t != BlockTypes.RED_FLOWER && t != BlockTypes.DEADBUSH && t != BlockTypes.YELLOW_FLOWER) {
					return new Location<World>(w, x, y + 1, z);
				}
			}
		}
		return null;
	}

	@Listener
	public void movePortal(MoveEntityEvent ev, @First Player p) {
		if (!isSame(ev.getFromTransform().getPosition(), ev.getToTransform().getPosition())) {
			BlockState block = ev.getToTransform().getLocation().getBlock();
			if (block.getType() == PixelmonUtils.getBlockType("pixelmon:warp_plate") || block.getType() == PixelmonUtils.getBlockType("pixelmon:hidden_pressure_plate")) {
				boolean effects = true;
				if (block.getType() == PixelmonUtils.getBlockType("pixelmon:hidden_pressure_plate")) {
					effects = false;
				}

				Location bau = ev.getToTransform().getLocation().getRelative(Direction.DOWN).getRelative(Direction.DOWN);
				if (bau.getTileEntity().isPresent() && bau.getTileEntity().get() instanceof Sign) {

					Sign c = (Sign) bau.getTileEntity().get();
					SignData li = c.getSignData();
					if (li.get(0).isPresent() && li.get(1).isPresent() && li.get(2).isPresent()) {
						if (li.get(0).get().toPlain().equalsIgnoreCase("[instamc]")) {
							String mundo = li.get(1).get().toPlain();
							if (mundo.equalsIgnoreCase("randomtp")) {
								randomTp(p);
								return;
							}

							String[] loc = li.get(2).get().toPlain().split(",");
							if (loc.length == 3) {
								try {
									int x = Integer.valueOf(loc[0]);
									int y = Integer.valueOf(loc[1]);
									int z = Integer.valueOf(loc[2]);
									World w = null;
									if (mundo.isEmpty()) {
										w = p.getWorld();
									} else {
										w = Sponge.getServer().getWorld(mundo).orElse(null);
									}
									EnumBadges badge = null;
									String chave = null;
									if (li.get(3).isPresent()) {
										if (li.get(3).get().toPlain().startsWith("c:")) {
											chave = li.get(3).get().toPlain().split(":")[1];
											chave = chave.trim();
											if (chave.isEmpty()) {
												chave = null;
											}
										} else {
											String a = li.get(3).get().toPlain() + "badge";
											for (EnumBadges bad : EnumBadges.values()) {
												if (bad.name().equalsIgnoreCase(a)) {
													badge = bad;
												}
											}
										}
									}
									if (w != null) {
										if (Cooldown.isCooldown(p, "usewarp")) {

											return;
										}
										Cooldown.addCoolDown(p, "usewarp", 2000);
										if (chave != null) {
											if (!ChavesDB.getChaves(p.getUniqueId()).contains(chave)) {
												InstaPokemon.sendMessage(p, "§eVocê não tem os requisitos para entrar neste portal!");

												return;
											}
										}
										if (badge != null) {
											if (!InsigniaDB.getInsignias(p.getUniqueId()).contains(badge)) {
												InstaPokemon.sendMessage(p, "§eVocê precisa da insignia §c" + badge.name().replace("Badge", "") + " §epara usar o portal!");

												return;
											}
										}

										// TELEPORT
										Location<World> lo = w.getLocation(x + 0.5, y, z + 0.5);

										if (effects) {
											p.getWorld().playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, p.getLocation().getPosition(), 1);
											p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.ENDER_TELEPORT).quantity(1).build(), p.getLocation().getPosition());
											p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.PORTAL).quantity(10).offset(new Vector3d(0.2, 0.5, 0.2)).build(), p.getLocation().getPosition());
										}
										p.setLocation(lo);
										if (effects) {
											p.getWorld().playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, p.getLocation().getPosition(), 1);
											p.getWorld().spawnParticles(ParticleEffect.builder().type(ParticleTypes.ENDER_TELEPORT).quantity(1).build(), p.getLocation().getPosition());
										}

									}

								} catch (NumberFormatException ex) {

								}

							}

						}
					}
				}
			}

		}

	}

}
