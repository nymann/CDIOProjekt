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
import gui.TextPanel;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mikkel
 */
public class ExceptionListener implements IExceptionListener{
	
	TextPanel output;
	IARDrone drone;
	
	public ExceptionListener(TextPanel output, IARDrone drone){
		this.output = output;
		this.drone = drone;
	}

	@Override
	public void exeptionOccurred(ARDroneException arde) {
		if (output == null){
			System.err.println(arde.getMessage());
		} else {
			output.addTextLine(arde.getMessage());
		}
		if (arde.getCause() instanceof SocketTimeoutException){
			output.addTextLine("Timeout detected!!");
			try {
				drone.getConfigurationManager().connect(ARDroneUtils.CONTROL_PORT);
			} catch (IOException ex) {
				output.addTextLine("Couldn't recconect");
				output.addTextLine(ex.getMessage());
			}
		}
	}
	
}
