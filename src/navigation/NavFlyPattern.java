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

	private NavigationControl nc = new NavigationControl();
	private NavFindPosition fp = new NavFindPosition();
	private List<NavSpot> spots = new ArrayList<>();
	private modeling.MainModel mm = new MainModel();
	private BufferedImage image;
	private modeling.AverageFlowVector of = new AverageFlowVector();
	//	private video.PictureAnalyser paRed = new PictureAnalyser();
	//	private List<Point> colorAnalyseRed = new List<>();
	//	private video.PictureAnalyser paGreen = new PictureAnalyser();
	//	private List<Point> colorAnalyseGreen = new List<>();
	private IARDrone drone;

	public void flyLane(){
		/*
		 * using OF, fly between one start and end
		 * finde ud af hvordan man ikke flyver ind i ting!!
		 * once every 1 meter or something take picture, get this analyzed for cubes
		 * needs plan for flying around/over boxes
		 *  Optical flow:
		 * tage to billeder og give OF
		 * få en gennemsnits vektor for dronens bevægelse
		 */
		//		colorAnalyseRed.add(new Point(926, 904));
		//		paRed.setColor(colorAnalyseRed);
		//		colorAnalyseGreen.add(new Point(926, 904));
		//		paGreen.setColor(colorAnalyseRed);
//		of.(image);

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
		
		
	}

	private void findCubes(){
		/* Cubes:
		 * ska have nuværende droneposition og attitude
		 * DronePos burde kunne regnes ud fra OF
		 * resultat af analyze og attitude ska sendes til anden klasse som gemmer dem i modellen
		 */

		//		paRed.getAnalyse(img);
		

	}
    
	private void imageUpdated(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }


}










