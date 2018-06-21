package br.com.instamc.poke.sorteios;

import java.util.Date;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.apis.EconAPI;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.InventoryUtils;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.Txt;

public class SorteioMenu extends Menu {
	Sorteio s;

	public SorteioMenu(Sorteio s) {

		super("Concurso " + s.id, 2);
		if (s.itens.size() <= 9) {
			for (ItemStack it : s.itens) {
				addButtonToSquare(new SlotPos(0, 0), new SlotPos(8, 0), new NothingButton(new SlotPos(0, 0), it.copy()));
			}
		}
		for (int x : new int[] { 0, 1, 2, 3, 5, 6, 7, 8 }) {
			addButton(new NothingButton(new SlotPos(x, 1),
					ItemStackBuilder.of(ItemTypes.STAINED_GLASS_PANE).withName(" ").build()));

		}
	
		addButton(new MenuButton(new SlotPos(4, 1), ItemStackBuilder.of(ItemTypes.PAPER).withName("§e§lComprar Bilhete")
				.withLore("§fClique aqui para comprar", "§fum bilhete por", "§6" + s.preco + " coins.","§aSorteio: §f"+s.getTerminaFormatado())
				.build()) {

			@Override
			public void click(Player p, Menu arg1, ClickType arg2) {
				if (s.termina != null && s.termina.after(new Date(System.currentTimeMillis())) && s.sorteado == -1) {
					if(SorteioDB.comprou(p.getUniqueId(),s.id))
					{
						p.sendMessage(Txt.f("§cVocê já comprou o bilhete deste sorteio!"));
						return;
					}
					if (!EconAPI.hasMoney(p.getUniqueId(), s.preco)) {
						p.sendMessage(Txt.f("§cVocê não possuí dinheiro para comprar!"));
						close(p);
						return;
					}
					if (InventoryUtils.getEmpty(p) < 1) {
						p.sendMessage(Txt.f("§cSem espaço no inventário para o bilhete!"));
						close(p);
						return;
					}
					p.getInventory().offer(SorteioManager.makeBilhete(s));
					SorteioDB.setComprou(p.getUniqueId(), s.id);
					EconAPI.removeMoney(p.getUniqueId(), s.preco+0.0);
					p.sendMessage(Txt.f("§a§lVocê comprou um bilhete de loteria!"));

				} else {
					p.sendMessage(Txt.f("§eConcurso inválido!"));
				}
				close(p);

			}
		});

		this.s = s;
		// TODO Auto-generated constructor stub
	}

}
