
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
public class Grid {
    private ReentrantLock lock = new ReentrantLock();
    public static final int Side1  = 10 ;
    public static final int Side2 = 10;
    private final int numStations = 32;
    private Stations[][] s;
    public static final int maxWeight = 100;


//Generate the floor/gird.
    public Grid() {
        s = new Stations[Side1][Side2];
        initialize();
    }

    //Duplicate a starting floor/grid.
    public Grid(Grid grid) {
        s = new Stations[grid.getStations().length][grid.getStations()[0].length];
        for(int i = 0; i < grid.getStations().length; i++) {
            for(int j = 0; j < grid.getStations()[i].length; j++) {
                s[i][j] = grid.getStations()[i][j];
            }
        }
    }

    //Within the grid, set 32 stations with any random weight.
    private void initialize() {

            lock.lock();
                Random r = new Random();
                for (int i = 0; i < numStations; i++) {
                    for (; ; ) {
                        int x = r.nextInt(s.length);
                        int y = r.nextInt(s[0].length);
                        if (s[x][y] == null) {
                            s[x][y] = new Stations(r.nextInt(maxWeight - 1) + 1);
                            break;
                        }
                    }
                }
                lock.unlock();

        }


    public Stations[][] getStations() {

        return s;
    }

    //Swap two stations.
    public void swap(CoordinatePoint p1, CoordinatePoint p2) {

                lock.lock();
                Stations t = s[p1.x][p1.y];
                s[p1.x][p1.y] = s[p2.x][p2.y];
                s[p2.x][p2.y] = t;
                lock.unlock();

        }



    public double getMetric() {
        double total;
        for(;;) {
            lock.lock();
                total = 0;
                for (int i = 0; i < s.length; i++) {
                    for (int j = 0; j < s[i].length; j++) {

                        if (s[i][j] != null) {

                            for (int k = 0; k < s.length; k++) {
                                for (int l = 0; l < s[k].length; l++) {

                                    if (s[k][l] != null && !(i == k && j == l)) {

                                        double distance = Math.pow((i - k), 2) + Math.pow((j - l), 2);

                                        double totalWeight = s[i][j].getStationWeight() * s[k][l].getStationWeight();

                                        double metric = (totalWeight / distance) / 100; //The total metric of the floor/grid is (((Station 1's weight * Station 2's weight) / distance) / 100).

                                        total += metric;
                                    }
                                }
                            }
                        }
                    }
                }
                lock.unlock();
                break;
            }

        return total;
    }


    public void unlock() {
    }
}
