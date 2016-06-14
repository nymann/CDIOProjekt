package test;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.UltrasoundData;
import de.yadrone.base.navdata.UltrasoundListener;
import de.yadrone.base.video.VideoManager;
import video.VideoReader;

public class UltraSoundTest implements AltitudeListener{

	public UltraSoundTest() {
		IARDrone drone = null;
		drone = new ARDrone();
		System.out.println("Starting Drone");
		drone.start();
		final CommandManager cmd = drone.getCommandManager();
		drone.getNavDataManager().addAltitudeListener(this);
//		new UltraSoundListener(drone);
	}
	
	public static void main(String[] args) {
		// connecting to drone
		try {
			
			new UltraSoundTest();
//			final VideoManager vmd = drone.getVideoManager();
//			video.VideoReader vid = new VideoReader(vmd, cmd);
			Thread.sleep(5000);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		} 
	}

	@Override
	public void receivedAltitude(int arg0) {
//		System.out.println("Altitude: " + "\t" + arg0);
		
	}

	@Override
	public void receivedExtendedAltitude(Altitude arg0) {
		System.out.println("Altitude: " + "\t" + arg0);
		
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
	
