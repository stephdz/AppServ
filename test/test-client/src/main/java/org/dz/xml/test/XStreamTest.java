package org.dz.xml.test;

import java.util.List;

import org.dz.launcher.Task;
import org.dz.launcher.TaskException;


import com.thoughtworks.xstream.XStream;

public class XStreamTest implements Task {

	@Override
	public void run(List<String> args) throws TaskException {
		XStream xstream = new XStream();
		Person joe = new Person("Joe", "Walnes");
		joe.setPhone(new PhoneNumber(123, "1234-456"));
		joe.setFax(new PhoneNumber(123, "9999-999"));
		String xml = xstream.toXML(joe);
		System.out.println(xml);
		joe = (Person) xstream.fromXML(xml);
		xml = xstream.toXML(joe);
		System.out.println(xml);
	}

}
