package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

public class ScaleImageXY extends WorldImage {
    public WorldImage img;
    public double scaleX, scaleY;

    protected ScaleImageXY(Posn pinhole, WorldImage img, double scaleX,
            double scaleY) {
        super(pinhole, Color.black);
        this.img = img;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public ScaleImageXY(WorldImage img, double scaleX, double scaleY) {
        this(new Posn(0, 0), img, scaleX, scaleY);
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    @Override
    public void drawAt(Graphics2D g, int x, int y) {
        if (this.getWidth() <= 0)
            return;
        if (this.getHeight() <= 0)
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
        trans.scale(this.scaleX, this.scaleY);
        g.setTransform(trans);

        // draw scaled shape/image
        this.img.drawAt(g, (int) Math.round(x / this.scaleX),
                (int) Math.round(y / this.scaleY));

        // reset the original paint/scale
        g.setTransform(old);
        g.setPaint(oldPaint);
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return "new ScaleImageXY(this.pinhole = (" + this.pinhole.x + ", "
                + this.pinhole.y + "), \nthis.color = " + this.color.toString()
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
        return classNameString(indent, "ScaleImageXY")
                + pinholeString(indent, this.pinhole) + "this.img = "
                + this.img.toIndentedString(indent) + "\n" + indent + ")\n";
    }

    /**
     * Is this <code>ScaleImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof ScaleImageXY) {
            ScaleImageXY that = (ScaleImageXY) o;
            return this.pinhole.x == that.pinhole.x
                    && this.pinhole.y == that.pinhole.y
                    && this.scaleX == that.scaleX && this.scaleY == that.scaleY
                    && this.img.equals(that.img);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.pinhole.x + this.pinhole.y + this.color.hashCode();
    }

    @Override
    public WorldImage getMovedImage(int dx, int dy) {
        return getMovedTo(new Posn(this.pinhole.x + dx, this.pinhole.y + dy));
    }

    @Override
    public WorldImage getMovedTo(Posn p) {
        return new ScaleImageXY(p, this.img, this.scaleX, this.scaleY);
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
