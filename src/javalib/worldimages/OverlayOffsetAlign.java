package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public final class OverlayOffsetAlign extends OverlayOffsetAlignBase {

    public OverlayOffsetAlign(AlignModeX alignX, AlignModeY alignY,
            WorldImage top, double dx, double dy, WorldImage bot) {
        super(alignX, alignY, top, dx, dy, bot);
    }

    public OverlayOffsetAlign(String alignX, String alignY, WorldImage top,
            double dx, double dy, WorldImage bot) {
        super(AlignModeX.fromString(alignX), AlignModeY.fromString(alignY),
                top, dx, dy, bot);
    }
}

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
abstract class OverlayOffsetAlignBase extends WorldImage {

    /** the bottom image for the combined image */
    public WorldImage bot;

    /** the top image for the combined image */
    public WorldImage top;

    public Posn deltaTop, deltaBot;

    public AlignModeX alignX;
    public AlignModeY alignY;

    private int width, height;
    private double dx, dy;

    /**
     * Overlays image top on top of bot, using alignX and alignY as the starting
     * points for the overlaying, and then adjusts bot by dx to the right and dy
     * pixels down.
     * 
     * @param top
     *            the bottom image for the combined image
     * @param dx
     *            the horizontal offset for the top image
     * @param dy
     *            the vertical offset for the top image
     * @param bot
     *            the bottom image for the combined image
     */
    public OverlayOffsetAlignBase(AlignModeX alignX, AlignModeY alignY,
            WorldImage top, double dx, double dy, WorldImage bot) {
        super();
        this.bot = bot;
        this.top = top;
        this.alignX = alignX;
        this.alignY = alignY;
        this.dx = dx;
        this.dy = dy;

        // Calculate proper center point
        int botWidth = this.bot.getWidth();
        int topWidth = this.top.getWidth();
        int botHeight = this.bot.getHeight();
        int topHeight = this.top.getHeight();

        int rightX, leftX, topY, bottomY;
        if (alignX == AlignModeX.PINHOLE) {
            rightX = (int) Math.round(Math.max((topWidth / 2)
                    - this.top.pinhole.x, (botWidth / 2) - this.bot.pinhole.x
                    + dx));
            leftX = (int) Math.round(Math.min(
                    -((botWidth / 2) + this.bot.pinhole.x) + dx,
                    -((topWidth / 2) + this.top.pinhole.x)));
        } else {
            rightX = (int) Math.round(Math.max(botWidth / 2, dx
                    + (topWidth / 2)));
            leftX = (int) Math.round(Math.min(-botWidth / 2, dx
                    - (topWidth / 2)));
        }

        if (alignY == AlignModeY.PINHOLE) {
            bottomY = (int) Math.round(Math.max((botHeight / 2)
                    - this.bot.pinhole.y + dy, (topHeight / 2)
                    - this.top.pinhole.y));
            topY = (int) Math.round(Math.min(
                    -((botHeight / 2) + this.bot.pinhole.y) + dy,
                    -((topHeight / 2) + this.top.pinhole.y)));
        } else {
            bottomY = (int) Math.round(Math.max(botHeight / 2, dy
                    + (topHeight / 2)));
            topY = (int) Math.round(Math.min(-botHeight / 2, dy
                    - (topHeight / 2)));
        }

        this.width = rightX - leftX;
        this.height = bottomY - topY;

        Posn center = new Posn((rightX + leftX) / 2, (bottomY + topY) / 2);

        int botDeltaX = -center.x + (int) Math.round(dx), botDeltaY = -center.y
                + (int) Math.round(dy), topDeltaX = botDeltaX, topDeltaY = botDeltaY;
        if (alignX == AlignModeX.PINHOLE) {
            // move to pinhole, then move the based on where the center is
            botDeltaX = (int) (-(this.bot.pinhole.x + center.x) + dx);
            topDeltaX = -(this.top.pinhole.x + center.x);
        }
        if (alignY == AlignModeY.PINHOLE) {
            // move to pinhole, then move the based on where the center is
            botDeltaY = (int) (-(this.bot.pinhole.y + center.y) + dy);
            topDeltaY = -(this.top.pinhole.y + center.y);
        }

        this.deltaBot = new Posn(botDeltaX, botDeltaY);
        this.deltaTop = new Posn(topDeltaX, topDeltaY);

        if (alignY == AlignModeY.PINHOLE && alignX == AlignModeX.PINHOLE
                && dx == 0 && dy == 0) {
            // Set the pinhole
            this.pinhole = new Posn(-center.x, -center.y);
        }
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform temp = new AffineTransform(t);
        temp.translate(this.deltaBot.x, this.deltaBot.y);
        BoundingBox botBox = this.bot.getBB(temp);
        temp.setTransform(t);
        temp.translate(this.deltaTop.x, this.deltaTop.y);
        BoundingBox topBox = this.top.getBB(temp);
        return botBox.combine(topBox);
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
        g.translate(this.deltaBot.x, this.deltaBot.y);
        this.bot.draw(g);
        g.setTransform(old);
        g.translate(this.deltaTop.x, this.deltaTop.y);
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
        return "new OverlayImagesXY(this.deltaBot = "
                + this.deltaBot.toString() + ", this.deltaTop = "
                + this.deltaTop.toString() + "," + "\nthis.width = "
                + this.width + ", this.height = " + this.height + ","
                + "\nthis.bot = " + this.bot.toString() + "\nthis.top = "
                + this.top.toString() + ")\n";
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
                + "this.deltaBot = " + this.deltaBot.toString()
                + ", this.deltaTop = " + this.deltaTop.toString() + "\n"
                + indent + "this.bot = " + this.bot.toString() + "\n" + indent
                + "this.top = " + this.top.toString() + ")\n";
    }

    /**
     * Is this <code>OverlayImagesXY</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof OverlayOffsetImagesBase) {
            OverlayOffsetImagesBase that = (OverlayOffsetImagesBase) o;
            return this.bot.equals(that.bot) && this.top.equals(that.top)
                    && this.deltaTop.equals(that.deltaTop)
                    && this.deltaBot.equals(that.deltaBot);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.deltaBot.hashCode() + this.deltaTop.hashCode()
                + this.bot.hashCode() + this.top.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayOffsetAlign(this.alignX, this.alignY,
                this.top, this.dx, this.dy, this.bot);
        i.pinhole = p;
        return i;
    }
}
