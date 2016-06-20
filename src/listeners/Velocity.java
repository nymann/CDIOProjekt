package listeners;

import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.VelocityListener;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;

public class Velocity implements VelocityListener {

	public Point3D velocity;
	private long updated;
	
	private	List<VelocityListener> listeners;

    public Velocity(){
		this.listeners = new ArrayList<>();
    }
	
	@Override
	public void velocityChanged(float vy, float vx, float vz) {
		this.updated = System.currentTimeMillis();
		velocity = new Point3D(vx, vy, vz);
		for (VelocityListener listener : listeners) {
			new Thread() {
				@Override
				public void run() {
					listener.velocityChanged(vy, vx, vz);
				}
			}.start();
		}
	}
	
	public void addListener(VelocityListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(VelocityListener listener){
		listeners.remove(listener);
	}
	
	public long lastUpdate(){
		return lastUpdate();
	}
}
