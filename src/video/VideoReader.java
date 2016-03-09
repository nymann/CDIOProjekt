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
	private boolean hasNewImage;
	private BufferedImage newImage, currentImage;
	
	public VideoReader(VideoManager vm){
		this.vm = vm;
		vm.addImageListener(this);
	}
	
	public void run(){
		System.out.println("video.VideoReader.run()");
		MainWindow window = new MainWindow();
		window.setVisible(true);
		Graphics graphics = window.getGraphics();
		vm.addImageListener(this);
		while(!Main.done){
			if(!this.hasNewImage){
				System.out.println("No image ready");
				continue;
			}
			hasNewImage = false;
			currentImage = newImage;
			System.out.println("new image ready");
			graphics.drawImage(newImage, 0, 0, window);
			//window.getContentPane().
		}
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		this.newImage = bi;
		this.hasNewImage = true;
//S		System.out.println("video.VideoReader.imageUpdated()");
	}
	
	private void processImage(){
		//currentImage.
	}
}
