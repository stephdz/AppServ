package org.dz.neurone;

import java.util.List;

public class Perceptron<T> extends ReseauComplexe<T> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Perceptron(List<Neurone<T>> couche) {
		super();
		ajouterCouche((List)couche);
	}

}
