/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeling;

/**
 *
 * @author Mikkel
 */
public class Angle3D {
	private double pitch;
	private double roll;
	private double yaw;
	
	public Angle3D(double pitch, double roll,double yaw){
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
	}

	public Angle3D(double pitch, double roll){
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = 0;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}
	
}
