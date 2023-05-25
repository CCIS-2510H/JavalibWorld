package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Objects;
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
    private final double rotationDegrees;
    /**
     * Rotate the image
     * 
     * @param img
     *            -- Image to rotate
     * @param rotationDegrees
     *            -- Degrees to rotate the image
     * @throws NullPointerException if img is null
     */
    public RotateImage(WorldImage img, double rotationDegrees) {
        super(
                Objects.requireNonNull(img, "Rotated image cannot be null"),
                AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)));
        this.rotationDegrees = rotationDegrees;
    }

    private RotateImage(WorldImage img, double rotationDegrees, Posn pinhole) {
        super(
                Objects.requireNonNull(img, "Rotated image cannot be null"),
                AffineTransform.getRotateInstance(Math.toRadians(rotationDegrees)),
                pinhole);
        this.rotationDegrees = rotationDegrees;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.rotationDegrees = ").append(this.rotationDegrees).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("img", this.img)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof RotateImage && this.pinhole.equals(other.pinhole)){
            RotateImage that = (RotateImage)other;
            if (Math.abs(this.rotationDegrees - that.rotationDegrees) < 0.00001) {
                worklistThis.push(this.img);
                worklistThat.push(that.img);
                return true;
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
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new RotateImage(this.img, this.rotationDegrees, p);
    }
}
