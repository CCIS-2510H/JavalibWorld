package javalib.worldimages;

/**
 * To represent a point on the drawing <CODE>WorldCanvas</CODE> or
 * <CODE>AppletCanvaas</CODE>
 * 
 * @author Viera K.
 * @since August 2, 2007
 */
public class Posn {
    public int x;
    public int y;

    public Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Posn)) return false;
        Posn that = (Posn)other;
        return this.x == that.x && this.y == that.y;
    }
    @Override
    public String toString() {
        return String.format("new Posn(x = %d, y = %d)", this.x, this.y);
    }
}