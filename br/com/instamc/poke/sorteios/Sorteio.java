package br.com.instamc.poke.sorteios;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.item.inventory.ItemStack;

public class Sorteio {

	public int id = -1;
	public Timestamp termina = null;
	public int bilhetes = 0;
	public int sorteado = -1;
	public boolean premio = false;
	List<ItemStack> itens = new ArrayList();
	public int preco = 20;

	public String getTerminaFormatado() {
		if (termina == null) {
			return "";
		}
		return new SimpleDateFormat("HH:mm dd/MM/yyyy").format(termina);
	}

}
