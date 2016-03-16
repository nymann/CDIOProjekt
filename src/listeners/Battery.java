/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import de.yadrone.base.navdata.BatteryListener;
import gui.ListenerValuePanel;

/**
 *
 * @author Mikkel
 */
public class Battery implements BatteryListener{
	public int level;
	public int voltage;
	

	@Override
	public void batteryLevelChanged(int level) {
		this.level = level;
	}

	@Override
	public void voltageChanged(int voltage) {
		this.voltage = voltage;
	}
	
}
