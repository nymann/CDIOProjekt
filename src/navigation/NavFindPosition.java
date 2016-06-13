package navigation;

import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.AttitudeListener;
import modeling.MainModel;

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
    double positionX;
    double positionY;
    MainModel mainModel;

    public NavFindPosition(MainModel mainModel) {
        this.mainModel = mainModel;
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
                // move in random direction or spinLeft one more time.
                break;
            case 1:
                // move in one of the three other directions.
                break;
            case 2:
                // we should know the distance between the two qRCodes,
                break;
            case 3:
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

    // we could take yaw as a parameter, and then spin left until the yaw is
    // back to that value (from +180 to -180).
    private void turn360degrees() {
        double yawAtStart = mainModel.getDroneAttitude().getYaw();

        //mainModel.getDroneAttitude().getYaw();
        // until we can get yaw value from modeling.MainModel we implement it
        // this way..

        //drone.getCommandManager().spinLeft(5).doFor(5000); LEGACY

        while ((mainModel.getDroneAttitude().getYaw() - yawAtStart) > 0.1) {
            // Scanning for QR codes.
            System.out.println("Yaw: " + mainModel.getDroneAttitude().getYaw());
        }

        drone.hover();

    }
}











