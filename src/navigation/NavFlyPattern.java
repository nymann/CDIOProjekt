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
import javafx.geometry.Point3D;
import main.Main;
import modeling.AverageFlowVector;
import modeling.Cube;
import modeling.MainModel;
import modeling.NavSpot;
import modeling.QRPoint;
import video.OpticalFlow;
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
	private double vecX, vecY, hightZ;
	private BufferedImage bi;
	private long pastTimeStamp, currentTimeStamp;
	private ScheduledExecutorService excCubes;
	private ScheduledExecutorService excOF;
	private OpticalFlow opFlow;
	private Point3D p3d;

	public NavFlyPattern(MainModel mm, VideoReader vr, IARDrone drone){
		this.vr = vr;
		this.mm = mm;
		this.drone = drone;

		fp = new NavFindPosition(mm, vr, drone);
		of = new AverageFlowVector();
		spots = new ArrayList<>();
		drone.setSpeed(Main.globalDroneSpeed);

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


		bi = vr.getImage();
		currentTimeStamp = vr.getImageTime();
		pastTimeStamp = 0;

		//Runnable command, long initialDelay, long period, TimeUnit unit
		excCubes = Executors.newSingleThreadScheduledExecutor();
		excCubes.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				findCubes();
			}
		}, 0, 5, TimeUnit.SECONDS);

		excCubes = Executors.newSingleThreadScheduledExecutor();
		excCubes.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				p3d = flowFinderByVectors(bi, currentTimeStamp);
				while(p3d == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p3d = flowFinderByVectors(bi, currentTimeStamp);
				};
			}
		}, 0, 1, TimeUnit.SECONDS);

	}

	private Point3D flowFinderByVectors(BufferedImage img, long timeStamp){
		bi = img;
		currentTimeStamp = timeStamp;
		AverageFlowVector afv;
		double posByqrX;
		double posByqrY;

		Point3D posUpdateByOF = null;

		posByqrX = mm.getDronePosition().getX();
		posByqrY = mm.getDronePosition().getX();

		afv = opFlow.findFlows(bi);

		if(afv != null) {
			vecX = afv.x;
			vecY = afv.y;

			/* Finder tidsforskellen, som vi skal bruge sammen med farten til dronen.
			 * Herved kan vi finde den afstand vi har flyttet.
			 */
			
			long timeDifference = currentTimeStamp-pastTimeStamp; 
			double movedLength = Main.globalDroneSpeed * timeDifference;

			pastTimeStamp = currentTimeStamp;
			
			/* Her kan vi bruge den flyttede længde, sammen med retningsvektoren og det gamle koordinatsæt.
			 * Herved finder vi den nye position.
			 */
			
			double diffX = posByqrX+movedLength*vecX;
			double diffY = posByqrY+movedLength*vecY;

			// Z-værdien skal ændres.
			posUpdateByOF = new Point3D(diffX, diffY, hightZ);
		}
		return posUpdateByOF;
	}


	public boolean atSpot(int spotID){
		/*
		 * makes sure dronePosition is within acceptable range of spotIDs location
		 */
		boolean bool = false;
		double currentX;
		double currentY;

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
		 * Ud fra current position, skal vinklen være regnet til spotIDs position og distancen.
		 * Flyve afstanden.
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
		 * Skal have nuværende dronePosition og attitude(YAW).
		 * DronePos burde kunne regnes ud fra OF.
		 * Resultat af analyze og attitude skal sendes til anden klasse som gemmer dem i modellen.
		 */



	}



}










