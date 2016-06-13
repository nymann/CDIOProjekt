/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import listeners.Attitude;

import modeling.MainModel;

/**
 *
 * @author Mikkel
 */
public class YawTest {

	public static void main(String[] args) {
		IARDrone drone = null;
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

		System.out.println("Drone connected: " + cm.isConnected());
		MainModel model = new MainModel();
		Attitude att = new Attitude(model);
		nm.addAttitudeListener(att);
		
		while (true) {
			if (model.getDroneAttitude() != null){
				System.out.println("Yaw = " + model.getDroneAttitude().getYaw());
			}
			try {
				Thread.currentThread().wait(500);
			} catch (Exception e){
				
			}
		}

	}
}
