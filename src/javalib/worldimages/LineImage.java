package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Stack;

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

    /** the ending point of this line. the starting point is (0, 0) */
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
        super(1);
        this.endPoint = endPoint;
        this.color = color;

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
        Point2D end1 = WorldImage.transformPosn(t, -this.endPoint.x / 2.0,
                -this.endPoint.y / 2.0);
        Point2D end2 = WorldImage.transformPosn(t, this.endPoint.x / 2.0,
                this.endPoint.y / 2.0);
        return new BoundingBox(end1, end2);
    }
    
    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        DPosn midpoint = new DPosn(this.endPoint.x / 2.0, this.endPoint.y / 2.0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        if (color != null)
            g.setPaint(color);
        else
            g.setPaint(Color.BLACK);
        // draw the object
        g.draw(new Line2D.Double(-midpoint.x, -midpoint.y, midpoint.x,
                midpoint.y));
        // reset the original paint
        g.setPaint(oldPaint);
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.drawStackUnsafe(g);
    }

    @Override
    public double getWidth() {
        return Math.abs(this.endPoint.x);
    }

    @Override
    public double getHeight() {
        return Math.abs(this.endPoint.y);
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.endPoint = ").append(this.endPoint.coords()).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("color", this.color)));
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof LineImage) {
            LineImage that = (LineImage) other;
            return this.endPoint.x == that.endPoint.x && this.endPoint.y == that.endPoint.y
                    && this.color.equals(that.color)
                    && this.pinhole.equals(that.pinhole);
        }
        return false;
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