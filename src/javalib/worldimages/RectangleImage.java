package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
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
     * @throws NullPointerException if fill or color is null
     */
    public RectangleImage(int width, int height, OutlineMode fill, Color color) {
        super(width, height, fill, color);
    }

    private RectangleImage(int width, int height, OutlineMode fill, Color color, Posn pinhole) {
        super(width, height, fill, color, pinhole);
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
     * @throws NullPointerException if fill or color is null
     */
    public RectangleImage(int width, int height, String fill, Color color) {
        super(width, height, fill, color);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new RectangleImage(this.width, this.height, this.fill,
                this.color, p);
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
    public final int width;

    /** the height of the rectangle */
    public final int height;

    /** the color of the rectangle */
    public final Color color;

    /** the outline mode of the rectangle - solid/outline */
    public final OutlineMode fill;

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
     * @throws NullPointerException if fill or color is null
     */
    public RectangleImageBase(int width, int height, OutlineMode fill,
            Color color) {
        this(width, height, fill, color, DEFAULT_PINHOLE);
    }

    RectangleImageBase(int width, int height, OutlineMode fill, Color color, Posn pinhole) {
        super(pinhole,1);
        this.width = width;
        this.height = height;
        this.fill = Objects.requireNonNull(fill, "Fill cannot be null");
        this.color = Objects.requireNonNull(color, "Color cannot be null");
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
     * @throws NullPointerException if fill or color is null
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
    protected void drawStackUnsafe(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;

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
        this.drawStackUnsafe(g);
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.width = ").append(this.width).append(", ")
               .append("this.height = ").append(this.height).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("fill", this.fill),
                        new ImageField("color", this.color)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (this.getClass().equals(other.getClass())) {
            // Check for exact class matching, and then casting to the base class is safe
            RectangleImageBase that = (RectangleImageBase)other;
            return this.width == that.width && this.height == that.height
                    && this.color.equals(that.color) && this.fill == that.fill
                    && this.pinhole.equals(that.pinhole);
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.width + this.height;
    }
}