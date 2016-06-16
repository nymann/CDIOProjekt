/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class Position extends Velocity {

	private long lastUpdate = 0;
	private Point3D position;

	public Position() {
		this(null);
	}

	public Position(Point3D start) {
		if (start != null) {
			this.position = start;
		} else {
			this.position = new Point3D(0, 0, 0);
		}
	}

	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		super.velocityChanged(vx, vy, vz);
		if (lastUpdate != 0) {
			long elapsedTime = System.currentTimeMillis() - lastUpdate;
			Point3D movement = new Point3D(vx, vy, vz).multiply(elapsedTime);
			position = position.add(movement);
		}
		lastUpdate = System.currentTimeMillis();
	}
	
	public Point3D getPosition(){
		return this.position;
	}

}
