package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
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
public class OverlayImagesXY extends WorldImage {

    /** the bottom image for the combined image */
    public WorldImage bot;

    /** the top image for the combined image */
    public WorldImage top;

    /** the horizontal offset for the top image */
    public int dx;

    /** the vertical offset for the top image */
    public int dy;

    private Posn center;

    private int width, height;

    /**
     * The full constructor that produces the top image overlaid over the bottom
     * one with the given offset.
     * 
     * 
     * 
     * @param bot
     *            the bottom image for the combined image
     * @param top
     *            the bottom image for the combined image
     * @param dx
     *            the horizontal offset for the top image
     * @param dy
     *            the vertical offset for the top image
     */
    public OverlayImagesXY(WorldImage top, WorldImage bot, int dx, int dy) {
        super();
        this.bot = bot;
        this.top = top;
        this.dx = dx;
        this.dy = dy;

        // Calculate proper center point
        int botWidth = this.bot.getWidth();
        int topWidth = this.top.getWidth();
        int botHeight = this.bot.getHeight();
        int topHeight = this.top.getHeight();
        int rightX = Math.max(botWidth / 2, this.dx + (topWidth / 2));
        int leftX = Math.min(-botWidth / 2, this.dx - (topWidth / 2));
        int bottomY = Math.max(botHeight / 2, this.dy + (topHeight / 2));
        int topY = Math.min(-botHeight / 2, this.dy - (topHeight / 2));
        this.center = new Posn((rightX + leftX) / 2, (bottomY + topY) / 2);
        this.width = Math.abs(rightX) + Math.abs(leftX);
        this.height = Math.abs(bottomY) + Math.abs(topY);
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    public void draw(Graphics2D g) {
        // Save the old transform state
        AffineTransform old = g.getTransform();

        // draw the two objects
        g.translate(-this.center.x, -this.center.y);
        this.bot.draw(g);
        g.translate(this.dx, this.dy);
        this.top.draw(g);

        // Reset the transformation matrix
        g.setTransform(old);
    }

    /**
     * Produce the width of this image
     * 
     * @return the width of this image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Produce the height of this image
     * 
     * @return the height of this image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Produce a <code>String</code> representation of this overlay of images
     */
    public String toString() {
        return "new OverlayImagesXY(this.dx = " + this.dx + ", this.dy = "
                + this.dy + "," + "\nthis.width = " + this.width
                + ", this.height = " + this.height + "," + "\nthis.bot = "
                + this.bot.toString() + "\nthis.top = " + this.top.toString()
                + ")\n";
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
        return classNameString(indent, "OverlayImagesXY") + indent
                + "this.dx = " + this.dx + ", this.dy = " + this.dy + "\n"
                + indent + "this.bot = " + this.bot.toString() + "\n" + indent
                + "this.top = " + this.top.toString() + ")\n";
    }

    /**
     * Is this <code>OverlayImagesXY</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof OverlayImagesXY) {
            OverlayImagesXY that = (OverlayImagesXY) o;
            return this.bot.equals(that.bot) && this.top.equals(that.top)
                    && this.dx == that.dx && this.dy == that.dy;
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.dx + this.dy + this.bot.hashCode() + this.top.hashCode();
    }
}