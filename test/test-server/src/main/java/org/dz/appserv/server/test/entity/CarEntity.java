package org.dz.appserv.server.test.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CAR")
public class CarEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 175434786732324756L;
	
	private DriverEntity driver;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "driverId")
	public DriverEntity getDriver() {
		return driver;
	}
	
	public void setDriver(DriverEntity driver) {
		this.driver = driver;
	}
}
