/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.IARDrone;
import de.yadrone.base.video.VideoManager;
import main.Main;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Mikkel
 */
public class MainWindow extends javax.swing.JFrame {

	private IARDrone drone;
	
	private VideoPanel cam;
	private AnalysedVideoPanel analysed;
	private ListenerValuePanel values;

	/**
	 * Creates new form MainWindow
	 */
	public MainWindow(IARDrone drone) {
		this.drone = drone;
		this.cam = new VideoPanel();
		this.analysed = new AnalysedVideoPanel();
		this.values = new ListenerValuePanel();
	}
	
	private void init() {
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(cam);
		this.getContentPane().add(analysed);
		this.getContentPane().add(values);
		
/*		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Main.shutDown();
			}
		}
		);*/
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.shutDown();
			}
		});
	}
	
	public void run() {
		this.init();
		Dimension videoSize = new Dimension(1280, 720);
		cam.setBackground(Color.BLACK);
		cam.setPreferredSize(videoSize);
		cam.setSize(videoSize);
		analysed.setBackground(Color.BLACK);
		analysed.setPreferredSize(videoSize);
		analysed.setSize(videoSize);
		values.setPreferredSize(new Dimension(150,150));
		this.pack();
		this.setVisible(true);
		this.repaint();

		//connecting video
		System.out.println("Connecting video manager");
		VideoManager vm = drone.getVideoManager();
		vm.addImageListener(cam);
		vm.addImageListener(analysed);
		
		values.setListeners(drone.getNavDataManager());
	}
}
