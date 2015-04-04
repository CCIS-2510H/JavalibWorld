package javalib.worldimages;

import java.awt.*;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent circle images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public final class CircleImage extends EllipseImageBase {

    /** the radius of this circle */
    public int radius;

    /**
     * A full constructor for this circle image.
     * 
     * @param radius
     *            -- the radius of this circle
     * @param fill
     *            -- Outline or solid
     * @param color
     *            -- the color for this image
     */
    public CircleImage(int radius, OutlineMode fill, Color color) {
        super(radius * 2, radius * 2, fill, color);
        this.radius = radius;
    }

    /**
     * A full constructor for this circle image.
     * 
     * @param radius
     *            -- the radius of this circle
     * @param fill
     *            -- Outline or solid
     * @param color
     *            -- the color for this image
     */
    public CircleImage(int radius, String fill, Color color) {
        this(radius, OutlineMode.fromString(fill), color);
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
     * Produce a <code>String</code> representation of this circle image
     */
    @Override
    public String toString() {
        return className(this) + colorString(this.color) 
                + "\nthis.radius = " + this.radius + "\nthis.fill = "
                + this.fill + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + colorString(indent, this.color)
                + "\n" + indent + "this.radius = " + this.radius + ",\n"
                + indent + "this.fill = " + this.fill + ")\n";
    }

    /**
     * Is this <code>CircleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof CircleImage && this.same((CircleImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.radius + this.fill.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new CircleImage(this.radius, this.fill, this.color);
        i.pinhole = p;
        return i;
    }
}