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
				if (newCube.getPosition().subtract(currentCube.getPosition()).distance(Point3D.ZERO) < tolerance){
					return true;
				}
			}
		}
		return false;
	}
	
	
}
