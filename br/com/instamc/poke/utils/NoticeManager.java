package br.com.instamc.poke.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.GraphicDisplayTypes;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomNoticePacket;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import net.minecraft.entity.player.EntityPlayerMP;

public class NoticeManager {

	private static HashMap<UUID,CustomNoticePacket> messages = new HashMap(); 
	
	public static void sendNotice(Player p, EnumPokemon icone, String... lines) {
		ArrayList<String> list = new ArrayList();
		for (String s : lines) {
			list.add(s);
		}
		CustomNoticePacket cu = new CustomNoticePacket(list);
		cu.setPokemonSprite(icone, GraphicDisplayTypes.LeftRight);
		messages.put(p.getUniqueId(), cu);
		Pixelmon.network.sendTo(cu, (EntityPlayerMP) p);
	}

	public static void removeNotice(Player p) {
		CustomNoticePacket cu = new CustomNoticePacket();
		cu.setEnabled(false);
		Pixelmon.network.sendTo(cu, (EntityPlayerMP) p);
		messages.remove(cu);
	}

}
