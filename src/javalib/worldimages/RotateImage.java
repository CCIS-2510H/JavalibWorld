package javalib.worldimages;

import java.awt.geom.AffineTransform;

/**
 * Class representing the rotation of an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class RotateImage extends TransformImageBase {
    private double rotationDegrees;
    /**
     * Rotate the image
     * 
     * @param img
     *            -- Image to rotate
     * @param rotationDegrees
     *            -- Degrees to rotate the image
     */
    public RotateImage(WorldImage img, double rotationDegrees) {
        super(img, AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)));
        this.rotationDegrees = rotationDegrees;
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
                + "this.rotationDegrees = " + this.rotationDegrees + ")\n";
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
        return (int)(this.rotationDegrees * 1000);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new RotateImage(this.img, this.rotationDegrees);
        i.pinhole = p;
        return i;
    }
}
