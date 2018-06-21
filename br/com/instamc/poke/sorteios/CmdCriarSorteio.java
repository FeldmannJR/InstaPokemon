package br.com.instamc.poke.sorteios;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;


import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.menu.MenuCloseAction;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdCriarSorteio extends ComandoAPI {

	protected CmdCriarSorteio() {
		super(CommandType.OP, "criarsorteio");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;
		// CRIARSORTEIO ANO MES DIA HORA

		if (args.length != 5) {
			p.sendMessage(Txt.f("§eUse §a/criarsorteio PRECOBILHETE ANO MES DIA HORA"));
			return;
		}
		try {
			final int precobilhete = Integer.valueOf(args[0]);
			final int ano = Integer.valueOf(args[1]);
			final int mes = Integer.valueOf(args[2])-1;
			final int dia = Integer.valueOf(args[3]);
			final int hora = Integer.valueOf(args[4]);
			if(ano<0||mes<0||dia<0||hora<0){
				p.sendMessage(Txt.f("§eNúmeros inválidos!"));
				return;
			}
			if(precobilhete<=0){
				p.sendMessage(Txt.f("§ePreco inválido!"));
				return;
			}
			Calendar c = Calendar.getInstance();
			c.set(ano, mes, dia, hora, 0);
			if (c.getTime().before(new Date(System.currentTimeMillis()))) {
				p.sendMessage(Txt.f("§eEsta data já passou!"));
				return;
			}
			long dif = c.getTime().getTime() - System.currentTimeMillis();
		
			if (dif > 2592000000L) {
				p.sendMessage(Txt.f("§eVocê não pode criar sorteios muito distantes (LIMITE 1 mes)!"));
				return;
			}
			Menu m = new Menu("Itens", 1);
			m.setMoveItens(true);
			m.addClose(new MenuCloseAction() {

				@Override
				public void closeMenu(Player p, Menu ma) {
					final List<ItemStack> it = ma.getNonButtons();
					Menu confirma = new Menu("Criar Sorteio", 2);
					for (ItemStack item : it) {
						confirma.addButtonToSquare(new SlotPos(0, 0), new SlotPos(8, 0), new NothingButton(new SlotPos(0, 0), item.copy()));
					}
					final Sorteio s = new Sorteio();
					s.preco = precobilhete;
					s.termina = new Timestamp(c.getTime().getTime());
					s.itens = it;
					confirma.addButton(new MenuButton(new SlotPos(4, 1), ItemStackBuilder.of(ItemTypes.ANVIL).withName("§e" + s.getTerminaFormatado()).withLore("§6Preço: "+s.preco+"§fClique aqui para criar o sorteio!").build()) {

						@Override
						public void click(Player p, Menu arg1, ClickType arg2) {
							SorteioDB.salva(s);
							p.sendMessage(Txt.f("§a§lSorteio criado!"));
							close(p);
							
						}
					});
					confirma.open(p);

				}
			});
			m.open(p);

		} catch (NumberFormatException ex) {
			p.sendMessage(Txt.f("§eNúmeros inválidos!"));

			return;
		}

		// TODO Auto-generated method stub

	}

}
