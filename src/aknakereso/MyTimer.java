package aknakereso;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * Az idõzítõ miatt létrehozott osztály, JLabel leszármazottja
 * @author Mate
 *
 */
public class MyTimer extends JLabel{
	private int secondsPassed = 0;
	Timer myTimer = new Timer();
	/**
	 * A start fv. meghívásakor a run() fv. fog meghívódni
	 */
	TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
			setTxt(Integer.toString(secondsPassed));
		}
	};
	
	/**
	 * Azért volt erre a fv-re szükség, mert a run() fv-en belül nem volt lehetõség arra, hogy a this.setText(str) fv.t meghívjuk
	 * @param str: A megadott str-re állítja this Textjét
	 */
	public void setTxt(String str) {
		this.setText(str);
	}
	
	/** Az idõzítõ eltelt idõt mutat, ezt a getTime() függvény használatával érhetjük el
	 * getter
	 * @return eltelt idõ
	 */
	public int getTime() {
		return secondsPassed;
	}
	
	/**
	 * A MyTimer elindításáért felelõs fv. (a paraméterekben látható, hogy másodpercenként fog változni az értéke a secondsPassed-nak)
	 */
	public void start() {
		myTimer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	/**
	 * A MyTimer megállításáért felelõs fv.
	 */
	public void stop() {
		myTimer.cancel();
	}
	
}
