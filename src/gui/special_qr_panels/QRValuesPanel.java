/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.special_qr_panels;

import dronePosition.QRPositioning;
import gui.ListenerValuePanel;

/**
 *
 * @author Mikkel
 */
public class QRValuesPanel extends ListenerValuePanel{
	QRPositioning qpos;

		// debugging
	public QRValuesPanel(QRPositioning qpos){
		super();
		this.qpos = qpos;
	}
	
	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		super.attitudeUpdated(pitch, roll, yaw);
		if (this.qpos != null){
			qpos.setYaw(yaw);
		}
	}
}
