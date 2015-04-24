package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 * Class representing the scaling of an image in 2 directions
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class ScaleImageXY extends ScaleImageXYBase {

    /**
     * Scale the image
     * 
     * @param img
     *            -- Image to scale
     * @param scaleX
     *            -- amount to scale on the X axis
     * @param scaleY
     *            -- amount to scale on the Y axis
     */
    public ScaleImageXY(WorldImage img, double scaleX, double scaleY) {
        super(img, scaleX, scaleY);
    }
}

abstract class ScaleImageXYBase extends WorldImage {

    /** the image to scale */
    public WorldImage img;

    /** the x axis scale amount */
    public double scaleX;

    /** the y axis scale amount */
    public double scaleY;

    /**
     * Scale the image
     * 
     * @param img
     *            -- Image to scale
     * @param scaleX
     *            -- amount to scale on the X axis
     * @param scaleY
     *            -- amount to scale on the Y axis
     */
    public ScaleImageXYBase(WorldImage img, double scaleX, double scaleY) {
        super();
        this.img = img;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        // Scale the pinhole
        this.pinhole = new DPosn(img.pinhole.x * this.scaleX, img.pinhole.y
                * this.scaleY).asPosn();
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.scale(this.scaleX, this.scaleY);
        return this.img.getBB(newT);
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (this.getWidth() <= 0)
            return;
        if (this.getHeight() <= 0)
            return;

        // draw the object
        AffineTransform old = g.getTransform();
        g.scale(this.scaleX, this.scaleY);

        // draw scaled shape/image
        this.img.draw(g);

        // reset the original paint/scale
        g.setTransform(old);
    }
    @Override
    protected void drawStackless(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        if (this.getWidth() <= 0)
            return;
        if (this.getHeight() <= 0)
            return;
        images.push(this.img);
        AffineTransform tx = g.getTransform();
        tx.scale(this.scaleX, this.scaleY);
        txs.push(tx);
    }

    /**
     * Produce a <code>String</code> representation of this scaled image
     */
    public String toString() {
        return className(this) + "this.scaleX = " + this.scaleX
                + ", this.scaleY = " + this.scaleY + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.img = "
                + this.img.toIndentedString(indent) + ",\n" + indent
                + "this.scaleX = " + this.scaleX + ",\n" + indent
                + "this.scaleY = " + this.scaleY + ")\n";
    }

    /**
     * Is this <code>ScaleImageXY</code> same as the given object?
     */
    public boolean same(ScaleImageXYBase that) {
        return this.scaleX == that.scaleX && this.scaleY == that.scaleY
                && this.img.equals(that.img);
    }

    /**
     * Is this <code>ScaleImageXY</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof ScaleImageXY && this.same((ScaleImageXY) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return (int) (this.scaleX * 42 + this.scaleY * -57);
    }

    @Override
    public double getWidth() {
        return this.img.getWidth() * this.scaleX;
    }

    @Override
    public double getHeight() {
        return this.img.getHeight() * this.scaleY;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ScaleImageXY(this.img, this.scaleX, this.scaleY);
        i.pinhole = p;
        return i;
    }
}
