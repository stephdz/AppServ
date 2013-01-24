package org.dz.appserv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.dz.appserv.client.EJBCall;
import org.dz.appserv.client.EJBReturn;
import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;




public class EJBThread implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(EJBThread.class);
	
	private Thread thread;
	private Socket socket;
	
	public EJBThread(Socket socket) {
		this.thread = new Thread(this);
		this.socket = socket;
		thread.start();
	}

	@Override
	public void run() {
		try {
			executeEJB();
		} catch (Throwable t) {
			LOGGER.error("Error during EJB Call", t);
		}
	}
	
	private void executeEJB() {
		InputStream in;
		OutputStream out;
		try {
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			throw new EJBException("Unable to get input and output streams", e);
		}
		try {
			EJBCall call = XmlFactory.getInstance().loadObject(EJBCall.class, in);
			Object ejb = EJBManager.getInstance().getStatelessEJB(call.getEJBClass());
			if ( ejb != null ) {
				LOGGER.debug("Call to "+call.getEJBClass().getName()+"."+call.getMethod().getName()+"()");
				Object result = call.getMethod().invoke(ejb, call.getArgs().toArray());
				XmlFactory.getInstance().saveObject(new EJBReturn(result, null), out);
			} else {
				EJBException e = new EJBException("Unable to get called EJB : "+call.getMethod().getDeclaringClass());
				XmlFactory.getInstance().saveObject(new EJBReturn(null, e), out);
				throw new EJBException("Unable to get called EJB : "+call.getMethod().getDeclaringClass());
			}
		} catch (InvocationTargetException e) {
			try {
				XmlFactory.getInstance().saveObject(new EJBReturn(null, e.getTargetException()), out);
				throw new EJBException(e);
			} catch (XmlException ioe) {
				throw new EJBException("Unable to send EJB exception to client", ioe);
			}
		} catch (Exception e) {
			try {
				EJBException ejbe = new EJBException(e);
				XmlFactory.getInstance().saveObject(new EJBReturn(null, ejbe), out);
				throw ejbe;
			} catch (XmlException ioe) {
				throw new EJBException("Unable to send EJB exception to client", ioe);
			}
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException ioe) {
				throw new EJBException("Unable to close socket", ioe);
			}
		}
	}
}
