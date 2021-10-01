import java.awt.*;

public class Swap {
    public final CoordinatePoint p1;
    public final CoordinatePoint p2;

    public Swap(CoordinatePoint p1, CoordinatePoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            Swap swap = (Swap) obj;
            return swap.p1 == this.p1 && swap.p2 == this.p2;
        }
        return false;
    }
}
