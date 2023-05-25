package javalib.worldimages;

/**
 * To represent a point on the drawing <code>WorldCanvas</code>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public class Posn {
    public final int x;
    public final int y;

    public Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Posn))
            return false;
        Posn that = (Posn) other;
        return this.x == that.x && this.y == that.y;
    }

    public Posn offset(int dx, int dy) {
        return new Posn(this.x + dx, this.y + dy);
    }

    @Override
    public int hashCode() {
        return 10000 * this.x + this.y;
    }

    @Override
    public String toString() {
        return String.format("new Posn(x = %d, y = %d)", this.x, this.y);
    }

    String coords() { return String.format("(%d, %d)", this.x, this.y); }
}