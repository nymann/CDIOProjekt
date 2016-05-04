/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class CustomPoint3D {
	
	public static final CustomPoint3D ZERO = new CustomPoint3D(0,0,0);
	
	public CustomPoint3D(){
		
	}
	
	public CustomPoint3D(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	private int x;
	private int y;
	private int z;

	CustomPoint3D subtract(CustomPoint3D position) {
		CustomPoint3D returnPoint = new CustomPoint3D();
		
		returnPoint.x = this.x - position.x;
		returnPoint.y = this.y -  position.y;
		returnPoint.z = this.z -  position.z;
		
		return returnPoint;
	}

	int distance(CustomPoint3D point) {
		int x = this.x - point.x;
		int y = this.y -  point.y;
		int z = this.z -  point.z;
		return (int) Math.sqrt(x*x+y*y+z*z);
	}
	
	
}
