package javalib.worldimages;

import java.awt.*;

/**
 * <p>Copyright 2014 Benjamin Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * Represents a Triangle, a special case of a regular polygon
 * 
 */
public final class EquilateralTriangleImage extends RegularPolyImageBase {
    /**
     * The full constructor for an equilateral triangle
     * 
     * @param sideLen
     *            -- the length of one of the sides
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this triangle
     */
    public EquilateralTriangleImage(double sideLen, OutlineMode fill,
            Color color) {
        super(sideLen, 3, fill, color);
    }

    public EquilateralTriangleImage(double sideLen, String fill, Color color) {
        this(sideLen, OutlineMode.fromString(fill), color);
    }

}