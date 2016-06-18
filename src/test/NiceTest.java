package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.UltrasoundFrequency;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.DroneStateListener;
import gui.InfoPanel;
import gui.TextPanel;
import gui.VelocityPanel;
import listeners.*;
import modeling.MainModel;
import video.VideoReader;

import javax.swing.*;
import java.awt.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import static test.NiceTest.velocityPanel;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest {

	static TextPanel output, exceptionOut;
	static InfoPanel infoPanel;
	static VelocityPanel velocityPanel;
	static UltraSound ult = new UltraSound();
	static Altitude alt = new Altitude();
	static Battery bat = new Battery();
	static NavDataManager navDataManager;
	static VideoManager videoManager;
	static CommandManager commandManager;
	static ConfigurationManager configurationManager;
	static final double movingSpeed = 50.0;
	static final double takeOffSpeed = 100.0;
	static final double hoverSpeed = 40.0;
	static final int hoverHeight = 1300;

	public static void main(String[] args) {
		IARDrone drone = null;




		output = new TextPanel();
		exceptionOut = new TextPanel();
		Dimension outputSize = new Dimension(300, 600);
		output.setPreferredSize(outputSize);
		exceptionOut.setPreferredSize(outputSize);
		
		TextPanel droneStatus = new TextPanel();
		DroneStateListener dsl = new DroneStateListener(droneStatus);
		droneStatus.setPreferredSize(outputSize);
		droneStatus.setSize(outputSize);

		infoPanel = new InfoPanel();
		infoPanel.setColumns(80);
		Dimension infoDimension = new Dimension(900, 200);
		infoPanel.setPreferredSize(infoDimension);
		infoPanel.setSize(infoDimension);

		velocityPanel = new VelocityPanel();
		Dimension velocityDimension = new Dimension(200, 200);
		velocityPanel.setSize(velocityDimension);
		velocityPanel.setPreferredSize(velocityDimension);

		JFrame mainWindow = new JFrame();
		mainWindow.getContentPane().setLayout(new FlowLayout());
		mainWindow.getContentPane().add(output);
		mainWindow.getContentPane().add(exceptionOut);
		mainWindow.getContentPane().add(droneStatus);
		mainWindow.getContentPane().add(velocityPanel);
		mainWindow.getContentPane().add(infoPanel);
		mainWindow.setVisible(true);
		mainWindow.pack();
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// connecting to drone
		do {
			try {
				drone = new ARDrone();
			} catch (Exception exc) {
				System.err.println("Error: " + exc.getMessage());
				exc.printStackTrace();
			}
		} while (drone == null);

		ExceptionListener exceptionListener = new ExceptionListener(exceptionOut, drone);
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

		navDataManager = drone.getNavDataManager();
		videoManager = drone.getVideoManager();
		commandManager = drone.getCommandManager();
		navDataManager.addVelocityListener(velocityPanel);
		navDataManager.addUltrasoundListener(ult);
		navDataManager.addAltitudeListener(alt);
		navDataManager.addStateListener(dsl);
		navDataManager.addBatteryListener(bat);

		commandManager.setMaxAltitude(2000);
		commandManager.emergency();
//		commandManager.setNavDataDemo(true);

		/*		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) {
		}*/
		try {
			doStuff(drone);
		} catch (Exception e) {
			exceptionOut.addTextLine(e.getMessage());
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				exceptionOut.addTextLine(stackTrace[i].toString());
			}
		}

		output.addTextLine("Landing drone");
		drone.landing();
		while (velocityPanel.velocity == null || velocityPanel.velocity.magnitude() > 0.1) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
			drone.landing();
		}
		output.addTextLine("Stopping drone");
		drone.stop();
	}

	private static void doStuff(IARDrone drone) {

//		VideoReader videoReader = new VideoReader(videoManager, commandManager);
		Attitude att = new Attitude();
		navDataManager.addAttitudeListener(att);
		commandManager.setOutdoor(false, true);
		commandManager.setNavDataDemo(false);

//		drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
		// Sets the camera to 720p instead of stretching a 640x360 image.
		commandManager.setUltrasoundFrequency(UltrasoundFrequency.F25Hz);

		Runnable infoUpdate = () -> {
			while (true) {
				NiceTest.infoPanel.setInfo("Batery Level:", NiceTest.bat.level);

				if (NiceTest.velocityPanel.velocity != null) {
					Point3D velocity = NiceTest.velocityPanel.velocity;
					NiceTest.infoPanel.setInfo("Speed X", velocity.getX());
					NiceTest.infoPanel.setInfo("Speed Y", velocity.getY());
				} else {
					NiceTest.infoPanel.setInfo("Speed X", "null");
					NiceTest.infoPanel.setInfo("Speed Y", "null");
				}

				if (NiceTest.alt.extendedAltitude != null) {
					NiceTest.infoPanel.setInfo("Altitude", NiceTest.alt.extendedAltitude.getRaw());
					NiceTest.infoPanel.setInfo("Z Velocity", NiceTest.alt.extendedAltitude.getZVelocity());
				} else {
					NiceTest.infoPanel.setInfo("Altitude", "null");
				}

			}
		};
		new Thread(infoUpdate).start();

		output.addTextLine("Taking off");
//		commandManager.takeOff();
		commandManager.takeOff().doFor(5000);

		output.addTextLine("Waiting for altitude update");
		while (alt.extendedAltitude == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}
		output.addTextLine("Altitude update recieved");

		//---------------------------------
		// Height test
		//--------------------------------
		output.addTextLine("Finding correct height");
		long stableTime = 0;
		long stableSince = System.currentTimeMillis();
		while (stableTime < 1000) {
			if (stabilize(hoverHeight, 0)) {
				stableTime = System.currentTimeMillis() - stableSince;
			} else {
				stableSince = System.currentTimeMillis();
				stableTime = 0;
			}
		}
		commandManager.move(0, 0, 0, 0).doFor(50);
		output.addTextLine("Stable hover");

/*		//---------------------------------
		// Direction Test
		//--------------------------------

		output.addTextLine("Moving forward");
		commandManager.move(20, 0, 0, 0).doFor(1000);
		output.addTextLine("Moving backwards");
		commandManager.move(-20, 0, 0, 0).doFor(1000);
		output.addTextLine("Moving rigt");
		commandManager.move(0, -20, 0, 0).doFor(1000);
		output.addTextLine("Moving left");
		commandManager.move(0, 20, 0, 0).doFor(1000);*/
		
		double startYaw = MainModel.getDroneAttitude().getYaw() + Math.PI;
		output.addTextLine("Spinning left");
		stabilizeHor(0, -50);

		output.addTextLine("Waiting for difference");
		double currentYaw;
		do {
			currentYaw = MainModel.getDroneAttitude().getYaw() + Math.PI;
			stabilizeHor(0, -50);
		} while (Math.abs(startYaw - currentYaw) < 0.05);

		output.addTextLine("Starting yaw difference:" + (currentYaw - startYaw));

		int qRCodesFound = 0;

		while (Math.abs(startYaw - currentYaw) > 0.05) {

			stabilizeHor(0, -50);

			// output.addTextLine("Current Yaw = " + currentYaw);
			/*QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(videoReader.getImage());
				if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
					output.addTextLine("Decodemessage: " + qrInfo.name + ". At: "
							+ qrInfo.x + ", " + qrInfo.y);
					qRCodesFound++;
				} else {
					output.addTextLine(qrInfo.error);
				}*/
			currentYaw = MainModel.getDroneAttitude().getYaw() + Math.PI;
			infoPanel.setInfo("Current Yaw", currentYaw);
			infoPanel.setInfo("Yaw difference", Math.abs(startYaw - currentYaw));
		}
		output.addTextLine("QR-codes found: " + qRCodesFound);
		commandManager.move(0, 0, -20, 0).doFor(50);

	}

	public static boolean stabilize(int height, int speedSpin) {
		if (System.currentTimeMillis() - alt.getLastUpdate() < 500) {
			int diffHeight = alt.extendedAltitude.getRaw() - hoverHeight;
			if (Math.abs(diffHeight) < 20 && Math.abs(alt.extendedAltitude.getZVelocity()) < 50) {
				return stabilizeHor(0, speedSpin);
			}

			int speed = Math.min(30, Math.abs(diffHeight) / 10);
			infoPanel.setInfo("Diff height", diffHeight);
			infoPanel.setInfo("Desired speed", speed);
			if (diffHeight < 0) {
				speed = -speed;
			}
			stabilizeHor(speed, speedSpin);
		} else {
			stabilizeHor(0, speedSpin);
		}
		return false;
	}

	public static boolean stabilizeHor(int speedZ, int speedSpin) {
		if (System.currentTimeMillis() - NiceTest.velocityPanel.updated < 500) {
			double speedX = NiceTest.velocityPanel.velocity.getX() / 20.0;
			double speedY = NiceTest.velocityPanel.velocity.getY() / 20.0;

			int dirX = (int) Math.signum(speedX);
			int dirY = (int) Math.signum(speedY);

			speedX = Math.min(20, Math.abs(speedX));
			speedY = Math.min(20, Math.abs(speedY));

			int reverseX = -dirX * (int) speedX;
			int reverseY = -dirY * (int) speedY;
			velocityPanel.setCounterVelocity(new Point2D(reverseX, reverseY));
//			commandManager.move(reverseX, reverseY, speedZ, speedSpin).doFor(100);
			commandManager.move(reverseY, -reverseX, speedZ, speedSpin).doFor(100);
			return speedX < 1.0 && speedY < 1.0;
		} else {
			commandManager.move(0, 0, speedZ, speedSpin).doFor(50);
			return false;
		}
	}
}

/*		//---------------------------------
		// Direction Test
		//--------------------------------

		output.addTextLine("Moving forward");
		commandManager.move(20, 0, 0, 0).doFor(1000);
		output.addTextLine("Moving backwards");
		commandManager.move(-20, 0, 0, 0).doFor(1000);
		output.addTextLine("Moving rigt");
		commandManager.move(0, -20, 0, 0).doFor(1000);
		output.addTextLine("Moving left");
		commandManager.move(0, 20, 0, 0).doFor(1000);*/

