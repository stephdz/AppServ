package org.dz.appserv;

import java.net.URL;
import java.net.URLClassLoader;

public class ServerClassLoader extends URLClassLoader {

	public ServerClassLoader() {
        super(new URL[0]);
    }
	
    public ServerClassLoader(URL[] urls) {
        super(urls);
    }
    
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
    
    public static ServerClassLoader getServerClassLoader() {
    	ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ServerClassLoader serverLoader = null;
		if ( loader instanceof ServerClassLoader ) {
			serverLoader = (ServerClassLoader) loader;
		} else if ( loader instanceof URLClassLoader ) {
			serverLoader = new ServerClassLoader(((URLClassLoader) loader).getURLs());
		} else {
			serverLoader = new ServerClassLoader();
		}
		Thread.currentThread().setContextClassLoader(serverLoader);
		
		return serverLoader;
    }
}
