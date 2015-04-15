package javalib.worldimages;

/**
 * <p>
 * Copyright 2012 Viera K. Proulx
 * </p>
 * <p>
 * This program is distributed under the terms of the GNU Lesser General Public
 * License (LGPL)
 * </p>
 */

/**
 * <p>
 * The class to represent an overlay of the top image on the bottom one with the
 * top offset by the given <code>dx</code> and <code>dy</code> combined into
 * <code>{@link WorldImage WorldImage}</code> to be drawn by the world when
 * drawing on its <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public final class OverlayOffsetImage extends OverlayOffsetAlignBase {

    /**
     * Overlay Image <code>top</code> on <code>bot</code>, shifting by
     * <code>dx</code> and <code>dy</code>
     * 
     * @param top
     *            -- Top image
     * @param dx
     *            -- Amount to shift the bottom image by along the X axis
     * @param dy
     *            -- Amount to shift the bottom image by along the Y axis
     * @param bot
     *            -- Bottom image
     */
    public OverlayOffsetImage(WorldImage top, double dx, double dy,
            WorldImage bot) {
        super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, top, dx, dy, bot);
    }

    /**
     * Produce a <code>String</code> representation of this overlay of images
     */
    public String toString() {
        return className(this) + "this.top = " + this.top.toString()
                + ",\nthis.dx = " + this.dx + ", this.dy = " + this.dy
                + ",\nthis.bot = " + this.bot.toString() + ")";
    }

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            -- the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.top = "
                + this.top.toIndentedString(indent) + ",\n" + indent
                + "this.dx = " + this.dx + ", this.dy = " + this.dy + ",\n"
                + indent + "this.bot = " + this.bot.toIndentedString(indent)
                + ")";
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayOffsetImage(this.top, this.dx, this.dy,
                this.bot);
        i.pinhole = p;
        return i;
    }

    /**
     * Is this <code>OverlayOffsetImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof OverlayOffsetImage
                && this.same((OverlayOffsetImage) o);
    }
}