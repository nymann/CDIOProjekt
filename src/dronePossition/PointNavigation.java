package dronePossition;

import java.math.*;
import java.awt.Point;

public class PointNavigation {
	
	int x1, y1, x2, y2, x3, y3, x, y, a, b;
	double alpha, beta, cx1, cy1, cx2, cy2, r1, r2, xfind, yfind;
	
	int centerx1 = (int) cx1;
	int centery1 = (int) cy1;
	
	int centerx2 = (int) cx2;
	int centery2 = (int) cy2;
	
	int xfindfinal = (int) xfind;
	int yfindfinal = (int) yfind;
	
	double a1 = (int) a;
	double b1 = (int) b;
	
	public void setCoordinats(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	public Point getCoordinat1() {
		Point coordinat1 = new Point(x1, y1);
		return coordinat1;
	}
	public Point getCoordinat2() {
		Point coordinat2 = new Point(x2, y2);
		return coordinat2;
	}
	public void setAngelA(double alpha) {
		this.alpha = alpha;
	}
	public double getAngelA() {
		return alpha;
	}
	public void setAngelB(double beta) {
		this.beta = beta;
	}
	public double getAngelB() {
		return beta;
	}
	
	public void findAandBLength() {
		
		// Finder længden mellem det første og andet punkt.
		// Og mellem det andet punkt og tredje punkt.
		a1 = Math.sqrt(Math.pow((x2-x1),2)+ Math.pow((y2-y1), 2));
		b1 = Math.sqrt(Math.pow((x3-x2),2)+ Math.pow((y3-y2), 2));
	}
	
	public void findCenter() {
		
		//Centrum for alpha-circle.
		cx1 = (1/2)*((y2-y1)/Math.sqrt(((Math.abs(-y2+y1))^2+(Math.abs(-x2+x1))^2)))*Math.sqrt((Math.pow(a,2)/Math.pow(Math.sin(alpha), 2)-Math.pow(a,2)))+(1/2)*x1+(1/2)*x2;
		cy1 = (1/2)*((x2-x1)/Math.sqrt(((Math.abs(-y2+y1))^2+(Math.abs(-x2+x1))^2)))*Math.sqrt((Math.pow(a,2)/Math.pow(Math.sin(alpha), 2)-Math.pow(a,2)))+(1/2)*y1+(1/2)*y2;
		
		Point coordinatSet1 = new Point(centerx1,centery1);
				
		// Centrum for beta-circle.
		cx2 = (1/2)*((y2-y3)/Math.sqrt(((Math.abs(-y2+y3))^2+(Math.abs(-x2+x3))^2)))*Math.sqrt((Math.pow(b,2)/Math.pow(Math.sin(beta), 2)-Math.pow(b,2)))+(1/2)*x3+(1/2)*x2;
		cy2 = (1/2)*((x2-x3)/Math.sqrt(((Math.abs(-y2+y3))^2+(Math.abs(-x2+x3))^2)))*Math.sqrt((Math.pow(b,2)/Math.pow(Math.sin(beta), 2)-Math.pow(b,2)))+(1/2)*y3+(1/2)*y2;
		
		Point coordinatSet2 = new Point(centerx2, centery2);
		
	}
	
	public void findRadius() {
		// Radius of alpha and beta circle.
		r1 = (1/2)*a/Math.sin(alpha);
		r2 = (1/2)*b/Math.sin(beta);
	}
	
	public Point findXandY() {
		
		//1. Step - Isoler for y.
		yfind = Math.sqrt(Math.pow(r1, 2)-Math.pow(x-centerx1,2))+centery1;
		//2. Step - Isolere for x, med ligningen for y indsat.
		xfind = 1/2*Math.pow((Math.pow(-r1,2)*centerx1+Math.pow(r1,2)*centerx2+Math.pow(r2,2)*centerx1-Math.pow(r2,2)*centerx2+Math.pow(centerx1,3)-Math.pow(centerx1,2)*centerx2-centerx1*Math.pow(centerx2,2)+centerx1*Math.pow(centery1,2)+2*centerx1*centery1*centery2+centerx1*Math.pow(centery2,2)+Math.pow(centerx2,3)+centerx2*Math.pow(centery1,2)+2*centerx2*centery1*centery2+centerx2*Math.pow(centery2,2)+(-Math.pow((centery1+centery2),2)*(Math.pow(r1,4)-2*Math.pow(r1,2)*Math.pow(r2,2)-2*Math.pow(r1,2)*Math.pow(centerx1,2)+4*Math.pow(r1,2)*centerx1*centerx2-2*Math.pow(r1,2)*Math.pow(centerx2,2))-2*Math.pow(r1,2)*Math.pow(centery1,2)-4*Math.pow(r1,2)*centery1*centery2-2*Math.pow(r1,2)*Math.pow(centery2,2)+Math.pow(r2,4)-2*Math.pow(r2,2)*Math.pow(centerx1,2)+4*Math.pow(r2,2)*centerx1*centerx2-2*Math.pow(r2,2)*Math.pow(centerx2,2)-2*Math.pow(r2,2)*Math.pow(centery1,2)-4*Math.pow(r2,2)*centery1*centery2-2*Math.pow(r2,2)*Math.pow(centery2,2)+Math.pow(centerx1,4)-4*Math.pow(centerx1,3)*centerx2+6*Math.pow(centerx1,2)*Math.pow(centerx2,2)+2*Math.pow(centerx1,2)*Math.pow(centery1,2)+4*Math.pow(centerx1,2)*centery1*centery2+2*Math.pow(centerx1,2)*Math.pow(centery2,2)-4*centerx1*Math.pow(centerx2,3)-4*centerx1*centerx2*Math.pow(centery1,2)-8*centerx1*centerx2*centery1*centery2-4*centerx1*centerx2*Math.pow(centery2,2)+Math.pow(centerx2,4)+2*Math.pow(centerx2,2)*Math.pow(centery1,2)+4*Math.pow(centerx2,2)*centery1*centery2+2*Math.pow(centerx2,2)*Math.pow(centery2,2)+Math.pow(centery1,4)+4*Math.pow(centery1,3)*centery2+6*Math.pow(centery1,2)*Math.pow(centery2,2)+4*centery1*Math.pow(centery2,3)+Math.pow(centery2,4))),0.5)/(Math.pow(centerx1,2)-2*centerx1*centerx2+Math.pow(centerx2,2)+Math.pow(centery1,2)+2*centery1*centery2+Math.pow(centery2,2));
		
		// Sætter koordinaterne for dronen.
		Point dronePositioning = new Point(xfindfinal, yfindfinal);
		return dronePositioning;	 
	}
}







