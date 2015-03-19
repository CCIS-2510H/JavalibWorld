package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent line images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class LineImage extends WorldImage {

    /**
     * the ending point of this line the starting point is (0, 0)
     */
    public Posn endPoint;

    /** The color of the line */
    public Color color;

    /**
     * A full constructor for this line image.
     * 
     * @param startPoint
     *            the starting point of this line
     * @param endPoint
     *            the ending point of this line
     * @param color
     *            the color for this image
     */
    public LineImage(Posn endPoint, Color color) {
        super();
        this.endPoint = endPoint;
        this.color = color;

    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
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

    /**
     * Produce the width of this image (of its bounding box)
     * 
     * @return the width of this image
     */
    public int getWidth() {
        return Math.abs(this.endPoint.x);
    }

    /**
     * Produce the height of this image (of its bounding box)
     * 
     * @return the height of this image
     */
    public int getHeight() {
        return Math.abs(this.endPoint.y);
    }

    /**
     * Produce a <code>String</code> representation of this triangle image
     */
    public String toString() {
        return "new LineImage(this.color = " + this.color.toString()
                + "\nthis.endPoint = (" + this.endPoint.x + ", "
                + this.endPoint.y + "))\n";
    }

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, "LineImage")
                + colorString(indent, this.color) + "\n" + indent
                + "this.endPoint = (" + this.endPoint.x + ", "
                + this.endPoint.y + "))\n";
    }

    /**
     * Is this <code>LineImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof LineImage) {
            LineImage that = (LineImage) o;
            return this.endPoint.x == that.endPoint.x
                    && this.endPoint.y == that.endPoint.y
                    && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.endPoint.x + this.endPoint.y;
    }
}