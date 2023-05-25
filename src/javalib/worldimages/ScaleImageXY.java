package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Objects;
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
     * @throws NullPointerException if img is null
     */
    public ScaleImageXY(WorldImage img, double scaleX, double scaleY) {
        super(img, scaleX, scaleY);
    }

    private ScaleImageXY(WorldImage img, double scaleX, double scaleY, Posn pinhole) {
        super(img, scaleX, scaleY, pinhole);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new ScaleImageXY(this.img, this.scaleX, this.scaleY, p);
    }
}

abstract class ScaleImageXYBase extends TransformImageBase {
    /** the x axis scale amount */
    public final double scaleX;

    /** the y axis scale amount */
    public final double scaleY;

    /**
     * Scale the image
     * 
     * @param img
     *            -- Image to scale
     * @param scaleX
     *            -- amount to scale on the X axis
     * @param scaleY
     *            -- amount to scale on the Y axis
     * @throws NullPointerException if img is null
     */
    public ScaleImageXYBase(WorldImage img, double scaleX, double scaleY) {
        super(Objects.requireNonNull(img, "Scaled image cannot be null"),
                AffineTransform.getScaleInstance(scaleX, scaleY));
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public ScaleImageXYBase(WorldImage img, double scaleX, double scaleY, Posn pinhole) {
        super(Objects.requireNonNull(img, "Scaled image cannot be null"),
                AffineTransform.getScaleInstance(scaleX, scaleY),
                pinhole);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.scaleX = ").append(this.scaleX).append(", ")
               .append("this.scaleY = ").append(this.scaleY).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("img", this.img)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (this.getClass().equals(other.getClass())
                && this.pinhole.equals(other.pinhole)){
            // Check for exact class matching, and then casting to the base class is safe
            ScaleImageXYBase that = (ScaleImageXYBase) other;
            if (Math.abs(this.scaleX- that.scaleX) < 0.00001 &&
                    Math.abs(this.scaleY - that.scaleY) < 0.00001) {
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
        return (int) (this.scaleX * 42 + this.scaleY * -57);
    }
}
