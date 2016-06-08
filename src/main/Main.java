/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.MainWindow;
import video.PictureAnalyser;
import video.VideoReader;

//import control.DroneControl;


/**
 *
 * @author Mikkel
 */
public class Main {

	public static Boolean downCamActive = false;

	static private IARDrone drone = null;

	static public void main(String[] args) {
		// Initialising OpenCV
		//PictureAnalyser.init();

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

		VideoManager vm = drone.getVideoManager();
		video.VideoReader videoReader = new VideoReader(vm);


		/*// get battery level
		Battery battery = new Battery();
		nm.addBatteryListener(battery);

		// get altitude height
		Altitude altitude = new Altitude();
		nm.addAltitudeListener(altitude);
		
		// get accelerometer listener
		Accelerometer accelerometer = new Accelerometer();
		nm.addAcceleroListener(accelerometer);
		
		// get ultrasound listener
		UltraSound ultrasound = new UltraSound();
		nm.addUltrasoundListener(ultrasound);
		
		// get velocity listener
		Velocity velocity = new Velocity();
		nm.addVelocityListener(velocity);*/
		

		// stop program if we get an exception
		drone.addExceptionListener(arde -> {
            System.out.println(arde.getMessage());
				//Main.shutDown();
        });

		System.out.println("Drone connected: " + cm.isConnected());

		// start drone control
		/*System.out.println("Starting Drone Control");
		DroneControl control = new DroneControl(drone);
		control.run();*/
		
		
		// Opening listener value panel
		//panel.ListenerValueGUI(200, 200);

		// Opening main window
		MainWindow window = new MainWindow(drone);
		window.run();

		/*demo.DownCamTest downCamTest = new DownCamTest(drone);
		downCamTest.run();*/
	}
	
	public static void shutDown(){
		// shut down
		System.out.println("Shutting down");
		drone.stop();
		System.exit(0);
	}
}
