package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.UltrasoundFrequency;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import dronePosition.QRPositioning;
import gui.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import listeners.*;
import modeling.MainModel;
import video.VideoReader;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nymann on 13-06-2016.
 */
public class QRDownCamTestAirfields {

    static TextPanel output, exceptionOut;
    static InfoPanel infoPanel;
    static VelocityPanel velocityPanel;
    static VideoPanel video = new VideoPanel();
    static UltraSound ult = new UltraSound();
    static Altitude alt = new Altitude();
    static Battery bat = new Battery();
    static Accelerometer acc = new Accelerometer();
    static NavDataManager navDataManager;
    static VideoManager videoManager;
    static CommandManager commandManager;
    static ConfigurationManager configurationManager;

    static final double movingSpeed = 50.0;
    static final double takeOffSpeed = 100.0;
    static final double hoverSpeed = 40.0;
    static final int hoverHeight = 1300;
    static final int rotationSpeed = -10;
    static final double intervalAngle = Math.PI / 4.0;

    public static void main(String[] args) {
        IARDrone drone = null;
        MainModel.init();

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

        video.setSize(new Dimension(640, 360));
        video.setPreferredSize(new Dimension(640, 360));

        JFrame mainWindow = new JFrame();
        JFrame videoFrame = new JFrame();
        JFrame velocityFrame = new JFrame();
        JFrame infoFrame = new JFrame();

        mainWindow.getContentPane().setLayout(new FlowLayout());
        videoFrame.getContentPane().setLayout(new FlowLayout());
        velocityFrame.getContentPane().setLayout(new FlowLayout());
        infoFrame.getContentPane().setLayout(new FlowLayout());

        mainWindow.getContentPane().add(output);
        mainWindow.getContentPane().add(exceptionOut);
        mainWindow.getContentPane().add(droneStatus);
        videoFrame.getContentPane().add(video);
        velocityFrame.getContentPane().add(velocityPanel);
        infoFrame.getContentPane().add(infoPanel);

        mainWindow.setVisible(true);
        videoFrame.setVisible(true);
        velocityFrame.setVisible(true);
        infoFrame.setVisible(true);

        videoFrame.setLocation(920, 0);
        velocityFrame.setLocation(920, 400);
        infoFrame.setLocation(0, 650);

        mainWindow.setTitle("Nicetest Main");
        videoFrame.setTitle("Nicetest Video");
        velocityFrame.setTitle("Nicetest Velocity");
        infoFrame.setTitle("Nicetest Info");

        mainWindow.pack();
        videoFrame.pack();
        velocityFrame.pack();
        infoFrame.pack();

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
        navDataManager.addAcceleroListener(acc);

        commandManager.emergency();

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

        Attitude att = new Attitude();
        navDataManager.addAttitudeListener(att);
        commandManager.setOutdoor(false, true);
        commandManager.setNavDataDemo(false);

        // Sets the camera to 720p instead of stretching a 640x360 image.
        drone.getCommandManager().setVideoCodec(VideoCodec.H264_360P);

        commandManager.setUltrasoundFrequency(UltrasoundFrequency.F25Hz);

        Runnable infoUpdate = () -> {
            while (true) {
                NiceTest.infoPanel.setInfo("Batery Level:", NiceTest.bat.level);

				/*				if (NiceTest.velocityPanel.velocity != null) {
					Point3D velocity = NiceTest.velocityPanel.velocity;
					NiceTest.infoPanel.setInfo("Speed X", velocity.getX());
					NiceTest.infoPanel.setInfo("Speed Y", velocity.getY());
				} else {
					NiceTest.infoPanel.setInfo("Speed X", "null");
					NiceTest.infoPanel.setInfo("Speed Y", "null");
				}*/
                if (NiceTest.alt.extendedAltitude != null) {
                    NiceTest.infoPanel.setInfo("Altitude", NiceTest.alt.extendedAltitude.getRaw());
                    //NiceTest.infoPanel.setInfo("Z Velocity", NiceTest.alt.extendedAltitude.getZVelocity());
                } else {
                    NiceTest.infoPanel.setInfo("Altitude", "null");
                }

                if (NiceTest.acc.acchysd != null) {
                    try {
                        NiceTest.infoPanel.setInfo("Acceleration Phys 0", NiceTest.acc.getCalibratedPhys()[0]);
                        NiceTest.infoPanel.setInfo("Acceleration Phys 1", NiceTest.acc.getCalibratedPhys()[1]);
                        NiceTest.infoPanel.setInfo("Acceleration Phys 2", NiceTest.acc.getCalibratedPhys()[2]);

                        NiceTest.infoPanel.setInfo("Acceleration Raw 0", NiceTest.acc.getCalibratedRaw()[0]);
                        NiceTest.infoPanel.setInfo("Acceleration Raw 1", NiceTest.acc.getCalibratedRaw()[1]);
                        NiceTest.infoPanel.setInfo("Acceleration Raw 2", NiceTest.acc.getCalibratedRaw()[2]);

                        NiceTest.velocityPanel.setAccelPhys(NiceTest.acc.getCalibratedPhys());
                        NiceTest.velocityPanel.setAccelRaw(NiceTest.acc.getCalibratedRaw());
                    } catch (Exception e){

                    }
                } else {
                    NiceTest.infoPanel.setInfo("Acceleration Phys 0", "null");
                    NiceTest.infoPanel.setInfo("Acceleration Phys 1", "null");
                    NiceTest.infoPanel.setInfo("Acceleration Phys 2", "null");

                    NiceTest.infoPanel.setInfo("Acceleration Raw 0", "null");
                    NiceTest.infoPanel.setInfo("Acceleration Raw 1", "null");
                    NiceTest.infoPanel.setInfo("Acceleration Raw 2", "null");
                }

            }
        };
        new Thread(infoUpdate).start();

        output.addTextLine("Waiting for acceleration data");
        while (acc.acchysd == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        output.addTextLine("Calibrating accelerometer");
        acc.calibration(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        output.addTextLine("Done Calibrating accelerometer");
        acc.calibration(false);

        //--------------------------------------------------------------------
        // Drone taking off
        //--------------------------------------------------------------------
        output.addTextLine("Taking off");
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
        while (stableTime < 250) {
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
        //--------------------------------------
        // QR positioning stuff
        //--------------------------------------
        QRPositioning qrpos = new QRPositioning();
        VideoReader videoReader = new VideoReader(videoManager, commandManager);
        videoManager.addImageListener(videoReader);
        videoReader.setCamMode(false);
        videoReader.addListener(qrpos);
        qrpos.setOutput(output);
        att.addListener(qrpos);
        videoReader.addListener(video);

        //---------------------------------------------------------------------
        // Turn around while looking for QR codes
        //---------------------------------------------------------------------
        double currentRotation = 0;
        double prevYaw = MainModel.getDroneAttitude().getYaw();
        int intervalCount = 1;

        output.addTextLine("Spinning left!");
        while (currentRotation < 2 * Math.PI) {
            double currentYaw = MainModel.getDroneAttitude().getYaw();
            if ((currentYaw - prevYaw) < -Math.PI) {
                currentRotation += currentYaw - prevYaw
                        + 2 * Math.PI;
            } else {
                currentRotation += currentYaw - prevYaw;
            }
            prevYaw = currentYaw;

            if (currentRotation > intervalAngle * intervalCount) {
                output.addTextLine("Pause no.:" + intervalCount);
                intervalCount++;
                long pauseTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - pauseTime < 2000) {
                    stabilize(hoverHeight, 0, true);
                }
            }
            stabilize(hoverHeight, rotationSpeed, true);
        }

        output.addTextLine("QR-codes found: " + qrpos.getQRCount());
        commandManager.move(0, 0, -20, 0).doFor(50);
    }

    public static boolean stabilize(int height, int speedSpin) {
        return stabilize(height, speedSpin, false);
    }

    public static boolean stabilize(int height, int speedSpin, boolean maintain) {
        if (System.currentTimeMillis() - alt.getLastUpdate() < 499) {
            int diffHeight = alt.extendedAltitude.getRaw() - hoverHeight;
            if (maintain && Math.abs(diffHeight) > 401) {
                diffHeight = 0;
            }

            if (Math.abs(diffHeight) < 20 && Math.abs(alt.extendedAltitude.getZVelocity()) < 50) {
                NiceTest.velocityPanel.setStabilityV(true);
                return stabilizeHor(0, speedSpin);
            }

            int speed = Math.min(20, Math.abs(diffHeight) / 10);
            infoPanel.setInfo("Diff height", diffHeight);
            infoPanel.setInfo("Desired speed", speed);
            if (diffHeight < 0) {
                speed = -speed;
            }
            stabilizeHor(speed, speedSpin);
        } else {
            stabilizeHor(0, speedSpin);
        }
        NiceTest.velocityPanel.setStabilityV(false);
        return false;
    }

    public static boolean stabilizeHor(int speedZ, int speedSpin) {
        if (System.currentTimeMillis() - NiceTest.velocityPanel.updated < 499) {
            double speedX = NiceTest.velocityPanel.velocity.getX() / 30.0;
            double speedY = NiceTest.velocityPanel.velocity.getY() / 30.0;
            speedX += NiceTest.velocityPanel.accelerationRaw.getX() / 10.0;
            speedY += NiceTest.velocityPanel.accelerationRaw.getY() / 10.0;


            int dirX = (int) Math.signum(speedX);
            int dirY = (int) Math.signum(speedY);

            speedX = Math.min(10, Math.abs(speedX));
            speedY = Math.min(10, Math.abs(speedY));

            int reverseX = -dirX * (int) speedX;
            int reverseY = -dirY * (int) speedY;
            velocityPanel.setCounterVelocity(new Point2D(reverseX, reverseY));
            commandManager.move(reverseY, -reverseX, speedZ, speedSpin).doFor(100);
            boolean stable = speedX < 2.0 && speedY < 2.0;
            NiceTest.velocityPanel.setStabilityH(stable);
            return stable;
        } else {
            commandManager.move(0, 0, speedZ, speedSpin).doFor(50);
            NiceTest.velocityPanel.setStabilityH(false);
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
