package listeners;

import de.yadrone.base.navdata.AttitudeListener;
import gui.ListenerValuePanel;
import modeling.Angle3D;
import modeling.MainModel;

/**
 * @author Simon
 */
public class Attitude implements AttitudeListener {
    MainModel model;

    public Attitude(MainModel model){
        this.model = model;
    }

    @Override
    public void attitudeUpdated(float pitch, float roll) {
		Angle3D attitude = model.getDroneAttitude();
		attitude.setPitch(pitch*Math.PI/180000);
		attitude.setRoll(roll*Math.PI/180000);
		model.setDroneAttitude(attitude);
	}

    @Override
    public void attitudeUpdated(float pitch, float roll, float yaw) {
		Angle3D attitude = new Angle3D(pitch*Math.PI/180000, roll*Math.PI/180000, yaw*Math.PI/180000);
		model.setDroneAttitude(attitude);
    }

    @Override
    public void windCompensation(float pitch, float roll) {
    }

}
