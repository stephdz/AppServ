package org.dz.converter;

import java.util.HashMap;
import java.util.Map;


public class ConverterManager {
	
	private Map<ConverterIdentity,Converter<?,?>> converters;

	/**
	 * Singleton
	 */
	private static ConverterManager instance;
	private ConverterManager() {
		this.converters = new HashMap<ConverterIdentity,Converter<?,?>>();
		registerConverter(new ArrayListConverter());
		registerConverter(new NumberToStringConverter());
	}
	public static ConverterManager getInstance() {
		if ( instance == null ) {
			instance = new ConverterManager();
		}
		return instance;
	}
	
	public <E,S> void registerConverter(Converter<E,S> converter) {
		registerConverter(converter, true);
	}
	
	@SuppressWarnings("unchecked")
	public <E,S> S convert(E in, Class<S> outClass) {
		S out = null;
		if ( in != null ) {
			Converter<E,S> converter = null;
			Class<?> inClass = in.getClass();
			while ( converter == null && inClass != null ) {
				converter = getConverter((Class<E>)inClass, outClass);
				inClass = inClass.getSuperclass();
			}
			if ( converter != null ) {
				out = converter.convert(in);
			}
		}
		return out;
	}
	
	@SuppressWarnings("unchecked")
	private <E,S> Converter<E,S> getConverter(Class<E> inClass, Class<S> outClass) {
		ConverterIdentity identity = new ConverterIdentity(inClass, outClass);
		return (Converter<E,S>) converters.get(identity);
	}
	
	private <E,S> void registerConverter(Converter<E,S> converter, boolean registerReverse) {
		if ( converter != null ) {
			ConverterIdentity identity = getIdentity(converter);
			if ( ! converters.containsKey(identity) ) {
				converters.put(identity, converter);
				if ( registerReverse ) {
					registerConverter(converter.getReverseConverter(), false);
				}
			}
		}
	}
	
	private <E,S> ConverterIdentity getIdentity(Converter<E,S> converter) {
		return new ConverterIdentity(converter);
	}
}
