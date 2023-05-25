package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
     * @throws NullPointerException if fill or color is null
     */
    public EllipseImage(int width, int height, OutlineMode outlineMode,
            Color color) {
        super(width, height, outlineMode, color);
    }

    private EllipseImage(int width, int height, OutlineMode outlineMode,
                 Color color, Posn pinhole) {
        super(width, height, outlineMode, color, pinhole);
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
     * @throws NullPointerException if outlineMode or color is null
     */
    public EllipseImage(int width, int height, String outlineMode, Color color) {
        super(width, height, outlineMode, color);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new EllipseImage(this.width, this.height, this.fill,
                this.color, p);
    }
}

abstract class EllipseImageBase extends WorldImage {

    /** the width of this ellipse */
    public final int width;

    /** the height of this ellipse */
    public final int height;

    /** Outline mode of the ellipse */
    public final OutlineMode fill;

    /** Color of the ellipse */
    public final Color color;

    /**
     * A full constructor for this ellipse image
     * @param width the width of the ellipse
     * @param height the height of the ellipse
     * @param mode the {@link OutlineMode} fill-mode of the ellipse
     * @param color the color of the ellipse
     * @throws NullPointerException if fill or color is null
     */
    public EllipseImageBase(int width, int height, OutlineMode mode, Color color) {
        this(width, height, mode, color, DEFAULT_PINHOLE);
    }
    EllipseImageBase(int width, int height, OutlineMode mode, Color color, Posn pinhole) {
        super(pinhole,1);
        this.width = width;
        this.height = height;
        this.fill = Objects.requireNonNull(mode, "Fill cannot be null");
        this.color = Objects.requireNonNull(color, "Color cannot be null");
    }

    /**
     * A full constructor for this ellipse image
     * @param width the width of the ellipse
     * @param height the height of the ellipse
     * @param outlineMode the {@link OutlineMode} fill-mode of the ellipse
     * @param color the color of the ellipse
     * @throws NullPointerException if fill or color is null
     */
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
            EllipseImageBase that = (EllipseImageBase)other;
            return this.width == that.width && this.height == that.height
                    && this.fill == that.fill && this.color.equals(that.color)
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