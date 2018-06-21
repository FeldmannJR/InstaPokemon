package br.com.instamc.poke.tpscroll;

import java.util.Date;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;

public class TPScrollInfo {

	public Date acaba;
	public double x;
	public double y;
	public double z;
	public String mundo;
	public String nome;
	public String regiao;
	
	boolean acabouagora = false;

	public Location<World> getLocation() {
		World w = Sponge.getServer().getWorld(mundo).orElse(null);
		if (w == null)
			return null;
		return w.getLocation(new Vector3d(x, y, z));
	}

	public boolean isValid() {
		return acaba.after(new Date(System.currentTimeMillis()));

	}

	public String getNome() {
		return nome;
	}

	public String getResta() {
		long dif = acaba.getTime() - System.currentTimeMillis();

		long segundos = dif / 1000;
		long minutos = segundos / 60;
		segundos = segundos % 60;

		return minutos + " minutos e " + segundos + " segundos";

	}

	public ItemStack toItemStack() {
		if (isValid()) {
			return ItemStackBuilder.of(PixelmonUtils.getItemType("pixelmon:warp_plate")).withName("§a§l" + nome).withLore("§eResta: §6" + getResta()).build();
		} else {
			return ItemStackBuilder.of(ItemTypes.BARRIER).withName("§c§l" + nome).withName("§cExpirado!").build();

		}
	}

}
