/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import control.DroneControl;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;

/**
 *
 * @author Mikkel
 */
public class Main {

	static public void main(String[] args) {

		IARDrone drone = null;
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone");
			drone.start();
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
		
//		IExceptionListener il = new ExceptionListener();
//		drone.addExceptionListener(il);

		ConfigurationManager cm = drone.getConfigurationManager();

		System.out.println("Drone connected: " + cm.isConnected());

		System.out.println("Starting Drone Control");
		DroneControl control = new DroneControl(drone);
		control.run();

		System.out.println("Shutting down");
		cm.close();
		if (drone != null) {
			drone.stop();
		}
		System.exit(0);
	}
}
