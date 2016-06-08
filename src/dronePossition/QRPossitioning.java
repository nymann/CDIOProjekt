package dronePossition;

import QRWallMarks.GetQRCode;
import QRWallMarks.QRInfo;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.video.ImageListener;
import javafx.geometry.Point3D;
import modeling.MainModel;
import modeling.QRPoint;
import video.CameraUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class QRPossitioning implements ImageListener, AttitudeListener {
		
	float yaw;
	ArrayList<QRInfo> qrListe;
	MainModel model;
	
	public QRPossitioning(MainModel model) {
		this.qrListe = new ArrayList<>();
		this.model = model;
	}

	@Override
	public void imageUpdated(BufferedImage qrcodeImage) {
		
		float currentYaw = this.yaw;		
		QRInfo qri = GetQRCode.readQRCode(qrcodeImage);
		
		if(qri.error.equals("")) {
			Point3D point3d = CameraUtil.pictureCoordToVectorFront(qri.x, qri.y);
			//TODO vi skal tage højde for at dronen måske ikke ligger vandret
			double angle = Math.atan(point3d.getX()/point3d.getZ()); 
			angle = (angle + currentYaw*Math.PI/180000+Math.PI)%(Math.PI*2);
			qri.angle = angle;
			qrListe.add(qri);
			
			if(qrListe.size() == 3) {
				double angle1 = qrListe.get(0).angle - qrListe.get(1).angle;
				angle1 = Math.abs(angle1) < Math.PI ? angle1 : qrListe.get(1).angle - qrListe.get(0).angle;
				
				double angle2 = qrListe.get(1).angle - qrListe.get(2).angle;
				angle2 = Math.abs(angle2) < Math.PI ? angle2 : qrListe.get(2).angle - qrListe.get(1).angle;
				
				double angle3 = qrListe.get(2).angle - qrListe.get(0).angle;
				angle3 = Math.abs(angle3) < Math.PI ? angle3 : qrListe.get(0).angle - qrListe.get(2).angle;
				
				double abs1 = Math.abs(angle1);
				double abs2 = Math.abs(angle2);
				double abs3 = Math.abs(angle3);
				
				double alpha,beta;
				ArrayList<QRInfo> sortedList = new ArrayList<>();
				
				if(abs1 < abs3 && abs2 < abs3) {
					//Brug angle1 og angle2. Disse er de mindste vinkler.
					//Kode 1 er i midten
					if (angle1 > 0) { // Kode 0 er til højre og Kode 2 til venstre
						alpha = angle2;
						beta = angle1;
						sortedList.add(qrListe.get(2));
						sortedList.add(qrListe.get(1));
						sortedList.add(qrListe.get(0));
					} else {
						alpha = angle1;
						beta = angle2;
						sortedList.add(qrListe.get(0));
						sortedList.add(qrListe.get(1));
						sortedList.add(qrListe.get(2));
					}
				}
				else if(abs1 < abs2 && abs3 < abs2) {
					//Brug angle1 og angle3. Disse er de mindste vinkler.
					//Kode 0 er i midten
					if (angle1 > 0) { // Kode 1 er til venstre og Kode 2 til højre
						alpha = angle1;
						beta = angle3;
						sortedList.add(qrListe.get(1));
						sortedList.add(qrListe.get(0));
						sortedList.add(qrListe.get(2));
					} else {
						alpha = angle3;
						beta = angle1;
						sortedList.add(qrListe.get(2));
						sortedList.add(qrListe.get(0));
						sortedList.add(qrListe.get(1));
					}
				} else {
					//Brug angle2 og angle3. Disse er de mindste vinkler.
					//Kode 2 er i midten
					if (angle2 > 0) { // Kode 1 er til højre og Kode 0 til venstre
						alpha = angle3;
						beta = angle2;
						sortedList.add(qrListe.get(0));
						sortedList.add(qrListe.get(2));
						sortedList.add(qrListe.get(1));
					} else {
						alpha = angle2;
						beta = angle3;
						sortedList.add(qrListe.get(1));
						sortedList.add(qrListe.get(2));
						sortedList.add(qrListe.get(0));
					}
				}
				
				List<QRPoint> points = new ArrayList<>();
				for (int i = 0; i < 3; i++){
					points.add(model.getQRPoint(sortedList.get(i)));
				}
				
				PointNavigation nav = new PointNavigation();
				nav.setAngelA(alpha);
				nav.setAngelB(beta);
				nav.setCoordinats(
						points.get(0).getX(),
						points.get(0).getY(),
						points.get(1).getX(),
						points.get(1).getY(),
						points.get(2).getX(),
						points.get(2).getY()
				);

				model.setDronePosition(nav.findPosition());
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