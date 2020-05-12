import java.util.ArrayList;
import java.util.Random;

public class TicTacToeGame {

	private ArrayList<Quad> grid = new ArrayList<Quad>();
	private ArrayList<QuadGroup> quadSums = new ArrayList<QuadGroup>();
	char playerPiece;
	char botPiece;
	boolean isPlayerTurn;
	int tickAnimation = 0;
	int move = 0;
	int playerPoints = 0;
	int botPoints = 0;
	boolean inProgress;
	String gameStatus;
	boolean gamePaused;
	boolean showMoves;
	String botMoveReason;
	
	public TicTacToeGame() {
		newGame();
		/*for(Quad q: grid) {
			System.out.println(q);
		}*/
	}
	
	public void newGame() {
		System.out.println("New Game");
		resetGrid();
		isPlayerTurn = false;
		randomPlayer();
		move = 0;
		inProgress = true;
		botMoveReason = null;
	}
	
	public boolean coinFlip() {
		Random r = new Random();
		int temp = Math.abs(r.nextInt(2));
		if(temp==0)
			return false;
		return true;
	}
	
	public void setTickAnimation(int i) {
		tickAnimation = i;
	}
	
	public void incrementTickAnimation() {
		tickAnimation++;
	}
	
	public void initQuads() {
		for(int x=0; x<3; x++) {
			for(int y=0; y<3; y++) {
				int xx = 100*x+150;
				int yy = 100*y+150;
				String id = ""+x+y;
				Points p = new Points(xx, yy);
				grid.add(new Quad(id, p));
			}
		}
	}
	
	public int getSumCorners(char piece) {
		int sum = 0;
		for(Quad q: grid) {
			if(q.getPiece() == piece && q.getPosition().equals("CORNER")) {
				sum++;
			}
		}
		return sum;
	}
	
	public int getSumEdges(char piece) {
		int sum = 0;
		for(Quad q: grid) {
			if(q.getPiece() == piece && q.getPosition().equals("EDGE")) {
				sum++;
			}
		}
		return sum;
	}
	
	public void randomPlayer() {
		if(coinFlip()) {
			playerPiece = 'x';
			botPiece = 'o';
			isPlayerTurn = true;
		}
		else
		{
			playerPiece = 'o';
			botPiece = 'x';
			isPlayerTurn = false;
		}
	}
	
	public ArrayList<Quad> getGrid() {
		return grid;
	}
	
	public void resetGrid() {
		grid = new ArrayList<Quad>();
		initQuads();
	}
	
	public void checkCondition() {
		if(checkWin(playerPiece)) {
			inProgress = false;
			setTickAnimation(-100);
			playerPoints++;
			gameStatus = "playerWon";
			System.out.println("Player Won");
		}
		else if(checkWin(botPiece)) {
			inProgress = false;
			setTickAnimation(-100);
			botPoints++;
			gameStatus = "botWon";
			System.out.println("Bot Won");
		}
		else if(tie()) {
			inProgress = false;
			setTickAnimation(-100);
			gameStatus = "tie";
			System.out.println("Tie");
		}
	}
	
	public boolean tie() {
		int temp = 0;
		for(Quad q: grid) {
			if(q.getPiece() != 0) {
				temp++;
			}
		}
		if(temp==9) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkWin(char piece) {
		return (checkWinRows(piece) != null || checkWinColumns(piece) != null || checkWinDiagonals(piece) != null);
	}
	
	public ArrayList<Points> getWinPoints(char piece) {
		ArrayList<Points> temp = new ArrayList<Points>();
		if(checkWinColumns(piece) != null) {
			for(Quad q: QuadGroup.getOuters(checkWinColumns(piece))) {
				temp.add(q.getPoint());
			}
		}
		if(checkWinRows(piece) != null) {
			for(Quad q: QuadGroup.getOuters(checkWinRows(piece))) {
				temp.add(q.getPoint());
			}
		}
		if(checkWinDiagonals(piece) != null) {
			for(Quad q: QuadGroup.getOuters(checkWinDiagonals(piece))) {
				temp.add(q.getPoint());
			}
		}
		return temp;
	}
	
	public Quad[] checkWinColumns(char piece) {
		for(int x=0;x<3;x++) {
			Quad q1 = grid.get(x*3);
			Quad q2 = grid.get(x*3+1);
			Quad q3 = grid.get(x*3+2);
			if (compareQuads(q1, q2, q3, piece)) {
				Quad[] temp = {q1, q2, q3};
				return temp;
			}
		}
		return null;
	}

	public Quad[] checkWinRows(char piece) {
		for(int y=0;y<3;y++) {
			Quad q1 = grid.get(y+0);
			Quad q2 = grid.get(y+3);
			Quad q3 = grid.get(y+6);
			if (compareQuads(q1, q2, q3, piece)) {
				Quad[] temp = {q1, q2, q3};
				return temp;
			}
		}
		return null;
	}
	
	public Quad[] checkWinDiagonals(char piece) {
		Quad q1 = grid.get(2);
		Quad q2 = grid.get(4);
		Quad q3 = grid.get(6);
		if (compareQuads(q1, q2, q3, piece)) {
			Quad[] temp = {q1, q2, q3};
			return temp;
		}
		q1 = grid.get(0);
		q2 = grid.get(4);
		q3 = grid.get(8);
		if (compareQuads(q1, q2, q3, piece)) {
			Quad[] temp = {q1, q2, q3};
			return temp;
		}
		return null;
	}
	
	public boolean compareQuads(Quad q1, Quad q2, Quad q3, char piece) {
		if(q1.getPiece() == piece && q2.getPiece() == piece && q3.getPiece() == piece) {
			return true;
		}
		//System.out.println(""+q1.getPiece()+q2.getPiece()+q3.getPiece());
		return false;
	}
	
	public void groupColumns() {
		for(int x=0;x<3;x++) {
			Quad q1 = grid.get(x*3);
			Quad q2 = grid.get(x*3+1);
			Quad q3 = grid.get(x*3+2);
			quadSums.add(new QuadGroup(q1, q2, q3, playerPiece, botPiece));
		}
	}
	
	public void groupRows() {
		for(int y=0;y<3;y++) {
			Quad q1 = grid.get(y+0);
			Quad q2 = grid.get(y+3);
			Quad q3 = grid.get(y+6);
			quadSums.add(new QuadGroup(q1, q2, q3, playerPiece, botPiece));
		}
	}
	
	public void groupDiagonals() {
		Quad q1 = grid.get(2);
		Quad q2 = grid.get(4);
		Quad q3 = grid.get(6);
		quadSums.add(new QuadGroup(q1, q2, q3, playerPiece, botPiece));
		q1 = grid.get(0);
		q2 = grid.get(4);
		q3 = grid.get(8);
		quadSums.add(new QuadGroup(q1, q2, q3, playerPiece, botPiece));
	}
	
	public void playerPlay(Quad q) {
		q.setPiece(playerPiece);
		setTickAnimation(0);
		checkCondition();
		isPlayerTurn = false;
		q.setMove(move);
		move++;
	}
	
	public boolean botPlay(Quad q) {
		if(q != null) {
			q.setPiece(botPiece);
			setTickAnimation(0);
			checkCondition();
			isPlayerTurn = true;
			q.setMove(move);
			move++;
			return true;
		}
		return false;
	}
	
	public void botThinkingTickTimer() {
		if(tickAnimation > 100 && inProgress)
		{
			setTickAnimation(0);
		}
		else if(tickAnimation==100 && inProgress)
		{
			if(botPlay(botChooseLast())) {
			}
			else if(move==0) {
				botChooseCorner();
			}
			else if(move==1 && getSumCorners(playerPiece) > 0) {
				botPlay(grid.get(4));
				System.out.println("Bot Chose Middle");
				botMoveReason = "Bot Chose Middle";
			}
			else if(move==1 && grid.get(4).getPiece()==playerPiece) {
				botChooseCorner();
			}
			else if(move==3 && getSumCorners(playerPiece) > 1) {
				botChooseEdge();
			}
			else if(move==3 && getSumCorners(playerPiece) > 0) {
				botChooseCorner();
			}
			else if(move==2) {
				if(!botChooseOppositeFirst()) {
					botChooseCorner();
				}
			}
			else if(move==4 && getSumCorners(botPiece) > 1) {
				botChooseCorner();
			}
			else {
				botChooseRandom();
			}
		}
	}
	
	public void gameEndTimer() {
		if(tickAnimation > 250) {
			setTickAnimation(-100);
		}
		else if(tickAnimation==250) {
			newGame();
		}
		else if(tickAnimation>0 && tickAnimation%25 == 0 && tickAnimation/25 < 10 && grid.size()>0) {
			Random r = new Random();
			grid.remove(r.nextInt(grid.size()));
		}
	}
	
	public boolean botChooseCorner() {
		ArrayList<Quad> pool = new ArrayList<Quad>();
		for(Quad q: grid) {
			if(q.getPiece()==0 && q.getPosition().equals("CORNER"))
				pool.add(q);
		}
		if(pool.size()>0) {
			Random r = new Random();
			int temp = Math.abs(r.nextInt(pool.size()));
			Quad quadTemp = pool.get(temp);
			botPlay(quadTemp);
			System.out.println("Bot Chose Corner");
			botMoveReason = "Bot Chose Corner";
			return true;
		}
		return false;
	}
	
	public boolean botChooseEdge() {
		ArrayList<Quad> pool = new ArrayList<Quad>();
		for(Quad q: grid) {
			if(q.getPiece()==0 && q.getPosition().equals("EDGE"))
				pool.add(q);
		}
		if(pool.size()>0) {
			Random r = new Random();
			int temp = Math.abs(r.nextInt(pool.size()));
			Quad quadTemp = pool.get(temp);
			botPlay(quadTemp);
			System.out.println("Bot Chose Edge");
			botMoveReason = "Bot Chose Edge";
			return true;
		}
		return false;
	}
	
	public Quad botChooseLast() {
		quadSums = new ArrayList<QuadGroup>();
		groupColumns();
		groupRows();
		groupDiagonals();
		for(QuadGroup q: quadSums) {
			int[] sums = q.getSums();
			if(sums[1] > 1) {
				for(Quad qq: q.getGroup()) {
					if(qq.getPiece()==0) {
						System.out.println("Bot Chose Last (Bot)");
						botMoveReason = "Bot Chose Last (Bot Win)";
						return qq;
					}
				}
			}
		}
		for(QuadGroup q: quadSums) {
			int[] sums = q.getSums();
			if(sums[0] > 1) {
				for(Quad qq: q.getGroup()) {
					if(qq.getPiece()==0) {
						System.out.println("Bot Chose Last (Player)");
						botMoveReason = "Bot Chose Last (Block Player)";
						return qq;
					}
				}
			}
		}
		return null;
	}
	
	public boolean botChooseOppositeFirst() {
		Quad botFirst = null;
		for(Quad q: grid) {
			if(q.piece==botPiece) {
				botFirst=q;
			}
		}
		String botFirstID = botFirst.getID();
		String botNewID = "";
		if(botFirstID.charAt(0)=='0') {
			botNewID+="2";
		}
		else {
			botNewID+="0";
		}
		if(botFirstID.charAt(1)=='0') {
			botNewID+="2";
		}
		else {
			botNewID+="0";
		}
		for(Quad q: grid) {
			if(q.getID().equals(botNewID) && q.getPiece()==0) {
				botPlay(q);
				//System.out.println(botFirstID);
				//System.out.println(botNewID);
				System.out.println("Bot Chose Opposite First");
				botMoveReason = "Bot Chose Opposite First";
				return true;
			}
		}
		System.out.println("Bot Failed to Choose Opposite First");
		return false;
	}
	
	public void botChooseRandom() {
		ArrayList<Quad> pool = new ArrayList<Quad>();
		for(Quad q: grid) {
			if(q.getPiece()==0)
				pool.add(q);
		}
		Random r = new Random();
		int temp = Math.abs(r.nextInt(pool.size()));
		Quad quadTemp = pool.get(temp);
		botPlay(quadTemp);
		System.out.println("Bot Chose Random");
		botMoveReason = "Bot Chose Random";
	}
}
