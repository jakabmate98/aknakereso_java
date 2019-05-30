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

	/**Szükséges, hogy az osztály törzsében hozzunk létre néhány objektumot, primitívet annak érdekében
	 * hogy a konstruktoron kívüli függvények számára is használhatóak legyenek
	 * pl: first: az elsõ kattintás elõtt igaz az értéke, utána hamis
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

    /**A játék magja, itt történik maga az aknakeresõ konstruálása (JPanel leszármazottja)
     * Mind a kinézeti, mind a játékmechanikát megoldó MouseListener itt található
     * @param sz : pálya szélessége
     * @param m : pálya magassága
     * @param db : aknák darabszáma 
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
	    

	    /**Egy Containert használok a MyButtonok tárolására
	     * Az alatta található for ciklusban állítom be a gombok kinézeti tulajdonságait, adok hozzá MouseListenert
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
                            	  * Az elsõ kattintás (bal egérgomb) után indul el az idõzítõ, továbbá láthatóvá teszi a mezõket
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
                             	 * A jobb egérgomb hatására az aknák darabszámát figyelembe véve jelennek meg flag-ek a pályán
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
	
	/**A játék elvesztésekor hívódik meg a függvény, figyelembe veszi azt, hogy melyik aknára léptünk rá,
	 * ez alapján azt a mezõt külön színnel jelöli.
	 * @param buttons : A  gombokat tartalmazó 2D-s tömb
	 * @param x : A játék elvesztését okozó gomb szélességet megadó koordinátája
	 * @param y : A játék elvesztését okozó gomb magasságát megadó koordinátája
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

	/**A játék megnyerésekor megjelenik az összes gomb "tartalma"
	 * 
	 * @param buttons : A  gombokat tartalmazó 2D-s tömb
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
		
    /**A paraméterek alapján megadja, hogy a játékos milyen nehézségi szinten játszik
     * 
     * @param sz : pálya szélessége
     * @param m : pálya magassága
     * @param db : aknák darabszáma 
     * @return a megfelelõ Stringeket adja vissza (ha nem találja, akkor a "custom"-ot
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
	 * @param buttons: A  gombokat tartalmazó 2D-s tömb
	 * @param x: A jobb klikkel kiválasztott gomb x paramétere
	 * @param y: A jobb klikkel kiválasztott gomb y paramétere
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
	 * @param buttons:  A  gombokat tartalmazó 2D-s tömb
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
	 * @param buttons:  A  gombokat tartalmazó 2D-s tömb
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

	/**Random generálja az aknákat a pályán
	 * Elõzetesen el lett kerülve, hogy elsõ kattintás után aknát találjunk
	 * @param buttons: A  gombokat tartalmazó 2D-s tömb
	 * @param a: A gomb egyik paramétere
	 * @param b: A gomb másik paramétere
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
	
	/**A gombok értékét beállítja aszerint, hogy hány akna található a környezetében
	 * dupla for ciklust (3*3) használ módszer, figyel a pálya széli határokra
	 * @param buttons:  A  gombokat tartalmazó 2D-s tömb
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
	 * Megadja két int típusú változó közül a nagyobbat
	 * @param a A gomb egyik paramétere
	 * @param b A gomb másik paramétere
	 * @return
	 */
	int maxof(int a, int b){
	    return a<b ? b : a;
	}

	/**
	 * Megadja két int típusú változó közül a kisebbet
	 * @param a A gomb egyik paramétere
	 * @param b A gomb másik paramétere
	 * @return
	 */
	int minof(int a, int b){
	    return a<b ? a : b;
	}

	/**A függvény felel azért, hogy a 0 értékû gomb környezetében minden gombot felfedjen (rekurzívan)
	 * 
	 * @param buttons: A  gombokat tartalmazó 2D-s tömb
	 * @param i: A gomb egyik paramétere
	 * @param j: A gomb másik paramétere
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
	
	/** Szerializálás
	 *  A Leaderboard szerkesztése miatt szükség volt egy load() fv. megírása is, amely egyezik a Menu classban találhatóval
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
	
	/** Szerializálás
	 *  A Leaderboard szerkesztése miatt szükség volt egy save() fv. megírása is, amely egyezik a Menu classban találhatóval
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

	
    /** Megadja a pálya szélességét
     *  getter
     * @return a pálya szélessége
     */
    public int getszelesseg() {
		return szelesseg;
	}  
    
    /** Megadja a pályán lévõ aknák számát
     * getter
     * @return a pályán található aknák száma
     */
    public int getaknadb() {
		return aknadb;
	}    
    
    /** Megadja a pálya magasságát
     *  getter
     * @return a pálya magassága 
     */
    public int getmagassag() {
		return magassag;
	}
}
