package aknakereso;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import aknakereso.Menu;

public class Minesweeper extends JPanel{
	
	private ArrayList<Player> BLeaderboard= new ArrayList<>();
	private ArrayList<Player> MLeaderboard= new ArrayList<>();
	private ArrayList<Player> HLeaderboard= new ArrayList<>();

	/**Sz�ks�ges, hogy az oszt�ly t�rzs�ben hozzunk l�tre n�h�ny objektumot, primit�vet annak �rdek�ben
	 * hogy a konstruktoron k�v�li f�ggv�nyek sz�m�ra is haszn�lhat�ak legyenek
	 * pl: first: az els� kattint�s el�tt igaz az �rt�ke, ut�na hamis
	 */
	int szelesseg;
	int magassag;
	int aknadb;
	public MyButton[][] buttons; 
	String nehezseg = "";
	boolean first = true;
	MyTimer timer= new MyTimer();
	
    private JLabel hatralevoakna = new JLabel(Integer.toString(aknadb));
    ImageIcon akna = new ImageIcon("images/akna2.png");
    JLabel vegeredmeny = new JLabel("Have fun!");

    /**A j�t�k magja, itt t�rt�nik maga az aknakeres� konstru�l�sa (JPanel lesz�rmazottja)
     * Mind a kin�zeti, mind a j�t�kmechanik�t megold� MouseListener itt tal�lhat�
     * @param sz : p�lya sz�less�ge
     * @param m : p�lya magass�ga
     * @param db : akn�k darabsz�ma 
     */
	public Minesweeper(int m, int sz, int db){
		load();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEtchedBorder(1));
		szelesseg = sz;
		magassag = m;
		aknadb = db;
		nehezseg = setnehezseg(m, sz, db); 
		JPanel gamedata = new JPanel(new GridLayout(1,3));
		gamedata.setBackground(Color.WHITE);
		gamedata.setSize(200,30*szelesseg);
	    ImageIcon imagetime = new ImageIcon("images/time.png");
	    ImageIcon imageflag = new ImageIcon("images/flag.png");
	    
	    Image imagetmp = imagetime.getImage(); // transform it 
	    Image newimg = imagetmp.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    imagetime = new ImageIcon(newimg);
	    Box box = Box.createHorizontalBox();
	    JLabel labelimage = new JLabel();
	    labelimage.setIcon(imageflag);
	    box.add(labelimage);
	    
	    Box box2 = Box.createHorizontalBox();
	    JLabel labelimage2 = new JLabel();
	    labelimage2.setIcon(imagetime);
	    box2.add(labelimage2);
	    box2.add(Box.createRigidArea(new Dimension(10,0)));
	    JLabel temp = new JLabel("0");
	    box2.add(temp); 
		gamedata.add(box);
		gamedata.add(vegeredmeny);
		gamedata.add(box2);
		gamedata.setBackground(new java.awt.Color(139, 152, 237));
		this.add(gamedata, BorderLayout.NORTH);
	    MyButton[][] buttons = new MyButton[m][sz];
	    

	    /**Egy Containert haszn�lok a MyButtonok t�rol�s�ra
	     * Az alatta tal�lhat� for ciklusban �ll�tom be a gombok kin�zeti tulajdons�gait, adok hozz� MouseListenert
	     */
	    Container grid = new Container();
        grid.setLayout(new GridLayout(0,sz));
        for (int i = 0; i<magassag; i++) {
            for (int j = 0; j < szelesseg; j++) {
            	buttons[i][j] = new MyButton(i, j);
            	if((i+j)%2==0)
            		buttons[i][j].setBackground(new java.awt.Color(189, 128, 94));
            	if((i+j)%2!=0) 
            		buttons[i][j].setBackground(new java.awt.Color(155, 96, 64));
            	buttons[i][j].setPreferredSize(new Dimension(30, 30));
            	Border thickBorder = new LineBorder(new java.awt.Color(228, 205, 192), 1);
            	buttons[i][j].setBorder(thickBorder); 
               	buttons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
                    	MyButton source = (MyButton) e.getSource();

                    	if(source.isEnabled()) {
                    		 switch (e.getButton()) {
                             case MouseEvent.BUTTON1:
                            	 /**
                            	  * Az els� kattint�s (bal eg�rgomb) ut�n indul el az id�z�t�, tov�bb� l�that�v� teszi a mez�ket
                            	  */
                            	 if(first) {
                            	        aknageneralas(buttons, source.getx(), source.gety());
                            	        ertekAdas(buttons);
                            	        first = false;
                            	        timer.start();
                            	        box2.remove(temp);
                            	        box2.add(timer);	
                            	 }
                                 source.setEnabled(false);
                                 source.setBackground(new java.awt.Color(220, 230, 250));
                                 source.setlathato(true);
                                 source.setmegjelolt(false);
                                 source.setIcon(null);
                                 if(source.getvalue()==-1) {
                                    source.setBackground(Color.RED);
                                 	source.setIcon(akna);
                                 	vesztettem(buttons, source.getx(), source.gety());
                                 }
                                 
                                 if(source.getvalue()>0) {
                                 	source.setText(Integer.toString(source.getvalue()));
                                 }
                                 if(source.getvalue()==0) {
                                 	nulla(buttons, source.getx(), source.gety());
                                 }
                             	Border thickBorder2 = new LineBorder(new java.awt.Color(205, 205, 205), 1);
                             	source.setBorder(thickBorder2);  //BorderFactory.createEtchedBorder(1)
                             	if(lathato_db(buttons)==(szelesseg*magassag-aknadb)) nyertem(buttons);
                                 break;
                             case MouseEvent.BUTTON3:
                             	/**
                             	 * A jobb eg�rgomb hat�s�ra az akn�k darabsz�m�t figyelembe v�ve jelennek meg flag-ek a p�ly�n
                             	 */
                                 if (e.getButton() == MouseEvent.BUTTON3 && !source.lathato()) {
                                	 boolean megnyomva = true;
                              		if(megjelolt_db(buttons)<=aknadb && source.megjelolt() && megnyomva) {
                              			source.setIcon(null);
                              			source.setmegjelolt(false);
                              			megnyomva = false;
                              		}
                             		if(megjelolt_db(buttons)<aknadb && !source.megjelolt() && megnyomva) {
                             			ImageIcon imageflagg = new ImageIcon("images/flag.png");
                                     	Image imagetmp = imageflagg.getImage(); // transform it 
                                     	Image newimg = imagetmp.getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                                     	imageflagg = new ImageIcon(newimg);
                                 		source.setIcon(imageflagg);
                             			source.setmegjelolt(true);
                             			megnyomva = false;
                             		}

                             		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
                                 }                                                            	
                                 break;
                             default:
                                 break;
                         }
                    	}
                		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
                       
                    	
                    }



					@Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                grid.add(buttons[i][j]);
            }
        }
 		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
        grid.setSize(30*magassag, 30*szelesseg);
        this.add(grid, BorderLayout.SOUTH);
        gamedata.add(vegeredmeny, BorderLayout.NORTH);
        box.add(hatralevoakna);

	}
	
	/**A j�t�k elveszt�sekor h�v�dik meg a f�ggv�ny, figyelembe veszi azt, hogy melyik akn�ra l�pt�nk r�,
	 * ez alapj�n azt a mez�t k�l�n sz�nnel jel�li.
	 * @param buttons : A  gombokat tartalmaz� 2D-s t�mb
	 * @param x : A j�t�k elveszt�s�t okoz� gomb sz�less�get megad� koordin�t�ja
	 * @param y : A j�t�k elveszt�s�t okoz� gomb magass�g�t megad� koordin�t�ja
	 */
	public void vesztettem(MyButton[][] buttons, int x, int y) {
		timer.stop();
		vegeredmeny.setText("Looser!");
		this.setEnabled(false);
		for(int i=0; i<magassag; i++){
	        for(int j=0; j<szelesseg; j++){
	        	if((i!=x || j!=y ) && buttons[i][j].getvalue()==-1) {
	        		buttons[i][j].setIcon(akna);
	        		buttons[i][j].setBackground(new java.awt.Color(220, 230, 250));
	        	}
	        	buttons[i][j].setEnabled(false);	        		
	        }
		}
		
	}	

	/**A j�t�k megnyer�sekor megjelenik az �sszes gomb "tartalma"
	 * 
	 * @param buttons : A  gombokat tartalmaz� 2D-s t�mb
	 */
    public void nyertem(MyButton[][] buttons) {
		timer.stop();
		vegeredmeny.setText("Winner!");
		this.setEnabled(false);
		for(int i=0; i<magassag; i++){
	        for(int j=0; j<szelesseg; j++){
	        	if(buttons[i][j].getvalue()==-1) 
	        		buttons[i][j].setIcon(akna);
	        	buttons[i][j].setBackground(new java.awt.Color(220, 230, 250));  	
	        	buttons[i][j].setEnabled(false);	        		
	        }
		}
		if(nehezseg!="custom") {
			JFrame nyertes = new JFrame("Winner (dif:" + nehezseg + ", " + timer.getTime() + "sec)");
			JPanel p = new JPanel(new GridLayout(2,2));
			JLabel nev = new JLabel("Please enter your name:");
			JTextField neved = new JTextField();
			JButton button1 = new JButton();
			button1.setText("Cancel");
			JButton button2 = new JButton();
			button2.setText("OK");
			button2.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent ae) {					
					if(!neved.getText().isEmpty()) {
						switch(nehezseg) {
						case "beginner": BLeaderboard.add(new Player(neved.getText(), timer.getTime())); break;
						case "medium": MLeaderboard.add(new Player(neved.getText(), timer.getTime())); break;
						case "hard": HLeaderboard.add(new Player(neved.getText(), timer.getTime())); break;
						}
						save();
					nyertes.dispose();
					}
					if(neved.getText().isEmpty())
						neved.setText("Anonymous");

				
					
					
				}
				
			});
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					nyertes.dispose();
				}
			});
			nyertes.add(p);
			p.add(nev); p.add(neved); p.add(button1); p.add(button2);
			nyertes.setLocationRelativeTo(null);
			nyertes.setSize(600, 100);
			nyertes.setVisible(true);
		}
		
	}
		
    /**A param�terek alapj�n megadja, hogy a j�t�kos milyen neh�zs�gi szinten j�tszik
     * 
     * @param sz : p�lya sz�less�ge
     * @param m : p�lya magass�ga
     * @param db : akn�k darabsz�ma 
     * @return a megfelel� Stringeket adja vissza (ha nem tal�lja, akkor a "custom"-ot
     */
	public String setnehezseg(int m, int sz, int db) {
		if(magassag==10 && szelesseg==10 && aknadb ==10)
			return "beginner";
		if(magassag==16 && szelesseg==16 && aknadb ==40)
			return "medium";
		if(magassag==16 && szelesseg==30 && aknadb ==99)
			return "hard";
		return "custom";
	}
	
	/**
	 * 
	 * @param buttons: A  gombokat tartalmaz� 2D-s t�mb
	 * @param x: A jobb klikkel kiv�lasztott gomb x param�tere
	 * @param y: A jobb klikkel kiv�lasztott gomb y param�tere
	 */
	public void jelol(MyButton[][] buttons, int x, int y) {	
		if(megjelolt_db(buttons)<aknadb && !buttons[x][y].megjelolt()) {
       		ImageIcon imageflag = new ImageIcon("images/flag.png");
        	Image imagetmp = imageflag.getImage(); // transform it 
        	Image newimg = imagetmp.getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        	imageflag = new ImageIcon(newimg);
    		buttons[x][y].setIcon(imageflag);
			buttons[x][y].setmegjelolt(true);
		}
		if(megjelolt_db(buttons)<aknadb && buttons[x][y].megjelolt()) {
			buttons[x][y].setIcon(null);
			buttons[x][y].setmegjelolt(false);
		}

		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
		
		
		
	}
	/**
	 * 
	 * @param buttons:  A  gombokat tartalmaz� 2D-s t�mb
	 * @return
	 */
	int megjelolt_db(MyButton[][] buttons){ 
	    int db=0;
	    for(int i=0; i<magassag; i++){
	        for(int j=0; j<szelesseg; j++){
	            if(buttons[i][j].megjelolt())
	                db++;
	        }
	    }
	    return db;
	}
	/**
	 * 
	 * @param buttons:  A  gombokat tartalmaz� 2D-s t�mb
	 * @return
	 */
	int lathato_db(MyButton[][] buttons){
	    int db=0;
	    for(int i=0; i<magassag; i++){
	        for(int j=0; j<szelesseg; j++){
	            if(buttons[i][j].lathato())
	                db++;
	        }
	    }
	    return db;
	}

	/**Random gener�lja az akn�kat a p�ly�n
	 * El�zetesen el lett ker�lve, hogy els� kattint�s ut�n akn�t tal�ljunk
	 * @param buttons: A  gombokat tartalmaz� 2D-s t�mb
	 * @param a: A gomb egyik param�tere
	 * @param b: A gomb m�sik param�tere
	 */
	public void aknageneralas(MyButton[][] buttons, int a, int b) {
		for(int i=0; i<aknadb; i++){
	        boolean volt_mar=true;
	        Random rand = new Random();
            int x = rand.nextInt(magassag);
            int y = rand.nextInt(szelesseg);
	        while(volt_mar){
	            volt_mar=false;
	            rand = new Random(); 
	            x = rand.nextInt(magassag);
	            rand = new Random();
	            y = rand.nextInt(szelesseg);
				if(buttons[x][y].getvalue()==-1) volt_mar = true; 
				if(x==a && y==b) volt_mar = true;				
	        }
	        buttons[x][y].setvalue(-1);  //a hozzajuk tartozo mezo feltoltese -1-gyel (csak az aknak erteke lehet -1)
	    }
	}
	
	/**A gombok �rt�k�t be�ll�tja aszerint, hogy h�ny akna tal�lhat� a k�rnyezet�ben
	 * dupla for ciklust (3*3) haszn�l m�dszer, figyel a p�lya sz�li hat�rokra
	 * @param buttons:  A  gombokat tartalmaz� 2D-s t�mb
	 */
	public void ertekAdas(MyButton[][] buttons){ 
		for(int i=0; i<magassag; i++){
	        for(int j=0; j<szelesseg; j++){
	            if(buttons[i][j].getvalue()==-1){
	                int a,b;
	                for(a=maxof(0, i-1); a<=minof(magassag-1, i+1); a++){ //ez azert szukseges, hogy ne legyen alul vagy tulindexeles
	                    for(b=maxof(0, j-1); b<=minof(szelesseg-1, j+1); b++){
	                        if(buttons[a][b].getvalue()!=-1)
	                            buttons[a][b].setvalue(buttons[a][b].getvalue()+1);
	                    }
	                }
	            }
	        }
	    }
	}
	
	/**
	 * Megadja k�t int t�pus� v�ltoz� k�z�l a nagyobbat
	 * @param a A gomb egyik param�tere
	 * @param b A gomb m�sik param�tere
	 * @return
	 */
	int maxof(int a, int b){
	    return a<b ? b : a;
	}

	/**
	 * Megadja k�t int t�pus� v�ltoz� k�z�l a kisebbet
	 * @param a A gomb egyik param�tere
	 * @param b A gomb m�sik param�tere
	 * @return
	 */
	int minof(int a, int b){
	    return a<b ? a : b;
	}

	/**A f�ggv�ny felel az�rt, hogy a 0 �rt�k� gomb k�rnyezet�ben minden gombot felfedjen (rekurz�van)
	 * 
	 * @param buttons: A  gombokat tartalmaz� 2D-s t�mb
	 * @param i: A gomb egyik param�tere
	 * @param j: A gomb m�sik param�tere
	 */
	public void nulla(MyButton[][] buttons, int i, int j) {
		buttons[i][j].setEnabled(false);
		buttons[i][j].setIcon(null);
        buttons[i][j].setBackground(new java.awt.Color(220, 230, 250));
    	Border thickBorder2 = new LineBorder(new java.awt.Color(205, 205, 205), 1);
    	buttons[i][j].setBorder(thickBorder2);  //BorderFactory.createEtchedBorder(1)
		buttons[i][j].setlathato(true);
		buttons[i][j].setmegjelolt(false);
 		hatralevoakna.setText(Integer.toString(aknadb - megjelolt_db(buttons)));
	    if(buttons[i][j].getvalue()!=0) {
	    	buttons[i][j].setText(Integer.toString(buttons[i][j].getvalue()));
	    	return;
	    }
	        //ha nem nulla, akkor mar nem megy be a fv-be
	    //egyesevel vegignezi a megfelelo feltetelekkel a 0 erteku mezo szomszedjait
	    if(i>0 && j>0)
	        if(!buttons[i-1][j-1].lathato())
	            nulla(buttons,  i-1, j-1);
	    if(i>0)
	        if(!buttons[i-1][j].lathato())
	            nulla(buttons,  i-1, j);
	    if(i>0 && j<szelesseg-1)
	        if(!buttons[i-1][j+1].lathato())
	            nulla(buttons,  i-1, j+1);
	    if(j>0)
	        if(!buttons[i][j-1].lathato())
	            nulla(buttons,  i, j-1);
	    if(i<magassag-1 && j>0)
	        if(!buttons[i+1][j-1].lathato())
	            nulla(buttons,  i+1, j-1);
	    if(i<magassag-1)
	        if(!buttons[i+1][j].lathato())
	            nulla(buttons,  i+1, j);
	    if(i<magassag-1 && j<szelesseg-1)
	        if(!buttons[i+1][j+1].lathato())
	            nulla(buttons,  i+1, j+1);
	    if(j<szelesseg-1)
	        if(!buttons[i][j+1].lathato())
	            nulla(buttons,  i, j+1);
	}
	
	/** Szerializ�l�s
	 *  A Leaderboard szerkeszt�se miatt sz�ks�g volt egy load() fv. meg�r�sa is, amely egyezik a Menu classban tal�lhat�val
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
	
	/** Szerializ�l�s
	 *  A Leaderboard szerkeszt�se miatt sz�ks�g volt egy save() fv. meg�r�sa is, amely egyezik a Menu classban tal�lhat�val
	 */
    public void save() {
        try {
        	FileOutputStream fos1 = new FileOutputStream("beginner.txt");
        	FileOutputStream fos2 = new FileOutputStream("medium.txt");
            FileOutputStream fos3 = new FileOutputStream("hard.txt");
            ObjectOutputStream out1 = new ObjectOutputStream(fos1);
            ObjectOutputStream out2 = new ObjectOutputStream(fos2);
            ObjectOutputStream out3 = new ObjectOutputStream(fos3);
            out1.writeObject(BLeaderboard);
            out2.writeObject(MLeaderboard);
            out3.writeObject(HLeaderboard);            
            out1.close();
            out2.close();
            out3.close();
        } catch (IOException ignored) {
        }
    }

	
    /** Megadja a p�lya sz�less�g�t
     *  getter
     * @return a p�lya sz�less�ge
     */
    public int getszelesseg() {
		return szelesseg;
	}  
    
    /** Megadja a p�ly�n l�v� akn�k sz�m�t
     * getter
     * @return a p�ly�n tal�lhat� akn�k sz�ma
     */
    public int getaknadb() {
		return aknadb;
	}    
    
    /** Megadja a p�lya magass�g�t
     *  getter
     * @return a p�lya magass�ga 
     */
    public int getmagassag() {
		return magassag;
	}
}
