package navigation;

import QRWallMarks.QRInfo;
import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.AttitudeListener;
import modeling.MainModel;
import video.VideoReader;

/**
 * @author Kim
 */

public class NavFindPosition {

    // This class needs: video.VideoReader, modeling.MainModel
    //
    // and a attitudeListener to get the YAW value for the turn360().
    // We need modeling.MainModel, to specify number of scanned QRPoints pr.
    // rotation.
    // DronePosition is retrievable in modeling.MainModel.


	/*
     * call QR-reader
	 * spin to read 3 QR points
	 * get the calculation of position
	 * declare the position
	 */

    IARDrone drone;
    VideoReader videoReader;
    ImageDataListener imageDataListener;

    public NavFindPosition(ImageDataListener imageDataListener,
                           IARDrone drone) {
        this.imageDataListener = imageDataListener;
        this.drone = drone;
    }

    private void navigateWhenLost(int qRCodesFound) {
        /*  This method is called every time we are lost, eg. when we don't know
            our drone position. The drone, should turn it's cam 360 degrees and
            search for 3 QR-codes to read. If it can't we should delete our list
            with QR-codes and then move in a random location that isn't into
            a wall.
        */

        switch (qRCodesFound) {
            case 0:
                System.out.println("0 qRCodesFound");
                // move in random direction or spinLeft one more time.
                break;
            case 1:
                System.out.println("1 qRCodesFound");
                // move in one of the three other directions.
                break;
            case 2:
                System.out.println("2 qRCodesFound");
                // we should know the distance between the two qRCodes,
                break;
            case 3:
                System.out.println("3 qRCodesFound");
                // gucci
                break;
        }


        /* 1. call turn360degrees();
        *  2. if (qRCodesFound < 3) {
        *           qRCodesFound.removeAll
        *           moveInRandomDirectionThatDoesNotCollideWithWall();
        *     }
        * */

    }

    // yaw is presumed to go from 0 to 2*Math.PI
    public void turn360degrees() {
        double yawAtStart = MainModel.getDroneAttitude().getYaw();
        int qRCodesFound = 0;
        //drone.getCommandManager().spinLeft(5).doFor(5000); LEGACY

        // We need to have a different value before we go into the while loop
        // We should probably change the way this works. :-D
        drone.getCommandManager().spinLeft(1).doFor(100);

        while ((MainModel.getDroneAttitude().getYaw() - yawAtStart) > 0.1) {
            drone.getCommandManager().spinLeft(5);
            // Scanning for QR codes.
            QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(videoReader.getImage());
            if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
                System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
                        qrInfo.x + ", " + qrInfo.y);
                qRCodesFound++;
            } else {
                System.out.println(qrInfo.error);
            }
            //

            System.out.println("Yaw: " + MainModel.getDroneAttitude().getYaw());
        }

        drone.hover();
        navigateWhenLost(qRCodesFound);
    }
}











