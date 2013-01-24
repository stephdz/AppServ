package org.dz.xml.test;

public class PhoneNumber {

	private int code;
	private String number;
	
	public PhoneNumber(int code, String number) {
		setCode(code);
		setNumber(number);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
