package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
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
public final class StarImage extends WorldImage {
  /** the number of points of this polygon */
  public int points;

  /** the length of each side of this polygon */
  public double radius;

  /** the number of points to skip to the next one */
  public int skipCount;

  /** the outline mode - solid/outline of this polygon */
  public OutlineMode fill;

  /** the color of this polygon */
  public Color color;

  private Path2D poly;

  /**
   * Constructs a simple 5-pointed star
   * @param radius -- the radius of the enclosing circle
   * @param fill -- outline or solid
   * @param color -- ths color for this star
   */
  public StarImage(double radius, OutlineMode fill, Color color) {
    this(radius, 5, fill, color);
  }
  /**
   * Constructs a star with an arbitrary number of points
   *
   * @param radius -- the radius of the enclosing circle
   * @param numPoints -- the number of points of the star
   * @param fill -- outline or solid
   * @param color -- the color for this star
   */
  public StarImage(double radius, int numPoints, OutlineMode fill, Color color) {
    this(radius, numPoints, numPoints / 2, fill, color);
  }

  /**
   * Constructs a star with an arbitrary number of points, specifying which points are
   * connected to each other.
   * @param radius -- the radius of the enclosing circle
   * @param numPoints -- the number of points of the star
   * @param skipCount -- how many points away is the next point of the star
   * @param fill -- outline or solid
   * @param color -- the color for this star
   */
  public StarImage(double radius, int numPoints, int skipCount, OutlineMode fill, Color color) {
    super(1);

    if (numPoints < 3) {
      throw new IllegalArgumentException(
              "There must be at least 3 points in a polygon");
    }
    if (skipCount < 1 || skipCount >= numPoints) {
      throw new IllegalArgumentException(
              "The skip-count must be positive and less than the number of points");
    }

    this.radius = radius;
    this.points = numPoints;
    this.skipCount = skipCount;
    this.color = color;
    this.fill = fill;
    this.generatePoly();
  }

  /**
   * Create the internal polygon representing the set of points to draw
   */
  private void generatePoly() {
    this.poly = new Path2D.Double();
    // If the number of points and the skipCount aren't coprime, there could
    // be multiple pieces to this path
    double skipAngle = this.skipCount * (2.0 * Math.PI) / this.points;
    int pointsPerComponent, numComponents;
    int gcd = GCD(this.points, this.skipCount);
    pointsPerComponent = this.points / gcd;
    numComponents = gcd;
    for (int component = 0; component < numComponents; component++) {
      // start at the topmost point; rotate for each component
      double curAngle = (2.0 * Math.PI) * ((double)component / (double)this.points) + (Math.PI / 2.0);
      this.poly.moveTo(Math.cos(curAngle) * this.radius, -Math.sin(curAngle) * this.radius);
      for (int i = 0; i < pointsPerComponent; i++) {
        curAngle += skipAngle;
        this.poly.lineTo(Math.cos(curAngle) * this.radius, -Math.sin(curAngle) * this.radius);
      }
      this.poly.closePath();
    }
    Rectangle2D bb = this.poly.getBounds2D();
    this.poly.transform(AffineTransform.getTranslateInstance(-bb.getCenterX(), -bb.getCenterY()));
    this.pinhole = new Posn((int)-bb.getCenterX(), (int)-bb.getCenterY());
  }

  private int GCD(int a, int b) {
    int t;
    while (b != 0) {
      t = a;
      a = b;
      b = t % b;
    }
    return b==0 ? a : GCD(b, a%b);
  }

  @Override
  protected BoundingBox getBBHelp(AffineTransform t) {
    Rectangle2D ans = this.poly.createTransformedShape(t).getBounds2D();
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
           .append("this.radius = ").append(this.radius).append(",");
    stack.push(
            new FieldsWLItem(this.pinhole,
                    new ImageField("points", this.points),
                    new ImageField("skipCount", this.skipCount),
                    new ImageField("fill", this.fill),
                    new ImageField("color", this.color)));
    return sb;
  }

  @Override
  protected boolean equalsStacksafe(WorldImage other,
                                    Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
    if (this.getClass().equals(other.getClass())) {
      StarImage that = (StarImage)other;
      return this.radius == that.radius && this.points == that.points
              && this.skipCount == that.skipCount
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
            + (int) this.radius + this.points;
  }

  @Override
  public WorldImage movePinholeTo(Posn p) {
    WorldImage i = new StarImage(this.radius, this.points, this.skipCount,
            this.fill, this.color);
    i.pinhole = p;
    return i;
  }
}
