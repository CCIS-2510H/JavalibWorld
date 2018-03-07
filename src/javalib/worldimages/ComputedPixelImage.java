package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Stack;
/*

/**
 * <p>
 * The class to represent rectangle images drawn by the world when drawing on
 * its <code>Canvas</code>.
 * </p>
 *
 * @author Ben Lerner
 * @since March 6 2018
 */
public final class ComputedPixelImage extends WorldImage {

  /** the width of the rectangle */
  public final int width;

  /** the height of the rectangle */
  public final int height;

  private final BufferedImage image;

  private final WritableRaster raster;

  /**
   * A full constructor for this rectangle image.
   *
   * @param width
   *            -- the width of this rectangle
   * @param height
   *            -- the height of this rectangle
   */
  public ComputedPixelImage(int width, int height) {
    super(1);
    this.width = width;
    this.height = height;
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    this.raster = this.image.getRaster();
  }

  public void setPixel(int x, int y, Color c) {
    this.raster.setPixel(x, y, new int[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()});
  }

  public Color getPixel(int x, int y) {
    int[] ans = new int[4];
    this.raster.getPixel(x, y, ans);
    return new Color(ans[0], ans[1], ans[2], ans[3]);
  }

  public void setPixels(int x, int y, int width, int height, Color c) {
    int[] sample = new int[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()};
    for (int w = 0; w < width; w++)
      for (int h = 0; h < height; h++)
        this.raster.setPixel(x + w, y + h, sample);
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
  protected BoundingBox getBBHelp(AffineTransform t) {
    Point2D tl = WorldImage.transformPosn(t, -this.width / 2.0,
            -this.height / 2.0);
    Point2D tr = WorldImage.transformPosn(t, this.width / 2.0,
            -this.height / 2.0);
    Point2D bl = WorldImage.transformPosn(t, -this.width / 2.0,
            this.height / 2.0);
    Point2D br = WorldImage.transformPosn(t, this.width / 2.0,
            this.height / 2.0);
    return BoundingBox.containing(tl, tr, bl, br);
  }
  @Override
  protected void drawStackUnsafe(Graphics2D g) {
    // Adjust the position of the frame
    g.translate(-(this.width / 2.0), -(this.height / 2.0));

    g.drawRenderedImage(this.image, new AffineTransform());

    // Reset to original position
    g.translate((this.width / 2.0), (this.height / 2.0));
  }
  @Override
  protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
    this.drawStackUnsafe(g);
  }

  @Override
  public double getWidth() {
    return this.width;
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
    sb = sb.append("new ").append(this.simpleName()).append("(")
           .append("this.width = ").append(this.width).append(", ")
           .append("this.height = ").append(this.height).append(",");
    stack.push(
            new FieldsWLItem(this.pinhole,
                    new ImageField("pixels", "[...elided...]")));
    return sb;
  }


  @Override
  protected boolean equalsStacksafe(WorldImage other,
                                    Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
    if (this.getClass().equals(other.getClass())) {
      // Check for exact class matching, and then casting to the base class is safe
      ComputedPixelImage that = (ComputedPixelImage) other;
      if (this.width == that.width && this.height == that.height
          && this.pinhole.equals(that.pinhole)) {
        for (int x = 0; x < this.width; x++) {
          for (int y = 0; y < this.height; y++) {
            int[] thisPx = new int[4];
            int[] thatPx = new int[4];
            this.raster.getPixel(x, y, thisPx);
            that.raster.getPixel(x, y, thatPx);
            for (int c = 0; c < 4; c++)
              if (thisPx[c] != thatPx[c]) return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * The hashCode to match the equals method
   */
  public int hashCode() {
    return 1000 * this.width + this.height;
  }

  @Override
  public WorldImage movePinholeTo(Posn p) {
    ComputedPixelImage i = new ComputedPixelImage(this.width, this.height);
    i.pinhole = p;
    i.raster.setDataElements(0, 0, this.raster);
    return i;
  }
}