/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.navdata.ControlState;
import de.yadrone.base.navdata.DroneState;
import de.yadrone.base.navdata.StateListener;

/**
 *
 * @author Mikkel
 */
public class DroneStateListener implements StateListener{

	DroneState droneState;
	ControlState controlState;
	FullGUI gui;
	
	public DroneStateListener(FullGUI gui){
		this.gui = gui;
	}
	
	@Override
	public void stateChanged(DroneState state) {
		this.droneState = state;
		gui.setDroneStatus(state.toString());
	}

	@Override
	public void controlStateChanged(ControlState state) {
		this.controlState = state;
	}
	
}
