/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;

/**
 *
 * @author Mikkel
 */
public class ExceptionListener implements IExceptionListener{

    @Override
    public void exeptionOccurred(ARDroneException arde) {
        System.err.println(arde.getMessage());
    }
    
}
