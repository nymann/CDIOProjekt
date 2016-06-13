package navigation;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.yadrone.base.IARDrone;
import modeling.AverageFlowVector;
import modeling.Cube;
import modeling.MainModel;
import modeling.NavSpot;
import modeling.QRPoint;
import video.PictureAnalyser;

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

//	private NavigationControl nc = new NavigationControl();
	private NavFindPosition fp;
	private List<NavSpot> spots;
	private modeling.MainModel mm;
	private BufferedImage image;
	private modeling.AverageFlowVector of;
	//	private video.PictureAnalyser paRed = new PictureAnalyser();
	//	private List<Point> colorAnalyseRed = new List<>();
	//	private video.PictureAnalyser paGreen = new PictureAnalyser();
	//	private List<Point> colorAnalyseGreen = new List<>();
	private IARDrone drone;
	
	public NavFlyPattern(){
		fp = new NavFindPosition();
		spots = new ArrayList<>();
		mm = new MainModel();
		of = new AverageFlowVector();
		
	}

	public void flyLane(int startSpot, int endSpot){
		/*
		 * using OF, fly between one start and end
		 * once every 1 meter or something take picture, get this analyzed for cubes
		 * needs plan for flying around/over boxes
		 * finde ud af hvordan man ikke flyver ind i ting!!
		 *  Optical flow:
		 * tage to billeder og give OF
		 * få en gennemsnits vektor for dronens bevægelse
		 */
		//		colorAnalyseRed.add(new Point(926, 904));
		//		paRed.setColor(colorAnalyseRed);
		//		colorAnalyseGreen.add(new Point(926, 904));
		//		paGreen.setColor(colorAnalyseRed);
//		of.(image);
		
		
		NavSpot ss = spots.get(startSpot);
		NavSpot es = spots.get(endSpot);
		
		
		findCubes();

	}

	public boolean atSpot(int spotID){
		/*
		 * makes sure dronePosition is within acceptable range of spotIDs location
		 */
		boolean bool = false;

		double dronePosX = fp.getPositionX();
		double dronePosY = fp.getPositionY();
		//		double dronePosX = 75;
		//		double dronePosY = 76;

		NavSpot spot = mm.getNavSpot(spotID);
		int xSpot = spot.getX();
		int ySpot = spot.getY();

		System.out.println(xSpot + " " + ySpot );

		int range = 12;
		int xRangeMax = (int) dronePosX+range;
		int xRangeMin = (int) dronePosX-range;
		int yRangeMax = (int) dronePosY+range;
		int yRangeMin = (int) dronePosY-range;

		if ((xRangeMin<=xSpot&&xSpot<=xRangeMax) && (yRangeMin<=ySpot&&ySpot<=yRangeMax)) bool=true;

		return bool;
	}

	public void flyToSpot(double currentX, double currentY, int spotID){
		/*
		 * ud fra current position, ska regnet vinklen til spotIDs position og distancen
		 * flyve afstanden.
		 */
		NavSpot goToSpot = spots.get(spotID);
		int goToX = goToSpot.getX();
		int goToY = goToSpot.getY();
		double difX = currentX-goToX; 
		double difY = currentY-goToY;
		
		double atan = Math.atan2(difY, difX);
		double changeAngle = atan-mm.getAngleOffset()+mm.getDroneAttitude().getYaw();
		
		// Længde fra dronen til start spot.
		double distBetweenDroneAndSpot = Math.pow(difX,2) + Math.pow(difY,2);
		distBetweenDroneAndSpot = Math.sqrt(distBetweenDroneAndSpot);
		
		// Drej til spot, derefter flyv til spot.
		double maxSpin = (7*Math.PI/6);
		if(drone.spinLeft() < maxSpin) {
			double test1 = mm.getDroneAttitude().getYaw();
			drone.spinLeft();
		}
		else {
			drone.spinRight();
		}
		/* 1. Drej så vinklen passer.
		 * 2. Flyv distancen mellem droenens nuværende position og punktet. 
		 */
		
	}

	private void findCubes(){
		/* Cubes:
		 * ska have nuværende dronePosition og attitude(YAW)
		 * DronePos burde kunne regnes ud fra OF
		 * resultat af analyze og attitude ska sendes til anden klasse som gemmer dem i modellen
		 */

		//		paRed.getAnalyse(img);
		

	}



}










