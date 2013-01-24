package org.dz.neurone;

import java.util.ArrayList;
import java.util.List;

public class ReseauComplexe<T> implements Reseau<T> {
	
	private List<List<ElementReseau<T>>> couches;
	
	public ReseauComplexe() {
		
	}
	
	public ReseauComplexe(List<List<ElementReseau<T>>> couches) {
		this.couches = couches;
	}
	
	public void ajouterCouche(List<ElementReseau<T>> couche) {
		this.couches.add(couche);
	}

	@Override
	public List<EntreePonderee<T>> getEntrees() throws NeuroneException {
		if ( couches.size() > 0 ) {
			List<ElementReseau<T>> coucheInitiale = couches.get(0);
			List<EntreePonderee<T>> result = new ArrayList<EntreePonderee<T>>();
			for ( ElementReseau<T> elementReseau : coucheInitiale) {
				result.addAll(elementReseau.getEntrees());
			}
			return result;
		} else {
			throw new NeuroneException("Un réseau doit comporter au moins une couche");
		}
	}

	@Override
	public List<T> getSorties() throws NeuroneException {
		if ( couches.size() > 0 ) {
			List<ElementReseau<T>> coucheFinale = couches.get(couches.size()-1);
			List<T> result = new ArrayList<T>();
			for ( ElementReseau<T> elementReseau : coucheFinale) {
				result.add(elementReseau.getSortie());
			}
			return result;
		} else {
			throw new NeuroneException("Un réseau doit comporter au moins une couche");
		}
	}

}
