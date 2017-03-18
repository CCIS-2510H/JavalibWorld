package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

/**
 * A "phantom" image, that has a width and height that are either 0 or
 * specified, but in all other respects delegates to its underlying image.
 * 
 * @author eric
 * 
 */
final public class PhantomImage extends WorldImage {
    int width, height;
    WorldImage img;

    /**
     * Creates a phantom image based on <code>img</code> of size
     * <code>width</code> and <code>height</code>
     * 
     * @param img
     *            -- Image to turn into a "phantom" image
     * @param width
     *            -- The height of the "phantom" image
     * @param height
     *            -- The width of the "phantom" image
     */
    public PhantomImage(WorldImage img, int width, int height) {
        super(img.pinhole, 1 + img.depth);
        this.img = img;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a phantom image based on <code>img</code> that has a width and
     * height of 0
     * 
     * @param img
     *            -- Image to turn into a "phantom" image
     */
    public PhantomImage(WorldImage img) {
        this(img, 0, 0);
    }
    @Override
    int numKids() {
        return 1;
    }
    @Override
    WorldImage getKid(int i) {
        if (i == 0) { return this.img; }
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        if (i == 0) { return new AffineTransform(); }
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
    public WorldImage movePinholeTo(Posn p) {
        return new PhantomImage(this.img.movePinholeTo(p), this.width, this.height);
    }

    @Override
    public void draw(Graphics2D g) {
        this.img.draw(g);
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        images.push(this.img);
        txs.push(g.getTransform());
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
    public String toIndentedString(String indent) {
        return indent + "  new PhantomImage(\n" +
               indent + "    width = " + this.width + "\n" +
               indent + "    height = " + this.height + "\n" +
               indent + "    img = " + this.img.toIndentedString(indent + "  ") + ")";
    }

    @Override
    public String toString() {
        return "new PhantomImage(" + this.img.toString() + ", " + this.width + ", " + this.height + ")";
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist) {
       if (other instanceof PhantomImage) {
            PhantomImage that = (PhantomImage)other;
            if (this.width == that.width && this.height == that.height) {
                worklist.push(new ImagePair(this.img, that.img));
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(new Object[]{this.width, this.height, this.img});
    }
}
