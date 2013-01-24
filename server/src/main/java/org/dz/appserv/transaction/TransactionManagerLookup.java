package org.dz.appserv.transaction;

import java.util.Properties;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.hibernate.HibernateException;

public class TransactionManagerLookup implements
		org.hibernate.transaction.TransactionManagerLookup {

	@Override
	public Object getTransactionIdentifier(Transaction arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionManager getTransactionManager(Properties arg0)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserTransactionName() {
		// TODO Auto-generated method stub
		return null;
	}

}
