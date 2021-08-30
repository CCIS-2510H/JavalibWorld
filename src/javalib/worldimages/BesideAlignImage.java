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

import java.util.Stack;

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
        super(AlignModeX.LEFT, mode, im1, im1.getWidth(), 0, im2);
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
            return new EmptyImage();
        if (ims.length <= 1) {
            return ims[0];
        } else {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            return new BesideAlignImage(mode, ims[0], images);
        }
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.mode = ").append(this.alignY).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("im1", this.top),
                        new ImageField("im2", this.bot)));
        return sb;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new BesideAlignImage(this.alignY, this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}
