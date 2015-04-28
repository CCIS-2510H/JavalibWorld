package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

/**
 * <p>
 * Copyright 2015 Ben Lerner
 * </p>
 * <p>
 * This program is distributed under the terms of the GNU Lesser General Public
 * License (LGPL)
 * </p>
 */

public final class RectangleImage extends RectangleImageBase {

    /**
     * A full constructor for this rectangle image.
     * 
     * @param width
     *            -- the width of this rectangle
     * @param height
     *            -- the height of this rectangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public RectangleImage(int width, int height, OutlineMode fill, Color color) {
        super(width, height, fill, color);
    }

    /**
     * A full constructor for this rectangle image.
     * 
     * @param width
     *            -- the width of this rectangle
     * @param height
     *            -- the height of this rectangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public RectangleImage(int width, int height, String fill, Color color) {
        super(width, height, fill, color);
    }
}

/**
 * <p>
 * The class to represent rectangle images drawn by the world when drawing on
 * its <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
abstract class RectangleImageBase extends WorldImage {

    /** the width of the rectangle */
    public int width;

    /** the height of the rectangle */
    public int height;

    /** the color of the rectangle */
    public Color color;

    /** the outline mode of the rectangle - solid/outline */
    public OutlineMode fill;

    /**
     * A full constructor for this rectangle image.
     * 
     * @param width
     *            -- the width of this rectangle
     * @param height
     *            -- the height of this rectangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public RectangleImageBase(int width, int height, OutlineMode fill,
            Color color) {
        super(1);
        this.width = width;
        this.height = height;
        this.fill = fill;
        this.color = color;
    }

    /**
     * A full constructor for this rectangle image.
     * 
     * @param width
     *            -- the width of this rectangle
     * @param height
     *            -- the height of this rectangle
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public RectangleImageBase(int width, int height, String fill, Color color) {
        this(width, height, OutlineMode.fromString(fill), color);
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
        Point2D tl = WorldImage.transformPosn(t, -this.width / 2.0,
                -this.height / 2.0);
        Point2D tr = WorldImage.transformPosn(t, this.width / 2.0,
                -this.height / 2.0);
        Point2D bl = WorldImage.transformPosn(t, -this.width / 2.0,
                this.height / 2.0);
        Point2D br = WorldImage.transformPosn(t, this.width / 2.0,
                this.height / 2.0);
        return BoundingBox.containing(tl, tr, bl, br);
    }
    @Override
    public void draw(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(this.color);
        // draw the object
        if (this.fill == OutlineMode.OUTLINE) {
            g.draw(new Rectangle2D.Double(-this.width / 2.0,
                    -this.height / 2.0, this.width, this.height));
        } else if (this.fill == OutlineMode.SOLID) {
            g.fill(new Rectangle2D.Double(-this.width / 2.0,
                    -this.height / 2.0, this.width, this.height));
        }
        // reset the original paint
        g.setPaint(oldPaint);
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.draw(g);
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return className(this) + "this.width = " + width + ", this.height = "
                + height + ",\nthis.fill = " + this.fill + ",\n"
                + colorString(this.color) + ")";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.width = " + width
                + ", this.height = " + height + ",\n" + indent + "this.fill = "
                + this.fill + "," + colorString(indent, this.color) + ")";
    }

    /**
     * Is this <code>RectangleImage</code> same as the given
     * <code>RectangleImage</code>?
     */
    public boolean same(RectangleImageBase that) {
        return this.width == that.width && this.height == that.height
                && this.color.equals(that.color) && this.fill == that.fill;
    }

    /**
     * Is this <code>RectangleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof RectangleImageBase
                && this.same((RectangleImageBase) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.width + this.height;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new RectangleImage(this.width, this.height, this.fill,
                this.color);
        i.pinhole = p;
        return i;
    }
}