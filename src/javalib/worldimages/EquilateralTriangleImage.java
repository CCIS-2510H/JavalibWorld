package javalib.worldimages;

import java.awt.*;

/**
 * <p>Copyright 2014 Benjamin Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * Represents a Hexagon, a special case of a regular polygon
 * 
 */
public class EquilateralTriangleImage extends RegularPolyImage {
    /**
     * The full constructor for an equilateral hexagon, whose top and bottom are
     * rotated from the horizontal
     * 
     * @param center
     *            -- the central point of the hexagon
     * @param sideLen
     *            -- the length of one of the sides
     * @param angle
     *            -- the angle of rotation in radians
     * @param color
     *            -- the color for this hexagon
     */
    public EquilateralTriangleImage(double sideLen, OutlineMode fill,
            Color color) {
        super(new Posn(0, 0), sideLen, 3, fill, color);
    }

    public EquilateralTriangleImage(double sideLen, String fill, Color color) {
        this(sideLen, OutlineMode.fromString(fill), color);
    }

}