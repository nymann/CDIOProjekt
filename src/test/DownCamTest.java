package test;

import de.yadrone.base.IARDrone;

/**
 * Created by Nymann on 04-05-2016.
 */
public class DownCamTest {
    IARDrone drone = null;

    public DownCamTest(IARDrone drone) {
        this.drone = drone;
    }

    public void run() {
        drone.setMaxAltitude(2000);
        drone.setSpeed(15);
        System.out.println("OYOYxDXD");
        drone.getCommandManager().takeOff();
        drone.getCommandManager().hover().doFor(20000);
        //drone.getCommandManager().waitFor(2000);
        drone.getCommandManager().landing();
    }
}
