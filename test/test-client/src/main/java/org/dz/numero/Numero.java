package org.dz.numero;

import java.util.List;

import org.dz.launcher.Task;
import org.dz.launcher.TaskException;


public class Numero implements Task {

	@Override
	public void run(List<String> args) throws TaskException {
		if ( args.size() > 0 ) {
			int iterations = Integer.parseInt(args.get(0));
			StringBuffer numero = new StringBuffer("0");
			System.out.println(numero);
			for ( int i = 0; i < iterations; i++ ) {
				StringBuffer curString = new StringBuffer(2*numero.length());
				int curPos = 0;
				while ( curPos < numero.length() ) {
					char curChar = numero.charAt(curPos++);
					int nbChar = 1;
					while ( curPos < numero.length() && curChar == numero.charAt(curPos) ) {
						nbChar++;
						curPos++;
					}
					curString.append(""+nbChar+curChar);
				}
				numero = curString;
				System.out.println(numero);
			}
		} else {
			throw new TaskException("Iterations number must be given as a parameter");
		}
	}

}
