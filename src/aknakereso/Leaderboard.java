package aknakereso;

import java.awt.*;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A dicsõségtábla miatt létrehozott osztály, ami egy JFrame leszármazottja
 * @author Mate
 *
 */
public class Leaderboard extends JFrame {
	/**
	 * A konstruktorában történik minden, megkapja a kezdõ, haladó és profi dicsõségtáblákat, amelyeket megjelenít egy
	 * GridLayout segítségével megfelelõ formában
	 * Összesen 8 sora van a JTable-knek, amibõl az elsõ az oszlopnév, ezáltal 7 eredmény kerülhet a táblába
	 * @param BeginnerLeaderboard: kezdõjátékosok dicsõségtáblája
	 * @param MediumLeaderboard: haladójátékosok dicsõségtáblája
	 * @param HardLeaderboard: profijátékosok dicsõségtáblája
	 */
    public Leaderboard(ArrayList<Player> BeginnerLeaderboard, ArrayList<Player> MediumLeaderboard, ArrayList<Player> HardLeaderboard) {
        this.setTitle("Leaderboard");
        this.setResizable(false);
        this.setSize(new Dimension(800, 300));
        this.setLayout(new GridLayout(1, 3));
        JPanel beginner = new JPanel(new BorderLayout());
        JPanel medium = new JPanel(new BorderLayout());
        JPanel hard = new JPanel(new BorderLayout());
        JLabel lb = new JLabel("Beginner");
        JLabel lm = new JLabel("Medium");
        JLabel lh = new JLabel("Hard");
        beginner.add(lb, BorderLayout.NORTH);
        medium.add(lm, BorderLayout.NORTH);
        hard.add(lh, BorderLayout.NORTH);
        beginner.setBackground(Color.LIGHT_GRAY);
        medium.setBackground(Color.LIGHT_GRAY);
        hard.setBackground(Color.LIGHT_GRAY);
        
        BeginnerLeaderboard.sort(Comparator.<Player>naturalOrder());
        MediumLeaderboard.sort(Comparator.<Player>naturalOrder());
        HardLeaderboard.sort(Comparator.<Player>naturalOrder());
        
        DefaultTableModel tableModelBeginner = new DefaultTableModel(8, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
            	if(row == 0 && column == 0) return "Name";
            	if(row == 0 && column == 1) return "Time";
                if (column == 0 && row > 0 && row -1 < BeginnerLeaderboard.size())
                	return BeginnerLeaderboard.get(row - 1).getname();
                if (column == 1 && row > 0 && row -1 < BeginnerLeaderboard.size()) 
                    return BeginnerLeaderboard.get(row - 1).gettime();               
                return null;
            }
        };
        
        DefaultTableModel tableModelMedium = new DefaultTableModel(8, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
            	if(row == 0 && column == 0) return "Name";
            	if(row == 0 && column == 1) return "Time";
                if (column == 0 && row > 0 && row -1 < MediumLeaderboard.size())
                	return MediumLeaderboard.get(row - 1).getname();
                if (column == 1 && row > 0 && row -1 < MediumLeaderboard.size()) 
                    return MediumLeaderboard.get(row - 1).gettime();               
                return null;
            }

        };
        DefaultTableModel tableModelHard = new DefaultTableModel(8, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
            	if(row == 0 && column == 0) return "Name";
            	if(row == 0 && column == 1) return "Time";
                if (column == 0 && row > 0 && row -1 < HardLeaderboard.size())
                	return HardLeaderboard.get(row - 1).getname();
                if (column == 1 && row > 0 && row -1 < HardLeaderboard.size()) 
                    return HardLeaderboard.get(row - 1).gettime();               
                return null;
            }
        };
        

        this.add(beginner);
        this.add(medium);
        this.add(hard);
		this.setLocationRelativeTo(null);

        beginner.add(new JTable(tableModelBeginner), BorderLayout.CENTER);
        medium.add(new JTable(tableModelMedium), BorderLayout.CENTER);
        hard.add(new JTable(tableModelHard), BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }
}
