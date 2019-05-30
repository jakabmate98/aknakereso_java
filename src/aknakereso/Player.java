package aknakereso;

import java.io.Serializable;

/**
 * A játékosok tulajdonságait tárolja, implementálja a Comparable-t, amelyhez a szükséges compareTo fv.-t @override-olja
 * @author Mate
 *
 */
public class Player implements Comparable<Player>, Serializable {
	private String name;
	private int time;
	/**
	 * A konstruktorában kapott értékekkel tölti fel a Player objektumot
	 * @param n: megadott név (játékos által beírt)
	 * @param t: megadott idõ (MyTimer.gettime())
	 */
	Player(String n, int t){
		name=n;
		time=t;
	}
	/**
	 * A játékos nevét tudhatjuk meg a getter segítségével
	 * @return név
	 */
    public String getname() {
    	return name;
    }
    /**
     * A játékos idejét tudhatjuk meg a getter segítségével
     * @return idõ
     */
    public int gettime() {
        return time;
    }
    /**Override-olt fv. a Comparable implementálása miatt
     * @return a this.time és a paraméterben megadott játékos idejét vonja ki egymásból, majd adja vissza int-ként
     */
	@Override
	public int compareTo(Player arg0) {
		return (time - arg0.gettime());
	}
}