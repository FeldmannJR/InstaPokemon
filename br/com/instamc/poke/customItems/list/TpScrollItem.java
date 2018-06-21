package br.com.instamc.poke.customItems.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.CustomItem;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.poke.tpscroll.TPScrollInfo;
import br.com.instamc.poke.tpscroll.TPScrollManager;
import br.com.instamc.poke.tpscroll.menu.ScrollMenu;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.guard.utils.Utils;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.MathUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.library.utils.data.LocationSerializer;
import br.com.instamc.sponge.vip.SpongeVIP;
import net.minecraft.item.Item;

public class TpScrollItem extends CustomItem {

	public TpScrollItem() {
		super("Scroll de Teleporte", ItemStackBuilder.of(ItemTypes.ENCHANTED_BOOK).build(), "Ao usar ganha tempo de teleporte", "use /tps !");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Player player, InteractType type, ClickInteract click) {

		TPScrollInfo info = fromItemStack(player.getItemInHand(HandTypes.MAIN_HAND).orElse(null));
		if (info == null) {
			InstaPokemon.sendMessage(player, "§4Item inválido!");
			return;
		}
		if (TPScrollManager.has(player.getUniqueId(), info.getNome())) {
			InstaPokemon.sendMessage(player, "§cVocê ainda possuí tempo deste pergaminho, espere acabar para usar outro!");
			return;
		}
		TPScrollManager.addScroll(player.getUniqueId(), info);
		InstaPokemon.sendMessage(player, "§aPara abrir novamente use /tps!");
		consomeMain(player);
		new ScrollMenu(player).open(player);

	}



	public static TPScrollInfo fromItemStack(ItemStack it) {
		if (it == null) {
			return null;
		}

		Location<World> loc = null;
		String nome = null;
		int minutos = -1;
		String regiao = null;

		for (Text t : it.get(Keys.ITEM_LORE).orElse(new ArrayList<>())) {
			String plain = t.toPlain();
			if (plain.contains(":")) {
				String[] spl = plain.split(":");
				if (spl.length == 2) {
					String key = spl[0];
					String value = spl[1];
					if (key.equalsIgnoreCase("loc")) {
						loc = LocationSerializer.deserializeLocation(value);
					}
					if (key.equalsIgnoreCase("nome")) {
						nome = value;
					}
					if (key.equalsIgnoreCase("regiao")) {
						regiao = value;
					}
					if (key.equalsIgnoreCase("min")) {
						try {
							minutos = Integer.valueOf(value);
						} catch (NumberFormatException ex) {

						}
					}
				}
			}
		}

		if (loc == null || nome == null || minutos < 0 || regiao == null) {
			return null;
		}
		TPScrollInfo info = new TPScrollInfo();
		info.mundo = loc.getExtent().getName();
		info.x = loc.getX();
		info.y = loc.getY();
		info.z = loc.getZ();
		info.nome = nome;
		info.regiao = regiao;
		info.acaba = new Date(System.currentTimeMillis() + (1000L * 60L * minutos));
		return info;
	}

	public static ItemStack generate(String nome, String regiao, Location<World> pos, int minutos) {
		ItemStack it = ItemStackBuilder.of(ItemTypes.ENCHANTED_BOOK).withName("§e§lTPS: §f" + nome).build();
		ItemUtils.addLore(it, Txt.f("§6§lTempo: §f"+minutos+" minutos"));
		Location<World> l = new Location<World>(pos.getExtent(),MathUtils.arredondar(pos.getX(),1),MathUtils.arredondar(pos.getY(), 1),MathUtils.arredondar(pos.getZ(), 1));
		
		ItemUtils.addLore(it, Txt.f("§0§kLoc:" + LocationSerializer.serializeLocation(l)));
		ItemUtils.addLore(it, Txt.f("§0§kMin:" + minutos));
		ItemUtils.addLore(it, Txt.f("§0§kNome:" + nome));
		ItemUtils.addLore(it, Txt.f("§0§kRegiao:" + regiao));

		ItemUtils.addLore(it, EnumCustomItems.TPScroll.getItem().getLoreIdentifier());
		return it;
	}

}
