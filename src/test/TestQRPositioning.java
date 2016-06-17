/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import dronePossition.QRPossitioning;
import gui.TextPanel;
import gui.VideoPanel;
import gui.special_qr_panels.QRValuesPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import video.VideoReader;

/**
 *
 * @author Mikkel
 */
public class TestQRPositioning {

	public static void main(String[] args) {
		System.out.println("Starting QR positioning test");
		
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
		ConfigurationManager config = drone.getConfigurationManager();
		NavDataManager nm = drone.getNavDataManager();
		VideoManager vm = drone.getVideoManager();
		CommandManager command = drone.getCommandManager();
		//command.setVideoCodec(VideoCodec.H264_720P);
		command.setVideoCodec(VideoCodec.H264_360P);

		System.out.println("Drone connected: " + config.isConnected());
		QRPossitioning qrpos = new QRPossitioning();

		QRValuesPanel rotation = new QRValuesPanel(qrpos);
		rotation.setListeners(nm);
		rotation.setPreferredSize(new Dimension(300, 20));
		rotation.setSize(new Dimension(300, 20));

		VideoReader videoReader = new VideoReader(vm, command);
		VideoPanel video = new VideoPanel();
		video.setSize(new Dimension(1280, 720));
		video.setPreferredSize(new Dimension(1280, 720));
		vm.addImageListener(videoReader);
		videoReader.addListener(video);
		videoReader.addListener(qrpos);
		videoReader.setCamMode(false);
		
		TextPanel text = new TextPanel();
		qrpos.setOutput(text);
		
		JFrame mainWindow = new JFrame();
		mainWindow.getContentPane().setLayout(new FlowLayout());
		mainWindow.getContentPane().add(video);
		mainWindow.getContentPane().add(rotation);
		mainWindow.getContentPane().add(text);
		mainWindow.setVisible(true);
		mainWindow.pack();
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		text.addTextLine("Test output:");
	}
}
