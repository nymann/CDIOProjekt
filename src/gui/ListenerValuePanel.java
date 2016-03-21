package gui;

import de.yadrone.base.navdata.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nymann on 16-03-2016.
 */
public class ListenerValuePanel extends JPanel implements AttitudeListener,
		AcceleroListener {

	JTextArea jTextArea;
	JWindow jWindow = new JWindow();
	JLayeredPane jLayeredPane = new JLayeredPane();

	public ListenerValuePanel() {
		jTextArea = new JTextArea();
		this.add(jTextArea);
	}

	public void setListeners(NavDataManager nm) {
		nm.addAttitudeListener(this);
		nm.addAcceleroListener(this);
	}

	public void appendText(String text) {
		jTextArea.setText(text);
	}

	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		appendText("A-Pitch: " + pitch + ". A-Roll: " + roll + ". A-Yaw: "
				+ yaw + ".");
	}

	@Override
	public void attitudeUpdated(float pitch, float roll) {
		appendText("A-Pitch: " + pitch + ". A-Roll: " + roll + ".");
	}

	@Override
	public void windCompensation(float pitch, float roll) {
		appendText("W-Pitch: " + pitch + ". W-Roll: " + roll + ".");
	}

	@Override
	public void receivedRawData(AcceleroRawData acceleroRawData) {
		System.out.println("acceleroRawData updated.");
	}

	@Override
	public void receivedPhysData(AcceleroPhysData acceleroPhysData) {
		System.out.println("acceleroPhysData updated.");
	}
}
