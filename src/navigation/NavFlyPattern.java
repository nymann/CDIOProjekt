package navigation;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.yadrone.base.IARDrone;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import main.Main;
import modeling.Angle3D;
import modeling.AverageFlowVector;
import modeling.Cube;
import modeling.CubeStore;
import modeling.MainModel;
import modeling.NavSpot;
import modeling.QRPoint;
import modeling.TranslatePoint;
import video.CameraUtil;
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
	private ImageDataListener idl;
	private double vecX, vecY, hightZ;
	private BufferedImage bi;
	private long pastTimeStamp, currentTimeStamp;
	private ScheduledExecutorService excCubes;
	private ScheduledExecutorService excOF;
	private OpticalFlow opFlow;
	private Point3D p3d, dronePos3D;
	private PictureAnalyser paRed, paGreen;
	private List<org.opencv.core.Point> lsR, lsG;
	private CameraUtil ca;
	private TranslatePoint tp;

	public NavFlyPattern(MainModel mm, ImageDataListener idl, IARDrone drone){
		this.idl = idl;
		this.mm = mm;
		this.drone = drone;

		//Ændr vr til idl senere.
		fp = new NavFindPosition(mm, idl, drone);
		of = new AverageFlowVector();
		spots = new ArrayList<>();
		drone.setSpeed(Main.globalDroneSpeed);

	}

	public void flyLane(int startSpot, int endSpot){
		dronePos3D = mm.getDronePosition();
		NavSpot ss = spots.get(startSpot);
		NavSpot es = spots.get(endSpot);
		pastTimeStamp = 0;

		//Runnable command, long initialDelay, long period, TimeUnit unit
		excOF = Executors.newSingleThreadScheduledExecutor();
		excOF.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				p3d = flowFinderByVectors(idl);
				while(p3d == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					p3d = flowFinderByVectors(idl);
				};
			}
		}, 0, 1, TimeUnit.SECONDS);
		
		excCubes = Executors.newSingleThreadScheduledExecutor();
		excCubes.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				findCubes(idl);
			}
		}, 0, 5, TimeUnit.SECONDS);

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

	private Point3D flowFinderByVectors(ImageDataListener idl){
		bi = idl.getImageData().image;
		currentTimeStamp = idl.getImageData().time;
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
	
	private void findCubes(ImageDataListener idl){
		lsR = paRed.getAnalyse(idl.getImageData().image);
		lsG = paGreen.getAnalyse(idl.getImageData().image);
		Angle3D a3d = idl.getImageData().attitude;
		Point2D p2d;
		Point3D p3d;
		CubeStore cs;
		boolean bool;
		
		for(Point p : lsR){
			int x = (int) p.x;
			int y = (int) p.y;
			p3d = ca.pictureCoordToVectorDown(x, y);
			tp.setDroneInfo(a3d, p3d);
			p2d = tp.intersectFloor(p3d);
			Point3D found3d = new Point3D(p2d.getX(), p2d.getY(), 0);
			Cube c = new Cube(found3d, Color.RED);
			bool = mm.compareCube(c, 10.0);
			if(bool==false) mm.addCube(c);			
		}

		for(Point p : lsG){
			int x = (int) p.x;
			int y = (int) p.y;
			p3d = ca.pictureCoordToVectorDown(x, y);
			tp.setDroneInfo(a3d, p3d);
			p2d = tp.intersectFloor(p3d);
			Point3D found3d = new Point3D(p2d.getX(), p2d.getY(), 0);
			Cube c = new Cube(found3d, Color.GREEN);
			bool = mm.compareCube(c, 10.0);
			if(bool==false) mm.addCube(c);			
		}

	}



}










