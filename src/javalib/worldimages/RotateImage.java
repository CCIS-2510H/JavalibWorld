package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

public class RotateImage extends WorldImage {
    public WorldImage img;
    public double rotationDegrees;
    private int width, height;

    protected RotateImage(Posn pinhole, WorldImage img, double rotationDegrees) {
        super(pinhole, Color.black);
        this.img = img;
        this.rotationDegrees = rotationDegrees;
        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    public RotateImage(WorldImage img, int rotationDegrees) {
        this(new Posn(0, 0), img, rotationDegrees);
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    @Override
    public void drawAt(Graphics2D g, int x, int y) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(this.color);
        // draw the object
        AffineTransform old = g.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.setToRotation(Math.toRadians(this.rotationDegrees), x, y);
        g.setTransform(trans);

        // draw rotated shape/image
        this.img.drawAt(g, x, y);

        g.setTransform(old);
        // things you draw after here will not be rotated
        //
        // new RectangleImage(this.width, this.height, "outline", Color.black)
        // .drawAt(g, x, y);
        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return "new RectangleImage(this.pinhole = (" + this.pinhole.x + ", "
                + this.pinhole.y + "), \nthis.color = " + this.color.toString()
                + "\nthis.width = " + width + ", this.height = " + height
                + ")\n";
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
        return classNameString(indent, "RectangleImage")
                + pinholeString(indent, this.pinhole)
                + colorString(indent, this.color) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    /**
     * Is this <code>RectangleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof RectangleImage) {
            RectangleImage that = (RectangleImage) o;
            return this.pinhole.x == that.pinhole.x
                    && this.pinhole.y == that.pinhole.y
                    && this.width == that.width && this.height == that.height
                    && this.color.equals(that.color);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.pinhole.x + this.pinhole.y + this.color.hashCode()
                + this.width + this.height;
    }

    @Override
    public WorldImage getMovedImage(int dx, int dy) {
        return this;
    }

    @Override
    public WorldImage getMovedTo(Posn p) {
        return this;
    }

    @Override
    public int getWidth() {
        return (int) Math.round(Math.abs(this.img.getHeight()
                * Math.sin(Math.toRadians(this.rotationDegrees))) + Math
                .abs(this.img.getWidth()
                        * Math.cos(Math.toRadians(this.rotationDegrees))));
    }

    @Override
    public int getHeight() {
        return (int) Math.round(Math.abs(this.img.getWidth()
                * Math.sin(Math.toRadians(this.rotationDegrees))) + Math
                .abs(this.img.getHeight()
                        * Math.cos(Math.toRadians(this.rotationDegrees))));
    }
}
