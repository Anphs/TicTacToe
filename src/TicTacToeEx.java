import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class TicTacToeEx extends JFrame{
	
	private static final long serialVersionUID = 8389224153109556187L;

	public TicTacToeEx() {
		initUI();
	}
	
	public void initUI() {
		setTitle("Tic Tac Toe - Anthuony");
		setSize(806,529);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		
		TicTacToeUI ticTacToeUI = new TicTacToeUI();
		
		add(ticTacToeUI);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ticTacToeUI.getTimer().stop();
			}
		});
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				TicTacToeEx ex = new TicTacToeEx();
				ex.setVisible(true);
			}
		});
	}

}
