
public class Points {
	
	int x, y;
	
	public Points(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Points getPoint() {
		return this;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public static double getDistance(Points p1, Points p2) {
		double d = Math.sqrt(Math.pow(p2.x-p1.x, 2)+Math.pow(p2.y-p1.y, 2));
		return d;
	}
	
	public String toString() {
		return ""+x+" "+y;
	}
}
