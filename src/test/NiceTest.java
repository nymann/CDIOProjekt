package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;
import listeners.Attitude;
import modeling.MainModel;

import java.awt.image.BufferedImage;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest implements ImageListener {
    private IARDrone drone;
    private BufferedImage bufferedImage;

    public NiceTest() {
    	drone = new ARDrone();
    	drone.takeOff();
    	turn360degrees();
    }

    public static void main(String[] args) {
    	new NiceTest();
    }

    public void turn360degrees() {
        MainModel mainModel = new MainModel();
        Attitude attitude = new Attitude(mainModel);
        drone.getNavDataManager().addAttitudeListener(attitude);
        double yawAtStart = mainModel.getDroneAttitude().getYaw();
        int qRCodesFound = 0;
        //drone.getCommandManager().spinLeft(5).doFor(5000); LEGACY

        while ((mainModel.getDroneAttitude().getYaw() - yawAtStart) > 0.1) {
            drone.getCommandManager().spinLeft(5);
            // Scanning for QR codes.
            QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(bufferedImage);
            if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
                System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
                        qrInfo.x + ", " + qrInfo.y);
                qRCodesFound++;
            } else {
                System.out.println(qrInfo.error);
            }
            //

            System.out.println("Yaw: " + mainModel.getDroneAttitude().getYaw());
        }
        System.out.println("QR Codes found: " + qRCodesFound);

        drone.landing();
        //navigateWhenLost(qRCodesFound);
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
