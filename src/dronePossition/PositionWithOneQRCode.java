package dronePossition;

import javafx.geometry.Point2D;

public class PositionWithOneQRCode {
	
	double x, y, x1, x2, y1, y2;
	double d = 21.2, p, k1, k2, alpha, beta, fullLength;
	
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
	
	public double Length() {
		findLenght();
		findAngel();
		return LengthBetweenQRAndDrone();
	}
	
	public Point2D findPosition() {
		double firstLength = Length();
		//Drone skal have læst en ny QR kode imellem disse kald.
		double secondLength = Length();
		
		double c1 = Math.pow((x-x1), 2)+Math.pow((y-y1), 2)-Math.pow(firstLength,2);
		double c2 = Math.pow((x-x2), 2)+Math.pow((y-y2), 2)-Math.pow(secondLength,2);
		
		x = Math.pow((-Math.pow((y-y1), 2) + firstLength*firstLength + Math.pow((y-y2),2) - secondLength*secondLength - x1*x1 + x2*x2)/(-2*x1+2*x2)-x1,2)+Math.pow((y-y1),2);
		// y = Math.sqrt(firstLength*firstLength-Math.pow((x-x1), 2)) + y1;
		
		Point2D dronePositioning = new Point2D(x, y);
		
		return dronePositioning;
	}
	
}