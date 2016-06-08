package navigation;

import QRWallMarks.QRInfo;
import de.yadrone.base.IARDrone;

import java.awt.image.BufferedImage;

/**
*
* @author Kim
*/

public class NavFindPosition {
	
	/*
	 * call QR-reader
	 * spin to read 3 QR points
	 * get the calculation of position
	 * declare the position
	 */
	
	public BufferedImage image;
	NavigationControl nc = new NavigationControl();
	IARDrone drone;
	double positionX;
	double positionY;
	
	public NavFindPosition(){
		readQRPoints();
		int[] readQRs = readQRPoints();
		//dronePossition.PointNavigation
				//positionX = ;
				//positionY = ;
		
	}
	
	private int[] readQRPoints(){
		int[] QRs = new int[3];
		//QR
		drone.hover();
		drone.spinLeft();
		
		return QRs;
	}
	
    private void imageUpdated(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    private void letsGetSomeInfo() {
        QRInfo qrInfo = QRWallMarks.GetQRCode.readQRCode(this.image);
        if(qrInfo.error.equals("")) {
            // no errors!
            System.out.println("Decodemessage: " + qrInfo.name + ". At: " +
            					qrInfo.x + ", " + qrInfo.y);
        } else {
            System.out.println(qrInfo.error);
        }
    }
	
	public double getPositionX(){ return positionX; }
	public double getPositionY(){ return positionY; }
	
}











