package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;

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
    public int width, height;
    public Color color;
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
        super();
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
    protected BoundingBox getBB(AffineTransform t) {
        Point2D tl = WorldImage.transformPosn(t, -this.width / 2,
                -this.height / 2);
        Point2D tr = WorldImage.transformPosn(t, this.width / 2,
                -this.height / 2);
        Point2D bl = WorldImage.transformPosn(t, -this.width / 2,
                this.height / 2);
        Point2D br = WorldImage.transformPosn(t, this.width / 2,
                this.height / 2);
        return new BoundingBox(tl, tr).add(bl).add(br);
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
            g.draw(new Rectangle2D.Double(-this.width / 2, -this.height / 2,
                    this.width, this.height));
        } else if (this.fill == OutlineMode.SOLID) {
            g.fill(new Rectangle2D.Double(-this.width / 2, -this.height / 2,
                    this.width, this.height));
        }
        // reset the original paint
        g.setPaint(oldPaint);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return className(this) + colorString(this.color) + "\nthis.fill = "
                + this.fill + ",\nthis.width = " + width + ", this.height = "
                + height + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + colorString(indent, this.color)
                + "\n" + indent + "this.fill = " + this.fill + ",\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
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