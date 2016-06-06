/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import QRWallMarks.QRInfo;
import de.yadrone.base.video.ImageListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Mikkel
 */
public class VideoPanel extends JPanel implements ImageListener{

	BufferedImage image = null;
	
	public VideoPanel() {
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		this.image = bi;
		this.setSize(bi.getWidth(), bi.getHeight());

		qrCodeResult();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		if (image != null){
			g.drawImage(image, 0, 0, this);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	private void qrCodeResult() {
		QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(this.image);
		if(qrInfo.error.equals("")) {
			// no errors!
			System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
					qrInfo.x + ", " + qrInfo.y);
		} else {
			System.out.println(qrInfo.error);
		}
	}
}
