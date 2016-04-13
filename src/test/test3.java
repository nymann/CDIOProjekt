package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.BatteryListener;

public class test3 
{

	public static void main(String[] args)
	{
		IARDrone drone = null;
		try
		{
			drone = new ARDrone();
			drone.start();

			drone.getNavDataManager().addAttitudeListener(new AttitudeListener() {
				public void attitudeUpdated(float pitch, float roll, float yaw) {
					System.out.println("Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
				}
				public void attitudeUpdated(float pitch, float roll) { }
				public void windCompensation(float pitch, float roll) { }
			});

			drone.getNavDataManager().addBatteryListener(new BatteryListener() {
				public void batteryLevelChanged(int percentage) {
					System.out.println("Battery: " + percentage + " %");
				}
				public void voltageChanged(int vbat_raw) { }
			});
			drone.addExceptionListener(new IExceptionListener() {
				public void exeptionOccurred(ARDroneException exc)
				{
					exc.printStackTrace();
				}
			});

			//Dontrolling the drone

			drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
			
			drone.setSpeed(15);
			
			drone.getCommandManager().takeOff();
			drone.getCommandManager().hover().doFor(10000);
			//drone.getCommandManager().forward(15).waitFor(2000);
			//drone.getCommandManager().backward(15).waitFor(2000);
			//drone.getCommandManager().waitFor(5000);
			drone.getCommandManager().landing();
			//drone.stop();
			//System.exit(0);

			/**
			 *  CommandManager cmd = drone.getCommandManager();
				int speed = 30; // percentage of max speed
				
				cmd.takeOff().doFor(5000);
				
				cmd.goLeft(speed).doFor(1000);
				cmd.hover().doFor(2000);
				
				cmd.goRight(speed).doFor(1000);
				cmd.hover().doFor(2000);
				
				cmd.forward(speed).doFor(2000);
				cmd.hover().doFor(1000);
				
				cmd.backward(speed).doFor(2000);
				cmd.hover().doFor(2000);
				
				cmd.landing();
			 */
		}
		catch (Exception exc)
		{
			System.out.println("Exception");
			exc.printStackTrace();
		}
		finally
		{
			System.out.println("Finally");
			if (drone != null)
				drone.stop();
			System.exit(0);

		}	


	}
}
