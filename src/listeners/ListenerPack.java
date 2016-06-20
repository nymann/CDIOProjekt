/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.NavDataManager;
import dronePosition.QRPositioning;
import gui.DroneStateListener;
import gui.FullGUI;
import javafx.geometry.Point3D;

/**
 *
 * @author Mikkel
 */
public class ListenerPack {
	private final Altitude alt;
	private final Battery bat;
	private final Velocity vel;
	private final Attitude att = new Attitude();
	private final Accelerometer acc;
	private final DroneStateListener dsl;
	
	public ListenerPack(FullGUI gui){
		this.vel = new Velocity();
		this.bat = new Battery(gui);
		this.alt = new Altitude(gui);
		this.acc = new Accelerometer(gui);
		vel.addListener(gui);
		dsl = new DroneStateListener(gui);
		att.addListener(gui);
	}
	
	public void addListeners(NavDataManager navDataManager){
		navDataManager.addVelocityListener(vel);
		navDataManager.addAltitudeListener(alt);
		navDataManager.addStateListener(dsl);
		navDataManager.addBatteryListener(bat);
		navDataManager.addAcceleroListener(acc);
		navDataManager.addAttitudeListener(att);
	}
	
	public void addAttitudeListener(AttitudeListener listener){
		att.addListener(listener);
	}
	
	public void removeAttitudeListener(AttitudeListener listener) {
		att.removeListener(listener);
	}
	
	public long getLastAltUpdate(){
		return alt.getLastUpdate();
	}
	
	public long getLastVelocityUpdate(){
		return vel.lastUpdate();
	}
	
	public int getAltitude(){
		return alt.extendedAltitude.getRaw();
	}
	
	public float getZVelocity(){
		return alt.extendedAltitude.getZVelocity();
	}
	
	public Point3D getVelocity(){
		return vel.velocity;
	}
	
	public Point3D getAcceleration(){
		int[] v = acc.accrawd.getRawAccs();
		return new Point3D(-v[1], -v[0],v[2]);
	}
	
	public boolean accelerationUpdated(){
		return acc.acchysd != null;
	}
	
	public boolean altitudeUpdated(){
		return alt.extendedAltitude != null;
	}

	public void calibrateAcc(boolean cal){
		acc.calibration(cal);
	}
	
}
