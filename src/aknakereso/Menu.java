package aknakereso;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar implements ActionListener {
	/**
	 * Ez az osztály felel a menüsor megfelelõ mûködéséért, adattagjai egy JFrame, 
	 * és a 3 ArrayList a Leaderboard miatt
	 * 
	 */
	private JFrame f;
	public ArrayList<Player> BLeaderboard= new ArrayList<>();
	public ArrayList<Player> MLeaderboard= new ArrayList<>();
	public ArrayList<Player> HLeaderboard= new ArrayList<>();
	
	/**Menu konstruktora
	 * A menüsor részeit beállítja, ActionListenereket ad hozzájuk
	 * 
	 * @param f JFrame, amit azért kap meg a konstruktor, ugyanis a menüpontoknál szükséges
	 * az adott JFrame dispose() fv hívása
	 */
	public Menu(JFrame f) {
			this.f=f;
			JMenu jmFile = new JMenu("File");
		    JMenuItem jmiNewGame = new JMenuItem("New Game");
		    JMenuItem jmiLeaderboard = new JMenuItem("Leaderboard");
		    JMenuItem jmiExit = new JMenuItem("Exit");
		    jmFile.add(jmiNewGame);
		    jmFile.add(jmiLeaderboard);
		    jmFile.addSeparator();
		    jmFile.add(jmiExit);
		    this.add(jmFile);

		    JMenu jmOptions = new JMenu("Options");
		    JMenu jmDifficulty = new JMenu("Difficulty");
		    JMenuItem jmiBeginner = new JMenuItem("Beginner");
		    JMenuItem jmiMedium  = new JMenuItem("Medium");
		    JMenuItem jmiHard = new JMenuItem("Hard");
		    JMenuItem jmiCustom = new JMenuItem("Custom...");
		    jmDifficulty.add(jmiBeginner);
		    jmDifficulty.add(jmiMedium);
		    jmDifficulty.add(jmiHard);
		    jmDifficulty.add(jmiCustom);
		    jmOptions.add(jmDifficulty);

		    this.add(jmOptions);

		    JMenu jmHelp = new JMenu("Help");
		    JMenuItem jmiAbout = new JMenuItem("About");
		    jmHelp.add(jmiAbout);
		    this.add(jmHelp);

		
		    jmiNewGame.addActionListener(this);
		    jmiCustom.addActionListener(this);
		    jmiLeaderboard.addActionListener(this);
		    jmiExit.addActionListener(this);
		    jmiBeginner.addActionListener(this);
		    jmiMedium.addActionListener(this);
		    jmiHard.addActionListener(this);
		    jmiAbout.addActionListener(this);

	 }

	/**
	 * Az actionPerformed függvény felel azért, hogy a megfelelõ események történjenek a menüsorokon való kattintásokkor
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String str = ae.getActionCommand();
		switch(str) {
		case "Exit": System.exit(0); break;
		case "Leaderboard": load(); new Leaderboard(BLeaderboard, MLeaderboard, HLeaderboard); break;
		case "New Game": f.dispose(); mainn(10,10,10); break;
		case "About": AboutMe(); break;
		case "Beginner": f.dispose(); mainn(10, 10, 10); break;
		case "Medium": f.dispose(); mainn(16, 16, 40); break;
		case "Hard": f.dispose(); mainn(16, 30, 99); break;
		case "Custom...": custom(); break;
		default : System.out.println("Megnyomta!"); break;
		}
	}
	
	/**
	 * A Leaderboard elõhívása miatt szükség volt egy load() fv. megírása is, amely egyezik a Minesweeper classban találhatóval
	 */
	public void load() {
        try {
        	FileInputStream fis1 = new FileInputStream("beginner.txt");
        	FileInputStream fis2 = new FileInputStream("medium.txt");
        	FileInputStream fis3 = new FileInputStream("hard.txt");
            ObjectInputStream in1 = new ObjectInputStream(fis1);
            ObjectInputStream in2 = new ObjectInputStream(fis2);
            ObjectInputStream in3 = new ObjectInputStream(fis3);
            BLeaderboard = (ArrayList<Player>) in1.readObject();
            MLeaderboard = (ArrayList<Player>) in2.readObject();
            HLeaderboard = (ArrayList<Player>) in3.readObject();
            in1.close();
            in2.close();
            in3.close();
        } catch (IOException | ClassNotFoundException ignored) {

        }
	}

	/** Ugyanazt a feladatot látja el, mint a Main osztályban található main fv.
	 * 
     * @param sz : pálya szélessége
     * @param m : pálya magassága
     * @param db : aknák darabszáma 
	 */
	public void mainn(int m, int sz, int db) {
	    JFrame f = new JFrame("Minesweeper");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setLayout(new GridLayout());
	    f.setJMenuBar(new Menu(f));			
		f.add(new Minesweeper(m, sz, db));
		f.setVisible(true);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setResizable(false);
	}
		
	
	/** A tetszõleges méretû pálya beállításához szükséges függvény (új JFrame létrehozása)
	 * Megadott határokon belül
	 */
	public void custom(){
			JFrame customframe = new JFrame("Custom settings");
			JPanel panel = new JPanel(new GridLayout(4,2));
			JLabel label1 = new JLabel("Magasság: (maximum 30)");
			JTextField textf1 = new JTextField();
			JLabel label2 = new JLabel("Szélesség: (maximum 30)");
			JTextField textf2 = new JTextField();
			JLabel label3 = new JLabel("Aknák száma: (maximum szélesség*magasság-1)");
			JTextField textf3 = new JTextField();
			JButton button1 = new JButton();
			button1.setText("Cancel");
			JButton button2 = new JButton();
			button2.setText("OK");
			button2.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent ae) {
					int m = minof(Integer.parseInt(textf1.getText()), 30);
					int sz = minof(Integer.parseInt(textf2.getText()), 30);
					int a = Integer.parseInt(textf3.getText());
					f.dispose();
					mainn(m, sz, minof(a, m*sz-1));
					customframe.dispose();
				}
				
			});
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					customframe.dispose();
				}
			});
			panel.add(label1); panel.add(textf1);
			panel.add(label2); panel.add(textf2);
			panel.add(label3); panel.add(textf3);
			panel.add(button1); panel.add(button2);
			customframe.add(panel);
			customframe.setSize(600, 200);
			customframe.setLocationRelativeTo(null);
			customframe.setVisible(true);
	}
	
	/**
	 * Néhány plusz információt megjelenítõ JFrame (készítõ, dátum, készítés oka)
	 */
	public void AboutMe(){
		JFrame aboutframe = new JFrame("About");
    	ImageIcon bomb = new ImageIcon("C:/Users/Mate/Documents/Prog3/hf/aknakereso/anotherbomb.png");
		aboutframe.setTitle("About");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel subpanel = new JPanel(new BorderLayout());
        JLabel label1 = new JLabel("Minesweeper by Jakab Mate");
        JLabel label12 = new JLabel("Homework of Basics of Programming 3");
        JLabel label2 = new JLabel();
        label2.setIcon(bomb);
        JLabel label3 = new JLabel("© 2018 Jakab Corporation. All rights reserved."); 
        JButton button = new JButton();
        button.setText("OK");
        button.addActionListener(new ActionListener() {
        	@Override
    		public void actionPerformed(ActionEvent ae) {
    			aboutframe.setVisible(false);
    		}
        }	);
        subpanel.add(label1, BorderLayout.NORTH);
        subpanel.add(label12, BorderLayout.CENTER);
        subpanel.add(label2, BorderLayout.EAST);
        subpanel.add(label3, BorderLayout.SOUTH);
        panel.add(subpanel, BorderLayout.NORTH);
        panel.add(button, BorderLayout.SOUTH);
        aboutframe.add(panel);
        aboutframe.setSize(310, 170);
        aboutframe.setLocationRelativeTo(null);
        aboutframe.setVisible(true);
	}

	
	
	/** 
	 * Megadja két int típusú változó közül a kisebbet, és szól a Console-on, hogyha átállította megjelenítési és/vagy
	 * futtathatósági okokból a megadott adatokat
	 * @param a A gomb egyik paramétere
	 * @param b A gomb másik paramétere
	 * @return A kisebbel visszatér
	 */
	int minof(int a, int b){
		if(a>b) System.out.println("Value modified (" + a + " to " + b + ")" );
	    return a>b ? b : a;
	}
}