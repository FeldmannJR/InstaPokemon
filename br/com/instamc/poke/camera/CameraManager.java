package br.com.instamc.poke.camera;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.camera.ItemCameraPacket;

import net.minecraftforge.fml.relauncher.Side;

public class CameraManager {

	public static void init() {
		Pixelmon.network.registerMessage(CameraPacket.Handler.class, ItemCameraPacket.class, 500, Side.SERVER);
		CameraDB.init();
	}

}
