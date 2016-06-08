/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.special_qr_panels;

import dronePossition.QRPossitioning;
import gui.ListenerValuePanel;

/**
 *
 * @author Mikkel
 */
public class QRValuesPanel extends ListenerValuePanel{
	QRPossitioning qpos;

		// debugging
	public QRValuesPanel(QRPossitioning qpos){
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
