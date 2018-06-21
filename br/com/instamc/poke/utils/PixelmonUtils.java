/*
 *  _   __   _   _____   _____       ___       ___  ___   _____  
 * | | |  \ | | /  ___/ |_   _|     /   |     /   |/   | /  ___| 
 * | | |   \| | | |___    | |      / /| |    / /|   /| | | |     
 * | | | |\   | \___  \   | |     / / | |   / / |__/ | | | |     
 * | | | | \  |  ___| |   | |    / /  | |  / /       | | | |___  
 * |_| |_|  \_| /_____/   |_|   /_/   |_| /_/        |_| \_____| 
 * 
 * Projeto feito por Carlos Andre Feldmann Junior, Isaias Finger e Gabriel Augusto Souza
 */
package br.com.instamc.poke.utils;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pcClientStorage.PCAdd;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.ComputerBox;

import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerComputerStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import com.pixelmonmod.pixelmon.util.helpers.SpriteHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 * Skype: junior.feldmann GitHub: https://github.com/feldmannjr Facebook:
 * https://www.facebook.com/carlosandre.feldmannjunior
 *
 * @author Feldmann
 */
public class PixelmonUtils {

	public static void give(Player player, EntityPixelmon poke) {
		poke.friendship.initFromCapture();

		Optional<PlayerStorage> optstorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player);
		if (optstorage.isPresent()) {
			PlayerStorage storage = optstorage.get();
			storage.addToParty(poke);

			PixelmonAchievements.pokedexChieves((EntityPlayerMP) player);
			Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP) player, ReceiveType.Command, poke));
		}

	}

	public static ItemStack getItemStack(Item it) {
		final net.minecraft.item.ItemStack itemStack = new net.minecraft.item.ItemStack(it);
		ItemStack na = ItemStackUtil.fromNative(itemStack);

		return na;
	}
	public static boolean isLegendery(EnumPokemon en) {
		List<String> bugados =new ArrayList();
		bugados.add(EnumPokemon.Rotom.name);
		bugados.add(EnumPokemon.TapuBulu.name);
		bugados.add(EnumPokemon.TapuFini.name);
		bugados.add(EnumPokemon.TapuKoko.name);
		bugados.add(EnumPokemon.TapuLele.name);
		
		
		return EnumPokemon.legendaries.contains(en.name)||bugados.contains(en.name);
	}

	public static PixelmonData getPokemon(Player p,int slot){
		NBTTagCompound tag = getParty(p)[slot];
		if(tag!=null){
			return new PixelmonData(tag);
			
			}
		return null;
	}
	
	   public static int getGeneration(int dexNumber) {
	        if (dexNumber <= 151) {
	            return 1;
	        }
	        if (dexNumber <= 251) {
	            return 2;
	        }
	        if (dexNumber <= 386) {
	            return 3;
	        }
	        if (dexNumber <= 493) {
	            return 4;
	        }
	        if (dexNumber <= 649) {
	            return 5;
	        }
	        if (dexNumber <= 721) {
	            return 6;
	        }
	        return -1;
	    }

	public static boolean hasPokemonsOutside(Player p) {
		PlayerStorage sto = getPlayerStorage(p);
		  for (NBTTagCompound aPartyPokemon : sto.partyPokemon) {
	            Optional<EntityPixelmon> poke;
	            int[] id;
	            if (aPartyPokemon == null || !(sto.getAlreadyExists(id = PixelmonMethods.getID(aPartyPokemon), sto.getPlayer().worldObj)).isPresent()) return true;
	      
	        }
		return false;

	}

	public static Player getPlayer(EntityPlayerMP mp) {
		return Sponge.getServer().getPlayer(mp.getUniqueID()).orElse(null);
	}

	public static BlockType getBlockType(String nome) {
		Optional<BlockType> type = Sponge.getRegistry().getType(BlockType.class, nome);
		if (type.isPresent()) {
			return type.get();
		}
		return null;
	}

	public static ItemStack getItemStack(Block b) {
		final net.minecraft.item.ItemStack itemStack = new net.minecraft.item.ItemStack(b);
		return ItemStackUtil.fromNative(itemStack);

	}

	public static void giveBracelet(Player p) {
		PlayerStorage stora = PixelmonUtils.getPlayerStorage(p);
		stora.megaData.setMegaItem(EnumMegaItem.BraceletORAS, false);
		stora.megaData.obtainedItem(EnumPokemon.Blastoise, 1, (EntityPlayerMP) p);
	}

	public static boolean isInBattle(Player p) {
		if (BattleRegistry.getBattle((EntityPlayerMP) p) != null) {
			BattleControllerBase bat = BattleRegistry.getBattle((EntityPlayerMP) p);
			if (bat.battleEnded) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static ItemStack getPokeball(EnumPokeballs en) {

		Optional<ItemType> op = Sponge.getRegistry().getType(ItemType.class, "pixelmon:" + en.getFilenamePrefix());

		if (op.isPresent()) {

			ItemStack i = ItemStack.of(op.get(), 1);

			return i;

		}
		return ItemStack.of(ItemTypes.BIRCH_BOAT, 1);

	}

	public static ItemStack getBadge(EnumBadges ba) {
		return ItemStack.of(getItemType("pixelmon:" + ba.getFileName()), 1);
	}

	public static ItemType getItemType(String oq) {
		Optional<ItemType> type = Sponge.getRegistry().getType(ItemType.class, oq);

		if (type.isPresent()) {
			return type.get();

		}
		return ItemTypes.DIRT;
	}

	public static PlayerStorage getPlayerStorage(Player p) {
		Optional<PlayerStorage> optstorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) p);
		if (optstorage.isPresent()) {
			return optstorage.get();
		}
		return null;
	}

	public static NBTTagCompound[] getParty(Player p) {
		Optional<PlayerStorage> optstorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) p);
		NBTTagCompound[] pokes = new NBTTagCompound[6];
		if (optstorage.isPresent()) {
			PlayerStorage storage = optstorage.get();

			for (int i = 0; i < storage.partyPokemon.length; ++i) {

				if (storage.partyPokemon[i] != null) {
					pokes[i] = storage.partyPokemon[i];
				} else {
					pokes[i] = null;
				}
			}

		}
		return pokes;
	}

	public static EntityPixelmon build(Player player, EnumPokemon poke, EnumPokeballs bola, int lvl, boolean isshiny) {
		EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(poke.name, (World) player.getWorld());
		pokemon.caughtBall = bola;

		pokemon.getLvl().setLevel(lvl);
		pokemon.friendship.initFromCapture();
		pokemon.stats.IVs = IVStore.CreateNewIVs();
		pokemon.updateStats();

		pokemon.setIsShiny(isshiny);
		return pokemon;
	}

	public static ItemStack getItemStack(Item it, int qtd) {
		ItemStack ia = getItemStack(it);
		ia.setQuantity(qtd);
		return ia;
	}

	public static ComputerBox getBox(Player p,int box){
		PlayerComputerStorage s = PixelmonStorage.computerManager.getPlayerStorage((EntityPlayerMP) p);
		if(box<0)return null;
		if(box>=PlayerComputerStorage.boxCount){
			return null;
		}
		return s.getBox(box);
	}
	
	public static int getPokemonCount(Player p, boolean counteggs) {
		int x = 0;
		PlayerComputerStorage s = PixelmonStorage.computerManager.getPlayerStorage((EntityPlayerMP) p);

		for (ComputerBox b : s.getBoxList()) {
			for (NBTTagCompound n : b.getStoredPokemon()) {
				if (n == null) {
					continue;
				}
				if (!counteggs) {
					PixelmonData d = new PixelmonData(n);
					if (d.isEgg) {
						continue;
					}
				}
				x++;
			}
		}
		for (NBTTagCompound d : getParty(p)) {
			if (d != null) {
				if (!counteggs) {
					PixelmonData po = new PixelmonData(d);
					if (po.isEgg) {
						continue;
					}
				}
				x++;
			}
		}
		return x;
	}

	public static ItemStack getEgg() {
		Optional<ItemType> op = Sponge.getRegistry().getType(ItemType.class, spriteitem);
		if (op.isPresent()) {
			String filePath = "pixelmon:sprites/eggs/egg1";

			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("SpriteName", filePath);

			ItemStack i = ItemStack.of(op.get(), 1);

			final net.minecraft.item.ItemStack itemStack = ItemStackUtil.toNative(i);
			itemStack.setTagCompound(nbt);

			i = ItemStackUtil.fromNative(itemStack);
			ItemUtils.setItemName(i, Txt.f("§fOvo"));
			return i;
		}

		return ItemStack.of(ItemTypes.BIRCH_BOAT, 1);

	}

	public static String spriteitem = "pixelmon:pixelmon_sprite";

	public static ItemStack getPixelmonIcon(EnumPokemon poke, boolean shiny) {
		return getPixelmonIcon(poke, shiny, 0);
	}

	public static ItemStack getPixelmonIcon(EnumPokemon poke, boolean shiny, int form) {
		Optional<ItemType> op = Sponge.getRegistry().getType(ItemType.class, spriteitem);
		final Optional<BaseStats> stats = Entity3HasStats.getBaseStats(poke.name);
		if (op.isPresent()) {

			String shin = "";
			if (shiny) {
				shin = "shiny";
			}
			String filePath = "pixelmon:sprites/" + shin + "pokemon/" + String.format("%03d", stats.get().nationalPokedexNumber) + SpriteHelper.getSpriteExtra(poke.name, form);

			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("SpriteName", filePath);

			ItemStack i = ItemStack.of(op.get(), 1);

			final net.minecraft.item.ItemStack itemStack = ItemStackUtil.toNative(i);
			itemStack.setTagCompound(nbt);

			i = ItemStackUtil.fromNative(itemStack);
			ItemUtils.setItemName(i, Text.builder(poke.name).color(TextColors.GOLD).build());
			return i;

		}
		return ItemStack.of(ItemTypes.BIRCH_BOAT, 1);
	}
}
