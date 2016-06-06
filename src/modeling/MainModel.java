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
	
	public void FillQRPoints(){
		//WPoint: X-koordinat, y-koordinat, z-koordinat, QR-ID
		
		//Wall 0
		QRPoints.add(new QRPoint(188, 1055, 180, 0000));
		QRPoints.add(new QRPoint(0, 0, 180, 0001));
		QRPoints.add(new QRPoint(515, 1055, 180, 0002));
		QRPoints.add(new QRPoint(0, 0, 180, 0003));
		QRPoints.add(new QRPoint(840, 1055, 180, 0004));
		//Wall 1
		QRPoints.add(new QRPoint(926, 903, 180, 0100));
		QRPoints.add(new QRPoint(926, 721, 180, 0101));
		QRPoints.add(new QRPoint(926, 546, 180, 0102));
		QRPoints.add(new QRPoint(926, 324, 180, 0103));
		QRPoints.add(new QRPoint(926, 115, 180, 0104));
		//Wall 2
		QRPoints.add(new QRPoint(150, 0, 180, 0200));
		QRPoints.add(new QRPoint(350, 0, 180, 0201));
		QRPoints.add(new QRPoint(0, 0, 180, 0202));
		QRPoints.add(new QRPoint(0, 0, 180, 0203));
		QRPoints.add(new QRPoint(0, 0, 180, 0204));
		//Wall 3
		QRPoints.add(new QRPoint(0, 108, 180, 0300));
		QRPoints.add(new QRPoint(0, 357, 180, 0301));
		QRPoints.add(new QRPoint(0, 561, 180, 0302));
		QRPoints.add(new QRPoint(0, 740, 180, 0303));
		QRPoints.add(new QRPoint(0, 997, 180, 0304));

	}
}
