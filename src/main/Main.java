/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import control.DroneControl;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;

/**
 *
 * @author Mikkel
 */
public class Main {

    static public void main(String[] args) {
        IARDrone drone = new ARDrone();
        DroneControl control = new DroneControl(drone);
        control.run();

    }

}
