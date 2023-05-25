package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Objects;
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
    public final double sx;

    /** the shear amount along the y axis */
    public final double sy;

    /**
     * Shear the image
     * 
     * @param img
     *            -- Image to shear
     * @param sx
     *            -- Shear along the X axis
     * @param sy
     *            -- Shear along the Y axis
     * @throws NullPointerException if img is null
     */
    public ShearedImage(WorldImage img, double sx, double sy) {
        super(Objects.requireNonNull(img, "Sheared image cannot be null"),
                AffineTransform.getShearInstance(sx, sy));
        this.sx = sx;
        this.sy = sy;
    }

    private ShearedImage(WorldImage img, double sx, double sy, Posn pinhole) {
        super(Objects.requireNonNull(img, "Sheared image cannot be null"),
                AffineTransform.getShearInstance(sx, sy),
                pinhole);
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.sx = ").append(this.sx).append(", ")
               .append("this.sy = ").append(this.sy).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("img", this.img)));
        return sb;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new ShearedImage(this.img, this.sx, this.sy, p);
    }
}