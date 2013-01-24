package org.dz.jndi.test;

import java.util.List;

import org.dz.jndi.JndiServer;
import org.dz.launcher.Task;
import org.dz.launcher.TaskException;


public class JndiServerTest implements Task {

	@Override
	public void run(List<String> args) throws TaskException {
		try {
			JndiServer server = new JndiServer();
			server.getContext().bind("bonjour", "Hello world !");
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}

}
