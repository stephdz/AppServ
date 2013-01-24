package org.dz.appserv.client.test;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.dz.appserv.client.EJBClientConfiguration;
import org.dz.appserv.client.test.dto.DriverDTO;
import org.dz.launcher.Task;
import org.dz.launcher.TaskException;



public class EJBTest implements Task {
	
	public static final int NB_RANDOM_NUMBERS = 10000;

	@Override
	public void run(List<String> args) throws TaskException {
		try {
			Context context = new InitialContext();
			EJBClientConfiguration.setContext(context);
			EJBCoucheHauteRemote beanRemote = (EJBCoucheHauteRemote) context.lookup(EJBCoucheHauteRemote.class.getSimpleName()+"/remote");
			System.out.println(beanRemote.hello("Joe"));
			System.out.println("Demande de "+NB_RANDOM_NUMBERS+" nombres aléatoires : ");
			List<Integer> rand = beanRemote.getRandomNumbers(1, 10, NB_RANDOM_NUMBERS);
			System.out.println("\t- Premier : "+rand.get(0));
			System.out.println("\t- Dernier : "+rand.get(NB_RANDOM_NUMBERS-1));
			System.out.println("Statistiques :");
			Map<Integer,Double> stat = beanRemote.getRandomStatistics(1, 10, NB_RANDOM_NUMBERS);
			double total = 0;
			for ( Integer nb : stat.keySet() ) {
				System.out.println("\t- "+nb+" : "+stat.get(nb)+"%");
				total += stat.get(nb);
			}
			System.out.println("\tTotal : "+Math.round(total*100.0d)/100.0d+"%");
			System.out.println("Création des conducteurs :");
			beanRemote.createDrivers();
			List<DriverDTO> drivers = beanRemote.getDrivers();
			for ( DriverDTO driver : drivers ) {
				System.out.println("\t"+driver.getId()+" : "+driver.getCars());
			}
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}

}
