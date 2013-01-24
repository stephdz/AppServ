package org.dz.jndi;


public class JndiException extends RuntimeException {

	private static final long serialVersionUID = -5073433036609587456L;
	
	public JndiException(String message) {
		super(message);
	}

	public JndiException(Throwable t) {
		super(t);
	}

	public JndiException(String message, Throwable t) {
		super(message, t);
	}
}
