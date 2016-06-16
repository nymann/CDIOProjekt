package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import modeling.MainModel;

import java.awt.image.BufferedImage;

/**
 * Created by Nymann on 16-06-2016.
 */
public class QRDetectionTest implements ImageListener {

    static NavDataManager navDataManager;
    static VideoManager videoManager;
    static CommandManager commandManager;
    static ConfigurationManager configurationManager;
    BufferedImage bufferedImage;

    public QRDetectionTest() {
        IARDrone drone = null;
        // connecting to drone
        try {
            drone = new ARDrone();
            System.out.println("Starting DroneFormerlyKnownAsNightFury.");
            drone.start();
            drone.reset();
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }


        assert drone != null;
        configurationManager = drone.getConfigurationManager();
        while (!configurationManager.isConnected()) {
            System.out.println("Couldn't connect to drone, retrying connection");
            configurationManager.close();
            drone.start();
            configurationManager = drone.getConfigurationManager();

            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("sleep interupted");
            }

            System.out.println("Drone connected: " + configurationManager.isConnected());
        }

        // getting managers
        ConfigurationManager cm = drone.getConfigurationManager();
        //NavDataManager nm = drone.getNavDataManager();
        drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
        System.out.println("Drone connected: " + cm.isConnected());

        drone.getVideoManager().addImageListener(this);
    }

    public static void main(String[] args) {
        new QRDetectionTest();
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        //System.out.println("Hey!");
        this.bufferedImage = bufferedImage;
        letsGetSomeInfo();
    }

    private void letsGetSomeInfo() {
        QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(this.bufferedImage);
        System.out.println("inside letsgetsomeinfo");
        if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
            // no errors!
            System.out.println("Decode message: " + qrInfo.name + ". At: " +
                    qrInfo.x + ", " + qrInfo.y);
        } else if (!qrInfo.error.equals("")){
            System.out.println(qrInfo.error);
        } else {
            System.out.println("HEY!");
        }
    }
}
