package org.dz.neurone;

public class EntreePonderee<T> implements Entree<T> {

	private T poids;
	private Entree<T> entree;
	
	public EntreePonderee(T poids, Entree<T> entree) {
		setPoids(poids);
		setEntree(entree);
	}

	@Override
	public T getValeur() throws NeuroneException {
		throw new NeuroneException("Une entrée pondérée n'a pas de valeur sans fonction de calcul");
	}

	public void setPoids(T poids) {
		this.poids = poids;
	}

	public T getPoids() {
		return poids;
	}
	
	public void setEntree(Entree<T> entree) {
		this.entree = entree;
	}

	public Entree<T> getEntree() {
		return entree;
	}
}
