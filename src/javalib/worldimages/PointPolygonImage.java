package javalib.worldimages;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PointPolygonImage extends PolyImageBase {
  public final List<Posn> points;

  public PointPolygonImage(String fill, Color color, Posn... points) {
    this(Arrays.asList(points), OutlineMode.fromString(fill), color);
  }

  public PointPolygonImage(OutlineMode fill, Color color, Posn... points) {
    this(Arrays.asList(points), fill, color);
  }

  public PointPolygonImage(List<Posn> points, String fill, Color color) {
    this(points, OutlineMode.fromString(fill), color);
  }

  public PointPolygonImage(List<Posn> points, OutlineMode fill, Color color) {
    super(generatePoly(points), fill, color);
    this.points = Collections.unmodifiableList(new ArrayList<>(points));
  }

  private PointPolygonImage(List<Posn> points, OutlineMode fill, Color color, Posn pinhole) {
    super(generatePoly(points), fill, color, pinhole);
    this.points = Collections.unmodifiableList(new ArrayList<>(points));
  }

  private static Path2D generatePoly(List<Posn> points) {
    if (points.size() < 3) {
      throw new IllegalArgumentException("There must be at least 3 corners in a polygon");
    }

    Path2D.Double path = new Path2D.Double();
    path.moveTo(points.get(0).x, points.get(0).y);
    for (int i = 1; i < points.size(); i++) {
      Posn p = points.get(i);
      path.lineTo(p.x, p.y);
    }
    path.closePath();
    return path;
  }

  /**
   * The hashCode to match the equals method
   */
  @Override
  public int hashCode() {
    return this.color.hashCode() + this.fill.hashCode()
            + this.points.hashCode();
  }

  @Override
  public WorldImage movePinholeTo(Posn p) {
    Objects.requireNonNull(p, "Pinhole position cannot be null");
    return new PointPolygonImage(this.points, this.fill, this.color, p);
  }

}
