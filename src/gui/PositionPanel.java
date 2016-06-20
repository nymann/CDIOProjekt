/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.VelocityListener;
import java.awt.Dimension;
import java.awt.Graphics;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;
import modeling.Angle3D;

/**
 *
 * @author Mikkel
 */
public class PositionPanel extends JPanel implements AttitudeListener, VelocityListener {

	static final int ROOM_X = 900;
	static final int ROOM_Y = 1000;

	Point2D position;
	Point2D velocity;
	Matrix4d rotation;
	long lastUpdate = 0;

	public PositionPanel() {
		this.position = new Point2D(0, 0);
		this.velocity = new Point2D(0, 0);
		Dimension preferredSize = new Dimension(ROOM_X / 2, ROOM_Y / 2);
		super.setSize(preferredSize);
		super.setPreferredSize(preferredSize);
	}

	private void setRotationMatrix(Angle3D angle) {
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

	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		Angle3D attitude = new Angle3D(pitch * Math.PI / 180000, roll * Math.PI / 180000, yaw * Math.PI / 180000);
		setRotationMatrix(attitude);
	}

	@Override
	public void attitudeUpdated(float pitch, float roll) {
	}

	@Override
	public void windCompensation(float pitch, float roll) {
	}

	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		long currentTime = System.currentTimeMillis();
		if (lastUpdate != 0) {
			Point2D distance = velocity.multiply((currentTime - lastUpdate) / 10000.0);
			position.add(distance);
		}
		if (rotation != null) {
			Vector3f v = new Vector3f(vx, vy, vz);
			rotation.transform(v);
			velocity = new Point2D(v.x, v.y);
			this.repaint();
		}
		lastUpdate = currentTime;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int h = this.getHeight();
		int w = this.getWidth();
		int size = 10;
		int cx = (w - size) / 2;
		int cy = (h - size) / 2;
		int x = (int) position.getX() * w / ROOM_X;
		int y = (int) position.getY() * h / ROOM_Y;

		g.fillOval(cx + x, cy - y, size, size);
		g.drawString("X:" + (int) position.getX(), 0, 10);
		g.drawString("Y:" + (int) position.getY(), 0, 20);

	}

}
