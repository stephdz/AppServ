package org.dz.appserv;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.ejb.EJBException;

public class ClassUtil {

	public static List<Class<?>> getClasses(String packageName) {
		return getClasses(packageName, true);
	}
	
	public static List<Class<?>> getClasses(String packageName, boolean recursive) {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		String[] entries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].endsWith(".jar")) {
				classes.addAll(searchJar(entries[i], packageName, recursive));
			} else {
				classes.addAll(searchDirectory(entries[i], packageName, recursive));
			}
		}
		return classes;
	}
	
	public static List<Class<?>> searchDirectory(String directory, String packageName, boolean recursive) {
		return searchDirectory(directory, packageName, recursive, Thread.currentThread().getContextClassLoader());
	}

	public static List<Class<?>> searchDirectory(String directory, String packageName, boolean recursive, ClassLoader loader) {
		try {
			List<Class<?>> classes = new ArrayList<Class<?>>();

			// On génère le chemin absolu du package
			StringBuffer sb = new StringBuffer(directory);
			String[] repsPkg = packageName.split("\\.");
			for (int i = 0; i < repsPkg.length; i++) {
				sb.append(System.getProperty("file.separator") + repsPkg[i]);
			}
			File rep = new File(sb.toString());

			// Si le chemin existe, et que c'est un dossier, alors, on le liste
			if (rep.exists() && rep.isDirectory()) {
				FilenameFilter filter = new FilenameFilter() {
					public boolean accept(File file, String ext) {
						return file.isDirectory() || ext.endsWith(".class");
					}
				};
				File[] liste = rep.listFiles(filter);
				for (int i = 0; i < liste.length; i++) {
					if ( recursive && liste[i].isDirectory() ) {
						classes.addAll(searchDirectory(directory, packageName + "." + liste[i].getName(), recursive));
					} else {
						classes.add(Class.forName(packageName + "."	+ liste[i].getName().split("\\.")[0], true, loader));
					}
				}
			}
			return classes;
		} catch (ClassNotFoundException e) {
			throw new EJBException(e);
		}
	}
	
	public static List<Class<?>> searchJar(String jar, String packageName, boolean recursive) {
		return searchJar(jar, packageName, recursive, Thread.currentThread().getContextClassLoader());
	}

	public static List<Class<?>> searchJar(String jar, String packageName, boolean recursive, ClassLoader loader) {
		try {
			List<Class<?>> classes = new ArrayList<Class<?>>();
			JarFile jfile = new JarFile(jar);
			String pkgpath = packageName.replace(".", "/");

			// Pour chaque entrée du Jar
			for (Enumeration<JarEntry> entries = jfile.entries(); entries.hasMoreElements();) {
				JarEntry element = entries.nextElement();
				if (element.getName().startsWith(pkgpath) && ! element.isDirectory()) {
					String nomFichier = element.getName();
					if ( packageName != null && packageName.length() > 0 ) {
						nomFichier = nomFichier.substring(packageName.length() + 1);
					}
					String nomClasse = nomFichier;
					if ( packageName != null && packageName.length() > 0 ) {
						nomClasse = packageName + "." + nomClasse;
					}
					nomClasse = nomClasse.replace("/", ".").split("\\.class")[0];
					if ( recursive && element.isDirectory() ) {
						classes.addAll(searchJar(jar, packageName + "." + nomFichier, recursive));
					} else if (element.getName().endsWith(".class")) {
						classes.add(Class.forName(nomClasse, true, loader));
					}
				}
			}
			return classes;
		} catch (ClassNotFoundException e) {
			throw new EJBException(e);
		} catch (IOException e) {
			throw new EJBException(e);
		}
	}
}
