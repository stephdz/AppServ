package org.dz.appserv.server.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DRIVER")
public class DriverEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 18756487546365L;
	
	private List<CarEntity> cars;
	
	public DriverEntity() {
		this((CarEntity[])null);
	}
	
	public DriverEntity(CarEntity... cars) {
		this.cars = new ArrayList<CarEntity>();
		if ( cars != null ) {
			for ( CarEntity car : cars ) {
				this.cars.add(car);
			}
		}
	}

	@OneToMany(mappedBy = "driver", cascade = {CascadeType.MERGE})
	public List<CarEntity> getCars() {
		return cars;
	}
	
	public void setCars(List<CarEntity> cars) {
		this.cars = cars;
	}
}
