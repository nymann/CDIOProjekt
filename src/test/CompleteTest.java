/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dronePosition.QRPositioning;
import gui.FullGUI;
import modeling.MainModel;
import droneUtil.DroneControl;
import listeners.ListenerPack;
import static test.NiceTest.exceptionOut;
import video.VideoReader;

/**
 *
 * @author Mikkel
 */
public class CompleteTest {
	
	private static ListenerPack listenerPack;
	private static FullGUI gui;
	private static DroneControl droneControl;

	public static void main(String[] args) {
		MainModel.init();
		gui = new FullGUI("Complete Test");
		listenerPack = new ListenerPack(gui);
		droneControl = new DroneControl(gui,listenerPack);
		droneControl.start();
		listenerPack.addListeners(droneControl.getNavDataManager());
		
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
		droneControl.takeOff();
		boolean done = false;
		while(!done){
			findPosition();
			done = doFlightPattern();
		}
	}
	
	private static void findPosition(){
		//--------------------------------------
		// QR positioning stuuf
		//--------------------------------------
		QRPositioning qrpos = new QRPositioning();
		VideoReader videoReader = new VideoReader(droneControl);
		droneControl.getVideoManager().addImageListener(videoReader);
		videoReader.setCamMode(false);
		videoReader.addListener(qrpos);
		qrpos.setOutput(gui);
		listenerPack.addAttitudeListener(qrpos);
		videoReader.addListener(gui);
		
		droneControl.turnDroneInterval();

		gui.printLn("QR-codes found: " + qrpos.getQRCount());
		videoReader.removeListener(qrpos);
		listenerPack.removeAttitudeListener(qrpos);
	}

	private static boolean doFlightPattern(){
		// return true when we're done
		return true;
	}
}
