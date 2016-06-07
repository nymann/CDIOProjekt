package navigation;

import java.util.ArrayList;
import java.util.List;

import modeling.QRPoint;

/**
*
* @author Kim
*/

public class NavFlyPattern {
	
	/*
	 * check at startspot
	 * fly lane
	 * reach endspot
	 * 
	 */

	NavigationControl nc = new NavigationControl();
	List<NavSpot> spots = new ArrayList<>();
	
	private void lane(){
		/*
		 * using OF, fly between one start and end
		 * once every 1 meter or something take picture, get this analyzed for cubes
		 * needs plan for flying around/over boxes
		 */
 
	}
	
	private void startSpot(){
		/*
		 * make sure currentposition matches startSpot
		 */

	}

	private void endSpot(){
		/*
		 * make sure currentposition matches endSpot
		 * if not, calculate if within acceptable
		 */

	}
	
	public void fillStartEndSpots(){
		//x,y,type; 1=startSpot | 2=endSpot
		spots.add(new NavSpot(11, 22, 1));
	}
}
