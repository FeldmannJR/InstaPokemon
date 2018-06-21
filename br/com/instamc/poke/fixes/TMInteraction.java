package br.com.instamc.poke.fixes;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenReplaceMoveScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.database.DatabaseMoves;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.util.MoveCostList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TMInteraction implements IInteraction {

	@Override
	public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand arg2, ItemStack itemstack) {
		if (player instanceof EntityPlayerMP && itemstack.getItem() instanceof ItemTM) {
			if (player == entityPixelmon.getOwner() && !entityPixelmon.isInRanchBlock && DatabaseMoves.CanLearnAttack(entityPixelmon.baseStats.baseFormID, ((ItemTM) itemstack.getItem()).attackName)) {
				Attack a = DatabaseMoves.getAttack(((ItemTM) itemstack.getItem()).attackName);
				if (a == null) {
					ChatHandler.sendChat((Entity) entityPixelmon.getOwner(), ((ItemTM) itemstack.getItem()).attackName + " is corrupted", new Object[0]);
				} else
					if (entityPixelmon.getMoveset().size() >= 4) {
						for (int i = 0; i < 4; ++i) {
							if (!entityPixelmon.getMoveset().get((int) i).baseAttack.equals(a.baseAttack))
								continue;
							ChatHandler.sendChat((Entity) entityPixelmon.getOwner(), "pixelmon.interaction.tmknown", entityPixelmon.getNickname(), a.baseAttack.getLocalizedName());
							return true;
						}
						if (!(player.capabilities.isCreativeMode || PixelmonConfig.allowTMReuse)) {
							MoveCostList.addToList((EntityPlayerMP) player, new ItemStack(itemstack.getItem()));
						}
						Pixelmon.network.sendTo((IMessage) new OpenReplaceMoveScreen(entityPixelmon.getPokemonId(), a.baseAttack.attackIndex, 0, entityPixelmon.getLvl().getLevel()), (EntityPlayerMP) entityPixelmon.getOwner());
					} else {
						for (int i = 0; i < entityPixelmon.getMoveset().size(); ++i) {
							if (!entityPixelmon.getMoveset().get((int) i).baseAttack.equals(a.baseAttack))
								continue;
							ChatHandler.sendChat((Entity) entityPixelmon.getOwner(), "pixelmon.interaction.tmknown", entityPixelmon.getNickname(), a.baseAttack.getLocalizedName());
							return true;
						}
						entityPixelmon.getMoveset().add(a);
						ChatHandler.sendChat((Entity) entityPixelmon.getOwner(), "pixelmon.stats.learnedmove", entityPixelmon.getNickname(), a.baseAttack.getLocalizedName());
						if (!(player.capabilities.isCreativeMode || PixelmonConfig.allowTMReuse)) {
							player.inventory.clearMatchingItems(itemstack.getItem(), itemstack.getMetadata(), 1, itemstack.getTagCompound());
						}
					}
				entityPixelmon.update(EnumUpdateType.Moveset);
			} else {
				ChatHandler.sendChat((Entity) entityPixelmon.getOwner(), "pixelmon.interaction.tmcantlearn", new Object[] { entityPixelmon.getNickname(), new TextComponentTranslation("attack." + ((ItemTM) itemstack.getItem()).attackName.toLowerCase() + ".name", new Object[0]) });
			}
			return true;
		}
		return false;
	}

}
