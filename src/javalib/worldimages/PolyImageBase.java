package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

abstract class PolyImageBase extends WorldImage {
  protected PolyImageBase(Path2D poly, OutlineMode mode, Color color) {
    super(1);
    this.fill = mode;
    this.color = color;
    this.poly = poly;
    this.poly.setWindingRule(Path2D.WIND_NON_ZERO);
    Rectangle bounds = this.poly.getBounds();
    this.pinhole = new Posn((int)bounds.getCenterX(), (int)bounds.getCenterY());
  }

  /** the outline mode - solid/outline of this polygon */
  public OutlineMode fill;

  /** the color of this polygon */
  public Color color;

  private final Path2D poly;

  @Override
  protected BoundingBox getBBHelp(AffineTransform t) {
    Path2D path = new Path2D.Float(this.poly, t);
    Rectangle bounds = path.getBounds();
    return new BoundingBox(bounds);
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
    return this.poly.getBounds().getWidth();
  }

  @Override
  public double getHeight() {
    return this.poly.getBounds().getHeight();
  }

  @Override
  protected boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
    if (this.getClass().equals(other.getClass())) {
      PolyImageBase that = (PolyImageBase) other;
      AffineTransform identity = new AffineTransform();
      PathIterator piThis = this.poly.getPathIterator(identity);
      PathIterator piThat = that.poly.getPathIterator(identity);
      double[] thisCoords = new double[6];
      double[] thatCoords = new double[6];
      while (!piThis.isDone() && !piThat.isDone()) {
        if (piThis.currentSegment(thisCoords) != piThat.currentSegment(thatCoords)) { return false; }
        for (int i = 0; i < 6; i++) {
          if (Math.abs(thisCoords[i] - thatCoords[i]) > 0.000001) return false;
        }
        piThis.next();
        piThat.next();
      }
      return piThis.isDone() == piThat.isDone();
    }
    return false;
  }


  @Override
  protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
    sb = sb.append("new ").append(this.simpleName()).append("(");
    List<ImageField> fields = new ArrayList<>();
    fields.add(new ImageField("fill", this.fill));
    fields.add(new ImageField("color", this.color));
    double[] coords = new double[6];
    PathIterator pi = this.poly.getPathIterator(new AffineTransform());
    for (int i = 0; !pi.isDone(); i++, pi.next()) {
      switch(pi.currentSegment(coords)) {
        case PathIterator.SEG_MOVETO:
        case PathIterator.SEG_LINETO:
          fields.add(new ImageField(String.format("points[%d]", i), new Posn((int)coords[0], (int)coords[1])));
          break;
        case PathIterator.SEG_QUADTO:
          fields.add(new ImageField(String.format("points[%d]", i), new Posn((int)coords[2], (int)coords[3])));
          break;
        case PathIterator.SEG_CUBICTO:
          fields.add(new ImageField(String.format("points[%d]", i), new Posn((int)coords[4], (int)coords[5])));
          break;
        case PathIterator.SEG_CLOSE:
          break;
      }
    }
    stack.push(new FieldsWLItem(this.pinhole, fields.toArray(new ImageField[0])));
    return sb;

  }
}
