/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mikkel
 */
public class VideoReader implements ImageListener {
	
	private final int downWidth = 360;

	private VideoManager vm;
	private CommandManager cm;
	private long lastUpdate;
	private BufferedImage image;
	private List<ImageListener> listeners;
	private boolean camMode; // down = true, front = false;

	public VideoReader(VideoManager vm, CommandManager cm) {
		this.vm = vm;
		this.cm = cm;
		vm.addImageListener(this);
		listeners = new ArrayList<>();
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		if (camMode != (bi.getWidth() == downWidth)) {
			if (camMode) {
				cm.setVideoChannel(VideoChannel.VERT);
			} else {
				cm.setVideoChannel(VideoChannel.HORI);
			}
		}
		this.image = bi;
		this.lastUpdate = System.currentTimeMillis();
		for (ImageListener listener : listeners) {
			new Thread() {
				@Override
				public void run() {
					listener.imageUpdated(image);
				}
			}.start();
		}
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public long getImageTime() {
		return this.lastUpdate;
	}

	public Boolean isDownCamActive() {
		System.out.println("Width: " + this.image.getWidth() + " px.");
		System.out.println("Height: " + this.image.getHeight() + " px.");

		return this.image.getWidth() == downWidth;
	}

	public void addListener(ImageListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ImageListener listener) {
		listeners.remove(listener);
	}

	public void setCamMode(boolean mode) {
		if (mode) {
			cm.setVideoChannel(VideoChannel.VERT);
		} else {
			cm.setVideoChannel(VideoChannel.HORI);
		}
		this.camMode = mode;
	}

}
