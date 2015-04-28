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

/**
 * <p>
 * The class to represent Ellipse images.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public final class EllipseImage extends EllipseImageBase {

    /**
     * A full constructor for this ellipse image.
     * 
     * @param width
     *            -- the width of this ellipse
     * @param height
     *            -- the height of this ellipse
     * @param outlineMode
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public EllipseImage(int width, int height, OutlineMode outlineMode,
            Color color) {
        super(width, height, outlineMode, color);
    }

    /**
     * A full constructor for this ellipse image.
     * 
     * @param width
     *            -- the width of this ellipse
     * @param height
     *            -- the height of this ellipse
     * @param outlineMode
     *            -- outline or solid
     * @param color
     *            -- the color for this image
     */
    public EllipseImage(int width, int height, String outlineMode, Color color) {
        super(width, height, outlineMode, color);
    }
}

abstract class EllipseImageBase extends WorldImage {

    /** the width of this ellipse */
    public int width;

    /** the height of this ellipse */
    public int height;

    /** Outline mode of the ellipse */
    public OutlineMode fill;

    /** Color of the ellipse */
    public Color color;

    public EllipseImageBase(int width, int height, OutlineMode mode, Color color) {
        super(1);
        this.width = width;
        this.height = height;
        this.fill = mode;
        this.color = color;
    }

    public EllipseImageBase(int width, int height, String outlineMode,
            Color color) {
        this(width, height, OutlineMode.fromString(outlineMode), color);
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
        // From
        // https://stackoverflow.com/questions/24746834/calculating-an-aabb-for-a-transformed-ellipse
        double rx = (double) this.width / 2.0;
        double ry = (double) this.height / 2.0;

        // /M11 M21 M31\
        // |M12 M22 M32| Transform matrix format
        // \0 0 1 /
        double m11 = t.getScaleX();
        double m21 = t.getShearX();
        double m31 = t.getTranslateX();
        double m12 = t.getShearY();
        double m22 = t.getScaleY();
        double m32 = t.getTranslateY();
        double xOffset = Math.sqrt((Math.pow(m11, 2) * Math.pow(rx, 2))
                + (Math.pow(m21, 2) * Math.pow(ry, 2)));
        double yOffset = Math.sqrt((Math.pow(m12, 2) * Math.pow(rx, 2))
                + (Math.pow(m22, 2) * Math.pow(ry, 2)));

        double centerX = m31; // Transform center of
        double centerY = m32; // ellipse using M
        double xMin = centerX - xOffset;
        double xMax = centerX + xOffset;

        double yMin = centerY - yOffset;
        double yMax = centerY + yOffset;

        return new BoundingBox(xMin, yMin, xMax, yMax);
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
        if (this.fill == OutlineMode.SOLID) {
            g.fill(new Ellipse2D.Double(-this.width / 2.0, -this.height / 2.0,
                    this.width, this.height));
        } else if (this.fill == OutlineMode.OUTLINE) {
            g.draw(new Ellipse2D.Double(-this.width / 2.0, -this.height / 2.0,
                    this.width, this.height));
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
     * Produce a <code>String</code> representation of this ellipse image
     */
    public String toString() {
        return className(this) + "this.width = " + this.width
                + ", this.height = " + this.height + "," + "\nthis.fill = "
                + this.fill + ",\n" + colorString(this.color) + ")";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.width = " + this.width
                + ", this.height = " + this.height + "," + "\n" + indent
                + "this.fill = " + this.fill + ","
                + colorString(indent, this.color) + ")";
    }

    public boolean same(EllipseImageBase that) {
        return this.width == that.width && this.height == that.height
                && this.fill == that.fill && this.color.equals(that.color);
    }

    /**
     * Is this <code>EllipseImage</code> same as the given object?
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof EllipseImageBase && this.same((EllipseImageBase) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.width + this.height;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new EllipseImage(this.width, this.height, this.fill,
                this.color);
        i.pinhole = p;
        return i;
    }
}