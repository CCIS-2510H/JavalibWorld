package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Class representing the shearing of an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class ShearedImage extends WorldImage {
    WorldImage img;
    double sx, sy;

    /**
     * Shear the image
     * 
     * @param img
     *            -- Image to shear
     * @param sx
     *            -- Shear along the X axis
     * @param sy
     *            -- Shear along the Y axis
     */
    public ShearedImage(WorldImage img, double sx, double sy) {
        super();
        this.img = img;
        this.sx = sx;
        this.sy = sy;

        // Shear the pinhole
        AffineTransform newT = new AffineTransform();
        newT.shear(this.sx, this.sy);
        Point2D p = WorldImage.transformPosn(newT, img.pinhole);
        this.pinhole = new Posn((int) Math.round(p.getX()), (int) Math.round(p
                .getY()));
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform newT = new AffineTransform(t);
        newT.shear(this.sx, this.sy);
        return this.img.getBB(newT);
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform old = g.getTransform();
        g.shear(this.sx, this.sy);
        this.img.draw(g);
        g.setTransform(old);
    }

    @Override
    public int getWidth() {
        return (int) (this.img.getHeight() * Math.abs(this.sx)
                + this.img.getWidth() + 1.0);
    }

    @Override
    public int getHeight() {
        return (int) (this.img.getWidth() * Math.abs(this.sy)
                + this.img.getHeight() + 1.0);
    }

    /**
     * Produce a <code>String</code> representation of this sheared image
     */
    @Override
    public String toString() {
        return className(this) + this.img.toString() + ",\n" + "this.sx = "
                + this.sx + ",\n" + "this.sy = " + this.sy + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        return classNameString(indent, this)
                + this.img.toIndentedString(indent) + ",\n" + indent
                + "this.sx = " + this.sx + ",\n" + indent + "this.sy = "
                + this.sy + ")\n";
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ShearedImage(this.img, this.sx, this.sy);
        i.pinhole = p;
        return i;
    }
}