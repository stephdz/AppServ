package org.dz.jndi;

import java.lang.reflect.Method;
import java.util.List;

public class JndiCall {
	private Method method;
	private List<Object> args;
	
	public JndiCall(Method method, List<Object> args) {
		this.method = method;
		this.args = args;
	}

	public Method getMethod() {
		return method;
	}
	
	public List<Object> getArgs() {
		return args;
	}
}
