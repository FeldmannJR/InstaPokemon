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
package br.com.instamc.poke.blockcrafting;

import com.pixelmonmod.pixelmon.blocks.PixelmonBlock;
import com.pixelmonmod.pixelmon.blocks.furniture.CushionChair;
import com.pixelmonmod.pixelmon.blocks.furniture.GymSignBlock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;
import com.pixelmonmod.pixelmon.items.PixelmonItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.data.ItemStackSerializer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 * Skype: junior.feldmann GitHub: https://github.com/feldmannjr Facebook:
 * https://www.facebook.com/carlosandre.feldmannjunior
 *
 * @author Feldmann
 */
public class BlockCrafting {

	public static BlockCrafting instance;

	public BlockCrafting() {
		instance = this;
		itens.clear();
		createTables();
		ComandoAPI.enable(new CmdBlockCrafting());
		reloadCraftings();

	}

	public void createTables() {
		try {
			Connection c = InstaPokemon.getDB();
			boolean insert = false;

			DatabaseMetaData dbm = c.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "blockcraft", null);
			if (!tables.next()) {
				insert = true;
			} else {
				ResultSet rs = c.createStatement().executeQuery("SELECT * FROM blockcraft");
				if (rs.next()) {
					List<ItemStack> i = ItemStackSerializer.deserializeListItemStack(rs.getString("itens"));
					for (ItemStack it : i) {
						itens.add(it);
					}
				}
			}
			c.createStatement().execute("CREATE TABLE IF NOT EXISTS blockcraft (id INTEGER PRIMARY KEY,itens TEXT)");
			if (insert) {
				addDefault();
				PreparedStatement pre = c.prepareStatement("INSERT INTO blockcraft (`id`,`itens`) VALUES(1,?)");
				pre.setString(1, ItemStackSerializer.serializeListItemStack(itens));
				pre.execute();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isBloqueado(ItemStack i) {
		if (i == null) {
			return false;
		}
		HashSet<String> nomes = new HashSet();
		for (ItemStack it : itens) {
			nomes.add(it.getItem().getId());
		}
		return nomes.contains(i.getItem().getId());

	}

	public void save() {
		Connection c = InstaPokemon.getDB();
		try {

			PreparedStatement pre;
			pre = c.prepareStatement("UPDATE blockcraft set itens = ?");
			pre.setString(1, ItemStackSerializer.serializeListItemStack(itens));
			pre.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addDefault() {

		add(PixelmonBlocks.elevator);
		add(PixelmonBlocks.mechanicalAnvil);
		add(PixelmonBlocks.movementPlate);
		add(PixelmonBlocks.stickPlate);
		add(PixelmonBlocks.timedFall);
		add(PixelmonBlocks.pc);
		add(PixelmonItems.itemFinder);
		add(PixelmonBlocks.fossilCleaner);
		add(PixelmonBlocks.fossilMachine);
		add(PixelmonBlocks.tradeMachine);
		add(PixelmonItems.rareCandy);
		add(PixelmonItems.cameraItem);
		add(PixelmonItems.filmItem);
		add(PixelmonItems.wailmerPail);
		add(PixelmonItems.oldRunningShoes);
		add(PixelmonItems.newRunningShoes);
		add(PixelmonItems.gift);
		add(PixelmonItems.meteorite);
		add(PixelmonItemsHeld.dubiousDisc);
		add(PixelmonItemsHeld.electirizer);
		add(PixelmonItemsHeld.everStone);
		add(PixelmonItemsHeld.kingsRock);
		add(PixelmonItemsHeld.magmarizer);
		add(PixelmonItemsHeld.metalCoat);
		add(PixelmonItemsHeld.protector);
		add(PixelmonItemsHeld.leftovers);
		add(PixelmonItemsHeld.upGrade);
		add(PixelmonBlocks.bolderBlock);
		add(PixelmonBlocks.boxBlock);
		add(PixelmonBlocks.blueClockBlock);
		add(PixelmonBlocks.pinkClockBlock);
		add(PixelmonBlocks.redCushionChairBlock);
		add(PixelmonBlocks.yellowCushionChairBlock);
		add(PixelmonBlocks.yellowRugBlock);
		add(PixelmonBlocks.blueRugBlock);
		add(PixelmonBlocks.greenRugBlock);
		add(PixelmonBlocks.redRugBlock);

		add(PixelmonBlocks.blueUmbrellaBlock);
		add(PixelmonBlocks.yellowUmbrellaBlock);
		add(PixelmonBlocks.redUmbrellaBlock);
		add(PixelmonBlocks.greenUmbrellaBlock);
		add(PixelmonItems.pixelmonPaintingItem);
		add(PixelmonBlocks.endTableBlock);
		add(PixelmonBlocks.treeBlock);
		add(PixelmonBlocks.chairBlock);
		add(PixelmonBlocks.greenFoldingChairBlock);
		add(PixelmonBlocks.clothedTableBlock);
		add(PixelmonBlocks.fossilDisplayBlock);
		add(PixelmonBlocks.fridgeBlock);
		add(PixelmonBlocks.trashcanBlock);
		add(PixelmonBlocks.tvBlock);
		add(PixelmonBlocks.bridgeBlockBlock);
		add(PixelmonBlocks.caveRockBlock);
		add(PixelmonBlocks.rockBlock);
		add(PixelmonBlocks.pokeMartSignBlock);
		add(PixelmonBlocks.pokeCenterSignBlock);
		add(PixelmonBlocks.gymSignBlock);
		add(PixelmonBlocks.picketFenceNormalBlock);
		add(PixelmonBlocks.window1Block);
		add(PixelmonBlocks.window2Block);
		add(PixelmonBlocks.pokeGrassBlock);
		add(PixelmonBlocks.pokeSandBlock);
		add(PixelmonBlocks.pokeSandCorner1Block);
		add(PixelmonBlocks.pokeSandCorner2Block);
		add(PixelmonBlocks.pokeSandCorner3Block);
		add(PixelmonBlocks.pokeSandCorner4Block);
		add(PixelmonBlocks.pokeSandSide1Block);
		add(PixelmonBlocks.pokeSandSide2Block);
		add(PixelmonBlocks.pokeSandSide3Block);
		add(PixelmonBlocks.pokeSandSide4Block);
		add(PixelmonBlocks.ruinsWallBlock);
		add(PixelmonBlocks.dustyRuinsWallBlock);
		add(PixelmonBlocks.sandyGrassBlock);
		add(PixelmonBlocks.shinglesBlock);
		add(PixelmonBlocks.shinglesCorner1Block);
		add(PixelmonBlocks.shinglesCorner1Block);
		add(PixelmonBlocks.treeBottomBlock);
		add(PixelmonBlocks.treeTopBlock);
		add(PixelmonBlocks.templeBlock);
		add(PixelmonBlocks.templeBrick);
		add(PixelmonBlocks.fancyPillar);
		add(PixelmonBlocks.blockBraille);
		add(PixelmonBlocks.blockBraille2);
		add(PixelmonBlocks.blockUnown);
		add(PixelmonBlocks.blockUnown2);

		add(PixelmonBlocks.blueWaterFloatBlock);
		add(PixelmonBlocks.yellowWaterFloatBlock);
		add(PixelmonBlocks.greenWaterFloatBlock);
		add(PixelmonBlocks.orangeWaterFloatBlock);
		add(PixelmonBlocks.pinkWaterFloatBlock);
		add(PixelmonBlocks.redWaterFloatBlock);
		add(PixelmonBlocks.purpleWaterFloatBlock);

		add(PixelmonBlocks.blueVendingMachineBlock);
		add(PixelmonBlocks.yellowVendingMachineBlock);
		add(PixelmonBlocks.greenVendingMachineBlock);
		add(PixelmonBlocks.orangeVendingMachineBlock);
		add(PixelmonBlocks.pinkVendingMachineBlock);
		add(PixelmonBlocks.redVendingMachineBlock);

		removeArmors();
		removeTools();
		add(Items.ITEM_FRAME);
	}

	HashSet<IRecipe> removidas = new HashSet();

	public void reloadCraftings() {

		for (IRecipe r : removidas) {
			CraftingManager.getInstance().getRecipeList().add(r);
		}
		removidas.clear();

		HashSet<String> nomes = new HashSet();
		for (ItemStack i : itens) {
			nomes.add(i.getItem().getId());
		}
		for (IRecipe recipe : new ArrayList<IRecipe>(CraftingManager.getInstance().getRecipeList())) {
			if (recipe.getRecipeOutput() != null) {

				Item output = recipe.getRecipeOutput().getItem();
				if (nomes.contains((output.getRegistryName().toString()))) {
					CraftingManager.getInstance().getRecipeList().remove(recipe);
					removidas.add(recipe);
				}

			}
		}

	}

	public void removeArmors() {
		add(PixelmonItemsTools.galacticHelm);
		add(PixelmonItemsTools.galacticPlate);
		add(PixelmonItemsTools.galacticLegs);
		add(PixelmonItemsTools.galacticBoots);
		add(PixelmonItemsTools.sapphireHelm);
		add(PixelmonItemsTools.sapphirePlate);
		add(PixelmonItemsTools.sapphireLegs);
		add(PixelmonItemsTools.sapphireBoots);
		add(PixelmonItemsTools.rubyHelm);
		add(PixelmonItemsTools.rubyPlate);
		add(PixelmonItemsTools.rubyLegs);
		add(PixelmonItemsTools.rubyBoots);
		add(PixelmonItemsTools.neoHelm);
		add(PixelmonItemsTools.neoPlate);
		add(PixelmonItemsTools.neoLegs);
		add(PixelmonItemsTools.neoBoots);
		add(PixelmonItemsTools.plasmaHelm);
		add(PixelmonItemsTools.plasmaPlate);
		add(PixelmonItemsTools.plasmaLegs);
		add(PixelmonItemsTools.plasmaBoots);
		add(PixelmonItemsTools.rocketHelm);
		add(PixelmonItemsTools.rocketPlate);
		add(PixelmonItemsTools.rocketLegs);
		add(PixelmonItemsTools.rocketBoots);
		add(PixelmonItemsTools.dawnstoneHelm);
		add(PixelmonItemsTools.dawnstonePlate);
		add(PixelmonItemsTools.dawnstoneLegs);
		add(PixelmonItemsTools.dawnstoneBoots);
		add(PixelmonItemsTools.duskstoneHelm);
		add(PixelmonItemsTools.duskstonePlate);
		add(PixelmonItemsTools.duskstoneLegs);
		add(PixelmonItemsTools.duskstoneBoots);
		add(PixelmonItemsTools.firestoneHelm);
		add(PixelmonItemsTools.firestonePlate);
		add(PixelmonItemsTools.firestoneLegs);
		add(PixelmonItemsTools.firestoneBoots);
		add(PixelmonItemsTools.leafstoneHelm);
		add(PixelmonItemsTools.leafstonePlate);
		add(PixelmonItemsTools.leafstoneLegs);
		add(PixelmonItemsTools.leafstoneBoots);
		add(PixelmonItemsTools.moonstoneHelm);
		add(PixelmonItemsTools.moonstonePlate);
		add(PixelmonItemsTools.moonstoneLegs);
		add(PixelmonItemsTools.moonstoneBoots);
		add(PixelmonItemsTools.sunstoneHelm);
		add(PixelmonItemsTools.sunstonePlate);
		add(PixelmonItemsTools.sunstoneLegs);
		add(PixelmonItemsTools.sunstoneBoots);
		add(PixelmonItemsTools.thunderstoneHelm);
		add(PixelmonItemsTools.thunderstonePlate);
		add(PixelmonItemsTools.thunderstoneLegs);
		add(PixelmonItemsTools.thunderstoneBoots);
		add(PixelmonItemsTools.waterstoneHelm);
		add(PixelmonItemsTools.waterstonePlate);
		add(PixelmonItemsTools.waterstoneLegs);
		add(PixelmonItemsTools.waterstoneBoots);
	}

	public void removeTools() {
		add(PixelmonItemsTools.dawnstoneSwordItem);
		add(PixelmonItemsTools.dawnstoneShovelItem);
		add(PixelmonItemsTools.dawnstoneAxeItem);
		add(PixelmonItemsTools.dawnstonePickaxeItem);
		add(PixelmonItemsTools.dawnstoneHoeItem);
		add(PixelmonItemsTools.dawnstoneHammerItem);
		add(PixelmonItemsTools.duskstoneSwordItem);
		add(PixelmonItemsTools.duskstoneShovelItem);
		add(PixelmonItemsTools.duskstoneAxeItem);
		add(PixelmonItemsTools.duskstonePickaxeItem);
		add(PixelmonItemsTools.duskstoneHoeItem);
		add(PixelmonItemsTools.duskstoneHammerItem);
		add(PixelmonItemsTools.firestoneSwordItem);
		add(PixelmonItemsTools.firestoneShovelItem);
		add(PixelmonItemsTools.firestoneAxeItem);
		add(PixelmonItemsTools.firestonePickaxeItem);
		add(PixelmonItemsTools.firestoneHoeItem);
		add(PixelmonItemsTools.firestoneHammerItem);
		add(PixelmonItemsTools.leafstoneSwordItem);
		add(PixelmonItemsTools.leafstoneShovelItem);
		add(PixelmonItemsTools.leafstoneAxeItem);
		add(PixelmonItemsTools.leafstonePickaxeItem);
		add(PixelmonItemsTools.leafstoneHoeItem);
		add(PixelmonItemsTools.leafstoneHammerItem);
		add(PixelmonItemsTools.moonstoneSwordItem);
		add(PixelmonItemsTools.moonstoneShovelItem);
		add(PixelmonItemsTools.moonstoneAxeItem);
		add(PixelmonItemsTools.moonstonePickaxeItem);
		add(PixelmonItemsTools.moonstoneHoeItem);
		add(PixelmonItemsTools.moonstoneHammerItem);
		add(PixelmonItemsTools.sunstoneSwordItem);
		add(PixelmonItemsTools.sunstoneShovelItem);
		add(PixelmonItemsTools.sunstoneAxeItem);
		add(PixelmonItemsTools.sunstonePickaxeItem);
		add(PixelmonItemsTools.sunstoneHoeItem);
		add(PixelmonItemsTools.sunstoneHammerItem);
		add(PixelmonItemsTools.thunderstoneSwordItem);
		add(PixelmonItemsTools.thunderstoneShovelItem);
		add(PixelmonItemsTools.thunderstoneAxeItem);
		add(PixelmonItemsTools.thunderstonePickaxeItem);
		add(PixelmonItemsTools.thunderstoneHoeItem);
		add(PixelmonItemsTools.thunderstoneHammerItem);
		add(PixelmonItemsTools.waterstoneSwordItem);
		add(PixelmonItemsTools.waterstoneShovelItem);
		add(PixelmonItemsTools.waterstoneAxeItem);
		add(PixelmonItemsTools.waterstonePickaxeItem);
		add(PixelmonItemsTools.waterstoneHoeItem);
		add(PixelmonItemsTools.waterstoneHammerItem);
	}

	List<ItemStack> itens = new ArrayList<ItemStack>();

	public void add(Item a) {
		if (a != null) {
		
			itens.add(PixelmonUtils.getItemStack(a));
		}
	}

	public void add(Block b) {
		if (b != null) {
			itens.add(PixelmonUtils.getItemStack(b));

		}
	}

}
