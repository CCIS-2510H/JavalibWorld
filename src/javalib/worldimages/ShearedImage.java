package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javalib.colors.*;

class ShearedImage extends WorldImage {
    WorldImage img;
    Posn shearOrigin;
    double sx, sy;

    ShearedImage(WorldImage img, Posn shearOrigin, double sx, double sy) {
        super(img.pinhole, new White());
        this.shearOrigin = shearOrigin;
        this.img = img;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void draw(Graphics2D ctx) {
        AffineTransform t = ctx.getTransform();
        ctx.translate(this.shearOrigin.x, this.shearOrigin.y);
        ctx.shear(this.sx, this.sy);
        ctx.translate(-this.shearOrigin.x, -this.shearOrigin.y);
        this.img.draw(ctx);
        ctx.setTransform(t);
    }

    @Override
    public int getHeight() {
        return (int) (this.img.getHeight() * (1.0 + this.sy));
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
    public void movePinhole(int dx, int dy) {
        super.movePinhole(dx, dy);
        this.img.movePinhole(dx, dy);
        this.shearOrigin.x += dx;
        this.shearOrigin.y += dy;
    }

    @Override
    public void moveTo(Posn p) {
        super.moveTo(p);
        this.img.moveTo(p);
        this.shearOrigin.x += p.x - this.pinhole.x;
        this.shearOrigin.y += p.y - this.pinhole.y;
    }

    @Override
    public int getWidth() {
        return (int) (this.img.getWidth() * (1.0 + this.sx));
    }

    @Override
    public String toIndentedString(String arg0) {
        return "new ShearedImage(" + this.img.toIndentedString(arg0) + ",\n"
                + arg0 + "new Posn(" + this.shearOrigin.x + ", "
                + this.shearOrigin.y + "),\n" + arg0 + this.sx + ", " + this.sy
                + ")";
    }
}