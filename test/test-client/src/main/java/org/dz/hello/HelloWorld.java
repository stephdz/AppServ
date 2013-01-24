package org.dz.hello;

import java.util.List;

import org.dz.launcher.Task;
import org.dz.launcher.TaskException;


public class HelloWorld implements Task {

	@Override
	public void run(List<String> args) throws TaskException {
		System.out.println("Hello world !");
	}
	
}
