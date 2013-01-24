package org.dz.appserv.client.test.dto;


public class CarDTO extends AbstractDTO {
	private DriverDTO driver;

	public DriverDTO getDriver() {
		return driver;
	}
	
	public void setDriver(DriverDTO driver) {
		this.driver = driver;
	}
}
