import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
public class Main {

    private static boolean start = false;
    private static final ReentrantLock lock = new ReentrantLock();
    public static void setStart(boolean a) {

                lock.lock();
                start = a;
                lock.unlock();


        }

    public static boolean getStart(boolean b) {

                lock.lock();
                start = b;
                lock.unlock();

        return b;


    }
    public static void main(String[] args) {
        Grid gr = new Grid();
        Displaying di = new Displaying(gr);
        Executor ex= Executors.newSingleThreadExecutor();
        ex.execute(di);
        double startAffinity = gr.getMetric();
        EndSolution.setEndSolution(startAffinity, new ArrayList<>(), gr);
        for(;;) {
            if(getStart(false)) break;
        }
        for(int i = 0; i < Integer.parseInt(args[0]) ; i++) {
            Swapper sw = new Swapper(new Grid(gr));
            Executor exe = Executors.newSingleThreadExecutor();
            exe.execute(sw);
        }
    }

}
