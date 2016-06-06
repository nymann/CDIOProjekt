/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class MainModel {
	
	List<QRPoint> QRPoints = new ArrayList<>();
	
	private Point3D dronePosition;
	private Angle3D droneOrientation;
	private Point3D droneDirection;
	private Point3D roomSize;
	
	
	private List<Cube> cubes = new ArrayList<>();
	
	public void addCube(Cube newCube){
		cubes.add(newCube);
	}
	
	/**
	 * test whether the model contains a cube similar to the one tested
	 * eg. same color and similar position
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
	
	public void FillQRPoints(){
		//WPoint: X-koordinat, y-koordinat, z-koordinat, QR-ID
		int z = 180;
		//Wall 0
		QRPoints.add(new QRPoint(188, 1055, z, 0000));
		QRPoints.add(new QRPoint(338, 1060, z, 0001));
		QRPoints.add(new QRPoint(515, 1055, z, 0002));
		QRPoints.add(new QRPoint(694, 1060, z, 0003));
		QRPoints.add(new QRPoint(840, 1055, z, 0004));
		//Wall 1
		QRPoints.add(new QRPoint(926, 904, z, 0100));
		QRPoints.add(new QRPoint(926, 721, z, 0101));
		QRPoints.add(new QRPoint(926, 566, z, 0102));
		QRPoints.add(new QRPoint(926, 324, z, 0103));
		QRPoints.add(new QRPoint(926, 115, z, 0104));
		//Wall 2
		QRPoints.add(new QRPoint(847, -10, z, 0200));
		QRPoints.add(new QRPoint(656, -77, z, 0201));
		QRPoints.add(new QRPoint(420,   0, z, 0202));
		QRPoints.add(new QRPoint(350,   0, z, 0203));
		QRPoints.add(new QRPoint(150,   0, z, 0204));
		//Wall 3
		QRPoints.add(new QRPoint(0, 108, z, 0300));
		QRPoints.add(new QRPoint(0, 357, z, 0301));
		QRPoints.add(new QRPoint(0, 561, z, 0302));
		QRPoints.add(new QRPoint(0, 740, z, 0303));
		QRPoints.add(new QRPoint(0, 997, z, 0304));

	}
}
