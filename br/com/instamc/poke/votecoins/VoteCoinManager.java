package br.com.instamc.poke.votecoins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import com.pixelmonmod.pixelmon.config.PixelmonItems;

import br.com.instamc.poke.InstaPokemon;
import br.com.instamc.poke.customItems.EnumCustomItems;
import br.com.instamc.poke.placas.BlockClickSignEvent;
import br.com.instamc.poke.utils.PixelmonUtils;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.menus.venda.VendaMenu;
import br.com.instamc.sponge.library.menu.menus.venda.moedas.MoedaType;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;

public class VoteCoinManager {

	private static List<VoteCoinItem> itens = new ArrayList();

	public static void init() {
		itens.add(new VoteCoinItem("Filme", 1, PixelmonUtils.getItemStack(PixelmonItems.filmItem)));
		itens.add(new VoteCoinItem("Rare Candy", 3, PixelmonUtils.getItemStack(PixelmonItems.rareCandy)));
		itens.add(new VoteCoinItem("Revive", 4, PixelmonUtils.getItemStack(PixelmonItems.revive)));
		itens.add(new VoteCoinItem("Pokemon Aleatório", 18, EnumCustomItems.PokemonAleatorio.getItem().toItemStack()));
		itens.add(new VoteCoinItem("Bota de Corrida Velha", 25, PixelmonUtils.getItemStack(PixelmonItems.oldRunningShoes)));
		itens.add(new VoteCoinItem("Lucky Block", 27, EnumCustomItems.LuckyBlock.getItem().toItemStack()));

	}

	@Listener
	public void click(BlockClickSignEvent ev) {
		if (ev.getLinhas().get(0).equalsIgnoreCase("votecoins")) {
			final Player p = ev.getTargetEntity();
			SchedulerUtils.runSync(new Runnable() {

				@Override
				public void run() {
					openMenu(p);
				}
			}, 2);
		}
	}

	public static void openMenu(Player p) {
		Menu m = new Menu("VoteCoins SHOP", 1);
		m.addButton(new MenuButton(new SlotPos(8,0),(ItemStackBuilder.of(ItemTypes.REPEATER).withName("§eFechar").build())) {
			
			@Override
			public void click(Player arg0, Menu arg1, ClickType arg2) {
				close(arg0);
				
			}
		});
		int tem = MoedaType.VOTECOINS.m.get(p.getUniqueId());
		
		for (VoteCoinItem it : itens) {
			m.addButtonNextSlot(new MenuButton(new SlotPos(0, 0), it.getIcone(tem)) {

				@Override
				public void click(Player arg0, Menu arg1, ClickType arg2) {
					new VendaMenu(MoedaType.VOTECOINS.m, it.getIcone(tem), "VoteCoins " + it.nome, it.preco, "") {

						@Override
						public boolean compra(Player arg0) {
							int tenq = it.premios.length;
							if (InventoryUtils.getEmpty(arg0) < tenq) {
								arg0.sendMessage(Txt.f("§cLimpe espaço no seu inventário para comprar isto!"));
								return false;
							}
							for (ItemStack itp : it.premios) {
								arg0.getInventory().offer(itp.copy());

							}
							playSound(arg0, SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP);
							InstaPokemon.sendMessage(p, "§eVocê comprou §a" + it.nome + "§e por §f" + it.preco + " §eVote Coins!");
							return true;
						}

						@Override
						public void cancela(Player arg0) {
							close(arg0);

						}
					}.open(p);;

				}
			});
		}
		m.open(p);

	}

}
