package aknakereso;

import java.io.Serializable;

/**
 * A j�t�kosok tulajdons�gait t�rolja, implement�lja a Comparable-t, amelyhez a sz�ks�ges compareTo fv.-t @override-olja
 * @author Mate
 *
 */
public class Player implements Comparable<Player>, Serializable {
	private String name;
	private int time;
	/**
	 * A konstruktor�ban kapott �rt�kekkel t�lti fel a Player objektumot
	 * @param n: megadott n�v (j�t�kos �ltal be�rt)
	 * @param t: megadott id� (MyTimer.gettime())
	 */
	Player(String n, int t){
		name=n;
		time=t;
	}
	/**
	 * A j�t�kos nev�t tudhatjuk meg a getter seg�ts�g�vel
	 * @return n�v
	 */
    public String getname() {
    	return name;
    }
    /**
     * A j�t�kos idej�t tudhatjuk meg a getter seg�ts�g�vel
     * @return id�
     */
    public int gettime() {
        return time;
    }
    /**Override-olt fv. a Comparable implement�l�sa miatt
     * @return a this.time �s a param�terben megadott j�t�kos idej�t vonja ki egym�sb�l, majd adja vissza int-k�nt
     */
	@Override
	public int compareTo(Player arg0) {
		return (time - arg0.gettime());
	}
}