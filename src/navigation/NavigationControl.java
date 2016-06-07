package navigation;

import de.yadrone.base.IARDrone;

/**
 *
 * @author Kim
 */

public class NavigationControl {
	/*
	 * NavFindPosition 
	 * NavFlyPattern from currentPos -> startSpot1
	 * NavFlyPattern(1)
	 * NavFindPosition
	 * NavFlyPattern(2)
	 * .
	 * .
	 * PresentResults()
	 */

	NavFindPosition findPos = new NavFindPosition();
	NavFlyPattern flyPat = new NavFlyPattern();
	IARDrone drone;
	
	private void runNav (){
		//findPos;
		flyPat.flyToStartSpot1();
		flyPat.startSpot(1);
		
	}
	
	private void presentResults(){

	}
}
