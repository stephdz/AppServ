package org.dz.converter;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractConverter<E,S> implements Converter<E,S> {

	private Class<E> inClass;
	private Class<S> outClass;
	
	
	public AbstractConverter() {
		this(true);
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractConverter(boolean init) {
		if ( init ) {
			int retriesCount = 0;
			Class<?> clazz = getClass();
			while (!(clazz.getGenericSuperclass() instanceof ParameterizedType) 
					&& (retriesCount < 5)) {
				clazz = clazz.getSuperclass();
				retriesCount++;
			}
			this.inClass = (Class<E>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			this.outClass = (Class<S>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[1];
		}
	}
	
	@Override
	public Converter<S,E> getReverseConverter() {
		return new ReverseConverter();
	}
	
	@Override
	public Class<E> getInClass() {
		return inClass;
	}

	@Override
	public Class<S> getOutClass() {
		return outClass;
	}
	
	public class ReverseConverter extends AbstractConverter<S,E> {

		public ReverseConverter() {
			// Pas besoin d'initialiser les classes d'entrée et de sortie
			super(false);
		}
		
		@Override
		public Class<S> getInClass() {
			return AbstractConverter.this.getOutClass();
		}

		@Override
		public Class<E> getOutClass() {
			return AbstractConverter.this.getInClass();
		}

		@Override
		public E convert(S in) {
			return AbstractConverter.this.reverse(in);
		}

		@Override
		public S reverse(E in) {
			return AbstractConverter.this.convert(in);
		}
		
	}
	
}
