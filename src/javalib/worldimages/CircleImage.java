package javalib.worldimages;

import java.awt.*;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent circle outline images drawn by the world when drawing
 * on its <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class CircleImage extends EllipseImage {

    /** the radius of this circle */
    public int radius;

    /**
     * A full constructor for this circle image.
     * 
     * @param radius
     *            the radius of this circle
     * @param color
     *            the color for this image
     */
    public CircleImage(int radius, OutlineMode fill, Color color) {
        super(radius * 2, radius * 2, fill, color);
        this.radius = radius;
    }

    public CircleImage(int radius, String fill, Color color) {
        this(radius, OutlineMode.fromString(fill), color);
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
     * Produce a <code>String</code> representation of this circle image
     */
    public String toString() {
        return "new CircleImage(this.color = " + this.color.toString()
                + "\nthis.radius = " + this.radius + "\nthis.fill = "
                + this.fill + ")\n";
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
        return classNameString(indent, "CircleImage")
                + colorString(indent, this.color) + "\n" + indent
                + "this.radius = " + this.radius + indent + "this.fill = "
                + this.fill + ")\n";
    }

    /**
     * Is this <code>CircleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof CircleImage) {
            CircleImage that = (CircleImage) o;
            return this.radius == that.radius && this.fill == that.fill
                    && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.radius;
    }
}