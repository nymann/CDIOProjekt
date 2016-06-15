package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.UltrasoundData;
import de.yadrone.base.navdata.UltrasoundListener;

public class UltraSoundTest implements AltitudeListener, UltrasoundListener {

    public UltraSoundTest() {
        IARDrone drone = null;
        drone = new ARDrone();
        System.out.println("Starting Drone");
        drone.start();
        final CommandManager cmd = drone.getCommandManager();
        drone.getNavDataManager().addAltitudeListener(this);
        drone.getNavDataManager().addUltrasoundListener(this);
        cmd.takeOff();
        cmd.hover().doFor(10000);
        cmd.landing();
    }

    public static void main(String[] args) {
        // connecting to drone
        try {

            new UltraSoundTest();
//			final VideoManager vmd = drone.getVideoManager();
//			video.VideoReader vid = new VideoReader(vmd, cmd);
//			Thread.sleep(5000);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
    }

    @Override
    public void receivedAltitude(int altitude) {
        System.out.println("Altitude: " + "\t" + altitude);
    }

    @Override
    public void receivedExtendedAltitude(Altitude arg0) {
		System.out.println("AltitudeObj: " + "\t" + arg0);
    }

    @Override
    public void receivedRawData(UltrasoundData arg0) {
        System.out.println("UltrasoundData: " + "\t" + arg0);
    }

}