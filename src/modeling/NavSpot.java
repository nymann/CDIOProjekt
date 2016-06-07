package modeling;

public class NavSpot {

	/*
	 *Tif id is uneven -> startspot
	 *if id is even -> endspot
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
	
}
