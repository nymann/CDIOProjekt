package navigation;

import java.util.ArrayList;
import java.util.List;

import de.yadrone.base.IARDrone;
import modeling.MainModel;
import modeling.NavSpot;
import modeling.QRPoint;

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

	}

	public void startSpot(int id){
		/*
		 * make sure currentposition matches startSpot
		 */
		fp.getPositionX();
		fp.getPositionY();

		
	}

	public void endSpot(){
		/*
		 * make sure currentposition matches endSpot
		 * if not, calculate if within acceptable
		 */

	}
	
	public void flyToStartSpot1(){
		
		
	}

}
