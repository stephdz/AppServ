package org.dz.converter;

public interface Converter<E,S> {

	public S convert(E in);
	public E reverse(S in);
	public Converter<S,E> getReverseConverter();
	public Class<E> getInClass();
	public Class<S> getOutClass();
	
}
