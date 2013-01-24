package org.dz.jndi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.naming.Context;

import org.apache.log4j.Logger;
import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;


public class JndiClientThread implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(JndiClientThread.class);

	private Thread thread;
	private Socket socket;
	private Context context;
	
	public JndiClientThread(Socket socket, Context context) {
		this.thread = new Thread(this);
		this.socket = socket;
		this.context = context;
		thread.start();
	}
	
	@Override
	public void run() {
		InputStream in;
		OutputStream out;
		try {
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			throw new JndiException("Unable to get input and output streams", e);
		}
		try {
			JndiCall call = XmlFactory.getInstance().loadObject(JndiCall.class, in);
			LOGGER.info("Call to JNDI "+call.getMethod().getName()+"("+call.getArgs()+")");
			JndiReturn result = new JndiReturn(call.getMethod().invoke(context, call.getArgs().toArray()), null);
			XmlFactory.getInstance().saveObject(result, out);
		} catch (Exception e) {
			try {
				JndiException jndie = new JndiException(e);
				XmlFactory.getInstance().saveObject(new JndiReturn(null, jndie), out);
				throw jndie;
			} catch (XmlException ioe) {
				throw new JndiException("Unable to send EJB exception to client", ioe);
			}
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException ioe) {
				throw new JndiException("Unable to close socket", ioe);
			}
		}
	}

}
