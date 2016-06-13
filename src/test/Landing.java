/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;

/**
 *
 * @author Mikkel
 */
public class Landing {

	public static void main(String[] args) {
		IARDrone drone = null;

		// connecting to drone
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone.");
			drone.start();
		} catch (Exception exc) {
			System.err.println("Error: " + exc.getMessage());
			exc.printStackTrace();
		}

		// getting managers
		assert drone != null;
		drone.getCommandManager().landing();
	}

}
