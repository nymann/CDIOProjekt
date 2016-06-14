package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.navdata.UltrasoundData;
import de.yadrone.base.navdata.UltrasoundListener;
import de.yadrone.base.video.VideoManager;
import video.VideoReader;

public class UltraSoundTest {

	public static void main(String[] args) {
		IARDrone drone = null;
		// connecting to drone
		try {
			drone = new ARDrone();
			System.out.println("Starting Drone");
			drone.start();
			final CommandManager cmd = drone.getCommandManager();
//			final VideoManager vmd = drone.getVideoManager();
//			video.VideoReader vid = new VideoReader(vmd, cmd);
			new UltraSoundListener(drone);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		} 
	}
	
}
	class UltraSoundListener{

		public UltraSoundListener(IARDrone drone){
			drone.getNavDataManager().addUltrasoundListener(new UltrasoundListener(){

				@Override
				public void receivedRawData(UltrasoundData arg0) {
					System.out.println("UltraSound data: " + "\t" + arg0);
					
				}
				
			});
		}
	}
