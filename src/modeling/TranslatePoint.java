/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javax.vecmath.*;

/**
 *
 * @author Mikkel
 */
public class TranslatePoint {
	Matrix4d rotation;
	Point3D position;
	
	public void setDroneInfo(Angle3D angle, Point3D position){
		this.position = position;
		Matrix4d yaw = new Matrix4d();
		Matrix4d pitch = new Matrix4d();
		Matrix4d roll = new Matrix4d();
		yaw.rotZ(angle.getYaw());
		pitch.rotX(angle.getPitch());
		roll.rotY(angle.getRoll());
		rotation = new Matrix4d();
		rotation.setIdentity();
		rotation.mul(roll);
		rotation.mul(pitch);
		rotation.mul(yaw);
	}
	
	public Point2D intersectFloor(Point3D direction){
		Vector3d vector = new Vector3d(direction.getX(), direction.getY(), direction.getZ());
		rotation.transform(vector);
		double factor = position.getZ()/vector.z;
		vector.scale(factor);
		Point2D intersection = new Point2D(vector.x+position.getX(), vector.y+position.getY());
		return intersection;
	}
	
}
