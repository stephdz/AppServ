package org.dz.neurone;

import java.util.List;

public interface Fonction<T> {

	/**
	 * Fonction de calcul
	 * @param entree
	 * @return
	 */
	public T calculer ( List<EntreePonderee<T>> entree ) throws NeuroneException;
}
