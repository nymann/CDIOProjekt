/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import video.PictureAnalyser;
import video.PictureView;

import java.awt.image.BufferedImage;

import static main.Main.downCamActive;

/**
 * @author Mikkel
 */

public class AnalysedVideoPanel extends VideoPanel {
	public static BufferedImage bi2;
    @Override
    public void imageUpdated(BufferedImage bi) {

    	
        if (!downCamActive) {
        	bi2 = PictureView.setSize( bi,640,360);
            //this.image = MainWindow.paRed.getAnalyse(bi2);
            this.image= bi2;
           
          //  this.setSize(image.getWidth(), image.getHeight());
            this.repaint();
        }
    }
}