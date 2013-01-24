package org.dz.neurone;

import java.util.List;

public class FonctionSomme implements Fonction<Double> {

	@Override
	public Double calculer(List<EntreePonderee<Double>> entrees) throws NeuroneException {
		Double result = 0.0d;
		for ( EntreePonderee<Double> entree : entrees ) {
			result += entree.getPoids() * entree.getEntree().getValeur();
		}
		return result;
	}

}
