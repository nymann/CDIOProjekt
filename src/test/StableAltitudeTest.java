package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;

/**
 * Created by Nymann on 17-06-2016.
 * This tests goal is to listen continuously to the altitude from the drone (mm)
 * If the altitude is lower than 1750, it should fly up, and if it's higher
 * than 1850 it should move downward.
 */
public class StableAltitudeTest implements AltitudeListener {

    int desiredHeight = 1450;
    CommandManager cmd;
    IARDrone drone;

    public StableAltitudeTest() {
        drone = null;
        drone = new ARDrone();
        System.out.println("Starting Drone.");
        drone.start();
        //drone.reset();

        cmd = drone.getCommandManager();
        drone.getNavDataManager().addAltitudeListener(this);
        cmd.takeOff();
        System.out.println("Taking off!");
        //cmd.hover().doFor(10000);
        //cmd.landing();
        double currentTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - 35000) < currentTime) {
        }
        cmd.landing();
    }

    public static void main(String[] args) {
        try {
            new StableAltitudeTest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void receivedAltitude(int altitude) {

        System.out.println("altitude:\t\t" + altitude);
        if (altitude > desiredHeight) {
            // fly down.
            cmd.down(5).doFor(10);
        }
        else if (altitude < desiredHeight) {
            // fly up.
            cmd.up(5).doFor(10);
        }

        else {
            System.out.println("This shouldn't happen!");
            cmd.hover();
        }
    }

    @Override
    public void receivedExtendedAltitude(Altitude altitude) {

    }
}
