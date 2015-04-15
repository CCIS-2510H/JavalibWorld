package javalib.worldimages;

/**
 * To represent a point on the drawing <code>WorldCanvas</code>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
class DPosn {
    public double x;
    public double y;

    public DPosn(double botDeltaX, double botDeltaY) {
        this.x = botDeltaX;
        this.y = botDeltaY;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DPosn))
            return false;
        DPosn that = (DPosn) other;
        return this.x == that.x && this.y == that.y;
    }

    public Posn asPosn() {
        return new Posn((int) Math.round(this.x), (int) Math.round(this.y));
    }

    @Override
    public String toString() {
        return String.format("new DPosn(x = %d, y = %d)", this.x, this.y);
    }
}