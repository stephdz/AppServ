package org.dz.appserv.context;

import java.security.Identity;
import java.security.Principal;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.TimerService;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@SuppressWarnings("deprecation")
public class EJBContext implements javax.ejb.EJBContext {
	
	private EJBContext parent;
	private UserTransaction transaction;
	
	public EJBContext ( EJBContext parent ) {
		this(parent, null);
	}
	
	public EJBContext ( EJBContext parent, UserTransaction transaction ) {
		this.parent = parent;
		this.transaction = transaction;
	}
	
	public EJBContext getParentContext() {
		return parent;
	}
	
	public void setUserTransaction(UserTransaction transaction) {
		this.transaction = transaction;
	}
	
	@Override
	public UserTransaction getUserTransaction() throws IllegalStateException {
		return transaction;
	}
	
	@Override
	public void setRollbackOnly() throws IllegalStateException {
		try {
			transaction.setRollbackOnly();
		} catch (SystemException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Identity getCallerIdentity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getCallerPrincipal() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EJBHome getEJBHome() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EJBLocalHome getEJBLocalHome() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Properties getEnvironment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getRollbackOnly() throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public TimerService getTimerService() throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCallerInRole(Identity arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCallerInRole(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object lookup(String arg0) {
		throw new UnsupportedOperationException();
	}
}
