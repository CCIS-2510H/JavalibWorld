package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;

/**
 * <p>Copyright 2015 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent line images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public final class LineImage extends WorldImage {

    /**
     * the ending point of this line. the starting point is (0, 0)
     */
    public Posn endPoint;

    /** The color of the line */
    public Color color;

    /**
     * A full constructor for this line image. The starting point is the origin.
     * 
     * @param endPoint
     *            -- the ending point of this line
     * @param color
     *            -- the color for this line
     */
    public LineImage(Posn endPoint, Color color) {
        super();
        this.endPoint = endPoint;
        this.color = color;

    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        Point2D end1 = WorldImage.transformPosn(t, -this.endPoint.x / 2,
                -this.endPoint.y / 2);
        Point2D end2 = WorldImage.transformPosn(t, this.endPoint.x / 2,
                this.endPoint.y / 2);
        return new BoundingBox(end1, end2);
    }

    @Override
    public void draw(Graphics2D g) {
        if (color == null)
            color = new Color(0, 0, 0);

        Posn midpoint = new Posn(this.endPoint.x / 2, this.endPoint.y / 2);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(color);
        // draw the object
        g.draw(new Line2D.Double(-midpoint.x, -midpoint.y, midpoint.x,
                midpoint.y));
        // reset the original paint
        g.setPaint(oldPaint);
    }

    @Override
    public int getWidth() {
        return Math.abs(this.endPoint.x);
    }

    @Override
    public int getHeight() {
        return Math.abs(this.endPoint.y);
    }

    /**
     * Produce a <code>String</code> representation of this Line image
     */
    public String toString() {
        return className(this) + "this.endPoint = (" + this.endPoint.x + ", "
                + this.endPoint.y + "),\n" + colorString(this.color) + ")";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.endPoint = ("
                + this.endPoint.x + ", " + this.endPoint.y + "),"
                + colorString(indent, this.color) + ")";
    }

    /**
     * Is this <code>LineImage</code> same as that <code>LineImage</code>?
     */
    public boolean same(LineImage that) {
        return this.endPoint.x == that.endPoint.x
                && this.endPoint.y == that.endPoint.y
                && this.color.equals(that.color);
    }

    /**
     * Is this <code>LineImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof LineImage && this.same((LineImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.endPoint.x + this.endPoint.y;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new LineImage(this.endPoint, this.color);
        i.pinhole = p;
        return i;
    }
}