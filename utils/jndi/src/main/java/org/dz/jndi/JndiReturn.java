package org.dz.jndi;

public class JndiReturn {
	private Object result;
	private Throwable exception;
	
	public JndiReturn(Object result, Throwable exception) {
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
