package javalib.worldimages;

import java.awt.geom.AffineTransform;

/**
 * Class representing the shearing of an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class ShearedImage extends TransformImageBase {
    /** the shear amount along the x axis */
    public double sx;

    /** the shear amount along the y axis */
    public double sy;

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
        super(img, AffineTransform.getShearInstance(sx, sy));
        this.sx = sx;
        this.sy = sy;
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