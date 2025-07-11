package javalib.worldimages;

import java.awt.Color;

/**
 * <p>Copyright 2015 Benjamin Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * Represents a Hexagon, a special case of a regular polygon
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public final class HexagonImage extends RegularPolyImageBase {

    /**
     * The full constructor for an equilateral hexagon, whose top and bottom are
     * rotated from the horizontal
     * 
     * @param sideLen
     *            -- the length of one of the sides
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this hexagon
     */
    public HexagonImage(double sideLen, OutlineMode fill, Color color) {
        super(sideLen, LengthMode.SIDE, 6, fill, color);
    }

    public HexagonImage(double sideLen, String fill, Color color) {
        this(sideLen, OutlineMode.fromString(fill), color);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new HexagonImage(this.sideLen, this.fill, this.color);
        i.pinhole = p;
        return i;
    }
}