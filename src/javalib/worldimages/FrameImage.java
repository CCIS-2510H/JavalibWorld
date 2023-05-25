package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Stack;

/**
 * Class representing a frame around an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * 
 * @since April 4, 2015
 * 
 */
public final class FrameImage extends RectangleImageBase {

    /** The image being framed */
    public final WorldImage img;

    /**
     * Create a frame around the passed in image
     * 
     * @param img
     *            -- Image to frame
     * @param color
     *            -- Color of the frame
     * @throws NullPointerException if img or color is null
     */
    public FrameImage(WorldImage img, Color color) {
        super((int) Math.ceil(Objects.requireNonNull(img, "Image cannot be null").getBB().getWidth()), (int) Math.ceil(img
                .getBB().getHeight()), OutlineMode.OUTLINE, color, img.pinhole);
        this.img = img;
    }

    /**
     * Create a black frame around the passed in image
     * 
     * @param img
     *            -- Image to frame
     * @throws NullPointerException if img is null
     */
    public FrameImage(WorldImage img) {
        this(img, Color.black);
    }


    @Override
    public BoundingBox getBB() {
        return this.img.getBB();
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        return this.img.getBBHelp(t);
    }

    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;

        // save the current paint
        Paint oldPaint = g.getPaint();
        Stroke oldStroke = g.getStroke();
        // draw the object
        this.img.drawStackUnsafe(g);
        // set the paint to the given color
        g.setPaint(this.color);
        // Draw the frame
        BoundingBox bb = this.img.getBB();
        g.draw(new Rectangle2D.Double(bb.getTlx(), bb.getTly(), bb.getWidth(), bb
                .getHeight()));
        // reset the original paint
        g.setPaint(oldPaint);
        g.setStroke(oldStroke);
    }

    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;

        BoundingBox bb = this.img.getBB();
        images.push(new RectangleImage((int)Math.ceil(bb.getWidth()),
                (int)Math.ceil(bb.getHeight()), OutlineMode.OUTLINE, color));
        txs.push(g.getTransform());
        this.img.drawStacksafe(g, images, txs);
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("color", this.color),
                        new ImageField("img", this.img)));
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof FrameImage) {
            FrameImage that = (FrameImage)other;
            if (this.color.equals(that.color) && this.pinhole.equals(that.pinhole)) {
                worklistThis.push(this.img);
                worklistThat.push(that.img);
                return true;
            }
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return super.hashCode() + this.color.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new FrameImage(this.img.movePinholeTo(p), this.color);
    }
}
