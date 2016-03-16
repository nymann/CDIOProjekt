package listeners;

import de.yadrone.base.navdata.UltrasoundData;
import de.yadrone.base.navdata.UltrasoundListener;

public class UltraSound implements UltrasoundListener{
		public UltrasoundData arg0;
	


	@Override
	public void receivedRawData(UltrasoundData arg0) {
		this.arg0 = arg0;
	}
	
	public int getDisEcho() {
		
		int dist = arg0.getDistanceEcho();
		System.out.println(dist);
		
		return dist;
	}
	

}