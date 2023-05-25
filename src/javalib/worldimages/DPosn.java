package javalib.worldimages;

/**
 * To represent a point on the drawing <code>WorldCanvas</code>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
class DPosn {
    private static final double EPSILON = 10e-7;
    public final double x;
    public final double y;

    public DPosn(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DPosn))
            return false;
        DPosn that = (DPosn) other;
        return Math.abs(this.x - that.x) < EPSILON && 
                Math.abs(this.y - that.y) < EPSILON;
    }
    
    @Override
    public int hashCode() {
        return (int) (this.x * 2939.0 + this.y);
    }

    public Posn asPosn() {
        return new Posn((int) Math.round(this.x), (int) Math.round(this.y));
    }

    @Override
    public String toString() {
        return String.format("new DPosn(x = %f, y = %f)", this.x, this.y);
    }
}