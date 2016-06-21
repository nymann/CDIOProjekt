/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.VelocityListener;
import de.yadrone.base.video.ImageListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javafx.geometry.Point2D;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Mikkel
 */
public class FullGUI implements VelocityListener, AttitudeListener, ImageListener {

	private final TextPanel output = new TextPanel();
	private final TextPanel exceptionOut = new TextPanel();
	private final TextPanel droneStatus = new TextPanel();
	private final InfoPanel infoPanel = new InfoPanel();
	private final VelocityPanel velocityPanel = new VelocityPanel();
	private final VideoPanel video = new VideoPanel();
	private final PositionPanel positionPanel = new PositionPanel();

	public FullGUI(String name) {

		Dimension outputSize = new Dimension(300, 600);
		output.setPreferredSize(outputSize);
		exceptionOut.setPreferredSize(outputSize);
		droneStatus.setPreferredSize(outputSize);
		droneStatus.setSize(outputSize);

		Dimension infoDimension = new Dimension(900, 200);
		infoPanel.setColumns(80);
		infoPanel.setPreferredSize(infoDimension);
		infoPanel.setSize(infoDimension);

		Dimension velocityDimension = new Dimension(220, 200);
		velocityPanel.setSize(velocityDimension);
		velocityPanel.setPreferredSize(velocityDimension);

		Dimension videoSize = new Dimension(640, 360);
		video.setSize(videoSize);
		video.setPreferredSize(videoSize);

		JFrame mainWindow = new JFrame();
		JFrame videoFrame = new JFrame();
		JFrame velocityFrame = new JFrame();
		JFrame infoFrame = new JFrame();
		JFrame positionFrame = new JFrame();

		mainWindow.getContentPane().setLayout(new FlowLayout());
		videoFrame.getContentPane().setLayout(new FlowLayout());
		velocityFrame.getContentPane().setLayout(new FlowLayout());
		infoFrame.getContentPane().setLayout(new FlowLayout());
		positionFrame.getContentPane().setLayout(new FlowLayout());

		mainWindow.getContentPane().add(output);
		mainWindow.getContentPane().add(exceptionOut);
		mainWindow.getContentPane().add(droneStatus);

		videoFrame.getContentPane().add(video);
		velocityFrame.getContentPane().add(velocityPanel);
		infoFrame.getContentPane().add(infoPanel);
		positionFrame.getContentPane().add(positionPanel);

		mainWindow.setVisible(true);
		videoFrame.setVisible(true);
		velocityFrame.setVisible(true);
		infoFrame.setVisible(true);
		positionFrame.setVisible(true);

		videoFrame.setLocation(920, 0);
		velocityFrame.setLocation(920, 400);
		infoFrame.setLocation(0, 640);
		positionFrame.setLocation(1150, 400);

		mainWindow.setTitle(name + " Main");
		videoFrame.setTitle(name + " Video");
		velocityFrame.setTitle(name + " Velocity");
		infoFrame.setTitle(name + " Info");
		positionFrame.setTitle(name + " Position");

		mainWindow.pack();
		videoFrame.pack();
		velocityFrame.pack();
		infoFrame.pack();
		positionFrame.pack();

		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void setBateryLevel(int level){
		infoPanel.setInfo("Batery Level:", level);
	}
	
	public void setAltitude(int alt){
		infoPanel.setInfo("Altitude", alt);
	}
	
	public void setInfo(String text, Object value){
		infoPanel.setInfo(text, value);
	}
	
	public void setAcceleration(int[] acc){
		velocityPanel.setAccelRaw(acc);
	}

	public void printLn(String text) {
		output.addTextLine(text);
	}

	public void errorLn(String text) {
		exceptionOut.addTextLine(text);
	}

	public void setDroneStatus(String text) {
		droneStatus.setText(text);
	}
	
	public void setStabilityV(boolean b) {
		velocityPanel.setStabilityV(b);
	}

	public void setStabilityH(boolean stable){
		velocityPanel.setStabilityH(stable);
	}

	public void setCounterVelocity(int reverseX, int reverseY){
		velocityPanel.setCounterVelocity(new Point2D(reverseX, reverseY));
	}
	
	public void setVelocityV(float velocityH){
		velocityPanel.setVelocityV(velocityH);
	}

	public void setCounterV(int i) {
		velocityPanel.setCounterV(i);
	}
	
	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		velocityPanel.velocityChanged(vx, vy, vz);
		positionPanel.velocityChanged(vx, vy, vz);
	}

	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		positionPanel.attitudeUpdated(pitch, roll, yaw);
	}

	@Override
	public void attitudeUpdated(float pitch, float roll) {
		positionPanel.attitudeUpdated(pitch, roll);
	}

	@Override
	public void windCompensation(float pitch, float roll) {
		positionPanel.windCompensation(pitch, roll);
	}

	@Override
	public void imageUpdated(BufferedImage image) {
		video.imageUpdated(image);
	}
}
