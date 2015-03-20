package javalib.worldimages;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
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
 * <code>{@link OverlayImages OverlayImages}</code> by invoking its constructor
 * with <code>dx = 0</code> and <code>dy = 0</code>.
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class OverlayImages extends OverlayImagesXY {

    /**
     * The only constructor - invokes the constructor in the super class
     * <code>{@link OverlayImages OverlayImages}</code> with no offset.
     * 
     * @param bot
     *            the bottom image for the combined image
     * @param top
     *            the bottom image for the combined image
     */
    public OverlayImages(WorldImage top, WorldImage bot) {
        super(top, 0, 0, bot);
    }

    /**
     * Produce a <code>String</code> representation of this overlay of images
     */
    public String toString() {
        return "new OverlayImages(this.bot = " + this.bot.toString()
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
        return classNameString(indent, "OverlayImages") + indent
                + "this.bot = " + this.bot.toIndentedString(indent) + "\n"
                + indent + "this.top = " + this.top.toIndentedString(indent)
                + indent + ")\n";
    }

    /**
     * Is this <code>OverlayImages</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof OverlayImages) {
            OverlayImages that = (OverlayImages) o;
            return this.bot.equals(that.bot) && this.top.equals(that.top);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.bot.hashCode() + this.top.hashCode();
    }
}