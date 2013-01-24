package org.dz.jndi;

import java.io.IOException;
import java.net.ServerSocket;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class JndiServer implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(JndiServer.class);

	private Thread thread;
	private Context context;
	private boolean stop;

	public JndiServer() throws JndiException {
		try {
			this.context = new InitialContext();
			this.thread = new Thread(this);
			this.stop = false;
			thread.start();
			LOGGER.info("JNDI server started on port "+JndiServerContext.SERVER_PORT);
		} catch (NamingException e) {
			throw new JndiException(e);
		}
	}
	
	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(JndiServerContext.SERVER_PORT);
			while ( ! isStopped() ) {
				new JndiClientThread(ss.accept(), context);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void stop() {
		stop = true;
	}
	
	public synchronized boolean isStopped() {
		return stop;
	}
	
	public Context getContext() {
		return context;
	}
}
