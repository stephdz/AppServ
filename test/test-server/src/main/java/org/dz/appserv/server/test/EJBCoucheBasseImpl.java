package org.dz.appserv.server.test;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dz.appserv.server.test.entity.CarEntity;
import org.dz.appserv.server.test.entity.DriverEntity;
import org.dz.xml.XmlException;


@Stateless
public class EJBCoucheBasseImpl implements EJBCoucheBasseLocal, EJBCoucheBasseRemote {

	@PersistenceContext(unitName="test")
	private EntityManager em;
	
	@Override
	public String helloWorld() {
		return "Hello World !";
	}

	@Override
	public void throwsException() throws Exception {
		throw new XmlException("Hello world with exception !");		
	}

	@Override
	public String hello(String name) {
		return "Hello "+name+" !";		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public List<DriverEntity> getDrivers() {
		return em.createQuery("FROM DriverEntity").getResultList();
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public void createDrivers() {
		em.getTransaction().begin();
		
		// Driver 1
		CarEntity car11 = new CarEntity();
		CarEntity car12 = new CarEntity();
		DriverEntity driver1 = new DriverEntity(car11, car12);
		em.merge(driver1);
		
		// Driver 2
		CarEntity car21 = new CarEntity();
		CarEntity car22 = new CarEntity();
		CarEntity car23 = new CarEntity();
		DriverEntity driver2 = new DriverEntity(car21, car22, car23);
		em.merge(driver2);
		
		em.getTransaction().commit();
	}
}
