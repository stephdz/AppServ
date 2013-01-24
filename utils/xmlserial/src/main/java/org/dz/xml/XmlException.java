package org.dz.xml;

public class XmlException extends Exception {

	private static final long serialVersionUID = -3066608995900416864L;

	public XmlException(String message) {
		super(message);
	}

	public XmlException(Throwable t) {
		super(t);
	}
}
