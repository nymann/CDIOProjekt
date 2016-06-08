package navigation;

import java.util.ArrayList;
import java.util.List;

import de.yadrone.base.IARDrone;
import modeling.MainModel;
import modeling.NavSpot;

/**
 *
 * @author Kim
 */

public class NavFlyPattern {
	/*
	drone.up();
	drone.down();
	drone.forward();
	drone.backward();
	drone.goLeft();
	drone.goRight();
	drone.landing();
	drone.takeOff();
	drone.hover();
	drone.reset();
	 */
	/*
	 * check at startspot
	 * fly lane
	 * reach endspot
	 * 
	 */

	NavigationControl nc = new NavigationControl();
	NavFindPosition fp = new NavFindPosition();
	List<NavSpot> spots = new ArrayList<>();
	modeling.MainModel mm = new MainModel();
	IARDrone drone;

	public void flyLane(){
		/*
		 * using OF, fly between one start and end
		 * once every 1 meter or something take picture, get this analyzed for cubes
		 * needs plan for flying around/over boxes
		 */
		
		//Find cubes:
		//ska have nuværende droneposition og attitude
		//DronePos burde kunne regnes ud fra OF
		//gir billed til picanalyze, får en liste med klodskoordinater på billedet
		//resultat af analyze og attitude ska sendes til anden klasse som gemmer dem i modellen
		
		
	}

	public boolean atSpot(int spotID){
		/*
		 * makes sure dronePosition is within acceptable range of spotIDs location
		 */
		boolean bool = false;
		
		double dronePosX = fp.getPositionX();
		double dronePosY = fp.getPositionY();
		
		NavSpot spot = mm.getNavSpot(spotID);
		int xSpot = spot.getX();
		int ySpot = spot.getY();
		
		int range = 12;
		int xRangeMax = (int) dronePosX+range;
		int xRangeMin = (int) dronePosX-range;
		int yRangeMax = (int) dronePosX+range;
		int yRangeMin = (int) dronePosX-range;
		
		if ((xRangeMin<=xSpot&&xSpot<=xRangeMax) && (yRangeMin<=ySpot&&ySpot<=yRangeMax)) bool=true;
	
		return bool;
	}
	
	public void flyToStartSpot0(){
		
		
	}
	

}










