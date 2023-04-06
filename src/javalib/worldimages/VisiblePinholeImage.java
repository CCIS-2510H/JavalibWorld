package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 * This class represents the visible representation of a pinhole overlaid onto
 * an image
 * 
 * @author eric
 * 
 */
public final class VisiblePinholeImage extends WorldImage {
    WorldImage img;
    Color color;
    static final LineImage line1 = new LineImage(new Posn(10, 0), Color.BLACK);
    static final LineImage line2 = new LineImage(new Posn(0, 10), Color.BLACK);

    /**
     * VisiblePinholeImage constructor
     * 
     * @param img
     *            -- The image to overlay the pinhole representation onto
     */
    public VisiblePinholeImage(WorldImage img) {
        this(img, Color.BLACK);
    }
    public VisiblePinholeImage(WorldImage img, Color c) {
        super(img.pinhole, 1 + img.depth);
        this.img = img;
        this.color = c;
    }
    @Override
    int numKids() {
        return 3;
    }
    @Override
    WorldImage getKid(int i) {
        if (i == 0) { return this.img; }
        if (i == 1) { return line1; }
        if (i == 2) { return line2; }
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        if (i == 0) { return new AffineTransform(); }
        if (i == 1) { return AffineTransform.getTranslateInstance(this.img.pinhole.x, this.img.pinhole.y); }
        if (i == 2) { return AffineTransform.getTranslateInstance(this.img.pinhole.x, this.img.pinhole.y); }
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        return this.img.getBB(t);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        return new VisiblePinholeImage(this.img.movePinholeTo(p));
    }

    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();
        this.img.drawStackUnsafe(g);
        g.translate(this.img.pinhole.x, this.img.pinhole.y);
        Paint oldPaint = g.getPaint();
        g.setColor(this.color);
        g.drawLine(-5, 0, 5, 0);
        g.drawLine(0, -5, 0, 5);
        g.setPaint(oldPaint);
        g.setTransform(oldTransform);
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        AffineTransform t = g.getTransform();
        t.translate(this.img.pinhole.x, this.img.pinhole.y);
        txs.push(t);
        images.push(new LineImage(new Posn(10, 0), this.color));
        txs.push(t);
        images.push(new LineImage(new Posn(0, 10), this.color));
        txs.push(g.getTransform());
        images.push(this.img);
    }

    @Override
    public double getWidth() {
        return this.img.getWidth();
    }

    @Override
    public double getHeight() {
        return this.img.getHeight();
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(");
        stack.push(
                new FieldsWLItem(null,
                        new ImageField("img", this.img)));
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof VisiblePinholeImage) {
            VisiblePinholeImage that = (VisiblePinholeImage)other;
            if (this.img.pinhole.equals(that.img.pinhole)) {
                worklistThis.push(this.img);
                worklistThat.push(that.img);
                return true;
            }
        }
        return false;
    }

}
