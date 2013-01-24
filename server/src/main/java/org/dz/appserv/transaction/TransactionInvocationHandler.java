package org.dz.appserv.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.dz.appserv.context.EJBContext;

public class TransactionInvocationHandler implements InvocationHandler {
	
	private static final Logger LOGGER = Logger.getLogger(TransactionInvocationHandler.class);
	
	private EJBContext ejbContext;
	
	public TransactionInvocationHandler(EJBContext ejbContext) {
		this.ejbContext = ejbContext;
	}
	
	@Override
	public Object invoke(Object object, Method method, Object[] args) throws Throwable {
		TransactionAttribute annotation = object.getClass().getAnnotation(TransactionAttribute.class);
		if ( method.getAnnotation(PersistenceContext.class) != null ) {
			annotation = method.getAnnotation(TransactionAttribute.class);
		}
		if ( annotation != null ) {
			// Prepare transaction
			boolean beginCommit = false;
			if ( TransactionAttributeType.REQUIRES_NEW.equals(annotation.value()) ) {
				ejbContext.setUserTransaction(createTransaction());
				beginCommit = true;
			} else if ( TransactionAttributeType.NEVER.equals(annotation.value()) || TransactionAttributeType.NOT_SUPPORTED.equals(annotation.value()) ) {
				ejbContext.setUserTransaction(null);
				beginCommit = false;
			} else if ( TransactionAttributeType.MANDATORY.equals(annotation.value()) ) {
				UserTransaction tx = null;
				if ( ejbContext.getParentContext() != null ) {
					tx = ejbContext.getParentContext().getUserTransaction();
				}
				if ( tx != null && tx.getStatus() != 0 ) {
					ejbContext.setUserTransaction(tx);
					beginCommit = false;
				} else {
					throw new EJBException("Mandatory transaction not found");
				}
			} else if ( TransactionAttributeType.REQUIRED.equals(annotation.value()) ) {
				UserTransaction tx = null;
				if ( ejbContext.getParentContext() != null ) {
					tx = ejbContext.getParentContext().getUserTransaction();
				}
				if ( tx != null && tx.getStatus() != 0 ) {
					ejbContext.setUserTransaction(tx);
					beginCommit = false;
				} else {
					ejbContext.setUserTransaction(createTransaction());
					beginCommit = true;
				}
			} else if ( TransactionAttributeType.SUPPORTS.equals(annotation.value()) ) {
				UserTransaction tx = null;
				if ( ejbContext.getParentContext() != null ) {
					tx = ejbContext.getParentContext().getUserTransaction();
				}
				if ( tx != null && tx.getStatus() != 0 ) {
					ejbContext.setUserTransaction(tx);
					beginCommit = false;
				} else {
					ejbContext.setUserTransaction(null);
					beginCommit = false;
				}
			}
			
			// Execution
			Object result;
			UserTransaction tx = ejbContext.getUserTransaction();
			try {
				if ( beginCommit ) {
					tx.begin();
				}
				result = method.invoke(object, args);
				if ( beginCommit ) {
					tx.commit();
				}
				return result;
			} catch ( Throwable t ) {
				if ( tx != null ) {
					tx.rollback();
					LOGGER.info("Transaction rolled back", t);
				}
				throw t;
			}
		} else {
			return method.invoke(object, args);
		}
	}

	private UserTransaction createTransaction() {
		// TODO A coder
		return null;
	}
}
