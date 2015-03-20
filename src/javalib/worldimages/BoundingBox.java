package javalib.worldimages;

public class BoundingBox {
    Posn topLeft, botRight;
    BoundingBox(Posn tl, Posn br) {
        this(tl.x, tl.y, br.x, br.y);
    }
    BoundingBox(int tlx, int tly, int brx, int bry) {
        this.topLeft = new Posn(Math.min(tlx, brx), Math.min(tly, bry));
        this.botRight = new Posn(Math.max(tlx, brx), Math.max(tly, bry));
    }
    BoundingBox combine(BoundingBox other) {
        return new BoundingBox(
            Math.min(this.topLeft.x, other.topLeft.x),
            Math.min(this.topLeft.y, other.topLeft.y),
            Math.max(this.botRight.x, other.botRight.x),
            Math.max(this.botRight.y, other.botRight.y));
    }
    BoundingBox add(Posn p) {
        if (p.x >= this.topLeft.x && p.x <= this.botRight.x &&
            p.y >= this.topLeft.y && p.y <= this.botRight.y) {
            return this;
        } else {
            return new BoundingBox(
                Math.min(this.topLeft.x, p.x),
                Math.min(this.topLeft.y, p.y),
                Math.max(this.botRight.x, p.x),
                Math.max(this.botRight.y, p.y));
        }
    }
    int getWidth() { return this.botRight.x - this.topLeft.x; }
    int getHeight() { return this.botRight.y - this.topLeft.y; }
    @Override
    public String toString() {
        return String.format("BB((%d,%d)-(%d,%d))", this.topLeft.x, this.topLeft.y, this.botRight.x, this.botRight.y);
    }
}
