package org.dz.appserv.client.test.dto;


public class AbstractDTO {
	private Long id;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
