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
	final static double frontDepth = Math.sqrt(frontWidth*frontWidth + frontHeight*frontHeight) / 2 / Math.tan(Math.toRadians(frontCamAngle/2));
	
	final static float downCamAngle = 64;
	final static int downHeight = 360;
	final static int downWidth = 640;
	final static double downDepth = Math.sqrt(downWidth*downWidth + downHeight*downHeight)/ 2 / Math.tan(Math.toRadians(downCamAngle/2));
	
	/**
	 *
	 * @param x
	 * @param y
	 * @return a normalized vector of the direction relative to the camera
	 */
	public static Point3D pictureCoordToVectorFront(int x, int y){
		x = convertX(x, frontWidth);
		y = convertY(y, frontHeight);
		Point3D point = new Point3D(x, y, frontDepth);
		return point.normalize();
	} 

	/**
	 *
	 * @param x
	 * @param y
	 * @return a normalized vector of the direction relative to the camera
	 */
	public static Point3D pictureCoordToVectorDown(int x, int y){
		x = convertX(x, downWidth);
		y = convertY(y, downHeight);
		Point3D point = new Point3D(x, y, downDepth);
		return point.normalize();
	}
	
	private static int convertX(int x, int width){
		return x - width/2;
	}
	
	private static int convertY(int y, int height){
		return height/2 -y;
	}
	
}
