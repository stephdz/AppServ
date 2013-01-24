package org.dz.appserv.client;

import javax.naming.Context;


public abstract class EJBClientConfiguration {
	public static final String PORT_KEY = "ejb.server.port";
	
	private static Context context;
	
	public static synchronized Context getContext() {
		return context;
	}
	
	public static synchronized void setContext(Context context) {
		EJBClientConfiguration.context = context;
	}
}
