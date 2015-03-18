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
    public void draw(Graphics2D g) {
        AffineTransform old = g.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.setTransform(old);
        g.shear(this.sx, this.sy);
        this.img.draw(g);
        g.setTransform(old);
    }

    @Override
    public int getWidth() {
        return (int) (this.img.getHeight() * Math.abs(this.sx)
                + this.img.getWidth() + 1.0);
    }

    @Override
    public int getHeight() {
        return (int) (this.img.getWidth() * Math.abs(this.sy)
                + this.img.getHeight() + 1.0);
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
                + indent + this.sx + ", " + this.sy + ")";
    }
}