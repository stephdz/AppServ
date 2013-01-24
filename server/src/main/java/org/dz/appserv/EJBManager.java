package org.dz.appserv;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.dz.appserv.client.EJBClientInvocationHandler;
import org.dz.appserv.jdbc.DriverAdapter;

public class EJBManager {
	
	private static final Logger LOGGER = Logger.getLogger(EJBManager.class);
	
	public static final String REMOTE_JNDI_SUFFIX = "/remote";
	public static final String LOCAL_JNDI_SUFFIX = "/local";
	public static final String ENTITY_MANAGER_FACTORY_PREFIX = "javax.persistence.EntityManagerFactory";
	public static final String SERVER_DEPLOY_DIR = "deploy";
	public static final String SERVER_LIBRARY_DIR = "lib";
	
	private Map<Class<?>,Object> statelessObjects;
	private Map<Class<?>,Object> statelessEJBs;
	private Context context;
	
	/**
	 * Singleton
	 */
	private static EJBManager instance;
	private EJBManager() throws EJBException {
		try {
			this.statelessObjects = new HashMap<Class<?>,Object>();
			this.statelessEJBs = new HashMap<Class<?>,Object>();
			this.context = new InitialContext();
		} catch ( NamingException e ) {
			throw new EJBException(e);
		}
	}
	
	public static EJBManager getInstance() {
		if ( instance == null ) {
			instance = new EJBManager();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> clazz) {
		if ( statelessObjects.containsKey(clazz) ) {
			return (T) statelessObjects.get(clazz);
		} else {
			return createObject(clazz);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getStatelessEJB(Class<T> clazz) {
		if ( statelessEJBs.containsKey(clazz) ) {
			return (T) statelessEJBs.get(clazz);
		} else {
			throw new EJBException("Stateless EJB "+clazz+" not bound");
		}
	}
	
	public void deploy(String name) {
		File file = new File(name);
		if ( file.exists() ) {
			if ( file.isDirectory() ) {
				deployServer(name);
			} else if ( name.endsWith(".jar") ) {
				deployJar(file);
			}
		} else {
			deployPackage(name);
		}
	}
	
	public void deployServer(String serverName) {
		LOGGER.info("Loading server "+serverName+" librairies");
		File libDir = new File(serverName+File.separator+SERVER_LIBRARY_DIR);
		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		};
		for ( File jar : libDir.listFiles(jarFilter) ) {
			addLibrary(jar);
		}
		LOGGER.info("Server "+serverName+" librairies loaded");
		prepareJdbcDrivers();
		LOGGER.info("Deploying server "+serverName);
		File deployDir = new File(serverName+File.separator+SERVER_DEPLOY_DIR);
		for ( File jar : deployDir.listFiles(jarFilter) ) {
			deployJar(jar);
		}
		LOGGER.info("Server "+serverName+" deployed");
	}

	public void deployJar(File jar) {
		try {
			LOGGER.info("Deploying jar "+jar.getAbsolutePath());
			ServerClassLoader.getServerClassLoader().addURL(jar.toURI().toURL());
			deployClasses(ClassUtil.searchJar(jar.getAbsolutePath(), "", true));
			LOGGER.info("Jar "+jar.getAbsolutePath()+" deployed");
		} catch (MalformedURLException e) {
			throw new EJBException(e);
		}
	}
	
	public void deployPackage(String packageName) {
		LOGGER.info("Deploying package "+packageName);
		deployClasses(ClassUtil.getClasses(packageName));
		LOGGER.info("Package "+packageName+" deployed");
	}
	
	/**
	 * Préparation des drivers JDBC
	 * TODO A externaliser et utiliser une conf
	 */
	private void prepareJdbcDrivers() {
		String driverClassName = "org.hsqldb.jdbcDriver";
		Driver d;
		try {
			d = (Driver) Class.forName(driverClassName, true, ServerClassLoader.getServerClassLoader()).newInstance();
			DriverManager.registerDriver(new DriverAdapter(d));
			LOGGER.info("JDBC driver "+driverClassName+" successfully registered");
		} catch (Exception e) {
			throw new EJBException("Unable to prepare JDBC Driver "+driverClassName, e);
		}
	}
	
	private void deployClasses(List<Class<?>> classes) {
		List<Class<?>> toBeImplemented = new ArrayList<Class<?>>();
		for ( Class<?> clazz : classes) {
			// TODO Gérer les statefull
			if ( clazz.getAnnotation(Stateless.class) != null ) {
				createStatelessEJB(clazz);
			} else if ( clazz.getAnnotation(Local.class) != null || clazz.getAnnotation(Remote.class) != null ){
				toBeImplemented.add(clazz);
			}
		}
		for ( Class<?> clazz : toBeImplemented ) {
			try {
				getEJB(clazz);
			} catch (EJBException e) {
				throw new EJBException("Class "+clazz+" must be implemented", e);
			}
		}
	}
	
	private void addLibrary(File jar) {
		try {
			ServerClassLoader.getServerClassLoader().addURL(jar.toURI().toURL());
			LOGGER.info("Library "+jar.getAbsolutePath()+" added");
		} catch (MalformedURLException e) {
			throw new EJBException(e);
		}
	}
	
	private <T> T createObject(Class<T> clazz) {
		try {
			T object = clazz.newInstance();
			inject(object);
			statelessObjects.put(clazz, object);
			return object;
		} catch (InstantiationException e) {
			throw new EJBException(e);
		} catch (IllegalAccessException e) {
			throw new EJBException(e);
		}
	}
	
	private <T> T inject(T object) {
		if ( object != null ) {
			for ( Field field : object.getClass().getDeclaredFields() ) {
				if ( field.getAnnotation(EJB.class) != null ) {
					fillField(object, field, getEJB((Class<?>)field.getGenericType()));
				} else if ( field.getAnnotation(PersistenceContext.class) != null ) {
					fillField(object, field, getEntityManager(field.getAnnotation(PersistenceContext.class)));
				}
			}
			return object;
		} else {
			throw new EJBException("Unable to inject EJBs into null object");
		}
	}
	
	private EntityManager getEntityManager(PersistenceContext annotation) {
		return getEntityManagerFactory(annotation.unitName()).createEntityManager();
	}
	
	// TODO Close sur les entity manager et entity manager factory
	private EntityManagerFactory getEntityManagerFactory(String unitName) {
		try {
			String jndiName = ENTITY_MANAGER_FACTORY_PREFIX;
			if ( unitName != null && unitName.length() > 0 ) {
				jndiName += "." + unitName;
			}
			EntityManagerFactory factory = (EntityManagerFactory) context.lookup(jndiName);
			if ( factory == null ) {
				factory = Persistence.createEntityManagerFactory(unitName);
				if ( factory != null ) {
					context.bind(jndiName, factory);
				}
			}
			if ( factory == null ) {
				throw new EJBException("No persistence context defined for this unit : "+unitName);
			}
			return factory;
		} catch ( NamingException e ) {
			throw new EJBException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getEJB(Class<T> clazz) {
		// TODO Gérer la distinction Local Remote
		if ( statelessEJBs.containsKey(clazz) ) {
			return (T) statelessEJBs.get(clazz);
		} else {
			throw new EJBException("Unable to find implementation for EJB "+clazz.getName());
		}
	}
	
	private <T> T createStatelessEJB(Class<T> clazz) {
		LOGGER.info("EJB : "+clazz);
		T object = createObject(clazz);
		statelessEJBs.put(clazz, object);
		for ( Class<?> interf : clazz.getInterfaces() ) {
			if ( interf.getAnnotation(Local.class) != null || interf.getAnnotation(Remote.class) != null ) {
				registerStatelessEJB(interf, object);
			}
		}
		return object;
	}
	
	private void registerStatelessEJB(Class<?> clazz, Object object) {
		if ( clazz.getAnnotation(Local.class) != null || clazz.getAnnotation(Remote.class) != null ) {
			if ( ! statelessEJBs.containsKey(clazz) ) {
				statelessEJBs.put(clazz, object);
				registerJndiEJB(clazz, object);
				LOGGER.info("\t - "+clazz+" => "+object.getClass());
			} else if ( ! statelessEJBs.get(clazz).getClass().equals(object.getClass()) ) {
				throw new EJBException("Multiple implementation of class "+clazz+" : "+statelessEJBs.get(clazz)+", "+object.getClass());
			}
		}
		for ( Class<?> interf : clazz.getInterfaces() ) {
			registerStatelessEJB(interf, object);
		}
	}
	
	private void registerJndiEJB(Class<?> clazz, Object object) {
		if ( clazz.getAnnotation(Local.class) != null ) {
			try {
				context.bind(clazz.getSimpleName()+LOCAL_JNDI_SUFFIX, object);
			} catch (NamingException e) {
				throw new EJBException(e);
			}
		} else if ( clazz.getAnnotation(Remote.class) != null ) {
			try {
				Object proxy = Proxy.newProxyInstance (
						clazz.getClassLoader(),
						new Class<?>[]{clazz},
						new EJBClientInvocationHandler(clazz) );
				context.bind(clazz.getSimpleName()+REMOTE_JNDI_SUFFIX, proxy);
			} catch (NamingException e) {
				throw new EJBException(e);
			}
		} else {
			throw new EJBException(clazz+" is not an EJB interface");
		}
	}
	
	private void fillField(Object object, Field field, Object value) {
		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(object, value);
			field.setAccessible(accessible);
		} catch (IllegalArgumentException e) {
			throw new EJBException(e);
		} catch (IllegalAccessException e) {
			throw new EJBException(e);
		}
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
}
