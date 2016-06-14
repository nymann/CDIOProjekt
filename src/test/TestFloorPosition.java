/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import modeling.Angle3D;
import modeling.TranslatePoint;
import video.CameraUtil;

/**
 *
 * @author Mikkel
 */
public class TestFloorPosition {
	public static void main(String[] args) {
		Angle3D rotation = new Angle3D(-0.2,0.1,Math.PI/2);
		Point3D position = new Point3D(400, 300, 180);
		Point3D direction = CameraUtil.pictureCoordToVectorDown(160, 120);
		TranslatePoint translate = new TranslatePoint();
		translate.setDroneInfo(rotation, position);
		Point2D floorPosition = translate.intersectFloor(direction);
		System.out.println("direction:" + direction);
		System.out.println("floorPosition:" + floorPosition);
	}
}
