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

    public DPosn(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DPosn))
            return false;
        DPosn that = (DPosn) other;
        return Math.abs(this.x - that.x) < 0.00000001 && 
                Math.abs(this.y - that.y) < 0.00000001;
    }
    
    @Override
    public int hashCode() {
        return (int) this.x * 96 + (int) this.y * 35;
    }

    public Posn asPosn() {
        return new Posn((int) Math.round(this.x), (int) Math.round(this.y));
    }

    @Override
    public String toString() {
        return String.format("new DPosn(x = %d, y = %d)", this.x, this.y);
    }
}