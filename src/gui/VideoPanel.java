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
		// qrResult[0] is the decoded QR code, or an error message
		// qrResult[1] is the pixel coordinate of the QR code center as a
		// string "300.00, 150.00".
		String[] qrResult;
		qrResult = QRWallMarks.GetQRCode.readQRCode(this.image);
		float xMiddleCoordinate = Float.valueOf(qrResult[1].substring(0,
				qrResult[1].indexOf(",")));
		float yMiddleCoordinate = Float.valueOf(qrResult[1].substring
				(qrResult[1].indexOf(",") + 1));
		String qrDecodeMessage = qrResult[0];

		System.out.println("Decodemessage: " + qrDecodeMessage + ". " +
				xMiddleCoordinate + ", " +
				"" +
				yMiddleCoordinate +
				".");
	}
}
