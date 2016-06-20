/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import de.yadrone.base.navdata.BatteryListener;
import gui.FullGUI;

/**
 *
 * @author Mikkel
 */
public class Battery implements BatteryListener{
	public int level;
	public int voltage;
	private FullGUI gui;
	
	public Battery(FullGUI gui){
		this.gui = gui;
	}

	@Override
	public void batteryLevelChanged(int level) {
		this.level = level;
		gui.setBateryLevel(level);
	}

	@Override
	public void voltageChanged(int voltage) {
		this.voltage = voltage;
	}
	
}
