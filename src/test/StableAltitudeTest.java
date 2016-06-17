package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.UltrasoundFrequency;

/**
 * Created by Nymann on 17-06-2016.
 * This tests goal is to listen continuously to the altitude from the drone (mm)
 * If the altitude is lower than 1750, it should fly up, and if it's higher
 * than 1850 it should move downward.
 */
public class StableAltitudeTest {

    int desiredHeight = 1300;
    int tolerance = 20;
    CommandManager cmd;
    IARDrone drone;

    public StableAltitudeTest() {
        drone = null;
        drone = new ARDrone();
        System.out.println("Starting Drone.");
        drone.start();
        //drone.reset();
        listeners.Altitude altitude = new listeners.Altitude();
        cmd = drone.getCommandManager();
        drone.getNavDataManager().addAltitudeListener(altitude);
        cmd.setUltrasoundFrequency(UltrasoundFrequency.F22Hz);
        cmd.takeOff();
        System.out.println("Taking off!");
        //cmd.hover().doFor(10000);
        //cmd.landing();
        double currentTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - 60000) < currentTime) {
            int altitudeRaw = altitude.extendedAltitude == null ? 0 : altitude
                    .extendedAltitude.getRaw();
            System.out.println("altitudeRaw:\t\t" + altitudeRaw);
            if (altitudeRaw > (desiredHeight + tolerance)) {
                // fly down.
                cmd.down(5).doFor(50);
            } else if (altitudeRaw < (desiredHeight - tolerance)) {
                // fly up.
                cmd.up(5).doFor(50);
            } else if ((altitudeRaw >= (desiredHeight - tolerance)) && (altitudeRaw <=
                    (desiredHeight + 50))) {
                cmd.hover().doFor(50);
            } else {
                System.out.println("This shouldn't happen!");
                //cmd.hover();
            }
        }

        cmd.landing();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.printf("Exception: \t\t");
            e.printStackTrace();
        }
        cmd.stop();
    }

    public static void main(String[] args) {
        try {
            new StableAltitudeTest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
