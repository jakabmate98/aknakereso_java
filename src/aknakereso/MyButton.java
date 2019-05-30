package aknakereso;

import javax.swing.JButton;

public class MyButton extends JButton {
	private int value;
	private boolean lathato_e;
	private boolean megjelolt_e;
	private int x;
	private int y;
	
	/**A MyButton konstruktora
	 * Be�ll�tja kezd��llapotba a gombokat
	 * @param x: A gomb egyik param�tere
	 * @param y: A gomb m�sik param�tere
	 */
	public MyButton(int x, int y) {
		this.x=x;
		this.y=y;
		lathato_e = false;
		megjelolt_e = false;
		value=0;
	}
	
	/** A gomb k�r�l l�v� akn�k db sz�m�t adja meg, amennyiben -1, akkor egy akna a gomb
	 * getter
	 * @return k�rnyezetben (3*3) az akn�k sz�ma
	 */
	public int getvalue() {
		return value;
	}
	/** Megadja a gomb y koordin�t�j�t
	 * getter
	 * @return y koordin�ta
	 */
	public int gety() {
		return y;
	}
	/** Megadja a gomb x koordin�t�j�t
	 * getter
	 * @return x koordin�ta
	 */
	public int getx() {
		return x;
	}
	/** Megadja, hogy a gomb l�that�-e vagy sem
	 * getter
	 * @return boolean lathato_e
	 */
	public boolean lathato() {
		return lathato_e;
	}
	/** Megadja, hogy a gomb megjel�lt-e vagy sem
	 * getter
	 * @return boolean megjelolt_e
	 */
	public boolean megjelolt() {
		return megjelolt_e;
	}
	/** Be�ll�tja az akn�k �rt�k�t
	 * setter
	 * @param n: n �rt�kre �ll�tja be
	 */
	public void setvalue(int n) {
		value = n;
	}
	
	/**Be�ll�tja a gombok azon tulajdons�g�t, hogy megjel�lt-e
	 * setter
	 * @param b: a b logikai �rt�k�re �ll�tja a megjelolt_e adattagot
	 */
	public void setmegjelolt(boolean b) {
		megjelolt_e=b;
	}
	/**Be�ll�tja a gombok azon tulajdons�g�t, hogy l�that�-e
	 * setter
	 * @param b: a b logikai �rt�k�re �ll�tja a lathato_e adattagot
	 */
	public void setlathato(boolean b) {
		lathato_e=b;
	}

}
