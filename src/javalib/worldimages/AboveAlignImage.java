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
 * A class representing positioning images on top of one another, with specified
 * alignment along the X axis
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 * 
 */
public final class AboveAlignImage extends OverlayOffsetAlignBase {

    private AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage im2) {
        super(mode, AlignModeY.TOP, im1, 0, im1.getHeight(), im2);
    }

    /**
     * Position an image above another image
     * 
     * @param mode
     *            -- Alignment along the X axis. Left, right, center, middle,
     *            pinhole.
     * @param im1
     *            -- Top image
     * @param ims
     *            -- Bottom image(s)
     */
    public AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage... ims) {
        this(mode, im1, multipleImageHandling(mode, ims));
    }

    /**
     * Position an image above another image
     * 
     * @param mode
     *            -- Alignment along the X axis. Left, right, center, middle,
     *            pinhole.
     * @param im1
     *            -- Top image
     * @param ims
     *            -- Bottom image(s)
     */
    public AboveAlignImage(String mode, WorldImage im1, WorldImage... ims) {
        this(AlignModeX.fromString(mode), im1, ims);
    }

    /**
     * Combine many images into a series of <code>AboveAlignImages</code>
     */
    private static WorldImage multipleImageHandling(AlignModeX mode,
            WorldImage[] ims) {
        if (ims.length == 0)
            return new EmptyImage();    
        if (ims.length <= 1) {
            return ims[0];
        } else {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            return new AboveAlignImage(mode, ims[0], images);
        }
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.mode = ").append(this.alignX).append(",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("im1", this.top),
                        new ImageField("im2", this.bot)));
        return sb;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new AboveAlignImage(this.alignX, this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}
