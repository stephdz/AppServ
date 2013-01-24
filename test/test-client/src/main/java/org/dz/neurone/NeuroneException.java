package org.dz.neurone;

public class NeuroneException extends Throwable {

	private static final long serialVersionUID = -2053152725995248971L;
	
	public NeuroneException(String message) {
		super(message);
	}

	public NeuroneException(Throwable t) {
		super(t);
	}
}