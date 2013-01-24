package org.dz.neurone;

import java.util.ArrayList;
import java.util.List;

public class Neurone<T> extends ElementReseau<T> {
	private List<EntreePonderee<T>> entrees;
	private Fonction<T> fonction;
	
	public Neurone() {
		this (
			new ArrayList<EntreePonderee<T>>(),
			null);
	}
	
	public Neurone(	List<EntreePonderee<T>> entrees, Fonction<T> fonction ) {
		this.entrees = entrees;
		this.fonction = fonction;
	}
	
	@Override
	public List<EntreePonderee<T>> getEntrees() throws NeuroneException {
		return entrees;
	}
	
	@Override
	public T getSortie() throws NeuroneException {
		return fonction.calculer(entrees);
	}
	
}
