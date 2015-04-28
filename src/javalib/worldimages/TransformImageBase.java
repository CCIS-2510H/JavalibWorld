package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

abstract public class TransformImageBase extends WorldImage {

    /** the image to scale */
    public WorldImage img;

    /** the transformation for that image */
    public AffineTransform tx;
    
    TransformImageBase(WorldImage img, AffineTransform tx) {
        super(1 + img.depth);
        this.img = img;
        this.tx = tx;
        Point2D p = WorldImage.transformPosn(tx, img.pinhole);
        this.pinhole = new DPosn(p.getX(), p.getY()).asPosn();
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
    public void draw(Graphics2D g) {
        if (this.getWidth() <= 0)
            return;
        if (this.getHeight() <= 0)
            return;

        // draw the object
        AffineTransform old = g.getTransform();
        g.transform(this.tx);

        // draw rotated shape/image
        this.img.draw(g);

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
    public double getWidth() {
        return this.getBB().getWidth();
    }

    @Override
    public double getHeight() {
        return this.getBB().getHeight();
    }
}
