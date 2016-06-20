/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droneUtil;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.UltrasoundFrequency;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.FullGUI;
import javafx.geometry.Point2D;
import listeners.ExceptionListener;
import listeners.ListenerPack;
import modeling.MainModel;

/**
 *
 * @author Mikkel
 */
public class DroneControl {

	public static final int HOVER_HEIGHT = 1300;
	public static final int ROTATION_SPEED = -10;
	public static final double INTERVAL_ANGLE = Math.PI / 4.0;


	private IARDrone drone = null;
	private FullGUI gui;
	private final ListenerPack listenerPack;

	public DroneControl(FullGUI gui, ListenerPack listenerPack) {
		do {
			try {
				drone = new ARDrone();
			} catch (Exception exc) {
				gui.errorLn(exc.getMessage());
			}
		} while (drone == null);
		this.listenerPack = listenerPack;
	}

	public void start() {
		// connecting to drone
		ExceptionListener exceptionListener = new ExceptionListener(gui, drone);
		drone.addExceptionListener(exceptionListener);

		gui.printLn("Starting Drone.");
		drone.start();
		drone.reset();

		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException ex) {
			gui.printLn("sleep interupted");
		}

		while (!getConfigurationManager().isConnected()) {
			gui.printLn("Couldn't connect to drone, retrying connection");
			getConfigurationManager().close();
			drone.start();
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException ex) {
				gui.printLn("sleep interupted");
			}

			gui.printLn("Drone connected: " + getConfigurationManager().isConnected());
		}

		getCommandManager().emergency();
		getCommandManager().setOutdoor(false, true);
		getCommandManager().setNavDataDemo(false);
		getCommandManager().setVideoCodec(VideoCodec.H264_360P);
		getCommandManager().setUltrasoundFrequency(UltrasoundFrequency.F25Hz);
		getCommandManager().flatTrim();

		gui.printLn("Waiting for acceleration data");
		while (!listenerPack.accelerationUpdated()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}

		gui.printLn("Calibrating accelerometer");
		listenerPack.calibrateAcc(true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		gui.printLn("Done Calibrating accelerometer");
		listenerPack.calibrateAcc(false);
	}

	public void takeOff() {
		//--------------------------------------------------------------------
		// Drone taking off
		//--------------------------------------------------------------------
		gui.printLn("Taking off");
		getCommandManager().takeOff().doFor(5000);

		gui.printLn("Waiting for altitude update");
		while (!listenerPack.altitudeUpdated()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}
		gui.printLn("Altitude update recieved");

		//---------------------------------
		// Height test
		//--------------------------------
		gui.printLn("Finding correct height");
		long stableTime = 0;
		long stableSince = System.currentTimeMillis();
		while (stableTime < 250) {
			if (stabilize(HOVER_HEIGHT, 0)) {
				stableTime = System.currentTimeMillis() - stableSince;
			} else {
				stableSince = System.currentTimeMillis();
				stableTime = 0;
			}
		}
		getCommandManager().move(0, 0, 0, 0).doFor(50);
		gui.printLn("Stable hover");
	}

	public boolean stabilize(int height, int speedSpin) {
		return stabilize(height, speedSpin, false);
	}

	public boolean stabilize(int height, int speedSpin, boolean maintain) {
		if (System.currentTimeMillis() - listenerPack.getLastAltUpdate() < 500) {
			int diffHeight = listenerPack.getAltitude() - HOVER_HEIGHT;
			if (maintain && Math.abs(diffHeight) > 400) {
				diffHeight = 0;
			}

			float velocityH = listenerPack.getZVelocity();
			gui.setVelocityV(velocityH);
			if (Math.abs(diffHeight) < 20 && Math.abs(velocityH) < 50) {
				gui.setStabilityV(true);
				gui.setCounterV(0);
				return stabilizeHor(0, speedSpin);
			}

			int speed = Math.min(20, Math.abs(diffHeight) / 10);
			gui.setInfo("Diff height", diffHeight);
			gui.setInfo("Desired speed", speed);
			if (diffHeight < 0) {
				speed = -speed;
			}
			gui.setCounterV(speed);
			stabilizeHor(speed, speedSpin);
		} else {
			stabilizeHor(0, speedSpin);
		}
		gui.setStabilityV(false);
		return false;
	}

	public boolean stabilizeHor(int speedZ, int speedSpin) {
		if (System.currentTimeMillis() - listenerPack.getLastVelocityUpdate() < 500) {
			double speedX = listenerPack.getVelocity().getX() / 40.0;
			double speedY = listenerPack.getVelocity().getY() / 40.0;
			speedX -= listenerPack.getAcceleration().getX() / 50.0;
			speedY += listenerPack.getAcceleration().getY() / 50.0;

			int dirX = (int) Math.signum(speedX);
			int dirY = (int) Math.signum(speedY);

			speedX = Math.min(5, Math.abs(speedX));
			speedY = Math.min(5, Math.abs(speedY));

			int reverseX = -dirX * (int) speedX;
			int reverseY = -dirY * (int) speedY;
			gui.setCounterVelocity(reverseX, reverseY);
			getCommandManager().move(reverseY, -reverseX, speedZ, speedSpin).doFor(50);
			boolean stable = speedX < 2.0 && speedY < 2.0;
			gui.setStabilityH(stable);
			return stable;
		} else {
			getCommandManager().move(0, 0, speedZ, speedSpin).doFor(50);
			gui.setStabilityH(false);
			return false;
		}
	}

	public void turnDroneInterval() {
		double currentRotation = 0;
		double prevYaw = MainModel.getDroneAttitude().getYaw();
		int intervalCount = 1;

		gui.printLn("Spinning left");
		while (currentRotation < 2 * Math.PI) {
			double currentYaw = MainModel.getDroneAttitude().getYaw();
			if ((currentYaw - prevYaw) < -Math.PI) {
				currentRotation += currentYaw - prevYaw
						+ 2 * Math.PI;
			} else {
				currentRotation += currentYaw - prevYaw;
			}
			prevYaw = currentYaw;

			if (currentRotation > INTERVAL_ANGLE * intervalCount) {
				gui.printLn("Pause #" + intervalCount);
				intervalCount++;
				long pauseTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - pauseTime < 2000) {
					stabilize(HOVER_HEIGHT, 0, true);
				}
			}
			stabilize(HOVER_HEIGHT, ROTATION_SPEED, true);
		}
	}

	public ConfigurationManager getConfigurationManager() {
		return this.drone.getConfigurationManager();
	}

	public NavDataManager getNavDataManager() {
		return drone.getNavDataManager();
	}

	public CommandManager getCommandManager() {
		return drone.getCommandManager();
	}

	public VideoManager getVideoManager() {
		return drone.getVideoManager();
	}

	public void landing() {
		drone.landing();
		while (listenerPack.getVelocity() == null || listenerPack.getVelocity().magnitude() > 0.1) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
			drone.landing();
		}
	}

	public void stop() {
		drone.stop();
	}

}
