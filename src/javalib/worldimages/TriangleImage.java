package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent filled triangle images drawn by the world when drawing
 * on its <code>Canvas</code>.
 * </p>
 * <p>
 * The pinhole for the triangle is in the center of the triangle.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class TriangleImage extends WorldImage {
    public Posn p1;
    public Posn p2;
    public Posn p3;
    public OutlineMode fill;
    public Color color;
    private Polygon poly;

    /**
     * A full constructor for this triangle image.
     * 
     * @param p1
     *            the first point of this triangle
     * @param p2
     *            the second point of this triangle
     * @param p3
     *            the third point of this triangle
     * @param color
     *            the color for this image
     */
    public TriangleImage(Posn p1, Posn p2, Posn p3, OutlineMode fill,
            Color color) {
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = color;
        this.fill = fill;

        // find the center of the triangle
        int centerX = Math.min(this.p1.x, Math.min(this.p2.x, this.p3.x))
                + (this.getWidth() / 2);
        int centerY = Math.min(this.p1.y, Math.min(this.p2.y, this.p3.y))
                + (this.getHeight() / 2);

        int[] xCoord = new int[] { p1.x - centerX, p2.x - centerX,
                p3.x - centerX };
        int[] yCoord = new int[] { p1.y - centerY, p2.y - centerY,
                p3.y - centerY };
        this.poly = new Polygon(xCoord, yCoord, 3);
        
    }
    @Override
    protected BoundingBox getBB(AffineTransform t) {
        Point2D p1 = WorldImage.transformPosn(t, this.poly.xpoints[0], this.poly.ypoints[0]);
        Point2D p2 = WorldImage.transformPosn(t, this.poly.xpoints[1], this.poly.ypoints[1]);
        Point2D p3 = WorldImage.transformPosn(t, this.poly.xpoints[2], this.poly.ypoints[2]);
        return new BoundingBox(p1, p2).add(p3);
    }

    public TriangleImage(Posn p1, Posn p2, Posn p3, String fill, Color color) {
        this(p1, p2, p3, OutlineMode.fromString(fill), color);
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

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(color);
        // draw the triangle
        if (this.fill == OutlineMode.OUTLINE) {
            g.draw(this.poly);
        } else if (this.fill == OutlineMode.SOLID) {
            g.fill(this.poly);
        }

        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce the width of this triangle image
     * 
     * @return the width of this image
     */
    public int getWidth() {
        return Math.max(p1.x, Math.max(p2.x, p3.x))
                - Math.min(p1.x, Math.min(p2.x, p3.x));
    }

    /**
     * Produce the height of this triangle image
     * 
     * @return the height of this image
     */
    public int getHeight() {
        return Math.max(p1.y, Math.max(p2.y, p3.y))
                - Math.min(p1.y, Math.min(p2.y, p3.y));
    }

    /**
     * Produce a <code>String</code> representation of this triangle image
     */
    public String toString() {
        return "new TriangleImage(this.color = " + this.color.toString()
                + "\nthis.p1 = (" + this.p1.x + ", " + this.p1.y
                + "), \nthis.p2 = (" + this.p2.x + ", " + this.p2.y
                + "), \nthis.p3 = (" + this.p3.x + ", " + this.p3.y + "))\n";
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
        indent = indent + " ";
        return classNameString(indent, "TriangleImage")
                + colorString(indent, this.color) + "\n" + indent
                + "this.p1 = (" + this.p1.x + ", " + this.p1.y + "), \n"
                + indent + "this.p2 = (" + this.p2.x + ", " + this.p2.y
                + "), \n" + indent + "this.p3 = (" + this.p3.x + ", "
                + this.p3.y + "))\n";
    }

    /**
     * Is this <code>TriangleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof TriangleImage) {
            TriangleImage that = (TriangleImage) o;
            return this.p1.x == that.p1.x && this.p1.y == that.p1.y
                    && this.p2.x == that.p2.x && this.p2.y == that.p2.y
                    && this.p3.x == that.p3.x && this.p3.y == that.p3.y
                    && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.p1.x * this.p1.y + this.p2.x
                * this.p2.y + this.p3.x * this.p3.y;
    }
}