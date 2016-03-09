/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;
import main.Main;

import de.yadrone.base.IARDrone;
import java.util.Scanner;

/**
 *
 * @author Mikkel
 */
public class DroneControl {

    IARDrone drone;

    public DroneControl(IARDrone drone) {
        this.drone = drone;
    }

    public void run(){
		System.out.println("Controlling drone");
        System.out.println("Drone speed:" + drone.getSpeed());
		
       Scanner scanner = new Scanner(System.in); // opret scanner-objekt
	   boolean done = false;
       while (!done) {
			System.out.println("Type command");
            String input = scanner.nextLine();
			switch (input){
				case "q":
					drone.up();
				break;
				case "e":
					drone.down();
				break;
				case "w":
					drone.forward();
				break;
				case "s":
					drone.backward();
				break;
				case "a":
					drone.goLeft();
				break;
				case "d":
					drone.goRight();
				break;
				case " ":
					drone.landing();
				break;
				case "o":
					drone.takeOff();
				break;
				case "h":
					drone.hover();
				break;
				case "r":
					drone.reset();
				break;
				default:
					done = true;
					break;
			}
        } 
        
        System.out.println("Stoping control");
		Main.done = true;
 
    }
}


