package modeling;

public class NavSpot {

	/*
	 *if id is even -> startspot
	 *if id is uneven -> endspot
	 */
	
	private int x;
	private int y;
	private int z;
	private int id;

	public NavSpot(int x, int y, int z, int id){
		this.x=x;
		this.y=y;
		this.z=z;
		this.id=id;
	}
	
	public NavSpot(int id){
		this.id=id;
	}

	public int getX() { return x; }
	public void setX(int x) { this.x = x; }

	public int getY() { return y; }
	public void setY(int y) { this.y = y; }

	public int getZ() {	return z; }
	public void setZ(int z) { this.z = z; }

	public int getId() { return id;	}
	public void setId(int id) {	this.id = id; }
	
}
