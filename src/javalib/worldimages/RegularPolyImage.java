package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * <p>Copyright 2014 Benjamin Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

public final class RegularPolyImage extends RegularPolyImageBase {
    
    public RegularPolyImage(double sideLen, int numSides, OutlineMode fill,
            Color color) {
        super(sideLen, numSides, fill, color);
    }
    
    public RegularPolyImage(double sideLen, int numSides, String fill,
            Color color) {
        super(sideLen, numSides, fill, color);
    }
}

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

abstract class RegularPolyImageBase extends WorldImage {
    public Posn center;
    public int sides;
    public double sideLen;
    public OutlineMode fill;
    private Polygon poly;
    public Color color;

    /**
     * The full constructor for an equilateral regular polygon, whose rightmost
     * point is rotated from the horizontal
     * 
     * @param center
     *            -- the central point of the regular polygon
     * @param sideLen
     *            -- the length of one of the sides
     * @param numSides
     *            -- the number of sides of the polygon
     * @param angle
     *            -- the angle of rotation in radians
     * @param color
     *            -- the color for this regular polygon
     */
    public RegularPolyImageBase(double sideLen, int numSides, OutlineMode fill,
            Color color) {
        super();

        if (numSides < 3) {
            throw new IllegalArgumentException(
                    "There must be at least 3 sides in a polygon");
        }

        this.sideLen = sideLen;
        this.sides = numSides;
        this.color = color;
        this.fill = fill;
        this.generatePoly();
    }

    public RegularPolyImageBase(double sideLen, int numSides, String fill,
            Color color) {
        this(sideLen, numSides, OutlineMode.fromString(fill), color);
    }

    private void generatePoly() {
        int[] xCoord = new int[this.sides];
        int[] yCoord = new int[this.sides];
        double internalAngle = (2.0 * Math.PI) / this.sides;
        double rotation = ((this.sides - 2) * (Math.PI / this.sides)) / 2;

        // Rotation adjustment for polygons:
        // This adjustment makes the output polygons look a lot nicer

        // There are 2 angles to care about:
        // The angle as seen from the center (adds up to 360)
        // Each individual angle at the edge ((numSides - 2) * 180 / numSides)
        // The second angle is what matters to determine how much to rotate
        // the polygon

        // Shape    | Sides | Rotation     | rotationAngle
        // -------------------------------------------------
        // Triangle |   3   |  pi / 6      | pi / 3
        // Square   |   4   |  pi / 4      | pi * 2 / 4
        // Pentagon |   5   |  pi * 3 / 10 | pi * 3 / 5
        // ...      |  ...  |   ...        | ...

        double xMin = sideLen, xMax = -sideLen, yMin = sideLen, yMax = -sideLen;
        for (int i = 0; i < this.sides; i++) {
            double x = Math.round((Math
                .cos(i * internalAngle + rotation) * sideLen));
            xMin = Math.min(xMin, x);
            xMax = Math.max(xMax, x);
            xCoord[i] = (int)x;
            double y = Math.round((Math
                .sin(i * internalAngle + rotation) * sideLen));
            yCoord[i] = (int)y;
            yMin = Math.min(yMin, y);
            yMax = Math.max(yMax, y);
        }
        double xAvg = (xMin + xMax) / 2.0;
        double yAvg = (yMin + yMax) / 2.0;
        for (int i = 0; i < this.sides; i++) {
            xCoord[i] -= xAvg;
            yCoord[i] -= yAvg;
        }

        this.poly = new Polygon(xCoord, yCoord, this.sides);
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        Point2D p1 = WorldImage.transformPosn(t, this.poly.xpoints[0], this.poly.ypoints[0]);
        Point2D p2 = WorldImage.transformPosn(t, this.poly.xpoints[1], this.poly.ypoints[1]);
        BoundingBox ans = new BoundingBox(p1, p2);
        for (int i = 2; i < this.sides; i++) {
            Point2D p = WorldImage.transformPosn(t, this.poly.xpoints[i], this.poly.ypoints[i]);
            ans = ans.add(p);
        }
        return ans;
    }
    
    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    public void draw(Graphics2D g) {
        if (color == null)
            color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(color);
        if (this.fill == OutlineMode.OUTLINE) {
            g.draw(this.poly);
        } else if (this.fill == OutlineMode.SOLID) {
            g.fill(this.poly);
        }

        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce the width of this triangle image
     * 
     * @return the width of this image
     */
    public int getWidth() {
        int minX = this.poly.xpoints[0];
        int maxX = this.poly.xpoints[0];
        for (int i = 0; i < this.sides; i = i + 1) {
            minX = Math.min(minX, this.poly.xpoints[i]);
            maxX = Math.max(maxX, this.poly.xpoints[i]);
        }
        return maxX - minX;
    }

    /**
     * Produce the height of this triangle image
     * 
     * @return the height of this image
     */
    public int getHeight() {
        int minY = this.poly.ypoints[0];
        int maxY = this.poly.ypoints[0];
        for (int i = 0; i < this.sides; i = i + 1) {
            minY = Math.min(minY, this.poly.ypoints[i]);
            maxY = Math.max(maxY, this.poly.ypoints[i]);
        }
        return maxY - minY;
    }

    /**
     * Produce a <code>String</code> representation of this triangle image
     */
    public String toString() {
        return "new RegularPolyImage(this.sideLen = "
                + Double.toString(this.sideLen) + ",\n" + "this.sides = "
                + Integer.toString(this.sides) + ",\n" + "this.color = "
                + this.color.toString() + "))\n";
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
        indent = indent + " ";
        return classNameString(indent, "RegularPolyImage") + indent
                + "this.sideLen = " + Double.toString(this.sideLen) + ",\n"
                + indent + "this.sides = " + Integer.toString(this.sides)
                + ",\n" + colorString(indent, this.color) + "))\n";
    }

    /**
     * Is this <code>RegularPolyImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof RegularPolyImageBase) {
            RegularPolyImageBase that = (RegularPolyImageBase) o;
            return this.sideLen == that.sideLen && this.sides == that.sides
                    && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + (int) this.sideLen + this.sides;
    }
}
