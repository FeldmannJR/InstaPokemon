package br.com.instamc.poke.kits.cmds;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.poke.kits.Kit;
import br.com.instamc.poke.kits.KitManager;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuCloseAction;
import br.com.instamc.sponge.library.menu.buttons.NothingButton;
import br.com.instamc.sponge.library.utils.Txt;

public class CmdKitAdm extends ComandoAPI {

	public CmdKitAdm() {
		super(CommandType.OP, "kitadm");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(CommandSource cs, String[] args) {
		Player p = (Player) cs;

		if (args.length == 0) {
			showHelp(p);
			return;
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("criar")) {
				try {
					String nome = args[1];
					int minutos = Integer.valueOf(args[2]);
					if (minutos < 0) {
						p.sendMessage(Txt.f("§c§lMinutos inválidos!"));
						return;
					}
					if (KitManager.getKitByName(nome) != null) {
						p.sendMessage(Txt.f("§c§lKit já existente!"));
						return;
					}
					Kit k = new Kit(nome, minutos);
					p.sendMessage(Txt.f("§aKit salvo!!!"));
					KitManager.salvaKit(k);
					return;

				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§c§lMinutos inválidos!"));
					return;
				}
			}
			if (args[0].equalsIgnoreCase("setminutos")) {
				try {
					String nome = args[1];
					int minutos = Integer.valueOf(args[2]);
					if (minutos < 0) {
						p.sendMessage(Txt.f("§c§lMinutos inválidos!"));
						return;
					}
					Kit k = KitManager.getKitByName(nome);
					if (k == null) {
						p.sendMessage(Txt.f("§c§lKit inexistente!"));
						return;

					}
					k.setMinutos(minutos);
					KitManager.salvaKit(k);
					p.sendMessage(Txt.f("§aKit salvo!!!"));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§c§lMinutos inválidos!"));
					return;
				}
			}
			if (args[0].equalsIgnoreCase("setpriority")) {
				try {
					String nome = args[1];
					int minutos = Integer.valueOf(args[2]);
					if (minutos < 0) {
						p.sendMessage(Txt.f("§c§lPriority inválidos!"));
						return;
					}
					Kit k = KitManager.getKitByName(nome);
					if (k == null) {
						p.sendMessage(Txt.f("§c§lKit inexistente!"));
						return;

					}
					k.setPriority(minutos);
					KitManager.salvaKit(k);
					p.sendMessage(Txt.f("§aKit salvo!!!"));
					return;
				} catch (NumberFormatException ex) {
					p.sendMessage(Txt.f("§c§lMinutos inválidos!"));
					return;
				}
			}

		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("setitens")) {

				String nome = args[1];

				Kit k = KitManager.getKitByName(nome);
				if (k == null) {
					p.sendMessage(Txt.f("§c§lKit inexistente!"));
					return;

				}
				Menu m = new Menu("Editar Kit:" + nome, 3);
				m.setMoveItens(true);
				for (ItemStack i : k.getItens()) {
					m.addNonButton(i.copy());
				}
				for (ItemStack invitem : m.getNonButtons()) {
				
					p.sendMessage(Txt.f("-"+invitem.getItem().getId()));
				}
				m.addClose(new MenuCloseAction() {

					@Override
					public void closeMenu(Player fechou, Menu closed) {
						
						k.setItens(m.getNonButtons());
						KitManager.salvaKit(k);
						p.sendMessage(Txt.f("§aKit salvo com "+k.getItens().size()+" itens!"));

					}
				});
				m.open(p);

				return;

			}
			if (args[0].equalsIgnoreCase("seticone")) {
				Kit k = KitManager.getKitByName(args[1]);

				if (k == null) {
					p.sendMessage(Txt.f("§c§lKit inexistente!"));
					return;

				}
				Menu set = new Menu("Setar Icone", 1);
				set.setMoveItens(true);

				for (int x : new int[] { 0, 1, 2, 3, 5, 6, 7, 8 }) {
					set.addButton(new NothingButton(new SlotPos(x, 0), ItemStack.of(ItemTypes.BARRIER, 1)));

				}
				if (k.getIcone() != null) {
					set.addNonButton(k.getIcone().copy());
				}
				set.addClose(new MenuCloseAction() {

					@Override
					public void closeMenu(Player p, Menu m) {
						if (!m.getNonButtons().isEmpty()) {
							ItemStack i = m.getNonButtons().get(0);
							k.setIcone(i);
							p.sendMessage(Txt.f("§aIcone setado!"));
						} else {

							k.setIcone(null);
							p.sendMessage(Txt.f("§aIcone Removido!"));
						}
						KitManager.salvaKit(k);

					}
				});
				set.open(p);
			}
			if (args[0].equalsIgnoreCase("deletar")) {

				String nome = args[1];

				Kit k = KitManager.getKitByName(nome);
				if (k == null) {
					p.sendMessage(Txt.f("§c§lKit inexistente!"));
					return;

				}
				KitManager.deletaKit(k);
				p.sendMessage(Txt.f("§bKit deletado!!!"));
				return;
			}

		}
		showHelp(p);

	}

	public static void showHelp(Player p) {
		p.sendMessage(Txt.f("§e/kitadm criar KIT minutos"));
		p.sendMessage(Txt.f("§e/kitadm deletar KIT"));
		p.sendMessage(Txt.f("§e/kitadm setitens KIT"));
		p.sendMessage(Txt.f("§e/kitadm seticone KIT"));
		p.sendMessage(Txt.f("§e/kitadm setminutos KIT Minutos"));
		p.sendMessage(Txt.f("§e/kitadm setpriority KIT Minutos"));
		p.sendMessage(Txt.f("§7Minutos = 0 Só pode pegar uma vez"));

	}

}
