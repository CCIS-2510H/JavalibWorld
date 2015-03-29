package javalib.worldimages;

import java.awt.geom.Point2D;

public final class BoundingBox {
    double tlx, tly, brx, bry;

    BoundingBox(Posn tl, Posn br) {
        this(tl.x, tl.y, br.x, br.y);
    }

    BoundingBox(Point2D tl, Point2D br) {
        this(tl.getX(), tl.getY(), br.getX(), br.getY());
    }

    BoundingBox(double tlx, double tly, double brx, double bry) {
        this.tlx = Math.min(tlx, brx);
        this.tly = Math.min(tly, bry);
        this.brx = Math.max(tlx, brx);
        this.bry = Math.max(tly, bry);
    }

    BoundingBox combine(BoundingBox other) {
        return new BoundingBox(Math.min(this.tlx, other.tlx), Math.min(
                this.tly, other.tly), Math.max(this.brx, other.brx), Math.max(
                this.bry, other.bry));
    }

    BoundingBox add(Posn p) {
        return this.add(p.x, p.y);
    }

    BoundingBox add(Point2D p) {
        return this.add(p.getX(), p.getY());
    }

    BoundingBox add(double px, double py) {
        if (px >= this.tlx && px <= this.brx && py >= this.tly
                && py <= this.bry) {
            return this;
        } else {
            return new BoundingBox(Math.min(this.tlx, px), Math.min(this.tly,
                    py), Math.max(this.brx, px), Math.max(this.bry, py));
        }
    }

    double getWidth() {
        return this.brx - this.tlx;
    }

    double getHeight() {
        return this.bry - this.tly;
    }

    @Override
    public String toString() {
        return String.format("BB((%d,%d)-(%d,%d))", this.tlx, this.tly,
                this.brx, this.bry);
    }
}
