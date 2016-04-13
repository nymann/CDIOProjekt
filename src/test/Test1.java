package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.LEDAnimation;
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

	static IARDrone drone = null;

	static public void main(String[] args) {

		// connecting to drone
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone>>>>>>>");
			//drone.setMaxAltitude(2000);
			//drone.setSpeed(15);
			
			System.out.println("Speed: " + drone.getSpeed());
			drone.start();
			drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
			//drone.takeOff();
			//drone.up();
			//drone.hover();
			//drone.landing();
			//drone.wait();
			//System.out.println("HEEEEEEEEEEEEEEEEEEEEEJ__________----------");
			
		} catch (Exception exc) {
			System.out.println("Drone catch!!!!!");
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
/*		finally
		{
			System.out.println("Drone Finally!!!!!");
			if (drone != null)
				drone.stop();
			System.out.println("Drone Stop!!!!!");
			System.exit(0);
		}
*/
		// getting managers
		//ConfigurationManager cm = drone.getConfigurationManager();
		//NavDataManager nm = drone.getNavDataManager();
		
		//drone.setMaxAltitude(3000);
		
		//drone.setSpeed(10000);
		//cm.start();
		
		//drone.forward();
		//drone.stop();
		//drone.spinLeft();
		//drone.forward();
		//drone.landing();
		//drone.stop();
		
		//System.exit(0);
		
	}

}
