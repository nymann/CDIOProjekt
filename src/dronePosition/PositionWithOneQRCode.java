package dronePosition;

import javafx.geometry.Point2D;

/**
* @author sAkkermans
*/

public class PositionWithOneQRCode {
	
	double x, y, x1, x2, y1, y2;
	double d = 21.2, p, k1, k2, alpha, beta, fullLength, r1, r2;
	
	public void setCoordinats(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void setQRHightFromDrone(double p) {
		this.p = p;
	}
	
	public Point2D getCoordinat1() {
		Point2D coordinat1 = new Point2D(x1, y1);
		return coordinat1;
	}
	public Point2D getCoordinat2() {
		Point2D coordinat2 = new Point2D(x2, y2);
		return coordinat2;
	}
	
	public double getQRHightFromDrone() {
		return p;
	}
	
	public double findLenght() {
		// Finder længde forskellen mellem den faktiske højde af QR og dronens syn på QR-koden.
		k1 = d/p;
		return k1; 
	}
	
	public double findAngel() {
		// Finder den vinkel som skal bruges til at bregne afstanden fra dronen til QR-koden.
		beta = k1/(d-p);
		// TODO - Skal rettes til radianer!
		alpha = 180 - 90 - beta;
		return alpha;
	}
	
	public double LengthBetweenQRAndDrone() {
		// Berener afstanden mellem dronen og QR-koden.
		k2 = p*Math.sin(beta)/Math.sin(alpha);
		fullLength = k1 + k2;
		
		return fullLength;
	}
	
	public double LengthOneQR() {
		// Længde med én QR-kode.
		findLenght();
		findAngel();
		return LengthBetweenQRAndDrone();
	}
	
	public Point2D findPositionTwoQR() {
		/*Ved at kende længden fra dronen og til Q-koderne, kan man bruge cirkel beregning til
		 * at finde dronens position. 
		 */
		
		double f1, f2, yres1, yres2, xres1, xres2;
		
		r1 = LengthBetweenQRAndDrone();
		r2 = LengthBetweenQRAndDrone();
		
		k1 = -0.5*(2*y1-2*y2)/(x1-x2);
		k2 = -0.5*(r1*r1-r2*r2-x1*x1+x2*x2-y1*y1+y2*y2)/(x1-x2);
		
		f1 = Math.sqrt(k1*k1*r1*r1-k1*k1*y1*y1-2*k1*k2*y1+2*k1*x1*y1-k2*k2+2*k2*x1+r1*r1-x1*x1);
		f2 = k1*k1+1;
		yres1 = ((-k1*k2+k1*x1+f1)+y1)/f2;
		yres2 = -((k1*k2-k1*x1+f1)-y1)/f2;
		
		xres1 = yres1*k1+k2;
		xres2 = yres2*k1+k2;
		
		Point2D dronePositioning = new Point2D(0, 0);
		
		if(xres1 > 0 && xres1 < 926) {
			if(yres1 > 0 && yres1 < 1060) {
				dronePositioning.add(xres1, yres1);
				return dronePositioning;
			}
		}
		else {
			dronePositioning.add(xres2, yres2);
			return dronePositioning;
		}
		return dronePositioning;
	}
	
}