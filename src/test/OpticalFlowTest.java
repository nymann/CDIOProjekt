package test;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.video.VideoManager;
import video.OpticalFlow;
import video.VideoReader;

public class OpticalFlowTest {

	public static void main(String[] args) {
		IARDrone drone = null;
		// connecting to drone
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone");
			drone.start();
			final CommandManager cmd = drone.getCommandManager();
			final VideoManager vmd = drone.getVideoManager();
			video.VideoReader vid = new VideoReader(vmd, cmd);
			vid.setCamMode(false);
//			drone.setVerticalCamera();
//			testOne(cmd);
			testTwo(cmd, vid);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		} 
	}
	
	public static void testOne(CommandManager cmd){
		cmd.setMaxAltitude(2000);
		doCommand(cmd, 5, 1000);
		doCommand(cmd, 7, 2000);
		cmd.up(10).doFor(1000);
		doCommand(cmd, 1, 3000);
		doCommand(cmd, 7, 2000);
		doCommand(cmd, 2, 3000);
		doCommand(cmd, 7, 2000);
		doCommand(cmd, 3, 3000);
		doCommand(cmd, 7, 2000);
		doCommand(cmd, 4, 3000);
		doCommand(cmd, 7, 2000);
		doCommand(cmd, 6, 1000);
	}
	
	public static void testTwo(CommandManager cmd, VideoReader vid){
		OpticalFlow flow = new OpticalFlow();
		JFrame frame = new JFrame("Image");
		JPanel panel = new JPanel();
		frame.setPreferredSize(new Dimension(1080, 720));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		JLabel label = new JLabel();
		panel.add(label);
//		frame.add(label);
//		doCommand(cmd, 5, 50);
//		doCommand(cmd,7,100);
		JButton button = new JButton("Tag billede");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Taking picture...");
				BufferedImage img = vid.getImage();
				while (img == null) img = vid.getImage();
				label.setIcon(new ImageIcon(img));
				System.out.println("Picture taken");
			}
		});
		panel.add(button);
		frame.add(panel);
		frame.setVisible(true);
//		flow.findFlows(prev);
//		doCommand(cmd,3,80);
//		doCommand(cmd,7,100);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			System.out.println("Error");
//			e.printStackTrace();
//		}
//		BufferedImage next = null;
//		do { next = vid.getImage();}
//		while(next == null);
//		doCommand(cmd,6,50);
//		flow.findFlows(next);
	}
	
	public static void doCommand(CommandManager cmd, int direction, int time){
		double start = System.currentTimeMillis();
		switch(direction){
			case 1: while(System.currentTimeMillis() - start < time){
				cmd.forward(20);
			}
			break;
			case 2: while(System.currentTimeMillis() - start < time){
				cmd.backward(20);
			}
			break;
			case 3: while(System.currentTimeMillis() - start < time){
				cmd.goLeft(20);
			}
			break;
			case 4: while(System.currentTimeMillis() - start < time){
				cmd.goRight(20);
			}
			break;
			case 5: while(System.currentTimeMillis() - start < time){
				cmd.takeOff();
			}
			break;
			case 6: while(System.currentTimeMillis() - start < time){
				cmd.landing();
			}
			break;
			case 7: while(System.currentTimeMillis() - start < time){
				cmd.hover();
			}
			break;
			default: 
				cmd.hover();
			
					
		}
	}
}
