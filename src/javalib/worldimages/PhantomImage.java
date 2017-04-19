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
    protected void drawStackUnsafe(Graphics2D g) {
        this.img.drawStackUnsafe(g);
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
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.width = ").append(this.width).append(", ")
               .append("this.height = ").append(this.height).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("img", this.img)));
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
       if (other instanceof PhantomImage) {
            PhantomImage that = (PhantomImage)other;
            if (this.width == that.width && this.height == that.height
                    && this.pinhole.equals(that.pinhole)) {
                worklistThis.push(this.img);
                worklistThat.push(that.img);
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
