package demo;

import de.yadrone.base.IARDrone;
import video.VideoReader;

import static main.Main.*;

/**
 * Created by Nymann on 04-05-2016.
 */
public class DownCamTest {
    IARDrone drone = null;

    public DownCamTest(IARDrone drone) {
        this.drone = drone;
    }

    public void run() {
        //video.VideoReader videoReader = new VideoReader(main.Main.A);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        downCamActive = false;
        drone.setMaxAltitude(2000);
        drone.setSpeed(15);
        drone.getCommandManager().takeOff();

        drone.getCommandManager().hover().doFor(5000);

        drone.setVerticalCamera();
        //downCamActive = true;


        drone.getCommandManager().hover().doFor(10000);

        drone.setHorizontalCamera();
        //downCamActive = false;

        drone.getCommandManager().spinLeft(15).doFor(5000);

        drone.setVerticalCamera();
        //downCamActive = true;

        drone.getCommandManager().spinLeft(15).doFor(5000);

        drone.setHorizontalCamera();
        //downCamActive = false;

        drone.getCommandManager().landing();
    }
}
