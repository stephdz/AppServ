package org.dz.neurone.test;

import java.util.List;

import org.dz.launcher.AbstractTask;
import org.dz.launcher.TaskException;
import org.dz.neurone.NeuroneException;
import org.dz.neurone.Reseau;
import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;


public class ImportTest extends AbstractTask {

	@Override
	@SuppressWarnings("unchecked")
	public void run(List<String> args) throws TaskException {
		try {
			startStep("Chargement du réseau à partir du XML");
			Reseau<Double> reseau = XmlFactory.getInstance().loadObject(
					Reseau.class,
					ImportTest.class.getResourceAsStream("/neurone/test/reseau.xml"));
			System.out.println(reseau.getSorties());
			endStep();
			
			startStep("Sérialisation du réseau chargé");
			String xml = XmlFactory.getInstance().getObjectXml(reseau);
			System.out.println(xml);
			endStep();
			
			startStep("Rechargement à partir du XML obtenu");
			reseau = XmlFactory.getInstance().loadObject(Reseau.class, xml);
			System.out.println(reseau.getSorties());
			endStep();
		} catch ( NeuroneException e ) {
			throw new TaskException(e);
		} catch ( XmlException e ) {
			throw new TaskException(e);
		}
	}

}
