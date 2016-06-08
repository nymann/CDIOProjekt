/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import dronePossition.QRPossitioning;
import gui.ListenerValuePanel;
import gui.TextPanel;
import gui.VideoPanel;
import gui.special_qr_panels.QRValuesPanel;
import gui.special_qr_panels.QRVideoPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import modeling.MainModel;

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
		ConfigurationManager cm = drone.getConfigurationManager();
		NavDataManager nm = drone.getNavDataManager();
		VideoManager vm = drone.getVideoManager();

		System.out.println("Drone connected: " + cm.isConnected());
		MainModel model = new MainModel();
		QRPossitioning qrpos = new QRPossitioning(model);

		QRValuesPanel rotation = new QRValuesPanel(qrpos);
		rotation.setListeners(nm);
		rotation.setPreferredSize(new Dimension(250, 20));
		rotation.setSize(new Dimension(250, 20));

		QRVideoPanel video = new QRVideoPanel(qrpos);
		video.setSize(new Dimension(1280, 720));
		video.setPreferredSize(new Dimension(1280, 720));
		vm.addImageListener(video);
		
		TextPanel text = new TextPanel();
		qrpos.setOutput(text);
		
		JFrame mainWindow = new JFrame();
		mainWindow.getContentPane().setLayout(new FlowLayout());
		mainWindow.getContentPane().add(video);
		mainWindow.getContentPane().add(rotation);
		mainWindow.getContentPane().add(text);
		mainWindow.setVisible(true);
		mainWindow.pack();
		
		text.addTextLine("Test output:");

		synchronized (model) {
			try {
				model.wait();
			} catch (Exception e) {

			}
		}

	}
}
