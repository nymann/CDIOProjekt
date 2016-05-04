package modeling;

import org.opencv.core.Point;

/**
 * 
 * @author Simon
 *
 */
public class FlowVector {
	
	public Point p1, p2;
	
	public FlowVector(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public double getLength() {
		return Math.sqrt(Math.pow(p2.x-p1.x, 2)+Math.pow(p2.y-p1.y, 2));
	}

}
