package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Stack;

public final class WedgeImage extends EllipseImageBase {
  /**
   * the radius of this wedge
   */
  public int radius;

  /**
   * the angle of this wedge
   */
  public int angle;

  /**
   * A full constructor for this circle image.
   *
   * @param radius -- the radius of this circle
   * @param fill   -- Outline or solid
   * @param color  -- the color for this image
   */
  public WedgeImage(int radius, int angle, OutlineMode fill, Color color) {
    super(2 * radius, 2 * radius, fill, color);
    this.radius = radius;
    this.angle = angle;
    this.pinhole = new Posn(0, 0);
//    if ((angle % 360) >= 180) {
//      this.pinhole = new Posn(this.radius, this.radius);
//    } else if (angle >= 90) {
//      this.pinhole = new Posn(
//              (int)(-this.radius * Math.cos(Math.toRadians(this.angle))),
//              this.radius);
//    } else {
//      this.pinhole = new Posn(0, (int)(this.radius * Math.sin(Math.toRadians(this.angle))));
//    }
  }

  public WedgeImage(int radius, int angle, String fill, Color color) {
    this(radius, angle, OutlineMode.fromString(fill), color);
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


  private double getXAtY(double y, double A, double B, double C, double D, double E, double F) {
    final double CY2plusEYplusF = y * (C * y + E) + F;
    final double BYplusD = B * y + D;
    if (Math.abs(A) < Double.MIN_VALUE) {
      return -CY2plusEYplusF / BYplusD;
    } else {
      final double discXAtY = (BYplusD * BYplusD) - 4.0 * A * CY2plusEYplusF;
      if (discXAtY < Double.MIN_VALUE) {
        return -BYplusD / (2.0 * A);
      } else {
        return -Math.max(BYplusD + Math.sqrt(discXAtY), BYplusD - Math.sqrt(discXAtY)) / (2.0 * A);
      }
    }
  }

  private double getYAtX(double x, double A, double B, double C, double D, double E, double F) {
    final double BXplusE = B * x + E;
    double AX2plusDXplusF = x * (A * x + D) + F;
    if (Math.abs(C) < Double.MIN_VALUE) {
      return -AX2plusDXplusF / BXplusE;
    } else {
      final double discYAtX = (BXplusE * BXplusE) - 4.0 * C * AX2plusDXplusF;
      if (discYAtX < Double.MIN_VALUE) {
        return -BXplusE / (2.0 * C);
      } else {
        return -Math.max(BXplusE + Math.sqrt(discYAtX), BXplusE - Math.sqrt(discYAtX)) / (2.0 * C);
      }
    }
  }

  private double makePosAngle(double angle) {
    if (angle < 0) { return angle + 2 * Math.PI; }
    return angle;
  }
  @Override
  protected BoundingBox getBBHelp(AffineTransform t) {
    final BoundingBox ellipseBB = super.getBBHelp(t);
    // From
    // https://math.stackexchange.com/questions/13150/extracting-rotation-scale-values-from-2d-transformation-matrix/13165#13165
    // /M11 M21 M31\
    // |M12 M22 M32| Transform matrix format
    // \0 0 1 /
    final double xMin = ellipseBB.getTlx();
    final double xMax = ellipseBB.getBrx();
    final double yMin = ellipseBB.getTly();
    final double yMax = ellipseBB.getBry();

    // Now normalize the transformation to make the ellipse a circle, and invert
    t = new AffineTransform(t);
    t.scale(this.radius, this.radius); // Fix the transform so that this is really a circle
    t.scale(1, -1); // make y point up
    AffineTransform tInv;
    try {
      tInv = t.createInverse();
    } catch (NoninvertibleTransformException e) {
      return ellipseBB;
    }

    final double P = tInv.getScaleX();
    final double Q = tInv.getShearX();
    final double R = tInv.getTranslateX();
    final double S = tInv.getShearY();
    final double T = tInv.getScaleY();
    final double U = tInv.getTranslateY();

    // Given a circle (x) ^ 2 + (y) ^ 2 = 1, the transformed ellipse is
    // (m11 * x + m21 * y + m31) ^ 2 + (m12 * x + m22 * y + m32) ^ 1 = 1
    // Or expanded, Ax^2 + Bxy + Cy^2 + Dx + Ey + F = 0
    final double A = (P * P) + (S * S);
    final double B = 2.0 * ((P * Q) + (S * T));
    final double C = (Q * Q) + (T * T);
    final double D = 2.0 * ((P * R) + (S * U));
    final double E = 2.0 * ((Q * R) + (T * U));
    final double F = (R * R) + (U * U) - 1.0;


    // Compute coordinates for all extrema:
    final double yAtXMax = getYAtX(xMax, A, B, C, D, E, F);
    final double yAtXMin = getYAtX(xMin, A, B, C, D, E, F);
    final double xAtYMax = getXAtY(yMax, A, B, C, D, E, F);
    final double xAtYMin = getXAtY(yMin, A, B, C, D, E, F);

    // un-transform all four points
    final double invXMax    = P * xMax + Q * yAtXMax + R;
    final double invYAtXMax = S * xMax + T * yAtXMax + U;
    final double thetaXMax  = makePosAngle(Math.atan2(invYAtXMax, invXMax));
    final double invXMin    = P * xMin + Q * yAtXMin + R;
    final double invYAtXMin = S * xMin + T * yAtXMin + U;
    final double thetaXMin  = makePosAngle(Math.atan2(invYAtXMin, invXMin));
    final double invXAtYMax = P * xAtYMax + Q * yMax + R;
    final double invYMax    = S * xAtYMax + T * yMax + U;
    final double thetaYMax  = makePosAngle(Math.atan2(invYMax, invXAtYMax));
    final double invXAtYMin = P * xAtYMin + Q * yMin + R;
    final double invYMin    = S * xAtYMin + T * yMin + U;
    final double thetaYMin  = makePosAngle(Math.atan2(invYMin, invXAtYMin));

    final BoundingBox bb = new BoundingBox( // start with the transformed center of ellipse
            ellipseBB.getCenterX(), ellipseBB.getCenterY(),
            ellipseBB.getCenterX(), ellipseBB.getCenterY());
    double angleInRadians = Math.toRadians(this.angle); // arcs go counterclockwise
    bb.combineWith(t.transform(
            new Point2D.Double(1, 0), null));
    bb.combineWith(t.transform(
            new Point2D.Double(Math.cos(angleInRadians), Math.sin(angleInRadians)), null));
    if (0 <= thetaXMax && thetaXMax <= angleInRadians) {
      bb.combineWith(xMax, yAtXMax);
    }
    if (0 <= thetaXMin && thetaXMin <= angleInRadians) {
      bb.combineWith(xMin, yAtXMin);
    }
    if (0 <= thetaYMax && thetaYMax <= angleInRadians) {
      bb.combineWith(xAtYMax, yMax);
    }
    if (0 <= thetaYMin && thetaYMin <= angleInRadians) {
      bb.combineWith(xAtYMin, yMin);
    }

    return bb;
  }

  @Override
  protected void drawStackUnsafe(Graphics2D g) {
    if (this.radius <= 0)
      return;
    if (this.color == null)
      this.color = new Color(0, 0, 0);

    // save the current paint
    Paint oldPaint = g.getPaint();
    // set the paint to the given color
    g.setPaint(this.color);
    // draw the object
    if (this.fill == OutlineMode.SOLID) {
      g.fillArc(-this.radius, -this.radius,
              2 * this.radius, 2 * this.radius, 0, this.angle);
    } else if (this.fill == OutlineMode.OUTLINE) {
      g.drawArc(-this.radius, -this.radius,
              2 * this.radius, 2 * this.radius, 0, this.angle);
      g.drawLine(0, 0, this.radius, 0);
      double angleInRadians = Math.toRadians(this.angle); // arcs go counterclockwise
      double xEnd = Math.cos(angleInRadians) * this.radius;
      double yEnd = Math.sin(angleInRadians) * this.radius;
      g.drawLine(0, 0, (int)xEnd, (int)-yEnd);
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
    return this.radius * 2;
  }

  @Override
  public double getHeight() {
    return this.radius * 2;
  }

  @Override
  protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
    sb = sb.append("new ").append(this.simpleName()).append("(")
            .append("this.radius = ").append(this.radius).append(", ")
            .append("this.angle = ").append(this.angle).append(",");
    stack.push(
            new FieldsWLItem(this.pinhole,
                    new ImageField("fill", this.fill),
                    new ImageField("color", this.color)));
    return sb;
  }

  @Override
  protected boolean equalsStacksafe(WorldImage other,
                                    Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
    if (this.getClass().equals(other.getClass())) {
      // Check for exact class matching, and then casting to the base class is safe
      WedgeImage that = (WedgeImage) other;
      return this.radius == that.radius && this.angle == that.angle
              && this.fill == that.fill && this.color.equals(that.color)
              && this.pinhole.equals(that.pinhole);
    }
    return false;
  }

  /**
   * The hashCode to match the equals method
   */
  public int hashCode() {
    return this.color.hashCode() + this.radius + this.angle;
  }

  @Override
  public WorldImage movePinholeTo(Posn p) {
    WorldImage i = new WedgeImage(this.radius, this.angle, this.fill,
            this.color);
    i.pinhole = p;
    return i;
  }
}
