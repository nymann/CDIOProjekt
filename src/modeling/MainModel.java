/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

import QRWallMarks.QRInfo;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class MainModel {
	
	private List<List<QRPoint>> QRPoints = new ArrayList<>();
	private List<NavSpot> navSpots = new ArrayList<>();
	private List<Cube> cubes = new ArrayList<>();
	
	private Point3D dronePosition;
	private Angle3D droneAttitude;
	private Point3D droneDirection;
	private Point3D roomSize;

	public MainModel() {
		FillQRPoints();
		FillNavSpots();
	}
	
	public void addCube(Cube newCube){
		cubes.add(newCube);
	}
	
	/**
	 * test whether the model contains a cube similar to the one tested
	 * eg. same color and similar position
	 * @param newCube
	 * @param tolerance
	 * @return ture if the model contains a similar cube
	 */
	public boolean compareCube(Cube newCube, double tolerance){
		for (Cube currentCube: cubes){
			if (newCube.getColor().equals(currentCube.getColor())){
				if (newCube.getPosition().subtract(currentCube.getPosition()).distance(CustomPoint3D.ZERO) < tolerance){
					return true;
				}
			}
		}
		return false;
	}
	
	public QRPoint getQRPoint(QRInfo info){
		String[] id = info.name.substring(1).split("\\.");
		int wall = Integer.parseInt(id[0]);
		int kode = Integer.parseInt(id[1]);
		return QRPoints.get(wall).get(kode);
	}
	
	private void FillQRPoints(){
		//WPoint: X-koordinat, y-koordinat, z-koordinat, QR-ID
		int z = 180;
		//Wall 0
		ArrayList<QRPoint> wall = new ArrayList<>();
		wall.add(new QRPoint(188, 1055, z, 0000));
		wall.add(new QRPoint(338, 1060, z, 0001));
		wall.add(new QRPoint(515, 1055, z, 0002));
		wall.add(new QRPoint(694, 1060, z, 0003));
		wall.add(new QRPoint(840, 1055, z, 0004));
		QRPoints.add(wall);
		//Wall 1
		wall = new ArrayList<>();
		wall.add(new QRPoint(926, 904, z, 0100));
		wall.add(new QRPoint(926, 721, z, 0101));
		wall.add(new QRPoint(926, 566, z, 0102));
		wall.add(new QRPoint(926, 324, z, 0103));
		wall.add(new QRPoint(926, 115, z, 0104));
		QRPoints.add(wall);
		//Wall 2
		wall = new ArrayList<>();
		wall.add(new QRPoint(847, -10, z, 0200));
		wall.add(new QRPoint(656, -77, z, 0201));
		wall.add(new QRPoint(514,   0, z, 0202));
		wall.add(new QRPoint(328,   0, z, 0203));
		wall.add(new QRPoint(143,   0, z, 0204));
		QRPoints.add(wall);
		//Wall 3
		wall = new ArrayList<>();
		wall.add(new QRPoint(0, 108, z, 0300));
		wall.add(new QRPoint(0, 357, z, 0301));
		wall.add(new QRPoint(0, 561, z, 0302));
		wall.add(new QRPoint(0, 740, z, 0303));
		wall.add(new QRPoint(0, 997, z, 0304));
		QRPoints.add(wall);
	}
	
	private void FillNavSpots(){
		//spot: X-koordinat, y-koordinat, z-koordinat, spot-ID: lige->startSpots, ulige->endSpots
		int z = 0;
		int x0 = 75;
		int x1 = 851;
		
		navSpots.add(new NavSpot(x0,  76, z,  0));
		navSpots.add(new NavSpot(x1,  76, z,  1));
		navSpots.add(new NavSpot(x0, 228, z,  2));
		navSpots.add(new NavSpot(x1, 228, z,  3));
		navSpots.add(new NavSpot(x0, 380, z,  4));
		navSpots.add(new NavSpot(x1, 380, z,  5));
		navSpots.add(new NavSpot(x0, 532, z,  6));
		navSpots.add(new NavSpot(x1, 532, z,  7));
		navSpots.add(new NavSpot(x0, 684, z,  8));
		navSpots.add(new NavSpot(x1, 684, z,  9));
		navSpots.add(new NavSpot(x0, 836, z, 10));
		navSpots.add(new NavSpot(x1, 836, z, 11));
		navSpots.add(new NavSpot(x0, 988, z, 12));
		navSpots.add(new NavSpot(x1, 988, z, 13));
		
	}	
	
	public NavSpot getNavSpot(int id){
		NavSpot spot = navSpots.get(id);
		return spot;
	}

	public Point3D getDronePosition() {
		return dronePosition;
	}

	public void setDronePosition(Point3D dronePosition) {
		this.dronePosition = dronePosition;
	}
	
	public void setDronePosition(Point2D p) {
		this.dronePosition = new Point3D(p.getX(), p.getY(), this.dronePosition.getZ());
	}
	
	
}








