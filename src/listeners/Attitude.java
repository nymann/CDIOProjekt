package listeners;

import de.yadrone.base.navdata.AttitudeListener;
import gui.ListenerValuePanel;

/**
 * @author Simon
 */
public class Attitude implements AttitudeListener {
    ListenerValuePanel listenerValuePanel;
    float pitch, roll, yaw;

    public Attitude(ListenerValuePanel listenerValuePanel){
        this.listenerValuePanel = listenerValuePanel;
    }

    @Override
    public void attitudeUpdated(float pitch, float roll) {
        this.pitch = pitch;
        this.roll = roll;
        System.out.println("Attitude Updated: Pitch: " + pitch + " Roll: " + roll);
        listenerValuePanel.appendText("Attitude Updated: Pitch: " + pitch +
                " Roll: " + roll);
    }

    @Override
    public void attitudeUpdated(float pitch, float roll, float yaw) {
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        //System.out.println("Attitude Updated: Pitch: "+pitch+" Roll: " +
        //""+roll+" Yaw: "+yaw);
        listenerValuePanel.appendText("Attitude Updated: Pitch: " + pitch +
                " Roll: " + roll + " Yaw: " + yaw);
    }

    @Override
    public void windCompensation(float pitch, float roll) {
        this.pitch = pitch;
        this.roll = roll;
        System.out.println("Wind Compensation: Pitch: " + pitch + " Roll: " + roll);
    }

}
