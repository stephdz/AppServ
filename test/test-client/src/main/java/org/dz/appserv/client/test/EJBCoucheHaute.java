package org.dz.appserv.client.test;

import java.util.List;
import java.util.Map;

import org.dz.appserv.client.test.dto.DriverDTO;

public interface EJBCoucheHaute {
	public String hello(String name);
	public List<Integer> getRandomNumbers(int start, int end, int nb);
	public Map<Integer, Double> getRandomStatistics(int start, int end, int nb);
	public List<DriverDTO> getDrivers();
	public void createDrivers();
}
