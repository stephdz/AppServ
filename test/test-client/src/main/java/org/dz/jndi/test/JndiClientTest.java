package org.dz.jndi.test;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.dz.launcher.Task;
import org.dz.launcher.TaskException;


public class JndiClientTest implements Task {

	@Override
	public void run(List<String> args) throws TaskException {
		try {
			Context context = new InitialContext();
			System.out.println(context.lookup("bonjour"));
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}

}
