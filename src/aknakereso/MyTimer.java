package aknakereso;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * Az id�z�t� miatt l�trehozott oszt�ly, JLabel lesz�rmazottja
 * @author Mate
 *
 */
public class MyTimer extends JLabel{
	private int secondsPassed = 0;
	Timer myTimer = new Timer();
	/**
	 * A start fv. megh�v�sakor a run() fv. fog megh�v�dni
	 */
	TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
			setTxt(Integer.toString(secondsPassed));
		}
	};
	
	/**
	 * Az�rt volt erre a fv-re sz�ks�g, mert a run() fv-en bel�l nem volt lehet�s�g arra, hogy a this.setText(str) fv.t megh�vjuk
	 * @param str: A megadott str-re �ll�tja this Textj�t
	 */
	public void setTxt(String str) {
		this.setText(str);
	}
	
	/** Az id�z�t� eltelt id�t mutat, ezt a getTime() f�ggv�ny haszn�lat�val �rhetj�k el
	 * getter
	 * @return eltelt id�
	 */
	public int getTime() {
		return secondsPassed;
	}
	
	/**
	 * A MyTimer elind�t�s��rt felel�s fv. (a param�terekben l�that�, hogy m�sodpercenk�nt fog v�ltozni az �rt�ke a secondsPassed-nak)
	 */
	public void start() {
		myTimer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	/**
	 * A MyTimer meg�ll�t�s��rt felel�s fv.
	 */
	public void stop() {
		myTimer.cancel();
	}
	
}
