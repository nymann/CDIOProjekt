package test;

import java.awt.image.BufferedImage;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;
import video.OpticalFlow;

public class OpticalFlowTest implements ImageListener {

	private OpticalFlow of;
	private IARDrone drone;

	public OpticalFlowTest() {
		of = new OpticalFlow();
		drone = new ARDrone();
		drone.getVideoManager().addImageListener(this);
	}

	public static void main(String[] args) {
		new OpticalFlowTest().fly();
	}

	public void fly() {
		try {
			drone.setHorizontalCamera();
			drone.start();
			drone.getCommandManager().takeOff();
			drone.getCommandManager().hover().doFor(500);
			drone.getCommandManager().forward(8).doFor(500);
			drone.getCommandManager().landing();
		} catch (Exception e) {
			drone.getCommandManager().landing();
			e.printStackTrace();
		}
	}

	@Override
	public void imageUpdated(BufferedImage arg0) {
		of.findFlows(arg0);
	}

}
