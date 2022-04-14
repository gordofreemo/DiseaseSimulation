package DiseaseSim;

/**
 * Shame java doesn't have this built-in.
 * Essentially just used to make generating the random grid easy, in a separate
 * file to not clog up AgentManager class
 * @param <X> - first element of the tuple (X,Y)
 * @param <Y> - second element of the tuple (X,Y)
 */

public class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}
