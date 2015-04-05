package javalib.worldimages;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent an overlay of the top image on the bottom one combined
 * into <code>{@link WorldImage WorldImage}</code> to be drawn by the world when
 * drawing on its <code>Canvas</code>.
 * </p>
 * 
 * <p>
 * A convenience class that extends the
 * <code>{@link OverlayOffsetAlign OverlayOffsetAlign}</code> by invoking its
 * constructor with <code>alignX = PINHOLE</code>, <code>alignY = PINHOLE</code>, <code>dx = 0</code>, and <code>dy = 0</code>.
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public final class OverlayImage extends OverlayOffsetAlignBase {

    /**
     * The only constructor - invokes the constructor in the super class
     * <code>{@link OverlayOffsetAlign OverlayOffsetAlign}</code> with no
     * offset.
     * 
     * @param bot
     *            the bottom image for the combined image
     * @param top
     *            the bottom image for the combined image
     */
    public OverlayImage(WorldImage top, WorldImage bot) {
        super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, top, 0, 0, bot);
    }

    /**
     * Produce a <code>String</code> representation of this overlay of images
     */
    public String toString() {
        return className(this) + "this.bot = " + this.bot.toString()
                + "\nthis.top = " + this.top.toString() + ")\n";
    }

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + indent + "this.bot = "
                + this.bot.toIndentedString(indent) + "\n" + indent
                + "this.top = " + this.top.toIndentedString(indent) + ")\n";
    }

    /**
     * Is this <code>OverlayImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof OverlayImage && this.same((OverlayImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.bot.hashCode() + this.top.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayImage(this.top, this.bot);
        i.pinhole = p;
        return i;
    }
}