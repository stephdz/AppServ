package org.dz.appserv.client.test.dto;

import java.util.List;

public class DriverDTO extends AbstractDTO {
	private List<CarDTO> cars;
	
	public List<CarDTO> getCars() {
		return cars;
	}
	
	public void setCars(List<CarDTO> cars) {
		this.cars = cars;
	}
}
