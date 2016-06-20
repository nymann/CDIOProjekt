/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.utils.ARDroneUtils;
import gui.FullGUI;
import gui.TextPanel;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 *
 * @author Mikkel
 */
public class ExceptionListener implements IExceptionListener {

	FullGUI gui;
	IARDrone drone;

	public ExceptionListener(FullGUI gui, IARDrone drone) {
		this.gui = gui;
		this.drone = drone;
	}

	@Override
	public void exeptionOccurred(ARDroneException arde) {
		gui.errorLn(arde.getMessage());

		if (arde.getCause() instanceof SocketTimeoutException) {
			gui.errorLn("Timeout detected!!");
			try {
				if (!drone.getConfigurationManager().isConnected()) {
					drone.getConfigurationManager().connect(ARDroneUtils.CONTROL_PORT);
				}

				if (!drone.getNavDataManager().isConnected()) {
					drone.getNavDataManager().connect(ARDroneUtils.NAV_PORT);
				}

				if (!drone.getCommandManager().isConnected()) {
					drone.getCommandManager().connect(ARDroneUtils.PORT);
				}

				if (!drone.getVideoManager().isConnected()) {
					drone.getVideoManager().connect(ARDroneUtils.VIDEO_PORT);
				}

			} catch (IOException ex) {
				gui.errorLn("Couldn't recconect");
				gui.errorLn(ex.getMessage());
			}
		}
	}

}
