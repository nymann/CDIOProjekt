package listeners;

import de.yadrone.base.navdata.VelocityListener;

public class Velocity implements VelocityListener {

	public float vx, vy, vz;
	
	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		
	}
}
