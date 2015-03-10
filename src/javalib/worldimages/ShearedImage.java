package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ShearedImage extends WorldImage {
    WorldImage img;
    double sx, sy;

    public ShearedImage(WorldImage img, double sx, double sy) {
        super(img.pinhole, Color.WHITE);
        this.img = img;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void drawAt(Graphics2D g, int x, int y) {
        AffineTransform old = g.getTransform();
        g.shear(this.sx, this.sy);
        this.img.drawAt(g, x, y);
        g.setTransform(old);
    }

    @Override
    public int getWidth() {
        return (int) (this.img.getWidth() * (1.0 + Math.abs(this.sx)));
    }

    @Override
    public int getHeight() {
        return (int) (this.img.getHeight() * (1.0 + Math.abs(this.sy)));
    }

    @Override
    public WorldImage getMovedImage(int dx, int dy) {
        return new ShearedImage(img.getMovedImage(dx, dy), this.sx, this.sy);
    }

    @Override
    public WorldImage getMovedTo(Posn pinhole) {
        return new ShearedImage(img.getMovedTo(pinhole), this.sx, this.sy);
    }

    @Override
    public String toIndentedString(String indent) {
        return "new ShearedImage(" + this.img.toIndentedString(indent) + ",\n"
                + indent + this.sx + ", " + this.sy
                + ")";
    }
}