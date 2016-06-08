/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.special_qr_panels;

import dronePossition.QRPossitioning;
import gui.VideoPanel;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mikkel
 */
public class QRVideoPanel extends VideoPanel {
		QRPossitioning qpos;

		// debugging
	public QRVideoPanel(QRPossitioning qpos){
		super();
		this.qpos = qpos;
	}
	
	@Override
	public void imageUpdated(BufferedImage bi) {
		super.imageUpdated(bi);
		if (this.qpos != null){
			qpos.imageUpdated(bi);
		}
	}
	
	
}
