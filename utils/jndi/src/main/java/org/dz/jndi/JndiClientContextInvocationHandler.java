package org.dz.jndi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;

import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;


public class JndiClientContextInvocationHandler implements InvocationHandler {
	
	private Hashtable<String, Object> environment;
	private String providerUrl;
	private String hostname;
	private int port;

	public JndiClientContextInvocationHandler(Hashtable<String, Object> environment) throws JndiException {
		try {
			this.environment = environment;
			this.providerUrl = (String) environment.get(Context.PROVIDER_URL);
			URI uri = new URI(providerUrl);
			this.hostname = uri.getHost();
			this.port = uri.getPort();
		} catch (URISyntaxException e) {
			throw new JndiException(e);
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Socket socket;
		InputStream in;
		OutputStream out;
		try {
			socket = new Socket(hostname, port);
			out = new BufferedOutputStream(socket.getOutputStream());
			in = new BufferedInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			throw new JndiException(e);
		} catch (IOException e) {
			throw new JndiException(e);
		}
		try {
			List<Object> argsList = new ArrayList<Object>();
			if ( args != null ) {
				for ( Object arg : args ) {
					argsList.add(arg);
				}
			}
			JndiCall call = new JndiCall(method, argsList);
			XmlFactory.getInstance().saveObject(call, out);
			JndiReturn result = XmlFactory.getInstance().loadObject(JndiReturn.class, in);
			if ( result != null ) {
				return result.getResult();
			} else {
				throw new JndiException("Unable to get result from server");
			}
		} catch (XmlException e) {
			throw new JndiException(e);
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

	public Hashtable<String, Object> getEnvironment() {
		return environment;
	}
}
