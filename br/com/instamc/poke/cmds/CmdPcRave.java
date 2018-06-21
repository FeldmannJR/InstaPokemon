package br.com.instamc.poke.cmds;

import java.util.Optional;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPC;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;

import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CmdPcRave extends ComandoAPI {

	public CmdPcRave() {
		super(CommandType.PERMISSION, "pcrave");
		permission = "instamc.pokemon.pcrave";

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource arg0, String[] arg1) {
		Player p = (Player) arg0;
	BlockRay<World> blockRay = BlockRay.from(p)
				.stopFilter(e -> e.getExtent().getBlockType(e.getBlockPosition()) != BlockTypes.AIR).distanceLimit(8).build();
				

		if (!blockRay.hasNext()) {
			p.sendMessage(Txt.f("§c§lOlhe para um PC para usar o comando!!"));
			return;
		}
		BlockRayHit<World> hit = blockRay.next();
		Location<World> location = hit.getLocation();
		net.minecraft.world.World w = (net.minecraft.world.World) location.getExtent();
		Vector3i blockPosition = hit.getBlockPosition();
		
		TileEntity entity = w
				.getTileEntity(new BlockPos(blockPosition.getX(),blockPosition.getY(),blockPosition.getZ()));
		p.sendMessage(Txt.f(hit.getExtent().getBlock(blockPosition).getId()));
		if (entity == null || !(entity instanceof TileEntityPC)) {
			entity = w.getTileEntity(new BlockPos(blockPosition.getX(),blockPosition.getY()-1,blockPosition.getZ()));
		}
		if (entity == null || !(entity instanceof TileEntityPC)) {
			p.sendMessage(Txt.f("§c§lOlhe para um PC para usar o comando!!"));
			return;
		}
		TileEntityPC pc = (TileEntityPC) entity;
		pc.setRave(!pc.getRave());
		p.sendMessage(Txt.f("§a§lSetado RAVE!"));
	}

}
