package javalib.worldimages;

import java.awt.*;
import java.util.Stack;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent circle images
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
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.radius = ").append(this.radius).append(",");
        stack.push(
                new FieldsWLItem(
                        new ImageField("fill", this.fill),
                        new ImageField("color", this.color)));
        return sb;
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