
package gui;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.video.VideoManager;
import main.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;


import video.PictureAnalyser;

import java.util.List;

import javax.swing.JButton;




import video.PictureAnalyser;

/**
 *
 * @author Mikkel
 */
public class MainWindow extends javax.swing.JFrame implements ActionListener {

	private IARDrone drone;
	private JButton calibrateRed,calibrateGreen,runTest;
	
	private VideoPanel cam;
	private AnalysedVideoPanel analysed;
	private ListenerValuePanel values;
	public static PictureAnalyser paGreen = new PictureAnalyser();
	public static PictureAnalyser paRed = new PictureAnalyser();
	/**
	 * Creates new form MainWindow
	 */
	public MainWindow(IARDrone drone) {
		this.drone = drone;
	//	this.cam = new VideoPanel();
		this.analysed = new AnalysedVideoPanel();
		this.values = new ListenerValuePanel();
		this.calibrateRed = new JButton("CalibrateRed");
		this.calibrateGreen = new JButton("CalibrateGreen");
		this.runTest = new JButton("runTest");
	}
	
	private void init() {
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(analysed);
		this.getContentPane().add(calibrateRed);
	//	this.getContentPane().add(cam);
		this.getContentPane().add(values);
		this.getContentPane().add(calibrateGreen);
		this.getContentPane().add(runTest);
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
		Dimension videoSize = new Dimension(640,360 );
	/*	cam.setBackground(Color.BLACK);
		cam.setPreferredSize(videoSize);
		cam.setSize(videoSize);
	*/	analysed.setBackground(Color.BLACK);
		analysed.setPreferredSize(videoSize);
		analysed.setSize(videoSize);
		values.setPreferredSize(new Dimension(150,150));
		calibrateRed.addActionListener(this);
		calibrateGreen.addActionListener(this);
		runTest.addActionListener(this);
		this.pack();
		this.setVisible(true);
		this.repaint();

		//connecting video
		System.out.println("Connecting video manager");
		drone.setVerticalCamera();
		drone.getCommandManager().setVideoCodec(VideoCodec.H264_360P);

		VideoManager vm = drone.getVideoManager();
	//	vm.addImageListener(cam);
		vm.addImageListener(analysed);
		
		values.setListeners(drone.getNavDataManager());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ("CalibrateRed".equals(e.getActionCommand())){
	
			paRed.Calibrate(analysed.bi2);
			
		 }if("CalibrateGreen".equals(e.getActionCommand())){		 
				paGreen.Calibrate(analysed.bi2);
			 }
		 if("runTest".equals(e.getActionCommand())){		 
				// run a test with the drone
			 List<Point> lp = paRed.getAnalyse(AnalysedVideoPanel.bi2);
			 for(int i =0;i<lp.size();i++){
				 System.out.println("Cubes in x:"+lp.get(i).x+" y:"+lp.get(i).y);
			 }
			 }
	}
	
}

		
	