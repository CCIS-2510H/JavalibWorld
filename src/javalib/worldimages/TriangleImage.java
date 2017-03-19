package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent triangle images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public final class TriangleImage extends WorldImage {

    /** the first point of the triangle */
    public Posn p1;

    /** the second point of the triangle */
    public Posn p2;

    /** the third point of the triangle */
    public Posn p3;

    /** the outline mode of the triangle - outline/solid */
    public OutlineMode fill;

    /** the color of the triangle */
    public Color color;

    private Polygon poly;

    /**
     * A full constructor for this triangle image. The points are relative to
     * each other
     * 
     * @param p1
     *            -- the first point of this triangle
     * @param p2
     *            -- the second point of this triangle
     * @param p3
     *            -- the third point of this triangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public TriangleImage(Posn p1, Posn p2, Posn p3, OutlineMode fill,
            Color color) {
        super(1);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = color;
        this.fill = fill;

        // find the center of the triangle
        int centerX = (int) Math.round(Math.min(this.p1.x,
                Math.min(this.p2.x, this.p3.x))
                + (this.getWidth() / 2));
        int centerY = (int) Math.round(Math.min(this.p1.y,
                Math.min(this.p2.y, this.p3.y))
                + (this.getHeight() / 2));

        int[] xCoord = new int[] { p1.x - centerX, p2.x - centerX,
                p3.x - centerX };
        int[] yCoord = new int[] { p1.y - centerY, p2.y - centerY,
                p3.y - centerY };
        this.poly = new Polygon(xCoord, yCoord, 3);

    }

    /**
     * A full constructor for this triangle image. The points are relative to
     * each other
     * 
     * @param p1
     *            -- the first point of this triangle
     * @param p2
     *            -- the second point of this triangle
     * @param p3
     *            -- the third point of this triangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public TriangleImage(Posn p1, Posn p2, Posn p3, String fill, Color color) {
        this(p1, p2, p3, OutlineMode.fromString(fill), color);
    }
    @Override
    int numKids() {
        return 0;
    }
    @Override
    WorldImage getKid(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        Point2D p1 = WorldImage.transformPosn(t, this.poly.xpoints[0],
                this.poly.ypoints[0]);
        Point2D p2 = WorldImage.transformPosn(t, this.poly.xpoints[1],
                this.poly.ypoints[1]);
        Point2D p3 = WorldImage.transformPosn(t, this.poly.xpoints[2],
                this.poly.ypoints[2]);
        return BoundingBox.containing(p1, p2, p3);
    }
    
    @Override
    protected void drawStackUnsafe(Graphics2D g) {
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
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.drawStackUnsafe(g);
    }

    @Override
    public double getWidth() {
        return Math.max(p1.x, Math.max(p2.x, p3.x))
                - Math.min(p1.x, Math.min(p2.x, p3.x));
    }

    @Override
    public double getHeight() {
        return Math.max(p1.y, Math.max(p2.y, p3.y))
                - Math.min(p1.y, Math.min(p2.y, p3.y));
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(");
        stack.push(
                new FieldsWLItem(
                        new ImageField("p1", this.p1),
                        new ImageField("p2", this.p2),
                        new ImageField("p3", this.p3),
                        new ImageField("fill", this.fill),
                        new ImageField("color", this.color)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist) {
        if (other instanceof TriangleImage) {
            TriangleImage that = (TriangleImage)other;
            return this.fill == that.fill && this.p1.x == that.p1.x
                    && this.p1.y == that.p1.y && this.p2.x == that.p2.x
                    && this.p2.y == that.p2.y && this.p3.x == that.p3.x
                    && this.p3.y == that.p3.y && this.color.equals(that.color);
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.p1.x * this.p1.y + this.p2.x
                * this.p2.y + this.p3.x * this.p3.y;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new TriangleImage(this.p1, this.p2, this.p3, this.fill,
                this.color);
        i.pinhole = p;
        return i;
    }
}