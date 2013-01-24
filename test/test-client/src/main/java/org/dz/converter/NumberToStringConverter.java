package org.dz.converter;

public class NumberToStringConverter extends AbstractConverter<Number,String> {

	@Override
	public String convert(Number in) {
		return in.toString();
	}

	@Override
	public Number reverse(String in) {
		return Double.parseDouble(in);
	}

}
