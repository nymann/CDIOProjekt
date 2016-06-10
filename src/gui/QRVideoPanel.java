
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import QRWallMarks.QRInfo;
import java.awt.image.BufferedImage;


/**
 *
 * @author Mikkel
 */

public class QRVideoPanel extends VideoPanel {

	private void qrCodeResult() {
		QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(this.image);
		if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
			// no errors!
			System.out.println("Decodemessage: " + qrInfo.name + ". At: "
					+ qrInfo.x + ", " + qrInfo.y);
            qrInfo.qRCodeFoundInCurrentImage = true;
		} else {
			System.out.println(qrInfo.error);
		}
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		super.imageUpdated(bi);
		qrCodeResult();
	}
}

