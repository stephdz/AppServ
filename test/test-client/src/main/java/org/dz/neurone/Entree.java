package org.dz.neurone;

public interface Entree<T> {
	
	/**
	 * Retourne la valeur de l'entrée
	 * @return
	 */
	public T getValeur() throws NeuroneException;
}
