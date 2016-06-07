package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

/**
 * Created by Nymann on 07-06-2016.
 */
public class HowToGetQRCode implements ImageListener {
    public BufferedImage image;

    public HowToGetQRCode() {

    }


    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    private void letsGetSomeInfo() {
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
