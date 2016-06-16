package dronePossition;

import javafx.geometry.Point2D;

/**
* @author sAkkermans
*/

public class PositionWithOneQRCode {
	
	double x, y, x1, x2, y1, y2;
	double d = 21.2, p, k1, k2, alpha, beta, fullLength, r1, r2;
	
	public double findLenght() {
		k1 = d/p;
		return k1; 
	}
	
	public double findAngel() {
		beta = k1/(d-p);
		alpha = 180 - 90 - beta;
		return alpha;
	}
	
	public double LengthBetweenQRAndDrone() {
		
		k2 = p*Math.sin(beta)/Math.sin(alpha);
		fullLength = k1 + k2;
		
		return fullLength;
	}
	
	public double LengthOneQR() {
		findLenght();
		findAngel();
		return LengthBetweenQRAndDrone();
	}
	
	public Point2D findPositionTwoQR() {
		
		double f1, f2, yres1, yres2, xres1, xres2, dist1, dist2;
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