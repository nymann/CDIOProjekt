package test;

import QRWallMarks.QRInfo;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.video.ImageListener;
import listeners.Attitude;
import modeling.Angle3D;
import modeling.MainModel;

import java.awt.image.BufferedImage;

/**
 * Created by Nymann on 13-06-2016.
 */
public class NiceTest implements ImageListener, AttitudeListener {
    private IARDrone drone;
    private BufferedImage bufferedImage;
    private double yaw;
    private Angle3D droneAttitude;

    public NiceTest() {
    	drone = new ARDrone();
        drone.start();
        drone.takeOff();
    	turn360degrees();
        drone.landing();
    }

    public static void main(String[] args) {
    	new NiceTest();

    }

    public void turn360degrees() {
        //MainModel mainModel = new MainModel();
        //Attitude attitude = new Attitude(mainModel);
        //drone.getNavDataManager().addAttitudeListener(attitude);
        yaw = droneAttitude.getYaw();
        System.out.println("\t\t\t" + yaw);
        System.out.println("\t\t\t" + yaw);
        System.out.println("\t\t\t" + yaw);

        double currentYaw = droneAttitude.getYaw();

        int qRCodesFound = 0;
        //drone.getCommandManager().spinLeft(5).doFor(5000); LEGACY
        drone.getCommandManager().spinLeft(5).doFor(1000);
        while ((currentYaw - yaw) > 0.1) {
            drone.getCommandManager().spinLeft(5);
            // Scanning for QR codes.
            QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(bufferedImage);
            if (qrInfo.error.equals("") && !qrInfo.name.equals("")) {
                System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
                        qrInfo.x + ", " + qrInfo.y);
                qRCodesFound++;
            } else {
                System.out.println(qrInfo.error);
            }
            //
            currentYaw = droneAttitude.getYaw();
            System.out.println("Yaw: " + currentYaw);
        }
        System.out.println("QR Codes found: " + qRCodesFound);

        drone.landing();
        //navigateWhenLost(qRCodesFound);
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void attitudeUpdated(float pitch, float roll, float yaw) {
        droneAttitude = new Angle3D(pitch*Math.PI/180000, roll*Math.PI/180000,
                yaw*Math.PI/180000);
    }

    @Override
    public void attitudeUpdated(float pitch, float roll) {
        Angle3D attitude = getDroneAttitude();
        attitude.setPitch(pitch*Math.PI/180000);
        attitude.setRoll(roll*Math.PI/180000);
        setDroneAttitude(attitude);
    }

    @Override
    public void windCompensation(float v, float v1) {

    }

    public void setDroneAttitude(Angle3D droneAttitude) {
        this.droneAttitude = droneAttitude;
    }
    public Angle3D getDroneAttitude() {
        return droneAttitude;
    }
}
