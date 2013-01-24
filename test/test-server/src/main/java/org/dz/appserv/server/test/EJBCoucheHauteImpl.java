package org.dz.appserv.server.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.dz.appserv.client.test.EJBCoucheHauteRemote;
import org.dz.appserv.client.test.dto.CarDTO;
import org.dz.appserv.client.test.dto.DriverDTO;
import org.dz.appserv.server.test.entity.CarEntity;
import org.dz.appserv.server.test.entity.DriverEntity;

@Stateless
public class EJBCoucheHauteImpl implements EJBCoucheHauteLocal, EJBCoucheHauteRemote {
	@EJB
	private EJBCoucheBasseLocal ejbLocal;
	
	@Override
	public String hello(String name) {
		return ejbLocal.helloWorld() + "\n" + ejbLocal.hello(name);
	}
	
	@Override
	public List<Integer> getRandomNumbers(int start, int end, int nb) {
		List<Integer> result = new ArrayList<Integer>(nb);
		for ( int i = 0; i < nb; i++ ) {
			result.add((int) (Math.random() * (double) (end - start + 1)) + start);
		}
		return result;
	}
	
	@Override
	public Map<Integer,Double> getRandomStatistics(int start, int end, int nb) {
		Map<Integer,Double> result = new TreeMap<Integer,Double>();
		for ( int i = 0; i < nb; i++ ) {
			int rand = (int) (Math.random() * (double) (end - start + 1));
			rand += start;
			double count = 0;
			if ( result.containsKey(rand) ) {
				count = result.get(rand);
			}
			result.put(rand, count + 1);
		}
		for ( Integer rand : result.keySet() ) {
			double count = Math.round(result.get(rand) / ((double)nb) * 10000.0d);
			result.put(rand, count / 100.0d);
		}
		return result;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<DriverDTO> getDrivers() {
		List<DriverDTO> result = new ArrayList<DriverDTO>();
		List<DriverEntity> entities = ejbLocal.getDrivers();
		for ( DriverEntity entity : entities ) {
			result.add(convert(entity));
		}
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createDrivers() {
		ejbLocal.createDrivers();
	}
	
	private DriverDTO convert(DriverEntity entity) {
		DriverDTO dto = new DriverDTO();
		dto.setId(entity.getId());
		List<CarDTO> cars = new ArrayList<CarDTO>();
		List<CarEntity> entities = entity.getCars();
		for ( CarEntity carEntity : entities ) {
			cars.add(convert(carEntity, dto));
		}
		dto.setCars(cars);
		return dto;
	}

	private CarDTO convert(CarEntity entity, DriverDTO driver) {
		CarDTO dto = new CarDTO();
		dto.setId(entity.getId());
		dto.setDriver(driver);
		return dto;
	}
}
