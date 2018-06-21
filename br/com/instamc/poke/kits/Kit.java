package br.com.instamc.poke.kits;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.Txt;
import scala.actors.threadpool.Arrays;

public class Kit {

	String nome;
	int minutos;
	List<ItemStack> itens = new ArrayList();
	ItemStack icone = null;
	boolean anyonecanuse = false;
	int priority = 0;

	public Kit(String nome, int minutos) {
		this.nome = nome;
		this.minutos = minutos;

	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getNome() {
		return nome;
	}

	public void setItens(List<ItemStack> itens){
		this.itens = itens;
	}
	public List<ItemStack> getItens() {
		return itens;
	}

	public int getMinutos() {
		return minutos;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ItemStack getIcone() {
		return icone;
	}

	public void setIcone(ItemStack icone) {
		this.icone = icone;
	}

	public boolean hasPermission(Player p) {
		if (p == null)
			return false;
		return p.hasPermission("instamc.kit." + getNome().toLowerCase());
	}

	public ItemStack buildItem(Player p) {
		ItemStack i;
		if (icone != null) {
			i = icone.copy();
		} else
			if (!itens.isEmpty()) {
				i = itens.get(0).copy();
			} else {
				i = ItemStack.of(ItemTypes.STICK, 1);
			}

		ItemUtils.ClearLore(i);
		if (hasPermission(p)) {
			ItemUtils.setItemName(i, Txt.f("§a§l" + getNome().replace("_", " ")));
		} else {
			ItemUtils.setItemName(i, Txt.f("§c§l" + getNome().replace("_", " ")));
			ItemUtils.addLore(i,Txt.f("§4Sem permissão."));
		}
		if (getMinutos() != 0) {
			ItemUtils.addLore(i, Txt.f("§6Recarga: §f" + KitManager.Minutos(getMinutos())));
		} else {
			ItemUtils.addLore(i, Txt.f("§6Uso único."));
		}
		ItemUtils.addLore(i, Txt.f(""));
		ItemUtils.addLore(i, Txt.f("§b§lClique §f§lesquerdo §b§lpara pegar!"));
		ItemUtils.addLore(i, Txt.f("§7§lClique §f§ldireito §7§lpara ver!"));
		return i;

	}

}
