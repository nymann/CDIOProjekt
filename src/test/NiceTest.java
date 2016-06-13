package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;
import listeners.Attitude;
import modeling.MainModel;
import video.VideoReader;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest {

    public static void main(String[] args) {
        IARDrone drone = null;

        // connecting to drone

        try {
            drone = new ARDrone();
            System.out.println("Starting Drone.");
            drone.start();
        } catch (Exception exc) {
            System.err.println("Error: " + exc.getMessage());
            exc.printStackTrace();
        }

        // getting managers
        assert drone != null;
        ConfigurationManager configurationManager = drone.getConfigurationManager();
        NavDataManager navDataManager = drone.getNavDataManager();
        VideoManager videoManager = drone.getVideoManager();
        CommandManager commandManager = drone.getCommandManager();

        System.out.println("Drone connected: " + configurationManager.isConnected());
        MainModel model = new MainModel();
        VideoReader videoReader = new VideoReader(videoManager, commandManager);
        Attitude att = new Attitude(model);
        navDataManager.addAttitudeListener(att);

        drone.takeOff();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(model.getDroneAttitude() == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double startYaw = model.getDroneAttitude().getYaw()+ Math.PI;

        drone.getCommandManager().spinLeft(5);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		double currentYaw = model.getDroneAttitude().getYaw()+ Math.PI;
		System.out.println("Starting yaw difference:" + (currentYaw-startYaw));
        int qRCodesFound = 0;
        while (Math.abs(startYaw - currentYaw) > 0.1) {

            if (model.getDroneAttitude() != null){
                System.out.println("Yaw = " + model.getDroneAttitude().getYaw());
                QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(videoReader.getImage());
                if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
                    System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
                            qrInfo.x + ", " + qrInfo.y);
                    qRCodesFound++;
                } else {
                    System.out.println(qrInfo.error);
                }
            }
			currentYaw = model.getDroneAttitude().getYaw()+ Math.PI;
        }

        System.out.println("\t\t\t\t\t\tQR-codes found: " + qRCodesFound);
		drone.landing();
    }
}
