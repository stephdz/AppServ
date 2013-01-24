package org.dz.appserv;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.dz.appserv.client.EJBClientConfiguration;
import org.dz.jndi.JndiServer;
import org.dz.launcher.Task;
import org.dz.launcher.TaskException;



public class ApplicationServer implements Task {
	
	public static final int SERVER_PORT = 6969;
	
	private JndiServer jndiServer;

	public ApplicationServer() throws EJBException {
		try {
			this.jndiServer = new JndiServer();
			jndiServer.getContext().bind(EJBClientConfiguration.PORT_KEY, SERVER_PORT);
		} catch ( NamingException e ) {
			throw new EJBException(e);
		}
	}
	
	@Override
	public void run(List<String> args) throws TaskException {
		for ( String arg : args ) {
			EJBManager.getInstance().deploy(arg);
		}
		try {
			ServerSocket ss = new ServerSocket(SERVER_PORT);
			while (true) {
				new EJBThread(ss.accept());
			}
		} catch (IOException e) {
			throw new TaskException(e);
		} finally {
			jndiServer.stop();
		}
	}
}
