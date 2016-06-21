package navigation;

import gui.MainWindow;
import gui.ResultPanel;

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

	private NavFindPosition fp;
	private List<NavSpot> spots;
	private AverageFlowVector of;
	private IARDrone drone;
	private VideoReader vr;
	private ImageDataListener idl;
	private double vecX, vecY, hightZ;
	private BufferedImage bi;
	private long pastTimeStamp, currentTimeStamp;
	private ScheduledExecutorService excCubes;
	private ScheduledExecutorService excOF;
	private OpticalFlow opFlow;
	private Point3D p3d, dronePos3D;
	private List<Point> lsR, lsG;
	public static ArrayList<Point> greenCubes, redCubes;
	private ResultPanel rp;

	private TranslatePoint tp;
	private int speed;

	public NavFlyPattern(ImageDataListener idl, IARDrone drone){
		this.idl = idl;
		this.drone = drone;
		
		fp = new NavFindPosition(idl, drone);
		of = new AverageFlowVector();
		
		greenCubes = new ArrayList<>();
	    redCubes = new ArrayList<>();
	    
		MainModel.setDronePosition(new Point3D(10, 10,100));
		MainModel.init();
		//MainModel.getDroneAttitude().setYaw(10);
		speed = Main.globalDroneSpeed;
		drone.setSpeed(speed);

	}

	public void flyLane(int startSpot, int endSpot){
		/*
		 * Flies the drone between two spots,
		 * and calls the method for finding cubes at intervals. 
		 */
		dronePos3D = MainModel.getDronePosition();
		NavSpot ss = MainModel.getNavSpot(startSpot);
		NavSpot es = MainModel.getNavSpot(endSpot);
		pastTimeStamp = 0;
		
		findAndChangeAttitude(startSpot);
		atSpot(startSpot);
		//flyToSpot(startSpot);
		//drone.getCommandManager().forward(speed).doFor(2000);
		
		//Runnable command, long initialDelay, long period, TimeUnit unit
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
		NavSpot spot = MainModel.getNavSpot(spotID);
		double currentX = MainModel.getDronePosition().getX();
		double currentY = MainModel.getDronePosition().getX();
		
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

	public void flyToSpot(int spotID){

		NavSpot goToSpot = MainModel.getNavSpot(spotID);

		/*
		 * Takes a spot and flies the drone to it, 
		 * by finding the angle the drone must turn, and the distance to the spot.
		 */

		double currentX = MainModel.getDronePosition().getX();
		double currentY = MainModel.getDronePosition().getX();
		int goToX = goToSpot.getX();
		int goToY = goToSpot.getY();
		double difX = currentX-goToX; 
		double difY = currentY-goToY;
		
		findAndChangeAttitude(spotID);

		// Længde fra dronen til start spot.
		double distBetweenDroneAndSpot = Math.pow(difX,2) + Math.pow(difY,2);
		distBetweenDroneAndSpot = Math.sqrt(distBetweenDroneAndSpot);

		// IKKE KLAR - Skal sættes en fart ind i stedet for ditrance.
		int distBetweenDroneAndSpot2 = (int) distBetweenDroneAndSpot;
		drone.getCommandManager().forward(5).doFor(distBetweenDroneAndSpot2);
	}
	
	private void findAndChangeAttitude(int spotID){

		NavSpot goToSpot = MainModel.getNavSpot(spotID);

		/*
		 * Finds the difference in angle between where the drone is pointed,
		 * and a particular spot
		 */
		double currentX = MainModel.getDronePosition().getX();
		double currentY = MainModel.getDronePosition().getX();
		int goToX = goToSpot.getX();
		int goToY = goToSpot.getY();
		double difX = currentX-goToX; 
		double difY = currentY-goToY;

		double atan = Math.atan2(difY, difX);
		/*double changeAngle = atan-MainModel.getAngleOffset()+MainModel.getDroneAttitude().getYaw();
		
		while(MainModel.getDroneAttitude().getYaw() != changeAngle) {
			drone.spinLeft();
		}*/

	}

	private Point3D flowFinderByVectors(ImageDataListener idl){
		/*
		 * Is meant to calculate the position of the drone, 
		 * updated by use of optical flow.
		 */
		bi = idl.getImageData().image;
		currentTimeStamp = idl.getImageData().time;
		AverageFlowVector afv;
		double posByqrX;
		double posByqrY;

		Point3D posUpdateByOF = null;

		posByqrX = MainModel.getDronePosition().getX();
		posByqrY = MainModel.getDronePosition().getX();

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
	 

	public void findCubes(ImageDataListener idl){
		/*
		 * Called in order to find the number of cubes in an image,
		 * and translating pixel points of the cubes in the image,
		 * to actual positions in the model of the room
		 */
		BufferedImage img = idl.getImageData().image;
		lsR = MainWindow.paRed.getAnalyse(img);
		lsG = MainWindow.paGreen.getAnalyse(img);
	    
		Angle3D a3d = idl.getImageData().attitude;
		Point2D p2d;
		Point3D p3d;
		Point p1d;
		CubeStore cs;
		TranslatePoint tp = new TranslatePoint();
	//	tp.setDroneInfo(a3d,new Point3D(0, 0, 100));
		
		tp.setDroneInfo(a3d,idl.getImageData().position);
		boolean bool;
		
		for(Point p : lsR){
			System.out.println("tjekker cube vs know cubes :"+p.x+" "+p.y);
			
			int x = (int) p.x;
			int y = (int) p.y;
			p3d = CameraUtil.pictureCoordToVectorDown(x, y);
	
			p2d = tp.intersectFloor(p3d);
			Point3D found3d = new Point3D(p2d.getX(), p2d.getY(), 0);
			Cube c = new Cube(found3d, Color.RED);
			System.out.println("cube coordinate : "+p2d.getX()+" "+p2d.getY());
			bool = MainModel.compareCube(c, 10.0);
			if(bool==false){ 
				MainModel.addCube(c); 
				System.out.println("new cube added");
				redCubes.add(new Point((int)c.getPosition().getX(), (int)c.getPosition().getY()));
			}			
		}

		for(Point p : lsG){
			int x = (int) p.x;
			int y = (int) p.y;
			p3d = CameraUtil.pictureCoordToVectorDown(x, y);
			tp.setDroneInfo(a3d, p3d);
			p2d = tp.intersectFloor(p3d);
			Point3D found3d = new Point3D(p2d.getX(), p2d.getY(), 0);
			Cube c = new Cube(found3d, Color.GREEN);
			bool = MainModel.compareCube(c, 10.0);
			
			if(bool==false) {
				MainModel.addCube(c);
				greenCubes.add(new Point((int)c.getPosition().getX(), (int)c.getPosition().getY()));
			}
		}

		
	}

	public ArrayList<Point> getRedCubes(){
		return redCubes;
	}
	
	public ArrayList<Point> getGreenCubes(){
		return greenCubes;
	}

}
