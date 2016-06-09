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
	 * Ha metode som holder øje med om vi er fløjet over slutstedet
	 * PresentResults()
	 */

	NavFindPosition findPos = new NavFindPosition();
	NavFlyPattern flyPat = new NavFlyPattern();
	IARDrone drone;

	private void runNav(){
		double xPos = findPos.getPositionX();
		double yPos = findPos.getPositionY();
		
		flyPat.flyToSpot(xPos, yPos, 0);
		
		if(flyPat.atSpot(0)) flyPat.flyLane(0, 1);
		else flyPat.flyToSpot(xPos, xPos, 0);
		
		
		
	}

	private void presentResults(){

	}
}
