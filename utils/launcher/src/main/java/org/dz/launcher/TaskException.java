package org.dz.launcher;

public class TaskException extends Throwable {

	private static final long serialVersionUID = -2053152725995248971L;
	
	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable t) {
		super(t);
	}
}