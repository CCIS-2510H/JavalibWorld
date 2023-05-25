package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.Stack;

abstract public class TransformImageBase extends WorldImage {

    /** the image to scale */
    public final WorldImage img;

    /** the transformation for that image */
    public final AffineTransform tx;
    
    TransformImageBase(WorldImage img, AffineTransform tx) {
        this(Objects.requireNonNull(img, "Transformed image cannot be null"),
                Objects.requireNonNull(tx, "Transformation cannot be null"),
                transformPosnAsPosn(tx, img.pinhole));
    }
    TransformImageBase(WorldImage img, AffineTransform tx, Posn pinhole) {
        super(pinhole,1 + Objects.requireNonNull(img, "Transformed image cannot be null").depth);
        this.img = img;
        this.tx = Objects.requireNonNull(tx, "Transformation cannot be null");
    }
    private static Posn transformPosnAsPosn(AffineTransform tx, Posn p) {
        Point2D p2d = WorldImage.transformPosn(tx, p);
        return new DPosn(p2d.getX(), p2d.getY()).asPosn();

    }
    
    @Override
    int numKids() {
        return 1;
    }
    @Override
    WorldImage getKid(int i) {
        if (i == 0) { return this.img; }
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        if (i == 0) { return this.tx; }
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        AffineTransform temp = new AffineTransform(t);
        temp.concatenate(this.tx);
        return this.img.getBB(temp);
    }

    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        // draw the object
        AffineTransform old = g.getTransform();
        g.transform(this.tx);

        // draw rotated shape/image
        this.img.drawStackUnsafe(g);

        // Reset the transform to the old transform
        g.setTransform(old);
    }

    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        if (this.getWidth() <= 0)
            return;
        if (this.getHeight() <= 0)
            return;
        images.push(this.img);
        AffineTransform temp = g.getTransform();
        temp.concatenate(this.tx);
        txs.push(temp);
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (this.getClass().equals(other.getClass())) {
            TransformImageBase that = (TransformImageBase) other;
            if (this.tx.equals(that.tx) && this.pinhole.equals(that.pinhole)) {
                worklistThis.push(this.img);
                worklistThat.push(that.img);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getWidth() {
        return this.getBB().getWidth();
    }

    @Override
    public double getHeight() {
        return this.getBB().getHeight();
    }
}
