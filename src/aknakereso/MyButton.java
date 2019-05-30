package aknakereso;

import javax.swing.JButton;

public class MyButton extends JButton {
	private int value;
	private boolean lathato_e;
	private boolean megjelolt_e;
	private int x;
	private int y;
	
	/**A MyButton konstruktora
	 * Beállítja kezdõállapotba a gombokat
	 * @param x: A gomb egyik paramétere
	 * @param y: A gomb másik paramétere
	 */
	public MyButton(int x, int y) {
		this.x=x;
		this.y=y;
		lathato_e = false;
		megjelolt_e = false;
		value=0;
	}
	
	/** A gomb körül lévõ aknák db számát adja meg, amennyiben -1, akkor egy akna a gomb
	 * getter
	 * @return környezetben (3*3) az aknák száma
	 */
	public int getvalue() {
		return value;
	}
	/** Megadja a gomb y koordinátáját
	 * getter
	 * @return y koordináta
	 */
	public int gety() {
		return y;
	}
	/** Megadja a gomb x koordinátáját
	 * getter
	 * @return x koordináta
	 */
	public int getx() {
		return x;
	}
	/** Megadja, hogy a gomb látható-e vagy sem
	 * getter
	 * @return boolean lathato_e
	 */
	public boolean lathato() {
		return lathato_e;
	}
	/** Megadja, hogy a gomb megjelölt-e vagy sem
	 * getter
	 * @return boolean megjelolt_e
	 */
	public boolean megjelolt() {
		return megjelolt_e;
	}
	/** Beállítja az aknák értékét
	 * setter
	 * @param n: n értékre állítja be
	 */
	public void setvalue(int n) {
		value = n;
	}
	
	/**Beállítja a gombok azon tulajdonságát, hogy megjelölt-e
	 * setter
	 * @param b: a b logikai értékére állítja a megjelolt_e adattagot
	 */
	public void setmegjelolt(boolean b) {
		megjelolt_e=b;
	}
	/**Beállítja a gombok azon tulajdonságát, hogy látható-e
	 * setter
	 * @param b: a b logikai értékére állítja a lathato_e adattagot
	 */
	public void setlathato(boolean b) {
		lathato_e=b;
	}

}
