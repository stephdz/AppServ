package org.dz.appserv.client;

import java.lang.reflect.Method;
import java.util.List;

public class EJBCall {
	private Class<?> clazz;
	private Method method;
	private List<Object> args;
	
	public EJBCall(Class<?> clazz, Method method, List<Object> args) {
		this.clazz = clazz;
		this.method = method;
		this.args = args;
	}
	
	public Class<?> getEJBClass() {
		return clazz;
	}

	public Method getMethod() {
		return method;
	}
	
	public List<Object> getArgs() {
		return args;
	}
}
