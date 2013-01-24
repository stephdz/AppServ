package org.dz.converter;


public class ConverterIdentity {
	private Class<?> in;
	private Class<?> out;
	
	public ConverterIdentity(Converter<?,?> converter) {
		this(converter.getInClass(), converter.getOutClass());
	}
	
	public ConverterIdentity(Class<?> in, Class<?> out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public boolean equals(Object obj) {
		if ( in != null && out != null && obj instanceof ConverterIdentity ){
			ConverterIdentity id = (ConverterIdentity) obj;
			return in.equals(id.getIn()) && out.equals(id.getOut());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return in.hashCode() + 31 * out.hashCode();
	}

	public Class<?> getIn() {
		return in;
	}
	
	public Class<?> getOut() {
		return out;
	}
}
