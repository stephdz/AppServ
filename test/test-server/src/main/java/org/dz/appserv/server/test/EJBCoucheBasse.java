package org.dz.appserv.server.test;

import java.util.List;

import org.dz.appserv.server.test.entity.DriverEntity;

public interface EJBCoucheBasse {
	public String helloWorld();
	public void throwsException() throws Exception;
	public String hello(String name);
	public List<DriverEntity> getDrivers();
	public void createDrivers();
}
