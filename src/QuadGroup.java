import java.util.ArrayList;

public class QuadGroup {
	
	int[] sums = new int[2];
	Quad[] qGroup = new Quad[3];
	
	public QuadGroup(Quad q1, Quad q2, Quad q3, char playerPiece, char botPiece) {
		this.sums = sumQuads(q1, q2, q3, playerPiece, botPiece);
		qGroup[0] = q1;
		qGroup[1] = q2;
		qGroup[2] = q3;
	}
	
	public int[] sumQuads(Quad q1, Quad q2, Quad q3, char playerPiece, char botPiece) {
		int sumPlayer = 0;
		int sumBot = 0;
		if(q1.getPiece()==playerPiece) {
			sumPlayer++;
		}
		if(q2.getPiece()==playerPiece) {
			sumPlayer++;
		}
		if(q3.getPiece()==playerPiece) {
			sumPlayer++;
		}
		if(q1.getPiece()==botPiece) {
			sumBot++;
		}
		if(q2.getPiece()==botPiece) {
			sumBot++;
		}
		if(q3.getPiece()==botPiece) {
			sumBot++;
		}
		int[] temp = {sumPlayer,sumBot};
		return temp;
	}
	
	public int[] getSums() {
		return sums;
	}
	
	public Quad[] getGroup() {
		return qGroup;
	}
	
	public static ArrayList<Quad> getOutersDiagonal(Quad[] group) {
		ArrayList<Quad> temp = new ArrayList<Quad>();
		for(Quad q: group) {
			if(!q.getID().equals("MIDDLE")) {
				temp.add(q);
			}
		}
		return temp;
	}
	
	public static ArrayList<Quad> getOuters(Quad[] g) {
		ArrayList<Quad> temp = new ArrayList<Quad>();
		if(g[0].getPosition().equals(g[1].getPosition())) {
			temp.add(g[0]);
			temp.add(g[1]);
		}
		if(g[0].getPosition().equals(g[2].getPosition())) {
			temp.add(g[0]);
			temp.add(g[2]);
		}
		if(g[1].getPosition().equals(g[2].getPosition())) {
			temp.add(g[1]);
			temp.add(g[2]);
		}
		return temp;
	}
	
	public String toString() {
		return sums[0] +" "+ sums[1];
	}
}
