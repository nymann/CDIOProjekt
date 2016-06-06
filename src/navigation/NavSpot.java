package navigation;

public class NavSpot {

	/*
	 *Type means either startSpot OR endSpot
	 *1=startSpot
	 *2=endSpot
	 */
	
	private int x;
	private int y;
	private int type;

	public NavSpot(int x, int y, int type){
		this.x=x;
		this.y=y;
		this.type=type;
	}
	
}
