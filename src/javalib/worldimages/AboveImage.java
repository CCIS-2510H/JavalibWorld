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
 * A class representing positioning images on top of one another
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class AboveImage extends OverlayOffsetAlignBase {

    private AboveImage(WorldImage im1, WorldImage im2) {
        super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, im1, 0, im1.getHeight()
                / 2 + im2.getHeight() / 2, im2);
    }

    /**
     * Position an image above another image
     * 
     * @param im1
     *            -- Top image
     * @param ims
     *            -- Bottom image(s)
     */
    public AboveImage(WorldImage im1, WorldImage... ims) {
        this(im1, multipleImageHandling(ims));
    }

    /**
     * Position an image above another image
     * 
     * @param im1
     *            -- Top image
     * @param ims
     *            -- Bottom image(s)
     */
    public AboveImage(String mode, WorldImage im1, WorldImage... ims) {
        this(im1, ims);
    }

    /**
     * Combine many images into a series of <code>AboveImages</code>
     */
    private static WorldImage multipleImageHandling(WorldImage[] ims) {
        if (ims.length == 0)
            throw new IllegalArgumentException("Cannot call AboveImage constructor with fewer than two images");
        if (ims.length <= 1) {
            return ims[0];
        } else {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            return new AboveImage(ims[0], images);
        }
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.im1 = "
                + this.top.toIndentedString(indent) + ",\n" + indent
                + "this.im2 = " + this.bot.toIndentedString(indent) + ")\n";
    }

    /**
     * Produce a <code>String</code> representation of this overlaid image
     */
    @Override
    public String toString() {
        return className(this) + "\nthis.im1 = " + this.top.toString()
                + ",\nthis.im2 = " + this.bot.toString() + ")\n";
    }

    /**
     * Is this <code>AboveImage</code> same as the given object?
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof AboveImage && this.same((AboveImage) o);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new AboveImage(this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}
