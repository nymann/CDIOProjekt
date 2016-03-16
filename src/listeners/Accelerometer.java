package listeners;

import de.yadrone.base.navdata.AcceleroListener;
import de.yadrone.base.navdata.AcceleroPhysData;
import de.yadrone.base.navdata.AcceleroRawData;

public class Accelerometer implements AcceleroListener {
	
	public AcceleroPhysData acchysd;
	public AcceleroRawData accrawd;

	@Override
	public void receivedPhysData(AcceleroPhysData arg0) {
		this.acchysd = arg0;		
	}

	@Override
	public void receivedRawData(AcceleroRawData arg1) {
		this.accrawd = arg1;		
	}
	
	

}
