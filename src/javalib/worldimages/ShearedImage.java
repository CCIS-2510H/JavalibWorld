package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public final class ShearedImage extends WorldImage {
    WorldImage img;
    double sx, sy;

    public ShearedImage(WorldImage img, double sx, double sy) {
        super();
        this.img = img;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.shear(this.sx, this.sy);
        return this.img.getBB(newT);
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
    public String toIndentedString(String indent) {
        return "new ShearedImage(" + this.img.toIndentedString(indent) + ",\n"
                + indent + this.sx + ", " + this.sy + ")";
    }
}