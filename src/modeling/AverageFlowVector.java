package modeling;

/**
 * 
 * @author Simon
 *
 */
public class AverageFlowVector {
	
	public double x, y;
	
	public AverageFlowVector() {
		x = 0;
		y = 0;
	}
	
	public double getLength() {
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public void addVector(FlowVector v) {
		this.x += v.p2.x - v.p1.x;
		this.y += v.p2.y - v.p1.y;
	}
	
	public void computeAverageVector(int vectors) {
		x /= vectors;
		y /= vectors;
	}
}
