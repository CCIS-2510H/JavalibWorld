package javalib.worldimages;

/**
 * <p>
 * Copyright 2015 Ben Lerner
 * </p>
 * <p>
 * This program is distributed under the terms of the GNU Lesser General Public
 * License (LGPL)
 * </p>
 */

/**
 * A class representing positioning images next to one another, with specified
 * alignment along the Y axis
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class BesideAlignImage extends OverlayOffsetAlignBase {

    private BesideAlignImage(AlignModeY mode, WorldImage im1, WorldImage im2) {
        super(AlignModeX.PINHOLE, mode, im1, im1.getWidth() / 2.0
                + im2.getWidth() / 2.0, 0, im2);
    }

    /**
     * Position an image next to another image
     * 
     * @param mode
     *            -- Alignment along the Y axis. Top, Bottom, center, middle,
     *            pinhole.
     * @param im1
     *            -- Left image
     * @param ims
     *            -- Right image(s)
     */
    public BesideAlignImage(AlignModeY mode, WorldImage im1, WorldImage... ims) {
        this(mode, im1, multipleImageHandling(mode, ims));
    }

    /**
     * Position an image next to another image
     * 
     * @param mode
     *            -- Alignment along the Y axis. Top, Bottom, center, middle,
     *            pinhole.
     * @param im1
     *            -- Left image
     * @param ims
     *            -- Right image(s)
     */
    public BesideAlignImage(String mode, WorldImage im1, WorldImage... ims) {
        this(AlignModeY.fromString(mode), im1, ims);
    }

    /**
     * Combine many images into a series of BesideAlignImages
     */
    private static WorldImage multipleImageHandling(AlignModeY mode,
            WorldImage[] ims) {
        if (ims.length == 0)
            throw new IllegalArgumentException("Cannot call BesideAlignImage constructor with fewer than two images");
        if (ims.length <= 1) {
            return ims[0];
        } else {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            return new BesideAlignImage(mode, ims[0], images);
        }
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.mode = " + this.alignY
                + ",\n" + indent + "this.im1 = "
                + this.top.toIndentedString(indent) + ",\n" + indent
                + "this.im2 = " + this.bot.toIndentedString(indent) + ")\n";
    }

    /**
     * Produce a <code>String</code> representation of this overlaid image
     */
    @Override
    public String toString() {
        return className(this) + "this.mode = " + this.alignY
                + ",\nthis.im1 = " + this.top.toString() + ",\nthis.im2 = "
                + this.bot.toString() + ")\n";
    }

    /**
     * Is this <code>BesideAlignImage</code> same as the given object?
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof BesideAlignImage && this.same((BesideAlignImage) o);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new BesideAlignImage(this.alignY, this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}
