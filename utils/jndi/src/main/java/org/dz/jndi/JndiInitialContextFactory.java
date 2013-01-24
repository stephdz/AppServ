package org.dz.jndi;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class JndiInitialContextFactory implements InitialContextFactory {
	
	public static final String SERVER_PREFIX = "jndi-server://";
	public static final String CLIENT_PREFIX = "jndi://";
	
	private static Map<String,JndiContext> contexts = new HashMap<String,JndiContext>();
	
	@Override
	public synchronized Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		String providerUrl = (String) environment.get(Context.PROVIDER_URL);
		if ( providerUrl == null ) {
			providerUrl = System.getProperty(Context.PROVIDER_URL);
		}
		if ( contexts.containsKey(providerUrl) ) {
			return contexts.get(providerUrl);
		} else if ( providerUrl.startsWith(SERVER_PREFIX) ) {
			JndiContext context = getServerInitialContext(environment);
			contexts.put(providerUrl, context);
			return context;
		} else if ( providerUrl.startsWith(CLIENT_PREFIX) ) {
			JndiContext context = getClientInitialContext(environment);
			contexts.put(providerUrl, context);
			return context;
		} else {
			throw new JndiException("Unknown protocol");
		}
	}

	@SuppressWarnings("unchecked")
	private JndiContext getClientInitialContext(Hashtable<?, ?> environment) {
		return (JndiContext) Proxy.newProxyInstance(
				JndiContext.class.getClassLoader(),
				new Class<?>[]{JndiContext.class},
				new JndiClientContextInvocationHandler((Hashtable<String, Object>) environment));
	}

	@SuppressWarnings("unchecked")
	private JndiContext getServerInitialContext(Hashtable<?, ?> environment) {
		return new JndiServerContext((Hashtable<String, Object>) environment);
	}
}
