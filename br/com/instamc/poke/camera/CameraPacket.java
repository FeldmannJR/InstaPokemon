package br.com.instamc.poke.camera;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.comm.packetHandlers.camera.ItemCameraPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.util.helpers.SpriteHelper;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.camera.Foto.Forma;
import br.com.instamc.poke.utils.InvUtils;
import br.com.instamc.poke.utils.PixelmonUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CameraPacket {
	public static class Handler implements IMessageHandler<ItemCameraPacket, IMessage> {
		public IMessage onMessage(ItemCameraPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			Player p = Sponge.getServer().getPlayer(player.getUniqueID()).orElse(null);

			EntityPixelmon pixelmon = (EntityPixelmon) ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);

			if (pixelmon != null) {
				if (InvUtils.tem(p, PixelmonUtils.getItemStack(PixelmonItems.filmItem)) >= 1) {

					Foto f = Foto.extractFromEntity(pixelmon);
					if (f == null) {
						return null;
					}
					if (CameraDB.getFotos(p.getUniqueId()).fotos.contains(f)) {
						InstaPokemon.sendMessage(p, "§aVocê já tem a foto deste pokemon!");
						return null;
					}
					CameraDB.addFoto(p.getUniqueId(), f);

					InstaPokemon.sendMessage(p, "§fVocê fotografou §a" + f.poke.name + " §fe colou a foto em seu §a/album !");
					pixelmon.worldObj.playSound(null, player.posX, player.posY, player.posZ, PixelSounds.cameraShutter, SoundCategory.AMBIENT, 1.0f, 1.0f);
					InvUtils.remove(p, PixelmonUtils.getItemStack(PixelmonItems.filmItem), 1);
					int tem = CameraDB.getFotos(p.getUniqueId()).fotos.size();
					if (tem == 10 || tem == 50 || tem % 100 == 0) {
						InstaPokemon.broadcast("§fO jogador §b" + p.getName() + "§f agora tem §6" + tem + " §ffotos em seu §a/album §f!");

					}

				} else {
					p.playSound(SoundTypes.BLOCK_FIRE_EXTINGUISH, p.getLocation().getPosition(), 1, 1);
					InstaPokemon.sendMessage(player, "§cVocê precisa de filme para camera!");

				}
			}
			return null;
		}
	}
}
