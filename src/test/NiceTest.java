package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.TextPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import listeners.Attitude;
import listeners.ExceptionListener;
import modeling.MainModel;
import video.VideoReader;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest {

	static TextPanel output, exceptionOut;
	
	public static void main(String[] args) {
		IARDrone drone = null;
		ConfigurationManager configurationManager = null;
		

		output = new TextPanel();
		exceptionOut = new TextPanel();
		
		Dimension outputSize = new Dimension(300, 500);
		output.setPreferredSize(outputSize);
		exceptionOut.setPreferredSize(outputSize);
		
		JFrame mainWindow = new JFrame();
		mainWindow.getContentPane().setLayout(new FlowLayout());
		mainWindow.getContentPane().add(output);
		mainWindow.getContentPane().add(exceptionOut);
		mainWindow.setVisible(true);
		mainWindow.pack();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ExceptionListener exceptionListener = new ExceptionListener(exceptionOut);
		
		// connecting to drone
		do {
			try {
				drone = new ARDrone();
			} catch (Exception exc) {
				System.err.println("Error: " + exc.getMessage());
				exc.printStackTrace();
			}
		} while (drone == null);
		
		drone.addExceptionListener(exceptionListener);

		output.addTextLine("Starting Drone.");
		drone.start();
		drone.reset();

		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException ex) {
			output.addTextLine("sleep interupted");
		}

		configurationManager = drone.getConfigurationManager();
		while (!configurationManager.isConnected()) {
			output.addTextLine("Couldn't connect to drone, retrying connection");
			configurationManager.close();
			drone.start();
			configurationManager = drone.getConfigurationManager();

			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException ex) {
				output.addTextLine("sleep interupted");
			}

			output.addTextLine("Drone connected: " + configurationManager.isConnected());
		}

		try {
			doStuff(drone);
		} catch (Exception e) {

		}

		drone.landing();
		drone.stop();
		System.exit(0);
	}

	private static void doStuff(IARDrone drone) {
		NavDataManager navDataManager = drone.getNavDataManager();
		VideoManager videoManager = drone.getVideoManager();
		CommandManager commandManager = drone.getCommandManager();

		MainModel model = new MainModel();
		VideoReader videoReader = new VideoReader(videoManager, commandManager);
		Attitude att = new Attitude(model);
		navDataManager.addAttitudeListener(att);
		commandManager.setOutdoor(false, true);

		output.addTextLine("Taking off");
		commandManager.takeOff().doFor(5000);
		//commandManager.hover();
		//commandManager.backward(5);
		
/*		output.addTextLine("waiting for take off");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		double startYaw = model.getDroneAttitude().getYaw() + Math.PI;
		output.addTextLine("Spinning left");
		commandManager.spinLeft(50).doFor(1000);

/*		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		


		double currentYaw = model.getDroneAttitude().getYaw() + Math.PI;
		output.addTextLine("Starting yaw difference:" + (currentYaw - startYaw));
		int qRCodesFound = 0;
		while (Math.abs(startYaw - currentYaw) > 0.01) {

			/*if (model.getDroneAttitude() != null) {
				output.addTextLine("Yaw = " + model.getDroneAttitude().getYaw());
				QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(videoReader.getImage());
				if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
					output.addTextLine("Decodemessage: " + qrInfo.name + ". At: "
							+ qrInfo.x + ", " + qrInfo.y);
					qRCodesFound++;
				} else {
					output.addTextLine(qrInfo.error);
				}
			}*/
			currentYaw = model.getDroneAttitude().getYaw() + Math.PI;
		}
		output.addTextLine("QR-codes found: " + qRCodesFound);

	}
}
