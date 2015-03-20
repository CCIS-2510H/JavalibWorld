package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ScaleImageXY extends WorldImage {
    public WorldImage img;
    public double scaleX, scaleY;

    public ScaleImageXY(WorldImage img, double scaleX, double scaleY) {
        super();
        this.img = img;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.scale(this.scaleX, this.scaleY);
        return this.img.getBB(newT);
    }
    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
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

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return "new ScaleImageXY(this.scaleX=" + this.scaleX + ", this.scaleY="
                + this.scaleY + ")\n";
    }

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, "ScaleImageXY") + "this.img = "
                + this.img.toIndentedString(indent) + "\n" + indent + ")\n";
    }

    /**
     * Is this <code>ScaleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof ScaleImageXY) {
            ScaleImageXY that = (ScaleImageXY) o;
            return this.scaleX == that.scaleX && this.scaleY == that.scaleY
                    && this.img.equals(that.img);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return (int) (this.scaleX * 42 + this.scaleY * -57);
    }

    @Override
    public int getWidth() {
        return (int) Math.round(this.img.getWidth() * this.scaleX);
    }

    @Override
    public int getHeight() {
        return (int) Math.round(this.img.getHeight() * this.scaleY);
    }
}
