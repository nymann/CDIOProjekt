package dronePossition;

public class PositionWithOneQRCode {
	
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
	
	public double findPosition() {
		double firstLength = Length();
		//Drone skal have læst en ny QR kode imellem disse kald.
		double secondLength = Length();
		
		
		
		return 0;
	}
	
}