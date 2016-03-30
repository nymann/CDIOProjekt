package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;

/**
 * 
 * @author Kim & Sandie
 * 
 * Testklasse til at prøve at navigere rundt i et afgrænset specifikt rum.
 * 
 */

public class Test1 {

	static private IARDrone drone = null;

	static public void main(String[] args) {

		// connecting to drone
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone");
			drone.start();
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}

		// getting managers
		ConfigurationManager cm = drone.getConfigurationManager();
		NavDataManager nm = drone.getNavDataManager();
		
		drone.setMaxAltitude(3000);
		
		drone.start();
		drone.hover();
		drone.forward();
		drone.stop();
		drone.spinLeft();
		drone.forward();
		drone.landing();
		drone.stop();
		
		System.exit(0);
		
	}

}
