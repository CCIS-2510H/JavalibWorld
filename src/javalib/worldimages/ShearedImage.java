package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ShearedImage extends WorldImage {
    WorldImage img;
    Posn shearOrigin;
    double sx, sy;

    public ShearedImage(WorldImage img, Posn shearOrigin, double sx, double sy) {
        super(img.pinhole, Color.white);
        this.shearOrigin = shearOrigin;
        this.img = img;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void drawAt(Graphics2D g, int x, int y) {
        AffineTransform t = g.getTransform();
        g.translate(this.shearOrigin.x, this.shearOrigin.y);
        g.shear(this.sx, this.sy);
        g.translate(-this.shearOrigin.x, -this.shearOrigin.y);
        this.img.drawAt(g, x, y);
        g.setTransform(t);
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
        return new ShearedImage(img.getMovedImage(dx, dy), new Posn(
                this.shearOrigin.x + dx, this.shearOrigin.y + dy), this.sx,
                this.sy);
    }

    @Override
    public WorldImage getMovedTo(Posn pinhole) {
        return new ShearedImage(img.getMovedTo(pinhole), new Posn(
                this.shearOrigin.x + pinhole.x - this.pinhole.x,
                this.shearOrigin.y + pinhole.y - this.pinhole.y), this.sx,
                this.sy);
    }

    @Override
    public String toIndentedString(String arg0) {
        return "new ShearedImage(" + this.img.toIndentedString(arg0) + ",\n"
                + arg0 + "new Posn(" + this.shearOrigin.x + ", "
                + this.shearOrigin.y + "),\n" + arg0 + this.sx + ", " + this.sy
                + ")";
    }
}