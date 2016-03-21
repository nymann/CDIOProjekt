/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import video.PictureAnalyser;

/**
 *
 * @author Mikkel
 */
public class AnalysedVideoPanel extends VideoPanel {
	@Override
	public void imageUpdated(BufferedImage bi) {
			this.image = PictureAnalyser.getAnalyse(bi);
			this.setSize(image.getWidth(), image.getHeight());
	}
}
