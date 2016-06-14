package listeners;

import de.yadrone.base.navdata.VelocityListener;
import javafx.geometry.Point3D;

public class Velocity implements VelocityListener {

	public Point3D velocity;
	
	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		velocity = new Point3D(vx, vy, vz);
	}
}
