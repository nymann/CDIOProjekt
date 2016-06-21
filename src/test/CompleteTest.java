/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import navigation.ImageDataListener;
import navigation.NavFlyPattern;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.FlyingMode;
import dronePosition.QRPositioning;
import gui.FullGUI;
import modeling.MainModel;
import droneUtil.DroneControl;
import listeners.ListenerPack;
import static test.NiceTest.exceptionOut;
import video.PictureAnalyser;
import video.PictureView;
import video.VideoReader;

/**
 *
 * @author Mikkel
 */
public class CompleteTest {
	
	private static ListenerPack listenerPack;
	private static FullGUI gui;
	private static DroneControl droneControl;
	private static VideoReader videoReader;
	private static QRPositioning qrpos;
	public static PictureAnalyser redAnalyse = new PictureAnalyser();
	public static PictureAnalyser greenAnalyse = new PictureAnalyser();
	static IARDrone drone ;
	public static boolean start =  false;
	public static ImageDataListener idl = new ImageDataListener();
	
	public static void main(String[] args) {
		MainModel.init();
		PictureView.init();
		gui = new FullGUI("Complete Test");
		start =  false;
		listenerPack = new ListenerPack(gui);
		droneControl = new DroneControl(gui,listenerPack);
		listenerPack.addListeners(droneControl.getNavDataManager());
		droneControl.start();
		try {
			doStuff();
		} catch (Exception e) {
			exceptionOut.addTextLine(e.getMessage());
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				exceptionOut.addTextLine(stackTrace[i].toString());
			}
		}

		gui.printLn("Landing drone");
		droneControl.landing();
		gui.printLn("Stopping drone");
		droneControl.stop();
	}
	
	private static void doStuff(){
		videoReader = new VideoReader(droneControl);
		videoReader.addListener(gui);
		NavFlyPattern nfp = new NavFlyPattern(idl, drone);
		while(!start){};
		

		qrpos = new QRPositioning();
		qrpos.setOutput(gui);

		droneControl.getCommandManager().setFlyingMode(FlyingMode.HOVER_ON_TOP_OF_ROUNDEL);
		//--------------------------------------------------------------------
		// Drone take off
		//--------------------------------------------------------------------
		droneControl.takeOff();

		videoReader.setCamMode(false);
		findPosition();
		droneControl.getCommandManager().setFlyingMode(FlyingMode.FREE_FLIGHT);

		boolean done = false;
		while(!done){
			videoReader.setCamMode(true);
			nfp.flyLane(1, 5);
			done = doFlightPattern();
			videoReader.setCamMode(false);
			findPosition();
		}
	}
	
	private static void findPosition(){
		//--------------------------------------
		// QR positioning stuuf
		//--------------------------------------
		qrpos.reset();
		
		videoReader.addListener(qrpos);
		listenerPack.addAttitudeListener(qrpos);
		
		droneControl.turnDroneInterval();
		gui.printLn("QR-codes found: " + qrpos.getQRCount());

		videoReader.removeListener(qrpos);
		listenerPack.removeAttitudeListener(qrpos);
	}

	private static boolean doFlightPattern(){
		// return false if we've lost our position
		// return true when we're done
		return true;
	}
}
