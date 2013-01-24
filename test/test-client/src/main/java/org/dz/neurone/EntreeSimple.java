package org.dz.neurone;

public class EntreeSimple<T> implements Entree<T> {

	private T valeur;
	
	public EntreeSimple() {
		setValeur(null);
	}
	
	public EntreeSimple(T valeur) {
		setValeur(valeur);
	}
	
	@Override
	public T getValeur() throws NeuroneException {
		return valeur;
	}
	
	public void setValeur(T valeur) {
		this.valeur = valeur;
	}
}
