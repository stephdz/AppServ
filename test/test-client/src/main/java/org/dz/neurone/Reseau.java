package org.dz.neurone;

import java.util.List;

public interface Reseau<T> {
	public List<EntreePonderee<T>> getEntrees() throws NeuroneException;
	public List<T> getSorties() throws NeuroneException;
}
