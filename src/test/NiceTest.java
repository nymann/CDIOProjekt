package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import gui.InfoPanel;
import gui.TextPanel;
import listeners.*;
import modeling.MainModel;
import video.VideoReader;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest {

    static TextPanel output, exceptionOut;
    static InfoPanel infoPanel;
    static Velocity vel = new Velocity();
    static UltraSound ult = new UltraSound();
    static Altitude alt = new Altitude();
    static NavDataManager navDataManager;
    static VideoManager videoManager;
    static CommandManager commandManager;
    static ConfigurationManager configurationManager;
    static final double movingSpeed = 50.0;
    static final double takeOffSpeed = 100.0;
    static final double hoverSpeed = 40.0;


    public static void main(String[] args) {
        IARDrone drone = null;

        output = new TextPanel();
        exceptionOut = new TextPanel();
        infoPanel = new InfoPanel();
//		infoPanel.setColumns(18);
        infoPanel.setColumns(80);

        Dimension outputSize = new Dimension(300, 500);
        output.setPreferredSize(outputSize);
        exceptionOut.setPreferredSize(outputSize);
//		Dimension infoDimension = new Dimension(200, 50);
        Dimension infoDimension = new Dimension(1200, 200);
        infoPanel.setPreferredSize(infoDimension);
        infoPanel.setSize(infoDimension);

        JFrame mainWindow = new JFrame();
        mainWindow.getContentPane().setLayout(new FlowLayout());
        mainWindow.getContentPane().add(output);
        mainWindow.getContentPane().add(exceptionOut);
        mainWindow.getContentPane().add(infoPanel);
        mainWindow.setVisible(true);
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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


        navDataManager = drone.getNavDataManager();
        videoManager = drone.getVideoManager();
        commandManager = drone.getCommandManager();
        navDataManager.addVelocityListener(vel);
        navDataManager.addUltrasoundListener(ult);
        navDataManager.addAltitudeListener(alt);
        commandManager.setMaxAltitude(2000);
        commandManager.setNavDataDemo(true);

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
        while (vel.velocity == null || vel.velocity.magnitude() > 0.1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        output.addTextLine("Stopping drone");
        drone.stop();
    }

    private static void doStuff(IARDrone drone) {

        VideoReader videoReader = new VideoReader(videoManager, commandManager);
        Attitude att = new Attitude();
        navDataManager.addAttitudeListener(att);
        //commandManager.setOutdoor(false, true);

        drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
        // Sets the camera to 720p instead of stretching a 640x360 image.

        commandManager.setOutdoor(true, true);

        Runnable infoUpdate = () -> {
            while (true) {
                if (NiceTest.vel.velocity != null) {
                    NiceTest.infoPanel.setInfo("Speed X", NiceTest.vel.velocity.getX());
                    NiceTest.infoPanel.setInfo("Speed Y", NiceTest.vel.velocity.getY());
                    NiceTest.infoPanel.setInfo("Speed Z", NiceTest.vel.velocity.getZ());
                    NiceTest.infoPanel.setInfo("Counter X", (int) -NiceTest.vel.velocity.getX() / 100);
                    NiceTest.infoPanel.setInfo("Counter Y", (int) -NiceTest.vel.velocity.getY() / 100);
                } else {
                    NiceTest.infoPanel.setInfo("Speed X", "null");
                    NiceTest.infoPanel.setInfo("Speed Y", "null");
                    NiceTest.infoPanel.setInfo("Speed Z", "null");
                }

                if (NiceTest.ult.arg0 != null)
                    NiceTest.infoPanel.setInfo("Ultrasound", NiceTest.ult.arg0);
                else
                    NiceTest.infoPanel.setInfo("Ultrasound", "null");

                if (NiceTest.alt.extendedAltitude != null)
                    NiceTest.infoPanel.setInfo("Altitude", NiceTest.alt.extendedAltitude);
                else
                    NiceTest.infoPanel.setInfo("Altitude", "null");

            }
        };
        new Thread(infoUpdate).start();

        output.addTextLine("Taking off");
        commandManager.takeOff();

        while (vel.velocity == null || vel.velocity.magnitude() < takeOffSpeed) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        long startTime = System.currentTimeMillis();

        while ((startTime + 4000) > System.currentTimeMillis())  {
            commandManager.up(50).doFor(50);

        }
        commandManager.hover();

        output.addTextLine("Started moving");
        while (vel.velocity.getZ() > 2.0) {
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        output.addTextLine("Reached hover height");

        while (vel.velocity.magnitude() > hoverSpeed) {
            
            //Point3D v = vel.velocity;
            //commandManager.move((int)-v.getX()/100, (int)-v.getY()/100, 0, 0);
            //commandManager.move(0, 0, 0, 0);

            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("Couldn't sleep current thread on line 208");
            }
        }
        output.addTextLine("Stable hover");

        double startYaw = MainModel.getDroneAttitude().getYaw() + Math.PI;
        output.addTextLine("Spinning left");
        commandManager.spinLeft(50);

        output.addTextLine("Waiting for difference");
        double currentYaw;
        do {
            currentYaw = MainModel.getDroneAttitude().getYaw() + Math.PI;
            commandManager.spinLeft(50).doFor(50);
            //Point3D v = vel.velocity;
            //commandManager.move((int)-v.getX()/100, (int)-v.getY()/100, 0, 0).doFor(50);
        } while (Math.abs(startYaw - currentYaw) > 0.025);

        output.addTextLine("Starting yaw difference:" + (currentYaw - startYaw));

        int qRCodesFound = 0;

        // seems to be too small of a value. (0.01 is too small suggested value
        // is 0.025 or 0.03)
        while (Math.abs(startYaw - currentYaw) > 0.025) {
            
            commandManager.spinLeft(50).doFor(50);
            //Point3D v = vel.velocity;
            //commandManager.move((int)-v.getX()/100, (int)-v.getY()/100, 0, 0).doFor(50);

            infoPanel.setInfo("Current Yaw", currentYaw);
            infoPanel.setInfo("Yaw difference", Math.abs(startYaw - currentYaw));

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

        }
        output.addTextLine("QR-codes found: " + qRCodesFound);

    }
}
