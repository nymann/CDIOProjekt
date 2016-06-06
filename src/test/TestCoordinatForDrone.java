package test;



import dronePossition.PointNavigation;
import javafx.geometry.Point2D;

public class TestCoordinatForDrone {
	public static void main(String[] args) {
		
		PointNavigation pn = new PointNavigation();
		
		pn.setCoordinats(2, 0, 0, 0, 0, 4);
		pn.setAngelA(Math.PI*0.25);
		pn.setAngelB(Math.PI*0.5);
		
		pn.findAandBLength();
		pn.findCenter();
		pn.findRadius();
		
		Point2D p = pn.findXandY();
		System.out.println(p);
		System.out.println("Center 1: " + pn.cx1 + "," + pn.cy1);
		System.out.println("Center 2: " + pn.cx2 + "," + pn.cy2);
		 
	}
}
