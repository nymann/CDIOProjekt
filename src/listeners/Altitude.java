package listeners;

import de.yadrone.base.navdata.AltitudeListener;
import gui.FullGUI;

/**
 * Created by Nymann on 16-03-2016.
 */
public class Altitude implements AltitudeListener {
    public int altitude;
    public de.yadrone.base.navdata.Altitude extendedAltitude;
	
	private long lastUpdate;
	private FullGUI gui;
	
	public Altitude(FullGUI gui){
		this.gui = gui;
	}

	@Override
    public void receivedAltitude(int altitude) {
        altitude = this.altitude;
    }

    @Override
    public void receivedExtendedAltitude(de.yadrone.base.navdata.Altitude
                                                 extendedAltitude) {
		this.lastUpdate = System.currentTimeMillis();
        this.extendedAltitude = extendedAltitude;
		this.gui.setAltitude(extendedAltitude.getRaw());
    }
	
	public long getLastUpdate(){
		return lastUpdate;
	}
}
