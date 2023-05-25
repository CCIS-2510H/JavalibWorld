package javalib.worldimages;

import java.util.Objects;

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
     * @throws NullPointerException if img is null
     */
    public ScaleImage(WorldImage img, double scale) {
        super(img, scale, scale);
    }

    private ScaleImage(WorldImage img, double scale, Posn pinhole) {
        super(img, scale, scale, pinhole);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new ScaleImage(this.img, this.scaleX, p);
    }
}