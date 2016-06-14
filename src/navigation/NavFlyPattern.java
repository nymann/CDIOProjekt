package navigation;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.yadrone.base.IARDrone;
import modeling.AverageFlowVector;
import modeling.Cube;
import modeling.MainModel;
import modeling.NavSpot;
import modeling.QRPoint;
import video.PictureAnalyser;
import video.VideoReader;

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

	private NavFindPosition fp;
	private List<NavSpot> spots;
	private AverageFlowVector of;
	private IARDrone drone;
	private MainModel mm;
	private VideoReader vr;
	private double vecX, vecY;
	private ScheduledExecutorService exec;

	public NavFlyPattern(MainModel mm, VideoReader vr, IARDrone drone){
		this.vr = vr;
		this.mm = mm;
		this.drone = drone;
		
		fp = new NavFindPosition(mm, vr, drone);
		of = new AverageFlowVector();
		spots = new ArrayList<>();

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

		NavSpot ss = spots.get(startSpot);
		NavSpot es = spots.get(endSpot);
		
		vr.getImage();
		vr.getImageTime();
		
		vecX = of.x;
		vecY = of.y;

		//Runnable command, long initialDelay, long period, TimeUnit unit
		exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
			  findCubes();
		  }
		}, 0, 5, TimeUnit.SECONDS);
		
		
	}

	public boolean atSpot(double currentX, double currentY, int spotID){
		/*
		 * makes sure dronePosition is within acceptable range of spotIDs location
		 */
		boolean bool = false;

		currentX = mm.getDronePosition().getX();
		currentY = mm.getDronePosition().getX();

		NavSpot spot = mm.getNavSpot(spotID);
		int xSpot = spot.getX();
		int ySpot = spot.getY();

		int range = 12;
		int xRangeMax = (int) currentX+range;
		int xRangeMin = (int) currentX-range;
		int yRangeMax = (int) currentY+range;
		int yRangeMin = (int) currentY-range;

		if ((xRangeMin<=xSpot&&xSpot<=xRangeMax) && (yRangeMin<=ySpot&&ySpot<=yRangeMax)) 
			bool=true;

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
		// Hvis mm bliver opdateret konstant.
		while(mm.getDroneAttitude().getYaw() != changeAngle) {
			drone.spinLeft();
		}
		// IKKE KLAR - Skal sættes en fart ind i stedet for ditrance.
		int distBetweenDroneAndSpot2 = (int) distBetweenDroneAndSpot;
		drone.getCommandManager().forward(5).doFor(distBetweenDroneAndSpot2);

	}

	private void findCubes(){
		/* Cubes:
		 * ska have nuværende dronePosition og attitude(YAW)
		 * DronePos burde kunne regnes ud fra OF
		 * resultat af analyze og attitude ska sendes til anden klasse som gemmer dem i modellen
		 */



	}



}










