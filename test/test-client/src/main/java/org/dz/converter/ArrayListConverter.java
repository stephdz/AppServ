package org.dz.converter;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class ArrayListConverter extends AbstractFillConverter<ArrayList,ArrayList> {

	@Override
	@SuppressWarnings("unchecked")
	protected void fillIn(ArrayList source, ArrayList target) {
		for ( Object o : source ) {
			target.add(o);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void fillOut(ArrayList source, ArrayList target) {
		for ( Object o : source ) {
			target.add(o);
		}
	}
	
}
