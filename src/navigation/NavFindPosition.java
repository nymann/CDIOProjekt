package navigation;

import de.yadrone.base.IARDrone;

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

    public NavFindPosition() {
        //dronePossition.PointNavigation
        //positionX = ;
        //positionY = ;
    }

    private void navigateWhenLost() {
        /*  This method is called every time we are lost, eg. when we don't know
            our drone position. The drone, should turn it's cam 360 degrees and
            search for 3 QR-codes to read. If it can't we should delete our list
            with QR-codes and then move in a random location that isn't into
            a wall.
        */


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

        // until we can get yaw value from modeling.MainModel we implement it
        // this way..
        drone.getCommandManager().spinLeft(5).doFor(5000);
    }


    // Currently gets called by NavFlyPattern, but NavFlyPattern should call
    // modeling.MainModel.
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}











