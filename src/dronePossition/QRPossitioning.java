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
			
			if(qrListe.size() == 3) {
				
			}
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
