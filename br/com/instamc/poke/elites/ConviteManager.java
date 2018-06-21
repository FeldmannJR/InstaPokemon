package br.com.instamc.poke.elites;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class ConviteManager {

	HashSet<Convite> convites = new HashSet();
	HashSet<ConviteRelation> convitesrelation = new HashSet();

	public Convite getConvite(int elite, UUID uid) {
		for (Convite c : convites) {
			if (c.vence > System.currentTimeMillis()) {
				if (c.eliteidconvidou == elite && c.convidado.equals(uid)) {
					return c;
				}
			}
		}
		return null;

	}

	public ConviteRelation getConviteRelation(int convidou, int convidada) {
		for (ConviteRelation c : convitesrelation) {
			if (c.vence > System.currentTimeMillis()) {
				if (c.pediu == convidou && c.praquem == convidada) {
					return c;
				}
			}
		}
		return null;

	}

	public void removeConviteRelation(int convidou, int convidada, boolean aliado) {
		for (ConviteRelation c : convitesrelation) {
			if (c.vence < System.currentTimeMillis()) {
				if (c.pediu == convidou && c.praquem == convidada) {
					convitesrelation.remove(c);
				}
			}
		}
	}

	public void removeConvite(int elite, UUID uid) {
		for (Convite c : new ArrayList<Convite>(convites)) {
			if (c.vence < System.currentTimeMillis()) {
				if (c.eliteidconvidou == elite && c.convidado.equals(uid)) {
					convites.remove(c);
				}
			}
		}
	}

	public void addConvite(ConviteRelation cv) {
		for (ConviteRelation c : convitesrelation) {

			if (c.pediu == cv.pediu && c.praquem == cv.pediu) {
				c.vence = System.currentTimeMillis() + 60000L;
				c.aliado = cv.aliado;
				return;
			}
		}
		convitesrelation.add(cv);
	}

	public void addConvite(Convite cv) {
		for (Convite c : convites) {

			if (c.eliteidconvidou == cv.eliteidconvidou && c.convidado.equals(cv.convidado)) {
				c.vence = System.currentTimeMillis() + 60000L;
				return;
			}
		}
		convites.add(cv);
	}

	public static class Convite {

		public int eliteidconvidou;
		public UUID convidado;
		long vence;

		public Convite(int eliteid, UUID convidado) {
			this.eliteidconvidou = eliteid;
			this.convidado = convidado;
			vence = System.currentTimeMillis() + 60000L;
		}

	}

	public static class ConviteRelation {
		public int pediu;
		public int praquem;
		public boolean aliado;
		public long vence = System.currentTimeMillis() + 60000L;

		public ConviteRelation(int pediu, int praquem, boolean aliado) {
			this.pediu = pediu;
			this.praquem = praquem;
			this.aliado = aliado;
		}

	}
}
