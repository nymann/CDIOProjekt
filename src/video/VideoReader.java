/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import gui.MainWindow;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Main;

/**
 *
 * @author Mikkel
 */
public class VideoReader implements ImageListener {
	private VideoManager vm;
	private long lastUpdate;
	private BufferedImage image;
	
	public VideoReader(VideoManager vm){
		this.vm = vm;
		vm.addImageListener(this);
	}
	
	public void run(){
		System.out.println("video.VideoReader.run()");
		vm.addImageListener(this);
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		this.image = bi;
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public BufferedImage getImage(){
		return this.image;
	}
	
	public long getImageTime(){
		return this.lastUpdate;
	}
	
}
