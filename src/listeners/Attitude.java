package listeners;

import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.video.ImageListener;
import gui.ListenerValuePanel;
import java.util.ArrayList;
import java.util.List;
import modeling.Angle3D;
import modeling.MainModel;

/**
 * @author Simon
 */
public class Attitude implements AttitudeListener {

	List<AttitudeListener> listeners;

	public Attitude() {
		this.listeners = new ArrayList<>();
	}

	@Override
	public void attitudeUpdated(float pitch, float roll) {
		Angle3D attitude = MainModel.getDroneAttitude();
		attitude.setPitch(pitch * Math.PI / 180000);
		attitude.setRoll(roll * Math.PI / 180000);
		MainModel.setDroneAttitude(attitude);
		for (AttitudeListener listener : listeners) {
			new Thread() {
				@Override
				public void run() {
					listener.attitudeUpdated(pitch, roll);
				}
			}.start();
		}
	}

	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		Angle3D attitude = new Angle3D(pitch * Math.PI / 180000, roll * Math.PI / 180000, yaw * Math.PI / 180000);
		MainModel.setDroneAttitude(attitude);
		for (AttitudeListener listener : listeners) {
			new Thread() {
				@Override
				public void run() {
					listener.attitudeUpdated(pitch, roll, yaw);
				}
			}.start();
		}
	}

	@Override
	public void windCompensation(float pitch, float roll) {
		synchronized (listeners) {
			for (AttitudeListener listener : listeners) {
				new Thread() {
					@Override
					public void run() {
						listener.windCompensation(pitch, roll);
					}
				}.start();
			}
		}
	}

	public void addListener(AttitudeListener listener) {
		listeners.add(listener);
	}

	public void removeListener(AttitudeListener listener) {
		listeners.remove(listener);
	}

}
