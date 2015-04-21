package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Class representing a cropped image
 * 
 * @author eric
 * 
 */
final public class CropImage extends WorldImage {
    int x, y;
    int width, height;
    WorldImage img;

    /**
     * Crops <code>img</code> to the rectangle with the upper left at the point
     * (<code>x</code>,<code>y</code>) and with <code>width</code> and
     * <code>height</code>.
     * 
     * @param x
     *            -- Upper left x coordinate of the crop location
     * @param y
     *            -- Upper left y coordinate of the crop location
     * @param width
     *            -- Width of the crop
     * @param height
     *            -- Height of the crop
     * @param img
     *            -- Image to crop
     */
    public CropImage(int x, int y, int width, int height, WorldImage img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        Point2D tl = WorldImage.transformPosn(t, -this.width / 2.0,
                -this.height / 2.0);
        Point2D tr = WorldImage.transformPosn(t, this.width / 2.0,
                -this.height / 2.0);
        Point2D bl = WorldImage.transformPosn(t, -this.width / 2.0,
                this.height / 2.0);
        Point2D br = WorldImage.transformPosn(t, this.width / 2.0,
                this.height / 2.0);
        return new BoundingBox(tl, tr).add(bl).add(br);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        CropImage c = new CropImage(this.x, this.y, this.width, this.height,
                this.img);
        c.pinhole = p;
        return c;
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;

        AffineTransform oldTransform = g.getTransform();

        BufferedImage cropped = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cropped.createGraphics();
        g2d.translate(this.width - this.x, this.height - this.y);
        img.draw(g2d);
        g2d.dispose();

        g.translate(-this.width / 2.0, -this.height / 2.0);
        g.drawImage(cropped, new AffineTransform(), null);

        g.setTransform(oldTransform);
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
        return indent + "  " + "new CropImage()";
    }

    @Override
    public String toString() {
        return "new CropImage()";
    }
}
