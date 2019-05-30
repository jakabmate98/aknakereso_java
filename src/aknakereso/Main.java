package aknakereso;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * Main f�ggv�ny, l�trehoz egy JFrame-et, amelyekhez hozz�ad egy men�sort �s mag�t a j�t�kmez�t
 * Kezdetben mindig 10, 10, 10-es param�terekkel j�n l�tre a p�lya
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
