/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class CameraUtil {
	
	final static double frontCamAngle = 93;
	final static int frontHeight = 720;
	final static int frontWidth = 1280;
	final static double frontDepth = Math.sqrt(frontWidth*frontWidth + frontHeight*frontHeight) / Math.tan(Math.toRadians(frontCamAngle/2));
	
	final static float downCamAngle = 64;
	final static int downHeight = 0;
	final static int downWidth = 0;
	final static double downDepth = Math.sqrt(downWidth*downWidth + downHeight*downHeight) / Math.tan(Math.toRadians(downCamAngle/2));
	
	/**
	 *
	 * @param x
	 * @param y
	 * @return a normalized vector of the direction relative to the camera
	 */
	public static Point3D pictureCoordToVectorFront(int x, int y){
		Point3D point = new Point3D(x, y, frontDepth);
		point.normalize();
		return point;
	} 

	/**
	 *
	 * @param x
	 * @param y
	 * @return a normalized vector of the direction relative to the camera
	 */
	public static Point3D pictureCoordToVectorDown(int x, int y){
		Point3D point = new Point3D(x, y, downDepth);
		point.normalize();
		return point;
	} 
	
}
