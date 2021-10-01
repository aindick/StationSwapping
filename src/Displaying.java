import java.util.concurrent.locks.ReentrantLock;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;


public class Displaying extends JFrame implements Runnable {

    private final int COLOR = (255 * 3) / Grid.maxWeight;
    private final int STATIONSIZE = 80;
    private static ReentrantLock lock = new ReentrantLock();
    private static boolean interruptFlag = false;
    private static Grid g;
    private JPanel window, panelSolution, labelTop, labelBottom;
    private JLabel solutionLabel, solutionMetric;
    private DrawPanel[][] solutionGrid = new DrawPanel[Grid.Side1][Grid.Side2];
    private DecimalFormat df = new DecimalFormat("#.###");

    public interface Drawable {
        void draw(Graphics g);
    }

    public class DrawPanel extends JPanel {
        private Drawable drawable;

        public DrawPanel() {}

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (drawable != null) {
                drawable.draw(g);
            }
        }
    }

    public class Rectangle implements Drawable {
        private int x, y;
        private JPanel surface;
        private Color color;
        public Rectangle(int x, int y, JPanel surface, Color color) {
            this.x = x;
            this.y = y;
            this.surface = surface;
            this.color = color;
        }
        @Override
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, surface.getWidth(), surface.getHeight());
        }
    }

    public Displaying(Grid gr) {
        initComponents(gr);
    }

    private void initComponents(Grid gr) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setSize((Grid.Side1 * STATIONSIZE * 5) + STATIONSIZE, (Grid.Side2 * STATIONSIZE) + STATIONSIZE);

        window = new JPanel();
        window.setLayout(new BorderLayout());
        window.setSize(this.getWidth(), this.getHeight());

        labelTop = new JPanel();
        labelTop.setLayout(new BorderLayout());

        solutionLabel = new JLabel("Solution");
        solutionLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        solutionLabel.setBorder(new EmptyBorder(window.getHeight()/20, window.getHeight()/2, window.getHeight()/20, 50));



        labelTop.add(solutionLabel, BorderLayout.LINE_START);
        window.add(labelTop, BorderLayout.PAGE_START);
        labelBottom = new JPanel();
        labelBottom.setLayout(new BorderLayout());

        solutionMetric = new JLabel("Affinity is: " + df.format(gr.getMetric()));
        solutionMetric.setFont(new Font("Times New Roman", Font.BOLD, 30));
        solutionMetric.setBorder(new EmptyBorder(window.getHeight()/20, window.getHeight()/2, window.getHeight()/20, 50));

        labelBottom.add(solutionMetric, BorderLayout.LINE_START);

        window.add(labelTop, BorderLayout.PAGE_START);
        window.add(labelBottom, BorderLayout.PAGE_END);

        panelSolution = new JPanel();

        GridLayout gridFloor = new GridLayout(Grid.Side1, Grid.Side2, 2, 2);
        panelSolution.setLayout(gridFloor);
        panelSolution.setPreferredSize(new Dimension(Grid.Side1 * STATIONSIZE, Grid.Side2 * STATIONSIZE));
        panelSolution.setBackground(Color.WHITE);


        for (int i = 0; i < Grid.Side1; i++) {
            for (int j = 0; j < Grid.Side2; j++) {
                DrawPanel panel = new DrawPanel();
                panel.setPreferredSize(new Dimension(STATIONSIZE, STATIONSIZE));
                solutionGrid[i][j] = panel;
                panelSolution.add(panel);
            }
        }
        for (int i = 0; i < Grid.Side1; i++) {
            for (int j = 0; j < Grid.Side2; j++) {
                DrawPanel panel = new DrawPanel();
                panel.setPreferredSize(new Dimension(STATIONSIZE, STATIONSIZE));


            }
        }
        window.add(panelSolution, BorderLayout.LINE_START);


        this.add(window);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }

    private void draw(Grid grid, boolean isEndSolution) {
        if(grid != null) {
            Stations[][] stations = grid.getStations();
            for (int i = 0; i < stations.length; i++) {
                for (int j = 0; j < stations[i].length; j++) {
                    if (isEndSolution) {
                        DrawPanel panel = solutionGrid[i][j];
                        panel.setDrawable(new Rectangle(0, 0, panel, getColor(stations[i][j])));
                    }
                }
            }
            if(isEndSolution) {
                solutionMetric.setText("Affinity is: " + df.format(grid.getMetric()));


            }
        }
    }

    private Color getColor(Stations station) {
        if(station == null) {
            return new Color(170, 14, 170);
        }
        int weight = station.getStationWeight();
        int r = 170;
        int g = 14;
        int b = 170;
        for(int i = 0; i < weight; i++) {
            if(r > COLOR) {
                r -= COLOR;
            } else if (r != 0) {
                int decrement = COLOR - r;
                r = 0;
                g -= decrement;
            } else {
                if(g > COLOR) {
                    g -= COLOR;
                } else if(g != 0) {
                    int decrement = COLOR - g;
                    g = 0;
                    b -= decrement;
                } else {
                    if(b > COLOR) {
                        b -= COLOR;
                    } else {
                        b = 0;
                    }
                }
            }
        }
        return new Color(r, g, b);
    }

    public static void interrupt(Grid gr) {
        for(;;) {
                lock.lock();
                interruptFlag = true;
                g = new Grid(gr);
                lock.unlock();
                break;
            }
        }


    private boolean isInterrupted() {
        boolean ret;
        for(;;) {
                lock.lock();
                ret = interruptFlag;
                interruptFlag = false;
                lock.unlock();
                break;
            }

        return ret;
    }

    @Override
    public void run() {
        this.setVisible(true);
        Main.setStart(true);
        for(;;) {
            try {
                if (isInterrupted()) {
                    draw(g, true);
                }
                draw(EndSolution.getGridUnchecked(), false);
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
