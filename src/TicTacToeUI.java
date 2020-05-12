import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class TicTacToeUI extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 5722572588622627859L;
	
	Timer timer;
	int delay = 13;
	TicTacToeGame game;
	double backgroundRotation;
	
	public TicTacToeUI() {
		initTimer();
		
		game = new TicTacToeGame();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent args0) {
				if(game.inProgress && game.isPlayerTurn && mouseIsNearQuad() != null && !game.gamePaused) {
					game.playerPlay(mouseIsNearQuad());
				}
			}
		});
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 82) {
					game.inProgress = false;
					game.tickAnimation = 249;
				}
				if(e.getKeyCode() == 80) {
					game.gamePaused = !game.gamePaused;
				}
				if(e.getKeyCode() == 77) {
					game.showMoves = !game.showMoves;
				}
				//System.out.println(e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//nothing
			}

			@Override
			public void keyTyped(KeyEvent e) {
				//nothing
			}
		});
		
		setFocusable(true);
		requestFocus();
		
		//Blank Cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "Blank Cursor");
		setCursor(blankCursor);
		
		/*String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for(String x: fonts) {
			System.out.println(x);
		}*/
	}
	
	public void initTimer() {
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public void drawX(Graphics2D g2d, int x, int y, boolean ghost) {
		if(ghost)
			g2d.setColor(new Color(255,75,100));
		else
			g2d.setColor(new Color(255,0,25));
		g2d.drawLine(x+25, y+25, x-25, y-25);
		g2d.drawLine(x-25, y+25, x+25, y-25);
	}
	
	public void drawO(Graphics2D g2d, int x, int y, boolean ghost) {
		if(ghost)
			g2d.setColor(new Color(100,75,255));
		else
			g2d.setColor(new Color(25,0,255));
		g2d.drawOval(x-25, y-25, 50, 50);
	}
	
	public void drawMove(Graphics2D g2d, int x, int y, String move, char piece) {
		/*if(piece == 'x') {
			g2d.setColor(new Color(255,0,25));
		}
		else {
			g2d.setColor(new Color(25,0,255));
		}*/
		g2d.setColor(new Color(0,0,0,(float).5));
		g2d.setFont(new Font("Courier New", Font.BOLD, 125));
		if(move != null) {
			g2d.drawString(""+move, x-35, y+40);
		}
	}
	
	public void drawPiece(Graphics2D g2d, int x, int y, boolean ghost, char piece) {
		if(piece=='x') {
			drawX(g2d, x, y, ghost);
		}
		else if(piece=='o') {
			drawO(g2d, x, y, ghost);
		}
	}
	
	public Quad mouseIsNearQuad() {
		int x = (int) getMousePosition().getX();
		int y = (int) getMousePosition().getY();
		for(Quad q: game.getGrid()) {
			if(q.getPiece()==0)
			{
				Points mouse = new Points(x, y);
				if(Points.getDistance(mouse, q.getPoint()) < 70) {
					return q;
				}
			}
		}
		return null;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		doBackground(g);
		doConsole(g);
		
		doGrid(g);
		
		if(game.isPlayerTurn && game.inProgress) {
			doMouseOverlay(g);
		}
		
		doUIBackground(g);
		
		if(!game.isPlayerTurn && game.inProgress && !game.gamePaused) {
			game.botMoveReason = "Bot is thinking...";
			doBotThinking(g);
		}
		
		if(game.isPlayerTurn & game.inProgress && !game.gamePaused) {
			doPlayerTurn(g);
		}
		
		if(game.gamePaused) {
			doGamePaused(g);
		}
		
		if(!game.inProgress && !game.gamePaused) {
			if(game.gameStatus != null) {
				if(game.gameStatus.equals("tie")) {
					doGameTie(g);
				}
				if(game.gameStatus.equals("playerWon")) {
					doPlayerWon(g);
				}
				if(game.gameStatus.equals("botWon")) {
					doBotWon(g);
				}
			}
			doGameResetting(g);
		}
		
		doQuads(g);
		
		doTitle(g);
		doScore(g);
		doIdentity(g);
		doUILines(g);
	}
	
	public void doBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		
		AffineTransform old = g2d.getTransform();
		g2d.translate(250, 250);
		g2d.rotate(Math.toRadians(backgroundRotation));
		backgroundRotation+=.2;
		//g2d.setPaint(new GradientPaint(0, 0, new Color(75,0,0), 250, 250, new Color(20,20,95)));
		g2d.setPaint(new GradientPaint(0, 0, new Color(20,20,95), 250, 250, new Color(75,0,0)));
		
		g2d.fillRect(0-375, 0-375, 750, 750);
		
		
		g2d.setTransform(old);
		//g2d.setPaint(new GradientPaint(500, 250, new Color(25,25,25), 800, 250, new Color(5,5,5)));
		//g2d.fillRect(500, 0, 300, 500);
	}
	
	public void doConsole(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 25));
		g2d.setColor(Color.WHITE);
		if(game.showMoves && game.botMoveReason != null) {
			g2d.drawString(game.botMoveReason, 5, 20);
		}
	}
	
	public void doUIBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		
		g2d.setPaint(new GradientPaint(500, 250, new Color(25,25,25), 800, 250, new Color(5,5,5)));
		g2d.fillRect(500, 0, 300, 500);
	}
	
	public void doTitle(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 30));
		g2d.setColor(Color.WHITE);
		
		g2d.drawString("Tic Tac Toe", 550, 50);
	}
	
	public void doScore(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 25));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score", 615, 125);
		g2d.drawString("(You) "+game.playerPoints+" : "+game.botPoints+" (Bot)", 525, 150);
	}
	
	public void doIdentity(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("You are playing as "+game.playerPiece, 525, 250);
	}
	
	public void doBotThinking(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Bot is thinking...", 550, 375);
	}
	
	public void doPlayerTurn(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("It is your turn.", 550, 375);
	}
	
	public void doGamePaused(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Game is paused...", 550, 375);
	}
	
	public void doGameTie(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Game is a tie!", 565, 375);
	}
	
	public void doPlayerWon(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("You won!", 590, 375);
		
		if(game.tickAnimation<0) {
			ArrayList<Points> winPoints = game.getWinPoints(game.playerPiece);
			if(winPoints != null) {
				for(int i=0; i<winPoints.size(); i+=2) {
					Points p1 = winPoints.get(2*i);
					Points p2 = winPoints.get(2*i+1);
					
					g2d.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
					g2d.setPaint(Color.WHITE);
					g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				}
			}
		}
	}
	
	public void doBotWon(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("The bot won!", 575, 375);
		
		if(game.tickAnimation<0) {
			ArrayList<Points> winPoints = game.getWinPoints(game.botPiece);
			if(winPoints != null) {
				for(int i=0; i<winPoints.size(); i+=2) {
					Points p1 = winPoints.get(2*i);
					Points p2 = winPoints.get(2*i+1);
					
					g2d.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
					g2d.setPaint(Color.WHITE);
					g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				}
			}
		}
	}
	
	public void doGameResetting(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Resetting...", 580, 400);
	}
	
	public void doUILines(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.drawLine(550, 70, 750, 70);
		g2d.drawLine(550, 190, 750, 190);
		g2d.drawLine(550, 300, 750, 300);
	}
	
	public void doGrid(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		
		if(game.inProgress) {
			g2d.setPaint(Color.WHITE);
		}
		else {
			if(game.gameStatus.equals("playerWon")) {
				if(game.playerPiece == 'x') {
					g2d.setColor(new Color(255,75,100));
				}
				else {
					g2d.setColor(new Color(100,75,255));
				}
			}
			else if(game.gameStatus.equals("botWon")){
				if(game.botPiece == 'x') {
					g2d.setColor(new Color(255,75,100));
				}
				else {
					g2d.setColor(new Color(100,75,255));
				}
			}
		}
		
		//Horizontal Lines
		g2d.drawLine(110, 200, 390, 200);
		g2d.drawLine(110, 300, 390, 300);
		
		//Vertical Lines
		g2d.drawLine(200, 110, 200, 390);
		g2d.drawLine(300, 110, 300, 390);
		
		//Test Lines
		//g2d.drawLine(0,h/2,w,h/2);
		//g2d.drawLine(500, 0, 500, h);
		
	}
	
	public void doQuads(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		
		for(Quad q: game.getGrid()) {
			if(game.showMoves) {
				drawMove(g2d, q.getPoint().getX(), q.getPoint().getY(), q.getMove(), q.getPiece());	
			}
			if(q.getPiece()=='x')
			{
				drawX(g2d, q.getPoint().getX(), q.getPoint().getY(), false);
			}
			if(q.getPiece()=='o')
			{
				drawO(g2d, q.getPoint().getX(), q.getPoint().getY(), false);
			}
		}
	}
	
	public void doMouseOverlay(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(getMousePosition() != null) {
			if(getMousePosition().x != 0 && getMousePosition().y != 0) {
				int x = getMousePosition().x;
				int y = getMousePosition().y;
				
				if(mouseIsNearQuad() != null) {
					drawPiece(g2d, mouseIsNearQuad().getPoint().getX(), mouseIsNearQuad().getPoint().getY(), true, game.playerPiece);
				}
				else if(game.playerPiece=='x') {
					drawX(g2d, x, y, true);
				}
				else if(game.playerPiece=='o') {
					drawO(g2d, x, y, true);
				}
				//System.out.println("X: "+x+" Y: "+y);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		if(!game.gamePaused) {
			game.incrementTickAnimation();
			if(!game.isPlayerTurn)
			{
				game.botThinkingTickTimer();
			}
			if(!game.inProgress) {
				game.gameEndTimer();
			}	
		}
		//System.out.println(game.tickAnimation);
	}

}
