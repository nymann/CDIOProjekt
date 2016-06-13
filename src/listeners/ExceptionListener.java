/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import gui.TextPanel;

/**
 *
 * @author Mikkel
 */
public class ExceptionListener implements IExceptionListener{
	
	TextPanel output;
	
	public ExceptionListener(TextPanel output){
		this.output = output;
	}

	@Override
	public void exeptionOccurred(ARDroneException arde) {
		if (output == null){
			System.out.println(arde.getMessage());
		} else {
			output.addTextLine(arde.getMessage());
		}
	}
	
}
