package org.dz.converter.test;

import java.util.ArrayList;
import java.util.List;

import org.dz.converter.ConverterManager;
import org.dz.launcher.AbstractTask;
import org.dz.launcher.TaskException;


public class ConverterTest extends AbstractTask {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run(List<String> args) throws TaskException {
		startStep("Création de la liste");
		List<Double> toBeConverted = new ArrayList<Double>();
		toBeConverted.add(1.50d);
		toBeConverted.add(4.75d);
		toBeConverted.add(6.32d);
		toBeConverted.add(3.15d);
		System.out.println(toBeConverted);
		endStep();
		
		startStep("Conversion");
		List<Number> converted = ConverterManager.getInstance().convert(toBeConverted, ArrayList.class);
		System.out.println(converted);
		endStep();
		
		startStep("Sans conversion");
		converted = (List)toBeConverted;
		System.out.println(converted);
		endStep();
		
		startStep("Conversion de nombre");
		String str = ConverterManager.getInstance().convert(3.0d, String.class);
		System.out.println(str);
		endStep();
		
		startStep("Conversion inverse");
		Number nbr = ConverterManager.getInstance().convert("3.0d", Number.class);
		System.out.println(nbr);
		endStep();
	}

}
