package org.dz.launcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Launcher {
	
	private static final Logger LOGGER = Logger.getLogger(Launcher.class);

	public static void main ( String[] args ) {
		if ( args.length > 0 ) {
			try {
				Class<?> taskClass = (Class<?>) Launcher.class.getClassLoader().loadClass(args[0]);
				if ( Task.class.isAssignableFrom(taskClass) ) {
					Task task = (Task) taskClass.newInstance();
					task.run(prepareArguments(args));
				} else {
					throw new TaskException("Task class must be inherited...");
				}
			} catch (Throwable t) {
				LOGGER.fatal("Error during execution", t);
			}
		} else {
			System.err.println("No task to execute...");
		}
	}
	
	private static List<String> prepareArguments(String[] args) {
		List<String> result = new ArrayList<String>(args.length-1);
		for ( int i = 1; i < args.length; i++ ) {
			result.add(args[i]);
		}
		return result;
	}
}
