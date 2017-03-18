package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Stack;

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

    @Override
    protected boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist) {
        if (other instanceof RotateImage){
            RotateImage that = (RotateImage)other;
            if (Math.abs(this.rotationDegrees - that.rotationDegrees) < 0.00001) {
                worklist.push(new ImagePair(this.img, that.img));
            }
        }
        return false;
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
