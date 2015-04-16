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
 * A class representing positioning images next to one another
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class BesideImage extends OverlayOffsetAlignBase {

    private BesideImage(WorldImage im1, WorldImage im2) {
        super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, im1, im1.getWidth() / 2
                + im2.getWidth() / 2, 0, im2);
    }

    /**
     * Position an image next to another image
     * 
     * @param im1
     *            -- Left image
     * @param ims
     *            -- Right image(s)
     */
    public BesideImage(WorldImage im1, WorldImage... ims) {
        this(im1, multipleImageHandling(ims));
    }

    /**
     * Combine many images into a series of BesideAlignImages
     */
    private static WorldImage multipleImageHandling(WorldImage[] ims) {
        if (ims.length <= 1) {
            return ims[0];
        } else {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            return new BesideImage(ims[0], images);
        }
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.im1 = "
                + this.top.toIndentedString(indent) + ",\n" + indent
                + "this.im2 = " + this.bot.toIndentedString(indent) + ")";
    }

    /**
     * Produce a <code>String</code> representation of this overlaid image
     */
    @Override
    public String toString() {
        return className(this) + "this.im1 = " + this.top.toString()
                + ",\nthis.im2 = " + this.bot.toString() + ")";
    }

    /**
     * Is this <code>BesideAlignImage</code> same as the given object?
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof BesideImage && this.same((BesideImage) o);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new BesideImage(this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}
