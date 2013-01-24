package org.dz.xml;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

public class XmlFactory {
	
	private XStream xstream;
	
	/**
	 * Singleton
	 */
	private static XmlFactory instance;
	private XmlFactory() {
		this.xstream = new XStream();
		this.xstream.setMode(XStream.ID_REFERENCES);
	}
	public static XmlFactory getInstance() {
		if ( instance == null ) {
			instance = new XmlFactory();
		}
		return instance;
	}
	
	public <T> T loadObject(Class<T> clazz, InputStream xml) throws XmlException {
		Object obj = xstream.fromXML(xml);
		if ( clazz.isInstance(obj) ) {
			return clazz.cast(obj);
		} else {
			throw new XmlException("Root element must be a " + clazz.getName());
		}
	}
	
	public <T> T loadObject(Class<T> clazz, String xml) throws XmlException {
		Object objReseau = xstream.fromXML(xml);
		if ( clazz.isInstance(objReseau) ) {
			return clazz.cast(objReseau);
		} else {
			throw new XmlException("Root element must be a " + clazz.getName());
		}
	}
	
	public <T> void saveObject(T element, OutputStream xml) throws XmlException {
		xstream.toXML(element, xml);
	}
	
	public <T> String getObjectXml(T element) throws XmlException {
		return xstream.toXML(element);
	}
}
