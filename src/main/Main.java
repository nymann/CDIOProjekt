/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

//import control.DroneControl;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.MainWindow;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import listeners.Accelerometer;
import listeners.Battery;
import video.VideoReader;


/**
 *
 * @author Mikkel
 */
public class Main {

	static public boolean done = false;

	static public void main(String[] args) {

		// connecting to drone
		IARDrone drone = null;
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone");
			drone.start();
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
			Main.done = true;
		}

		// getting managers
		ConfigurationManager cm = drone.getConfigurationManager();
		NavDataManager nm = drone.getNavDataManager();
		VideoManager vm = drone.getVideoManager();
		
		// get battery level
		Battery battery = new Battery();
		nm.addBatteryListener(battery);
		
		// get accelerometer
		Accelerometer accelerometer = new Accelerometer();
		nm.addAcceleroListener(accelerometer);
		
		// stop program if we get an exception
		drone.addExceptionListener(new IExceptionListener() {
			@Override
			public void exeptionOccurred(ARDroneException arde) {
				System.out.println(arde.getMessage());
				Main.done = true;
			}
		});

		System.out.println("Drone connected: " + cm.isConnected());

		// start drone control
		/*System.out.println("Starting Drone Control");
		DroneControl control = new DroneControl(drone);
		control.run();*/
		
		//connecting video
		System.out.println("Conneting video manager");
		VideoReader vr = new VideoReader(vm);
		vr.run();
		
		// Opening main window
		MainWindow window = new MainWindow();
		window.setVisible(true);
		Graphics graphics = window.getGraphics();

		long lastShown = System.currentTimeMillis();
		// draw window until we stop the program
		while(!Main.done){
			if(vr.getImageTime() <= lastShown){
				System.out.println("No image ready");
				try { Thread.sleep(16);} catch (Exception e) {}
				continue;
			}
			lastShown = System.currentTimeMillis();
			BufferedImage image = vr.getImage();
			//System.out.println("new image ready");
			graphics.drawImage(image, 0, 0, window);
		}

		// shut down
		System.out.println("Shutting down");
		cm.close();
		drone.stop();
		System.exit(0);
	}
}
