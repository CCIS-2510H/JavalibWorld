package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public final class RotateImage extends WorldImage {
    public WorldImage img;
    public double rotationDegrees;
    private int width, height;

    public RotateImage(WorldImage img, double rotationDegrees) {
        super();
        this.img = img;
        this.rotationDegrees = rotationDegrees;

        // Set width & height
        this.width = (int) Math.round(Math.abs(this.img.getHeight()
                * Math.sin(Math.toRadians(this.rotationDegrees)))
                + Math.abs(this.img.getWidth()
                        * Math.cos(Math.toRadians(this.rotationDegrees))));
        this.height = (int) Math.round(Math.abs(this.img.getWidth()
                * Math.sin(Math.toRadians(this.rotationDegrees)))
                + Math.abs(this.img.getHeight()
                        * Math.cos(Math.toRadians(this.rotationDegrees))));

        // Rotate the pinhole
        AffineTransform newT = new AffineTransform();
        newT.rotate(Math.toRadians(this.rotationDegrees));
        Point2D p = WorldImage.transformPosn(newT, img.pinhole);
        this.pinhole = new Posn((int) Math.round(p.getX()), (int) Math.round(p
                .getY()));
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.rotate(Math.toRadians(this.rotationDegrees));
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

    /**
     * Produce a <code>String</code> representation of this rotated image
     */
    public String toString() {
        return "new RotateImage(this.img = " + this.img.toString()
                + ", this.rotationDegrees = " + this.rotationDegrees + ")\n";
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
        return classNameString(indent, "RotateImage") + "this.img = "
                + this.img.toIndentedString(indent) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    /**
     * Is this <code>RotateImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof RotateImage) {
            RotateImage that = (RotateImage) o;
            return this.width == that.width && this.height == that.height
                    && this.rotationDegrees == that.rotationDegrees
                    && this.img.equals(that.img);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.width + this.height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new RotateImage(this.img, this.rotationDegrees);
        i.pinhole = p;
        return i;
    }
}
