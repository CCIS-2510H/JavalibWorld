package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Stack;

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


    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.sx = ").append(this.sx).append(", ")
               .append("this.sy = ").append(this.sy).append(",");
        stack.push(
                new FieldsWLItem(
                        new ImageField("img", this.img)));
        return sb;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ShearedImage(this.img, this.sx, this.sy);
        i.pinhole = p;
        return i;
    }
}