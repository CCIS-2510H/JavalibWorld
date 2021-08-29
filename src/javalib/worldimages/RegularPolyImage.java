package javalib.worldimages;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.Stack;

/**
 * <p>
 * Copyright 2015 Benjamin Lerner
 * </p>
 * <p>
 * This program is distributed under the terms of the GNU Lesser General Public
 * License (LGPL)
 * </p>
 */

/**
 * <p>
 * The class to represent filled regular polygon images drawn by the world when
 * drawing on its <code>Canvas</code>.
 * </p>
 * <p>
 * The pinhole for the polygon is in the center of the polygon.
 * </p>
 * 
 * @author Benjamin Lerner
 * @since November 14 2014
 */
public final class RegularPolyImage extends RegularPolyImageBase {

    /**
     * The full constructor for an equilateral regular polygon
     * 
     * @param sideLen
     *            -- the length of one of the sides
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double sideLen, int numSides, OutlineMode fill, Color color) {
        this(sideLen, LengthMode.SIDE, numSides, fill, color);
    }

    /**
     * The full constructor for an equilateral regular polygon
     * 
     * @param sideLen
     *            -- the length of one of the sides
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double sideLen, int numSides, String fill, Color color) {
        this(sideLen, LengthMode.SIDE, numSides, OutlineMode.fromString(fill), color);
    }

    /**
     * The full constructor for an equilateral regular polygon
     *
     * @param length
     *            -- the size of this polygon
     * @param lengthMode
     *            -- interpretation of the length parameter
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double length, String lengthMode, int numSides, OutlineMode fill, Color color) {
        this(length, LengthMode.fromString(lengthMode), numSides, fill, color);
    }
    /**
     * The full constructor for an equilateral regular polygon
     *
     * @param length
     *            -- the size of this polygon
     * @param lengthMode
     *            -- interpretation of the length parameter
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double length, LengthMode lengthMode, int numSides, OutlineMode fill, Color color) {
        super(length, lengthMode, numSides, fill, color);
    }

    /**
     * The full constructor for an equilateral regular polygon
     *
     * @param length
     *            -- the size of this polygon
     * @param lengthMode
     *            -- interpretation of the length parameter
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double length, String lengthMode, int numSides, String fill, Color color) {
        super(length, LengthMode.fromString(lengthMode), numSides, OutlineMode.fromString(fill), color);
    }
    /**
     * The full constructor for an equilateral regular polygon
     *
     * @param length
     *            -- the size of this polygon
     * @param lengthMode
     *            -- interpretation of the length parameter
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImage(double length, LengthMode lengthMode, int numSides, String fill, Color color) {
        super(length, lengthMode, numSides, OutlineMode.fromString(fill), color);
    }
}

abstract class RegularPolyImageBase extends PolyImageBase {

    /** the number of sides of this polygon */
    public int sides;

    /** the length of each side of this polygon */
    public double sideLen;

    /**
     * The full constructor for an equilateral regular polygon
     * 
     * @param length
     *            -- the size of this polygon
     * @param lengthMode
     *            -- interpretation of the length parameter
     * @param numSides
     *            -- the number of sides of the polygon
     * @param fill
     *            -- outline or solid
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImageBase(double length, LengthMode lengthMode, int numSides, OutlineMode fill,
            Color color) {
        super(generatePoly(numSides, length, lengthMode), fill, color);

        this.sides = numSides;
        // To ensure that each side has the specified length, we need
        // to compute the radius of the circumcircle
        switch(lengthMode) {
            case SIDE:
                this.sideLen = length;
                break;
            case RADIUS:
                double internalAngle = (2.0 * Math.PI) / sides;
                this.sideLen = 2.0 * length * Math.sin(internalAngle / 2.0);
                break;
        }

    }

    /**
     * Create the internal polygon representing the set of points to draw
     */
    private static Path2D generatePoly(int sides, double sideLen, LengthMode lengthMode) {
        if (sides < 3) {
            throw new IllegalArgumentException(
                    "There must be at least 3 sides in a polygon");
        }
        double internalAngle = (2.0 * Math.PI) / sides;
        double rotation = ((sides - 2) * (Math.PI / sides)) / 2;
        double radius = 0;

        // To ensure that each side has the specified length, we need
        // to compute the radius of the circumcircle
        switch(lengthMode) {
        case SIDE:
            radius = sideLen / (2.0 * Math.sin(internalAngle / 2.0));
            break;
        case RADIUS:
            radius = sideLen;
            break;
        }

        // Rotation adjustment for polygons:
        // This adjustment makes the output polygons look a lot nicer

        // There are 2 angles to care about:
        // The angle as seen from the center (adds up to 360)
        // Each individual angle at the edge ((numSides - 2) * 180 / numSides)
        // The second angle is what matters to determine how much to rotate
        // the polygon

        // Shape | Sides | Rotation | rotationAngle
        // -------------------------------------------------
        // Triangle | 3 | pi / 6 | pi / 3
        // Square | 4 | pi / 4 | pi * 2 / 4
        // Pentagon | 5 | pi * 3 / 10 | pi * 3 / 5
        // ...   | ... | ... | ...

        Path2D.Double path = new Path2D.Double();


        for (int i = 0; i < sides; i++) {
            double x = Math.round(Math.cos(((double)i) * internalAngle + rotation) * radius);
            double y = Math.round(Math.sin(((double)i) * internalAngle + rotation) * radius);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        return path;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
                .append("this.sidelen = ").append(this.sideLen).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("sides", this.sides),
                        new ImageField("fill", this.fill),
                        new ImageField("color", this.color)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (this.getClass().equals(other.getClass())) {
            RegularPolyImageBase that = (RegularPolyImageBase)other;
            return this.sideLen == that.sideLen && this.sides == that.sides
                    && this.fill == that.fill && this.color.equals(that.color)
                    && this.pinhole.equals(that.pinhole);
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    @Override
    public int hashCode() {
        return this.color.hashCode() + this.fill.hashCode()
                + (int) this.sideLen + this.sides;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new RegularPolyImage(this.sideLen, this.sides,
                this.fill, this.color);
        i.pinhole = p;
        return i;
    }
}
