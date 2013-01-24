package org.dz.converter;


public abstract class AbstractFillConverter<E,S> extends AbstractConverter<E,S> {

	protected abstract void fillIn(S source, E target);
	protected abstract void fillOut(E source, S target);
	
	@Override
	public S convert(E in) {
		S out = null;
		if ( in != null ) {
			out = createOutInstance();
			fillOut(in, out);
		}
		return out;
	}

	@Override
	public E reverse(S in) {
		E out = null;
		if ( in != null ) {
			out = createInInstance();
			fillIn(in, out);
		}
		return out;
	}
	
	protected E createInInstance() {
		try {
			return getInClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected S createOutInstance() {
		try {
			return getOutClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
