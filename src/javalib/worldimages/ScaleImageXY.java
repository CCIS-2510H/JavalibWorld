package javalib.worldimages;

import java.awt.geom.AffineTransform;
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
     */
    public ScaleImageXY(WorldImage img, double scaleX, double scaleY) {
        super(img, scaleX, scaleY);
    }
}

abstract class ScaleImageXYBase extends TransformImageBase {
    /** the x axis scale amount */
    public double scaleX;

    /** the y axis scale amount */
    public double scaleY;

    /**
     * Scale the image
     * 
     * @param img
     *            -- Image to scale
     * @param scaleX
     *            -- amount to scale on the X axis
     * @param scaleY
     *            -- amount to scale on the Y axis
     */
    public ScaleImageXYBase(WorldImage img, double scaleX, double scaleY) {
        super(img, AffineTransform.getScaleInstance(scaleX, scaleY));
        this.img = img;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.scaleX = ").append(this.scaleX).append(", ")
               .append("this.scaleY = ").append(this.scaleY).append(",");
        stack.push(
                new FieldsWLItem(
                        new ImageField("img", this.img)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist) {
        if (this.getClass().equals(other.getClass())){
            // Check for exact class matching, and then casting to the base class is safe
            ScaleImageXYBase that = (ScaleImageXYBase) other;
            if (Math.abs(this.scaleX- that.scaleX) < 0.00001 &&
                    Math.abs(this.scaleY - that.scaleY) < 0.00001) {
                worklist.push(new ImagePair(this.img, that.img));
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

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ScaleImageXY(this.img, this.scaleX, this.scaleY);
        i.pinhole = p;
        return i;
    }
}
