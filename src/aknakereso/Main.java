package aknakereso;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * Main függvény, létrehoz egy JFrame-et, amelyekhez hozzáad egy menüsort és magát a játékmezõt
 * Kezdetben mindig 10, 10, 10-es paraméterekkel jön létre a pálya
 * @author Mate
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    JFrame f = new JFrame("Minesweeper");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setLayout(new GridLayout());
	    f.setJMenuBar(new Menu(f));
		
		f.add(new Minesweeper(10, 10, 10));
		f.setVisible(true);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setResizable(false);
	}

}
