
public class Quad {
	
	String ID;
	Points points;
	char piece;
	String position;
	String move;
	
	public Quad(String ID, Points points) {
		this.ID = ID;
		this.points = points;
		this.piece = 0;
	}
	
	public Quad(String ID, Points points, char c) {
		this.ID = ID;
		this.points = points;
		this.piece = c;
	}
	
	public String getPosition() {
		if(ID.contains("1")) {
			if(ID.equals("11")) {
				return "MIDDLE";
			}
			else {
				return "EDGE";
			}
		}
		else {
			return "CORNER";
		}
	}
	
	public String getID() {
		return ID;
	}
	
	public Points getPoint() {
		return points;
	}
	
	public char getPiece() {
		return piece;
	}
	
	public Quad getQuad() {
		return this;
	}
	
	public String getMove() {
		return move;
	}
	
	public void setPiece(char piece) {
		this.piece = piece;
	}
	
	public void setMove(int move) {
		this.move = ""+move;
	}
	
	public String toString() {
		return ""+ID+" "+points.toString()+" "+piece;
	}
	
}
