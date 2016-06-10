package navigation;

import QRWallMarks.QRInfo;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

/**
 * @author Kim
 */

public class NavFindPosition {

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

        // hovering should probably be done in another class.
        drone.hover();

        /* 1. call turn360degrees();
        *  2. if (qRCodesFound < 3) {
        *           qRCodesFound.removeAll
        *           moveInRandomDirectionThatDoesNotCollideWithWall();
        *     }
        * */

    }

    // Bliver kaldt i NavFlyPattern pt, men NavFlyPattern bør kalde
    // QRPossitioning/PointNavigation.
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}











