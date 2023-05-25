package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Stack;

/**
 * <p>
 * Copyright 2018 Benjamin Lerner
 * </p>
 * <p>
 * This program is distributed under the terms of the GNU Lesser General Public
 * License (LGPL)
 * </p>
 */

/**
 * <p>
 * The class to represent filled regular star images drawn by the world when
 * drawing on its <code>Canvas</code>.
 * </p>
 * <p>
 * The pinhole for the star is in the center of the star.
 * </p>
 *
 * @author Benjamin Lerner
 * @since January 1 2018
 */
public final class RadialStarImage extends WorldImage {
  /** the number of points of this polygon */
  public final int points;

  /** the outer radius of the star */
  public final double outerRadius;

  /** the inner radius of the star */
  public final double innerRadius;

  /** the outline mode - solid/outline of this polygon */
  public final OutlineMode fill;

  /** the color of this polygon */
  public final Color color;

  private final Path2D poly;

  /**
   * Constructs a many-pointed star
   * @param numPoints -- how many points in the star
   * @param innerRadius -- the inner radius of the star (where the divots are)
   * @param outerRadius -- the outer radius of the star (where the points are)
   * @param fill -- outline or solid
   * @param color -- the color of the star
   * @throws NullPointerException if fill or color is null
   */
  public RadialStarImage(int numPoints, double innerRadius, double outerRadius, OutlineMode fill, Color color) {
    this(numPoints, innerRadius, outerRadius, fill, color, DEFAULT_PINHOLE);
  }
  private RadialStarImage(int numPoints, double innerRadius, double outerRadius, OutlineMode fill, Color color,
                         Posn pinhole) {
    super(pinhole, 1);

    if (numPoints < 3) {
      throw new IllegalArgumentException(
              "There must be at least 3 points in a polygon");
    }
    if (outerRadius < innerRadius) {
      throw new IllegalArgumentException(
              "The outer radius must be larger than the inner radius");
    }

    this.outerRadius = outerRadius;
    this.innerRadius = innerRadius;
    this.points = numPoints;
    this.color = Objects.requireNonNull(color, "Color cannot be null");
    this.fill = Objects.requireNonNull(fill, "Fill cannot be null");
    this.poly = this.generatePoly();
  }

  /**
   * Create the internal polygon representing the set of points to draw
   */
  private Path2D generatePoly() {
    Path2D poly = new Path2D.Double();
    // the angle from one outer point to the next inner divot
    double skipAngle = (2.0 * Math.PI) / (2.0 * this.points);
    // start at the topmost point; rotate for each component
    double curAngle = (Math.PI / 2.0);
    poly.moveTo(Math.cos(curAngle) * this.outerRadius, -Math.sin(curAngle) * this.outerRadius);
    for (int i = 0; i < this.points; i++) {
      if (i != 0) {
        poly.lineTo(Math.cos(curAngle) * this.outerRadius, -Math.sin(curAngle) * this.outerRadius);
      }
      curAngle += skipAngle;
      poly.lineTo(Math.cos(curAngle) * this.innerRadius, -Math.sin(curAngle) * this.innerRadius);
      curAngle += skipAngle;
    }
    poly.closePath();
    return poly;
  }

  @Override
  protected BoundingBox getBBHelp(AffineTransform t) {
    Rectangle2D ans = this.poly.getBounds2D();
    return new BoundingBox(ans.getMinX(), ans.getMinY(), ans.getMaxX(), ans.getMaxY());
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
  protected void drawStackUnsafe(Graphics2D g) {
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
  @Override
  protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
    this.drawStackUnsafe(g);
  }

  @Override
  public double getWidth() {
    return this.poly.getBounds2D().getWidth();
  }

  @Override
  public double getHeight() {
    return this.poly.getBounds2D().getHeight();
  }

  @Override
  protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
    sb = sb.append("new ").append(this.simpleName()).append("(")
           .append("this.outerRadius = ").append(this.outerRadius).append(",")
           .append("this.innerRadius = ").append(this.innerRadius).append(",");
    stack.push(
            new FieldsWLItem(this.pinhole,
                    new ImageField("points", this.points),
                    new ImageField("fill", this.fill),
                    new ImageField("color", this.color)));
    return sb;
  }

  @Override
  protected boolean equalsStacksafe(WorldImage other,
                                    Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
    if (this.getClass().equals(other.getClass())) {
      RadialStarImage that = (RadialStarImage)other;
      return this.outerRadius == that.outerRadius && this.innerRadius == that.innerRadius
              && this.points == that.points
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
            + (int) this.outerRadius + this.points;
  }

  @Override
  public WorldImage movePinholeTo(Posn p) {
    Objects.requireNonNull(p, "Pinhole position cannot be null");
    return new RadialStarImage(this.points, this.innerRadius, this.outerRadius,
            this.fill, this.color, p);
  }
}
