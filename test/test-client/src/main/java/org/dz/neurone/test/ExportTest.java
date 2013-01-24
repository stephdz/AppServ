package org.dz.neurone.test;

import java.util.ArrayList;
import java.util.List;

import org.dz.launcher.AbstractTask;
import org.dz.launcher.TaskException;
import org.dz.neurone.ElementReseau;
import org.dz.neurone.EntreePonderee;
import org.dz.neurone.FonctionSomme;
import org.dz.neurone.Neurone;
import org.dz.neurone.NeuroneException;
import org.dz.neurone.Reseau;
import org.dz.neurone.ReseauComplexe;
import org.dz.xml.XmlException;
import org.dz.xml.XmlFactory;


public class ExportTest extends AbstractTask {

	@Override
	@SuppressWarnings("unchecked")
	public void run(List<String> args) throws TaskException {
		try {
			startStep("Création du réseau");
			Reseau<Double> reseau = creerReseau();
			System.out.println(reseau.getSorties());
			endStep();
			
			startStep("Serialisation XML du réseau");
			String xml = XmlFactory.getInstance().getObjectXml(reseau);
			System.out.println(xml);
			endStep();
			
			startStep("Rechargement du réseau à partir du XML");
			System.out.println(reseau.getSorties());
			endStep();
			
			startStep("Serialisation du réseau rechargé");
			reseau = XmlFactory.getInstance().loadObject(Reseau.class, xml);
			xml = XmlFactory.getInstance().getObjectXml(reseau);
			System.out.println(xml);
			endStep();
		} catch ( NeuroneException e ) {
			throw new TaskException(e);
		} catch ( XmlException e ) {
			throw new TaskException(e);
		}
	}
	
	private Reseau<Double> creerReseau() {
		List<List<ElementReseau<Double>>> couches = new ArrayList<List<ElementReseau<Double>>>();
		List<ElementReseau<Double>> couche1 = new ArrayList<ElementReseau<Double>>();
		List<EntreePonderee<Double>> entrees11 = new ArrayList<EntreePonderee<Double>>();
		FonctionSomme fonction11 = new FonctionSomme();
		Neurone<Double> neurone11 = new Neurone<Double>(entrees11, fonction11);
		couche1.add(neurone11);
		couches.add(couche1);
		return new ReseauComplexe<Double>(couches);
	}

}
