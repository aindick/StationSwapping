import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public class EndSolution {

    private static ReentrantLock gridLockUnchecked = new ReentrantLock();
    private static ReentrantLock solutionLock = new ReentrantLock();

    private static ArrayList<Swap> swaps = null;

    private static double metric = 0;

    private static Grid grid = null;

    private static Grid gridUnchecked = null;

    public static void setEndSolution(double newMetric, ArrayList<Swap> newSwaps, Grid newGrid) {
            solutionLock.lock();

                try {
                    //Stores the swaps, the metric, and the floor/grid.
                    //If there is only 1 swap, just return them all.
                    int indexEnd = (2 * newSwaps.size()) / 3;
                    if(indexEnd != 0) swaps = new ArrayList<>(newSwaps.subList(0, indexEnd));
                    else swaps = newSwaps;
                    metric = newMetric;
                    grid = newGrid;
                    System.out.println("Solution:" + metric);
                    Displaying.interrupt(grid);
                } catch (Exception e) {
                    System.err.println(e);
                } finally {
                    solutionLock.unlock();
                }

            }



    public static Grid getGridEndSolution() {
                Grid r;
                solutionLock.lock();
                r = grid;
                solutionLock.unlock();
                return r;
            }




    public static Grid getGridUnchecked() {
            Grid re;
            gridLockUnchecked.lock();;
             re = gridUnchecked;
             gridLockUnchecked.unlock();

             return re;
    }

    public static void setGridUnchecked(Grid g) {

                gridLockUnchecked.lock();
                gridUnchecked = g;
                gridUnchecked.unlock();

            }



    public static ArrayList<Swap> getSwaps() {
            ArrayList<Swap> s;

                solutionLock.lock();
                s = swaps;
                solutionLock.unlock();
                return s;
            }




    public static double getSolutionMetric() {
                double m;
                solutionLock.lock();
                m = metric;
                solutionLock.unlock();
                return m;



    }}

