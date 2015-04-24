package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Stack;

/**
 * Class representing the rotation of an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class RotateImage extends WorldImage {

    /** the image to rotate */
    public WorldImage img;

    /** the number of degrees to rotate the image by */
    public double rotationDegrees;

    private double width, height;

    /**
     * Rotate the image
     * 
     * @param img
     *            -- Image to rotate
     * @param rotationDegrees
     *            -- Degrees to rotate the image
     */
    public RotateImage(WorldImage img, double rotationDegrees) {
        super();
        this.img = img;
        this.rotationDegrees = rotationDegrees;

        // Rotate the pinhole
        AffineTransform newT = new AffineTransform();
        newT.rotate(Math.toRadians(this.rotationDegrees));
        Point2D p = WorldImage.transformPosn(newT, img.pinhole);
        this.pinhole = new DPosn(p.getX(), p.getY()).asPosn();

        // Set width & height
        this.width = getBB().getWidth();
        this.height = getBB().getHeight();
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.rotate(Math.toRadians(this.rotationDegrees));
        return this.img.getBB(newT);
    }
    

    @Override
    public void draw(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;

        // draw the object
        AffineTransform old = g.getTransform();
        g.rotate(Math.toRadians(this.rotationDegrees));

        // draw rotated shape/image
        this.img.draw(g);

        // Reset the transform to the old transform
        g.setTransform(old);
    }
    @Override
    protected void drawStackless(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;
        images.push(this.img);
        AffineTransform tx = g.getTransform();
        tx.rotate(Math.toRadians(this.rotationDegrees));
        txs.push(tx);
    }

    /**
     * Produce a <code>String</code> representation of this rotated image
     */
    public String toString() {
        return className(this) + "this.img = " + this.img.toString()
                + ", this.rotationDegrees = " + this.rotationDegrees + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.img = "
                + this.img.toIndentedString(indent) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    public boolean same(RotateImage that) {
        return this.rotationDegrees == that.rotationDegrees
                && this.img.equals(that.img);
    }

    /**
     * Is this <code>RotateImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof RotateImage && this.same((RotateImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return (int) this.width + (int) this.height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new RotateImage(this.img, this.rotationDegrees);
        i.pinhole = p;
        return i;
    }
}
