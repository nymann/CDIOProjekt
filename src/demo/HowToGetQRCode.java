package demo;

import QRWallMarks.QRInfo;
import video.VideoReader;

/**
 * Created by Nymann on 07-06-2016.
 */

public class HowToGetQRCode {
    public VideoReader vr;

    public HowToGetQRCode(VideoReader vr) {
        this.vr = vr;
    }

    private void letsGetSomeInfo() {
        QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(vr.getImage());
        if (qrInfo.error.equals("")) {
            // no errors!
            System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
                    qrInfo.x + ", " + qrInfo.y);
        } else {
            System.out.println(qrInfo.error);
        }
    }

}
