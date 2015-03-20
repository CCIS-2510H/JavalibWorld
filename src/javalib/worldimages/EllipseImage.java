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
 * The class to represent filled ellipse images drawn by the world when drawing
 * on its <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class EllipseImage extends WorldImage {

    /** the width of this ellipse */
    public int width;

    /** the height of this ellipse */
    public int height;

    /** Outline mode of the ellipse */
    public OutlineMode fill;

    /** Color of the ellipse */
    public Color color;

    /**
     * A full constructor for this ellipse image.
     * 
     * @param pinhole
     *            the pinhole location for this image
     * @param width
     *            the width of this ellipse
     * @param height
     *            the height of this ellipse
     * @param color
     *            the color for this image
     */
    public EllipseImage(int width, int height, OutlineMode mode, Color color) {
        super();
        this.width = width;
        this.height = height;
        this.fill = mode;
        this.color = color;
    }

    public EllipseImage(int width, int height, String outlineMode, Color color) {
        this(width, height, OutlineMode.fromString(outlineMode), color);
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        // From https://stackoverflow.com/questions/24746834/calculating-an-aabb-for-a-transformed-ellipse
        double rx = (double)this.width / 2.0;
        double ry = (double)this.height / 2.0;
        
        // /M11 M21 M31\ 
        // |M12 M22 M32| Transform matrix format
        // \0   0   1  /
        double m11 = t.getScaleX();
        double m21 = t.getShearX();
        double m31 = t.getTranslateX();
        double m12 = t.getShearY();
        double m22 = t.getScaleY();
        double m32 = t.getTranslateY();
        int xOffset = (int)Math.sqrt((Math.pow(m11, 2) * Math.pow(rx, 2)) + (Math.pow(m21, 2) * Math.pow(ry, 2)));
        int yOffset = (int)Math.sqrt((Math.pow(m12, 2) * Math.pow(rx, 2)) + (Math.pow(m22, 2) * Math.pow(ry, 2)));

        int centerX = (int)m31; // Transform center of 
        int centerY = (int)m32; // ellipse using M
        int xMin = centerX - xOffset;
        int xMax = centerX + xOffset;

        int yMin = centerY - yOffset;
        int yMax = centerY + yOffset;
        
        return new BoundingBox(xMin, yMin, xMax, yMax);
    }
    
    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
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
            g.fill(new Ellipse2D.Double(-this.width / 2, -this.height / 2,
                    this.width, this.height));
        } else if (this.fill == OutlineMode.OUTLINE) {
            g.draw(new Ellipse2D.Double(-this.width / 2, -this.height / 2,
                    this.width, this.height));
        }
        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce the width of this image
     * 
     * @return the width of this image
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Produce the height of this image
     * 
     * @return the height of this image
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Produce a <code>String</code> representation of this ellipse image
     */
    public String toString() {
        return "new EllipseImage(this.fill = " + this.fill
                + ", \nthis.color = " + this.color.toString()
                + "\nthis.width = " + width + ", this.height = " + height
                + ")\n";
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
        return classNameString(indent, "EllipseImage") + indent
                + "this.fill = " + this.fill + "\n" + indent
                + colorString(indent, this.color) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    /**
     * Is this <code>EllipseImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof EllipseImage) {
            EllipseImage that = (EllipseImage) o;
            return this.width == that.width && this.height == that.height
                    && this.fill == that.fill && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.width + this.height;
    }
}