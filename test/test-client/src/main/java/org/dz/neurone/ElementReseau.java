package org.dz.neurone;

import java.util.List;

public abstract class ElementReseau<T> implements Entree<T> {

	/**
	 * Entrées
	 * @return
	 */
	public abstract List<EntreePonderee<T>> getEntrees() throws NeuroneException;
	
	/**
	 * Sortie
	 * @return
	 */
	public abstract T getSortie() throws NeuroneException;
	
	@Override
	public T getValeur() throws NeuroneException {
		return getSortie();
	}
}
