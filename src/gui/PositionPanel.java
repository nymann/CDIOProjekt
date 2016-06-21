/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.VelocityListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javafx.geometry.Point2D;
import javax.swing.JPanel;
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
	static final int PANEL_HEIGHT = 500;
	static final int PANEL_WIDTH = 450;

	private Point2D position;
	private Point2D velocity;
	private Matrix4d rotation;
	private Point2D distance;
	private long lastUpdate = 0;
	private long updateTime = 0;

	public PositionPanel() {
		this.position = new Point2D(0, 0);
		this.velocity = new Point2D(0, 0);
		Dimension preferredSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		super.setSize(preferredSize);
		super.setPreferredSize(preferredSize);
	}

	private void setRotationMatrix(Angle3D angle) {
		Matrix4d yaw = new Matrix4d();
		Matrix4d pitch = new Matrix4d();
		Matrix4d roll = new Matrix4d();
		yaw.rotZ(-angle.getYaw());
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
			updateTime = currentTime - lastUpdate;
			distance = velocity.multiply((updateTime) / 10000.0);
			position = position.add(distance);
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
		g.fillRect(0, 0, w, h);
		int size = 10;
		int cx = (w - size) / 2;
		int cy = (h - size) / 2;
		int x = (int) position.getX() * w / ROOM_X;
		int y = (int) position.getY() * h / ROOM_Y;
		Vector3f[] points = new Vector3f[4];
		points[0] = new Vector3f(0.0f, -5.0f, 0.0f);
		points[1] = new Vector3f(-10.0f, -15.0f, 0.0f);
		points[2] = new Vector3f(0.0f, 15.0f, 0.0f);
		points[3] = new Vector3f(10.0f, -15.0f, 0.0f);
		if (rotation != null) {
			rotation.transform(points[0]);
			rotation.transform(points[1]);
			rotation.transform(points[2]);
			rotation.transform(points[3]);
		}
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];
		for (int i = 0; i < 4; i++) {
			xPoints[i] = cx + x + (int) points[i].x;
			yPoints[i] = cy - y - (int) points[i].y;
		}
		g.setColor(Color.GREEN);
		g.fillPolygon(xPoints, yPoints, 4);
		g.drawLine(cx+x, cy-y, cx+x+(int)velocity.getX(), cy-y-(int)velocity.getY());
		
		
		g.setColor(Color.WHITE);
		g.drawString("X:" + (int)position.getX(), 0, 10);
		g.drawString("Y:" + (int)position.getY(), 0, 20);
		g.drawString("VX:" + velocity.getX() + "mm/s", 100, 10);
		g.drawString("VY:" + velocity.getY() + "mm/s", 100, 20);
		g.drawString("Update:" + updateTime + "ms", 0, 30);
		if (distance != null) {
			g.drawString("Dx:" + distance.getX() + "cm", 100, 30);
			g.drawString("Dy:" + distance.getY() + "cm", 100, 40);
		}
	}

}
