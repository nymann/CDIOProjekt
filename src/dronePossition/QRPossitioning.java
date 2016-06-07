package dronePossition;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import QRWallMarks.GetQRCode;
import QRWallMarks.QRInfo;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.video.ImageListener;
import javafx.geometry.Point3D;
import video.CameraUtil;

public class QRPossitioning implements ImageListener, AttitudeListener {
		
	float yaw;
	ArrayList<QRInfo> qrListe;
	
	public QRPossitioning() {
		qrListe = new ArrayList<QRInfo>();
	}

	@Override
	public void imageUpdated(BufferedImage qrcodeImage) {
		
		float currentYaw = this.yaw;		
		QRInfo qri = GetQRCode.readQRCode(qrcodeImage);
		
		if(qri.error.equals("")) {
			Point3D point3d = CameraUtil.pictureCoordToVectorFront(qri.x, qri.y);
			double angle = Math.atan(point3d.getX()/point3d.getZ()); 
			angle = (angle + currentYaw*Math.PI/180000+Math.PI)%(Math.PI*2);
			qri.angle = angle;
			qrListe.add(qri);
			
			/* Forslag: 
			 * if(qrListe.size() == 3) {
				double diff1 = qrListe.get(0).angle - qrListe.get(1).angle;
				double diff2 = qrListe.get(1).angle - qrListe.get(2).angle;
				double diff3 = qrListe.get(2).angle - qrListe.get(0).angle;
				
				if(diff1 < diff3 && diff2 < diff3) {
					//Brug diff1 og diff2.
					 * Disse er de mindste vinkler.
				}
				else if(diff1 < diff2 && diff3 < diff2) {
					//Brug diff1 og diff3.
					 * Disse er de mindste vinkler.
				}
				// Skal beregne alle vinklerne imellem QR-koderne.
				// Så, mellem 1-2, 2-3 og 1-3.
				// Finder de mindste vinkler.
				// Finder den QR-koder der bliver delt imellem de to vinkler.
				// Finder de to andre.
				// Herved finder man alpha og beta vinkelerne.
			} */
		}			
	}

	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		this.yaw = yaw;
	}
	
	@Override
	public void attitudeUpdated(float pitch, float roll) {}
	
	@Override
	public void windCompensation(float pitch, float roll) {}
	
	
	
}
