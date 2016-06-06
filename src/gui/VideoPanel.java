/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
	String[] prevQrResult = {"", ""};
	
	public VideoPanel() {
		//System.out.println(QRWallMarks.GetQRCode.readQRCode(this.image));
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		this.image = bi;
		this.setSize(bi.getWidth(), bi.getHeight());

		// Not a beautiful solution.
		String[] qrResult;
		qrResult = QRWallMarks.GetQRCode.readQRCode(bi);
		/*if (qrResult[0].charAt(0) == 'W' && !qrResult[0].equals
				(prevQrResult[0])) {
			System.out.println(qrResult[0] + ". Prev: " + prevQrResult[0]);
			prevQrResult = qrResult;
		}*/
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
	
}
