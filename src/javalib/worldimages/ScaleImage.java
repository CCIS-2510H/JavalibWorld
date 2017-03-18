package javalib.worldimages;

/**
 * Class representing the scaling of an image
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class ScaleImage extends ScaleImageXYBase {

    /**
     * Scale the image
     *
     * @param img
     *            -- Image to scale
     * @param scale
     *            -- Scale amount
     */
    public ScaleImage(WorldImage img, double scale) {
        super(img, scale, scale);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ScaleImage(this.img, this.scaleX);
        i.pinhole = p;
        return i;
    }
}