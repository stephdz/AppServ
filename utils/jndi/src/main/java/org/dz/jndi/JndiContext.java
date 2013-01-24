package org.dz.jndi;

import javax.naming.Context;

public interface JndiContext extends Context {

	public JndiReturn executeCall(JndiCall call) throws Exception;

}
