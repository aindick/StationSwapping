import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Swapper implements Runnable {
        private static int numSwaps = 10;
        private static ReentrantLock lock = new ReentrantLock();
        private Grid grid;
        private final Grid startGrid;
        Random r = new Random();
        ArrayList<Swap> swaps = new ArrayList<>();

    public Swapper(Grid g) {
            grid = g;
            startGrid = g;
        }

        private void swap() {
            int currentNumSwaps = numSwaps;
            Grid solutionGrid = EndSolution.getGridEndSolution();
            ArrayList<Swap> solutionSwaps = EndSolution.getSwaps();

            grid = startGrid;
            for(Swap swap : solutionSwaps) {
                grid.swap(swap.p1, swap.p2);
                swaps.add(swap);
            }
                grid = new Grid(solutionGrid);
                swaps.addAll(solutionSwaps);

            for(int i = swaps.size(); i < currentNumSwaps; i++) {
                CoordinatePoint point1, point2;
                for(;;) {
                    point1 = new CoordinatePoint(r.nextInt(grid.getStations().length), r.nextInt(grid.getStations()[0].length));
                    point2 = new CoordinatePoint(r.nextInt(grid.getStations().length), r.nextInt(grid.getStations()[0].length));
                    if(point1.x != point2.x || point1.y != point2.y) {
                        break;
                    }
                }
                grid.swap(point1, point2);
                Swap recent = new Swap(point1, point2);
                swaps.add(recent);
                EndSolution.setGridUnchecked(grid);

                //Check if the solution is better than the current solution, and then swap if it is.
                double metric = grid.getMetric();
                if(metric > EndSolution.getSolutionMetric()) {
                    EndSolution.setEndSolution(metric, swaps, grid);
                    //If the solution is worse, reverse the swap.
                } else if (r.nextInt(10) > 3){
                    grid.swap(point2, point1);
                    swaps.remove(recent);
                }
            }
            //Reset the swap list for the next iteration
            swaps = new ArrayList<>();
        }

        public void increaseNumSwaps() {
            for(;;) {
                lock.lock();
                    try {
                        int size = EndSolution.getSwaps().size();

                        double maxSwaps = (size == 0) ? 1 : size * (Math.sqrt(500 / size) + 2.1);
                        if (numSwaps <= maxSwaps) {
                            numSwaps++;
                        } else {
                            numSwaps = (int) maxSwaps;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                    break;
                }
            }


        @Override
        public void run() {
            for(;;) {
                swap();
                increaseNumSwaps();
            }
        }}

