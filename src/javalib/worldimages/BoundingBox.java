package javalib.worldimages;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * The class representing a bounding box of a WorldImage
 * 
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class BoundingBox {
    private double tlx, tly, brx, bry;

    static BoundingBox containing(Point2D... p) {
        if (p.length == 0) {
            throw new IllegalArgumentException("Can't create a bounding box without at least one point");
        } else {
            BoundingBox b = new BoundingBox(p[0], p[0]);
            for (int i = 1; i < p.length; i++) {
                b.combineWith(p[i]);
            }
            return b;
        }
    }
    
    BoundingBox(BoundingBox bb) {
        this(bb.tlx, bb.tly, bb.brx, bb.bry);
    }

    BoundingBox(Rectangle bounds) {
        this(bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY());
    }
    
    /**
     * Create a bounding box
     * 
     * @param tl
     *            -- Top left corner of the box
     * @param br
     *            -- Bottom right corner of the box
     */
    BoundingBox(Posn tl, Posn br) {
        this(tl.x, tl.y, br.x, br.y);
    }

    /**
     * Create a bounding box
     * 
     * @param tl
     *            -- Top left corner of the box
     * @param br
     *            -- Bottom right corner of the box
     */
    BoundingBox(Point2D tl, Point2D br) {
        this(tl.getX(), tl.getY(), br.getX(), br.getY());
    }

    /**
     * Create a bounding box
     * 
     * @param tlx
     *            -- Top left x coordinate of the box
     * @param tly
     *            -- Top left y coordinate of the box
     * @param brx
     *            -- Bottom right x coordinate of the box
     * @param bry
     *            -- Bottom right y coordinate of the box
     */
    BoundingBox(double tlx, double tly, double brx, double bry) {
        this.tlx = Math.min(tlx, brx);
        this.tly = Math.min(tly, bry);
        this.brx = Math.max(tlx, brx);
        this.bry = Math.max(tly, bry);
    }


    boolean contains(Posn p) {
        return this.contains(p.x, p.y);
    }
    boolean contains(Point2D p) {
        return this.contains(p.getX(), p.getY());
    }
    boolean contains(double px, double py) {
        return this.tlx <= px && px <= this.brx
            && this.tly <= py && py <= this.bry;
    }
    
    /**
     * Create a new bounding box that encompasses the combination of itself and
     * another bounding box
     * 
     * @param other
     *            -- Bounding box to attach to this bounding box
     * @return a new, extended bounding box
     */
    BoundingBox combine(BoundingBox other) {
        if (this.tlx <= other.tlx && other.brx <= this.brx &&
            this.tly <= other.tly && other.bry <= this.bry) {
            return this;            
        } else if (other.tlx <= this.tlx && this.brx <= other.brx &&
            other.tly <= this.tly && this.bry <= other.bry) {
            return other;
        } else {
            return new BoundingBox(
                Math.min(this.tlx, other.tlx), 
                Math.min(this.tly, other.tly), 
                Math.max(this.brx, other.brx), 
                Math.max(this.bry, other.bry));
        }
    }
    /**
     * Expands the current bounding box to encompass the given box
     * @param other -- Bounding box to include in this bounding box
     */
    void combineWith(BoundingBox other) {
        this.tlx = Math.min(this.tlx, other.tlx);
        this.tly = Math.min(this.tly, other.tly);
        this.brx = Math.max(this.brx, other.brx);
        this.bry = Math.max(this.bry, other.bry);
    }
    void combineWith(Posn p) {
        this.combineWith(p.x, p.y);
    }
    void combineWith(Point2D p) {
        this.combineWith(p.getX(), p.getY());
    }
    void combineWith(double px, double py) {
        this.tlx = Math.min(this.tlx, px);
        this.tly = Math.min(this.tly, py);
        this.brx = Math.max(this.brx, px);
        this.bry = Math.max(this.bry, py);
    }
    
    BoundingBox translated(double dx, double dy) {
        return new BoundingBox(this.tlx + dx, this.tly + dy, this.brx + dx, this.bry + dy);
    }

    
    /**
     * Extend the bounding box to encompass a new x and y coordinate. If the X
     * and Y coordinate are already within the box, return itself
     * 
     * @param p
     *            -- (x,y) coordinate
     * @return a new, extended Bounding Box
     */
    BoundingBox add(Posn p) {
        return this.add(p.x, p.y);
    }

    /**
     * Extend the bounding box to encompass a new x and y coordinate. If the X
     * and Y coordinate are already within the box, return itself
     * 
     * @param p
     *            -- (x,y) coordinate
     * @return a new, extended Bounding Box
     */
    BoundingBox add(Point2D p) {
        return this.add(p.getX(), p.getY());
    }

    /**
     * Extend the bounding box to encompass a new x and y coordinate. If the X
     * and Y coordinate are already within the box, return itself
     * 
     * @param px
     *            -- X coordinate
     * @param py
     *            -- Y coordinate
     * @return a new, extended Bounding Box
     */
    BoundingBox add(double px, double py) {
        if (this.contains(px, py)) {
            return this;
        } else {
            BoundingBox ans = new BoundingBox(this);
            ans.combineWith(px, py);
            return ans;
        }
    }

    /**
     * @return the width of the bounding box
     */
    double getWidth() {
        return this.brx - this.tlx;
    }

    /**
     * @return the height of the bounding box
     */
    double getHeight() {
        return this.bry - this.tly;
    }
    double getTlx() { return this.tlx; }
    double getTly() { return this.tly; }
    double getBrx() { return this.brx; }
    double getBry() { return this.bry; }
    double getCenterX() { return (this.tlx + this.bry) / 2.0; }
    double getCenterY() { return (this.tly + this.bry) / 2.0; }

    @Override
    public String toString() {
        return String.format("BB((%f,%f)-(%f,%f))", this.tlx, this.tly,
                this.brx, this.bry);
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoundingBox)) return false;
        BoundingBox other = (BoundingBox)obj;
        return this.tlx == other.tlx && this.tly == other.tly
            && this.brx == other.brx && this.bry == other.bry;
    }
    @Override
    public int hashCode() {
        return (int)(this.tlx * 37.0 + this.tly * 43.0 + this.brx * 91.0 + this.bry * 103.0);
    }
}
