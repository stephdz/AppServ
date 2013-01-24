package org.dz.appserv.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.Context;

import org.dz.appserv.client.EJBCall;
import org.dz.appserv.client.EJBReturn;
import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;



public class EJBClientInvocationHandler implements InvocationHandler {

	private Class<?> clazz;
	
	public EJBClientInvocationHandler(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public Object invoke(Object object, Method method, Object[] args) throws Throwable {
		Socket socket;
		InputStream in;
		OutputStream out;
		try {
			Context context = EJBClientConfiguration.getContext();
			URI jndiProviderUrl = new URI((String) context.getEnvironment().get(Context.PROVIDER_URL));
			String hostname = jndiProviderUrl.getHost();
			Integer port = (Integer) context.lookup(EJBClientConfiguration.PORT_KEY);
			socket = new Socket(hostname, port);
			out = new BufferedOutputStream(socket.getOutputStream());
			in = new BufferedInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			throw new EJBException(e);
		} catch (IOException e) {
			throw new EJBException(e);
		}
		try {
			List<Object> argsList = new ArrayList<Object>();
			if ( args != null ) {
				for ( Object arg : args ) {
					argsList.add(arg);
				}
			}
			EJBCall call = new EJBCall(clazz, method, argsList);
			XmlFactory.getInstance().saveObject(call, out);
			EJBReturn result = XmlFactory.getInstance().loadObject(EJBReturn.class, in);
			if ( result != null ) {
				return result.getResult();
			} else {
				throw new EJBException("Unable to get result from server");
			}
		} catch (XmlException e) {
			throw new EJBException(e);
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
