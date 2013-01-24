package org.dz.appserv.client;

public class EJBReturn {
	private Object result;
	private Throwable exception;
	
	public EJBReturn(Object result, Throwable exception) {
		this.result = result;
		this.exception = exception;
	}
	
	public Object getResult() throws Throwable {
		if ( exception != null ) {
			throw exception;
		} else {
			return result;
		}
	}
}
