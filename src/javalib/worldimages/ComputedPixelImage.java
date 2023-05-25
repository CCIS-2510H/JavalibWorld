package javalib.worldimages;

import javalib.worldcanvas.WorldCanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Objects;
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
   * Constructs an empty (transparent) rectangular image of the given size
   *
   * @param width - the width of this rectangle
   * @param height - the height of this rectangle
   */
  public ComputedPixelImage(int width, int height) {
    this(width, height, DEFAULT_PINHOLE);
  }
  private ComputedPixelImage(int width, int height, Posn pinhole) {
    super(pinhole, 1);
    this.width = width;
    this.height = height;
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    this.raster = this.image.getRaster();
  }

  /**
   * Modifies the requested pixel of this image to be the given color
   *
   * @param x - the column of the desired pixel
   * @param y - the row of the desired pixel
   * @param c - the color to set the desired pixel
   * @throws IndexOutOfBoundsException if (x, y) is out of bounds
   * @throws NullPointerException if c is null
   */
  public void setPixel(int x, int y, Color c) throws IndexOutOfBoundsException {
    boundsCheck(x, y, this.width, this.height);
    Objects.requireNonNull(c, "Color cannot be null");
    this.raster.setPixel(x, y, new int[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()});
  }

  /**
   * Retrieves the color of the requested pixel of this image
   *
   * @param x - the column of the desired pixel
   * @param y - the row of the desired pixel
   * @return the {@link Color} of the desired pixel
   * @throws IndexOutOfBoundsException if (x, y) is out of bounds
   */
  public Color getPixel(int x, int y) throws IndexOutOfBoundsException {
    boundsCheck(x, y, this.width, this.height);
    int[] ans = new int[4];
    this.raster.getPixel(x, y, ans);
    return new Color(ans[0], ans[1], ans[2], ans[3]);
  }

  /**
   * Retrieves the color of the requested  pixels of this image.
   * (Same as {@link ComputedPixelImage#getPixel}, but with consistent naming
   * with {@link FromFileImage#getColorAt} and
   * {@link WorldCanvas#getColorAt}.)
   *
   * @param x - the column of the desired pixel
   * @param y - the row of the desired pixel
   * @return the {@link Color} of the desired pixel
   * @throws IndexOutOfBoundsException if (x, y) is out of bounds
   */
  public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
    return this.getPixel(x, y);
  }

  /**
   * Modifies the requested pixel of this image to be the given color.
   * (Same as {@link ComputedPixelImage#setPixel}, but with consistent naming with
   * {@link ComputedPixelImage#getPixel}.)
   *
   * @param x - the column of the desired pixel
   * @param y - the row of the desired pixel
   * @param c - the color to set the desired pixel
   * @throws IndexOutOfBoundsException if (x, y) is out of bounds
   * @throws NullPointerException if c is null
   */
  public void setColorAt(int x, int y, Color c) throws IndexOutOfBoundsException {
    this.setPixel(x, y, c);
  }


  /**
   * Modifies the requested rectangle of pixels of this image to be the given color.
   * Rectangle covers the pixels [x, x + width) by [y, y + height)
   *
   * @param x - the leftmost column of the desired rectangle
   * @param y - the topmost row of the desired rectangle
   * @param width - the width of the rectangular region to set
   * @param height - the height of the rectangular region to set
   * @param c - the color to set the desired pixel
   * @throws IndexOutOfBoundsException if (x, y) is out of bounds
   * @throws NullPointerException if c is null
   */
  public void setPixels(int x, int y, int width, int height, Color c) throws IndexOutOfBoundsException {
    boundsCheck(x, y, this.width, this.height);
    Objects.requireNonNull(c, "Color cannot be null");
    if (width < 0)
      throw new IndexOutOfBoundsException("Width cannot be negative");
    else if (x + width > this.width)
      throw new IndexOutOfBoundsException(String.format("Right edge of rectangle (%d) is not in range [0, %d)",
              x + width, this.width));
    if (height < 0)
      throw new IndexOutOfBoundsException("Height cannot be negative");
    else if (y + height > this.height)
      throw new IndexOutOfBoundsException(String.format("Bottom edge of rectangle (%d) is not in range [0, %d)",
              y + height, this.height));
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
    Objects.requireNonNull(p, "Pinhole position cannot be null");
    ComputedPixelImage i = new ComputedPixelImage(this.width, this.height, p);
    i.raster.setDataElements(0, 0, this.raster);
    return i;
  }
}