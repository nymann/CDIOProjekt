package dronePossition;

import java.math.*;

import javafx.geometry.Point2D;

public class PointNavigation {
	
	public int x1, y1, x2, y2, x3, y3, x, y;
	public double alpha, beta, cx1, cy1, cx2, cy2, r1, r2, xfind, yfind, a, b;
	
	public void setCoordinats(int x1, int y1, int x2, int y2, int x3, int y3) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.x3 = x3;
		this.y3 = y3;
	}
	public Point2D getCoordinat1() {
		Point2D coordinat1 = new Point2D(x1, y1);
		return coordinat1;
	}
	public Point2D getCoordinat2() {
		Point2D coordinat2 = new Point2D(x2, y2);
		return coordinat2;
	}
	public Point2D getCoordinat3() {
		Point2D coordinat3 = new Point2D(x3, y3);
		return coordinat3;
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
		a = Math.sqrt(Math.pow((x2-x1),2)+ Math.pow((y2-y1), 2));
		b = Math.sqrt(Math.pow((x3-x2),2)+ Math.pow((y3-y2), 2));
		
	}
	
	public void findCenter() {
		
		//Centrum for alpha-circle.
		
		double t1, t2;
		t1 = Math.sqrt(((Math.abs(-y2+y1))^2+(Math.abs(-x2+x1))^2));
		t2 = Math.sqrt((Math.pow(a,2)/Math.pow(Math.sin(alpha), 2)-Math.pow(a,2)));
		
		System.out.println("T1: " + t1);
		System.out.println("T2: " + t2);
		
		cx1 = (0.5)*((double)(y2-y1)/t1)*t2+(0.5)*x1+(0.5)*x2;
		cy1 = (0.5)*((double)(-x2+x1)/t1)*t2+(0.5)*y1+(0.5)*y2;
				
		// Centrum for beta-circle.
		
		double t3, t4;
		t3 = Math.sqrt(((Math.abs(-y3+y2))^2+(Math.abs(-x3+x2))^2));
		t4 = Math.sqrt((Math.pow(b,2)/Math.pow(Math.sin(beta), 2)-Math.pow(b,2)));
		
		cx2 = (0.5)*((double)(y3-y2)/t3)*t4+(0.5)*x3+(0.5)*x2;
		cy2 = (0.5)*((double)(-x3+x2)/t3)*t4+(0.5)*y3+(0.5)*y2;
		
	}
	
	public void findRadius() {
		// Radius of alpha and beta circle.
		r1 = (0.5)*a/Math.sin(alpha);
		r2 = (0.5)*b/Math.sin(beta);
		
		System.out.println("R1: " + r1);
		System.out.println("R2: " + r2);
	}
	
	public Point2D findXandY() {
		
		
		double k1, k2, f1, f2, yres1, yres2, xres1, xres2, dist1, dist2;
		k1 = -cy2/(cx1-cx2);
		k2 = -0.5*(Math.pow(r1, 2)-Math.pow(r2, 2)-Math.pow(cx1, 2)+Math.pow(cx2, 2) - Math.pow(cy1, 2)+Math.pow(cy2, 2));
		
		f1 = Math.sqrt(k1*k1*r1*r1-k1*k1*cy1*cy1-2*k1*k2*cy1+2*k1*cx1*cy1-k2*k2+2*k2*cx1+r1*r1-cx1*cx1);
		f2 = k1*k1+1;
		yres1 = ((k1*k2+k1*cx1+f1)+cy1)/f2;
		yres2 = ((k1*k2-k1*cx1+f1)-cy1)/f2;
		
		xres1 = yres1*k1+k2;
		xres2 = yres2*k1+k2;
		
		dist1 = Math.sqrt(Math.pow(x2-xres1, 2)+Math.pow(y2-yres1, 2));
		dist2 = Math.sqrt(Math.pow(x2-xres2, 2)+Math.pow(y2-yres2, 2));
		
		Point2D dronePositioning;
		if(dist1 > dist2) {
			dronePositioning = new Point2D(xres1, yres1);
		}
		else {
			dronePositioning = new Point2D(xres2, yres2);
		}
		// Sætter koordinaterne for dronen.
		return dronePositioning;	 
	}
}







