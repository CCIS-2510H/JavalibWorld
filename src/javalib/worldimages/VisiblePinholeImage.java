package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
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

    /**
     * VisiblePinholeImage constructor
     * 
     * @param img
     *            -- The image to overlay the pinhole representation onto
     */
    public VisiblePinholeImage(WorldImage img) {
        this.img = img;
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        return this.img.getBB(t);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        VisiblePinholeImage img = new VisiblePinholeImage(this.img);
        img.pinhole = p;
        return img;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();
        this.img.draw(g);
        g.translate(this.img.pinhole.x, this.img.pinhole.y);
        new LineImage(new Posn(10, 0), Color.BLACK).draw(g);
        new LineImage(new Posn(0, 10), Color.BLACK).draw(g);
        g.setTransform(oldTransform);
    }
    @Override
    protected void drawStackless(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        AffineTransform t = g.getTransform();
        t.translate(this.img.pinhole.x, this.img.pinhole.y);
        txs.push(t);
        images.push(new LineImage(new Posn(10, 0), Color.BLACK));
        txs.push(t);
        images.push(new LineImage(new Posn(0, 10), Color.BLACK));
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
    public String toIndentedString(String indent) {
        return indent + "  new VisiblePinholeImage()";
    }

    @Override
    public String toString() {
        return "new VisiblePinholeImage()";
    }

}
