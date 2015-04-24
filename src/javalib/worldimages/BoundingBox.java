package javalib.worldimages;

import java.awt.geom.Point2D;

/**
 * The class representing a bounding box of a WorldImage
 * 
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class BoundingBox {
    double tlx, tly, brx, bry;

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

    /**
     * Create a new bounding box that encompasses the combination of itself and
     * another bounding box
     * 
     * @param other
     *            -- Bounding box to attach to this bounding box
     * @return a new, extended bounding box
     */
    BoundingBox combine(BoundingBox other) {
        return new BoundingBox(Math.min(this.tlx, other.tlx), Math.min(
                this.tly, other.tly), Math.max(this.brx, other.brx), Math.max(
                this.bry, other.bry));
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
        if (px >= this.tlx && px <= this.brx && py >= this.tly
                && py <= this.bry) {
            return this;
        } else {
            return new BoundingBox(Math.min(this.tlx, px), Math.min(this.tly,
                    py), Math.max(this.brx, px), Math.max(this.bry, py));
        }
    }

    /**
     * 
     * @return the width of the bounding box
     */
    double getWidth() {
        return this.brx - this.tlx;
    }

    /**
     * 
     * @return the height of the bounding box
     */
    double getHeight() {
        return this.bry - this.tly;
    }

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
