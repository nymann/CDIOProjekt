package test;



import dronePosition.PointNavigation;
import javafx.geometry.Point2D;

public class TestCoordinatForDrone {
	public static void main(String[] args) {
		
		PointNavigation pn = new PointNavigation();
		
		pn.setCoordinats(0, 0, 1, 2, 0, 4);
		pn.setAngelA(0.18);
		pn.setAngelB(0.2);
		
		pn.findAandBLength();
		pn.findCenter();
		pn.findRadius();
		
		Point2D p = pn.findXandY();
		System.out.println(p);
		System.out.println("Center 1: " + pn.cx1 + "," + pn.cy1);
		System.out.println("Center 2: " + pn.cx2 + "," + pn.cy2);
		 
	}
}
