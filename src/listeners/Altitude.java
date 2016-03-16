package listeners;

import de.yadrone.base.navdata.AltitudeListener;

/**
 * Created by Nymann on 16-03-2016.
 */
public class Altitude implements AltitudeListener {
    public int altitude;
    public de.yadrone.base.navdata.Altitude extendedAltitude;

    public void receivedAltitude(int altitude) {
        altitude = this.altitude;
    }

    @Override
    public void receivedExtendedAltitude(de.yadrone.base.navdata.Altitude
                                                 extendedAltitude) {
        this.extendedAltitude = extendedAltitude;
    }
}
