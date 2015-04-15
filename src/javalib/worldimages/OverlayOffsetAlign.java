package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * <p>
 * The class to represent an overlay of the top image on the bottom one, using
 * <code>alignX</code> and <code>alignY</code> as a starting point, with the
 * bottom offset by the given <code>dx</code> and <code>dy</code> combined into
 * <code>{@link WorldImage WorldImage}</code> to be drawn by the world when
 * drawing on its <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public final class OverlayOffsetAlign extends OverlayOffsetAlignBase {

    /**
     * Overlay of the top image on the bottom one, using <code>alignX</code> and
     * <code>alignY</code> as a starting point, with the bottom offset by the
     * given <code>dx</code> and <code>dy</code>
     * 
     * @param alignX
     *            -- the alignment on the X-axis to be used as a starting point
     *            for overlaying the top image on the bottom
     * @param alignY
     *            -- the alignment on the Y-axis to be used as a starting point
     *            for overlaying the top image on the bottom
     * @param top
     *            -- the bottom image for the combined image
     * @param dx
     *            -- the horizontal offset for the bottom image
     * @param dy
     *            -- the vertical offset for the bottom image
     * @param bot
     *            -- the bottom image for the combined image
     */
    public OverlayOffsetAlign(AlignModeX alignX, AlignModeY alignY,
            WorldImage top, double dx, double dy, WorldImage bot) {
        super(alignX, alignY, top, dx, dy, bot);
    }

    /**
     * Overlay of the top image on the bottom one, using <code>alignX</code> and
     * <code>alignY</code> as a starting point, with the bottom offset by the
     * given <code>dx</code> and <code>dy</code>
     * 
     * @param alignX
     *            -- the alignment on the X-axis to be used as a starting point
     *            for overlaying the top image on the bottom. Valid values are
     *            left, center, middle, right, pinhole.
     * @param alignY
     *            -- the alignment on the Y-axis to be used as a starting point
     *            for overlaying the top image on the bottom Valid values are
     *            top, center, middle, bottom, pinhole.
     * @param top
     *            -- the bottom image for the combined image
     * @param dx
     *            -- the horizontal offset for the bottom image
     * @param dy
     *            -- the vertical offset for the bottom image
     * @param bot
     *            -- the bottom image for the combined image
     */
    public OverlayOffsetAlign(String alignX, String alignY, WorldImage top,
            double dx, double dy, WorldImage bot) {
        super(AlignModeX.fromString(alignX), AlignModeY.fromString(alignY),
                top, dx, dy, bot);
    }
}

abstract class OverlayOffsetAlignBase extends WorldImage {

    /** the bottom image for the combined image */
    public WorldImage bot;

    /** the top image for the combined image */
    public WorldImage top;

    /** how much the top and bottom images need to move relative to each other */
    protected Posn deltaTop, deltaBot;
    public double dx, dy;

    /** The base alignments */
    public AlignModeX alignX;
    public AlignModeY alignY;

    private int width, height;

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

        int rightX = (int) (Math.max((botWidth / 2.0) + xBotMoveDist(),
                (topWidth / 2.0) + xTopMoveDist()));
        int leftX = (int) (Math.min((-botWidth / 2.0) + xBotMoveDist(),
                (-topWidth / 2.0) + xTopMoveDist()));
        int bottomY = (int) (Math.max((botHeight / 2.0) + yBotMoveDist(),
                (topHeight / 2.0) + yTopMoveDist()));
        int topY = (int) (Math.min((-botHeight / 2.0) + yBotMoveDist(),
                (-topHeight / 2.0) + yTopMoveDist()));

        // yBotMoveDist(), yTopMoveDist(), xBotMoveDist(), and
        // xTopMoveDist() position the two images relative to
        // each other, but not correctly positioned at the origin,
        // which is why centerX and centerY exist
        int centerX = (int) Math.round((rightX + leftX) / 2.0);
        int centerY = (int) Math.round((bottomY + topY) / 2.0);

        int botDeltaY = -centerY + yBotMoveDist();
        int topDeltaY = -centerY + yTopMoveDist();
        int botDeltaX = -centerX + xBotMoveDist();
        int topDeltaX = -centerX + xTopMoveDist();

        this.deltaBot = new Posn(botDeltaX, botDeltaY);
        this.deltaTop = new Posn(topDeltaX, topDeltaY);

        this.width = (int) Math.round(getBB().getWidth());
        this.height = (int) Math.round(getBB().getHeight());

        if (alignY == AlignModeY.PINHOLE && alignX == AlignModeX.PINHOLE
                && dx == 0 && dy == 0) {
            // Set the pinhole
            this.pinhole = new Posn(-centerX, -centerY);
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
     * How much should bottom image move in the Y direction relative to the top
     * image?
     * 
     * @return y move distance
     */
    private int yBotMoveDist() {
        double moveDist = 0;
        if (this.alignY == AlignModeY.TOP || this.alignY == AlignModeY.BOTTOM) {
            int h1 = this.top.getHeight();
            int h2 = this.bot.getHeight();
            if (this.alignY == AlignModeY.TOP) {
                moveDist = (h2 - h1) / 2.0;
            } else if (this.alignY == AlignModeY.BOTTOM) {
                moveDist = (h1 - h2) / 2.0;
            }
        } else if (this.alignY == AlignModeY.PINHOLE) {
            moveDist = -this.bot.pinhole.y;
        }
        moveDist += dy;
        return (int) Math.round(moveDist);
    }

    /**
     * How much should top image move in the Y direction relative to the bottom
     * image?
     * 
     * @return y move distance
     */
    private int yTopMoveDist() {
        if (this.alignY == AlignModeY.PINHOLE) {
            return -this.top.pinhole.y;
        }
        return 0;
    }

    /**
     * How much should bottom image move in the X direction relative to the top
     * image?
     * 
     * @return x move distance
     */
    private int xBotMoveDist() {
        double moveDist = 0;
        if (this.alignX == AlignModeX.LEFT || this.alignX == AlignModeX.RIGHT) {
            int w1 = this.top.getWidth();
            int w2 = this.bot.getWidth();
            if (this.alignX == AlignModeX.LEFT) {
                moveDist = (w2 - w1) / 2.0;
            } else if (this.alignX == AlignModeX.RIGHT) {
                moveDist = (w1 - w2) / 2.0;
            }
        } else if (this.alignX == AlignModeX.PINHOLE) {
            moveDist = -this.bot.pinhole.x;
        }
        moveDist += dx;
        return (int) Math.round(moveDist);
    }

    /**
     * How much should top image move in the X direction relative to the bottom
     * image?
     * 
     * @return x move distance
     */
    private int xTopMoveDist() {
        if (this.alignX == AlignModeX.PINHOLE) {
            return -this.top.pinhole.x;
        }
        return 0;
    }

    @Override
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

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Produce a <code>String</code> representation of this overlay of images
     */
    @Override
    public String toString() {
        return className(this) + "this.alignX = " + this.alignX
                + ",\nthis.alignY = " + this.alignY + ",\nthis.top = "
                + this.top.toString() + ",\nthis.dx = " + this.dx
                + ", this.dy = " + this.dy + "\nthis.bot = "
                + this.bot.toString() + "\nthis.top = " + ")\n";
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.alignX = " + this.alignX
                + ",\n" + indent + "this.alignY = " + this.alignY + ",\n"
                + indent + "this.top = " + this.top.toString() + ",\n" + indent
                + ",\nthis.dx = " + this.dx + ", this.dy = " + this.dy + ",\n"
                + indent + "this.bot = " + this.bot.toString() + ",\n" + indent
                + ")\n";
    }

    /**
     * Is this <code>OverlayOffsetAlign</code> the same as that
     * <code>OverlayOffsetAlign</code>?
     * 
     * @param that
     * @return
     */
    public boolean same(OverlayOffsetAlignBase that) {
        return this.bot.equals(that.bot) && this.top.equals(that.top)
                && this.alignX == that.alignX && this.alignY == that.alignY
                && this.dx == that.dx && this.dy == that.dy;
    }

    /**
     * Is this <code>OverlayOffsetAlign</code> same as the given object?
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof OverlayOffsetAlignBase
                && this.same((OverlayOffsetAlignBase) o);
    }

    /**
     * The hashCode to match the equals method
     */
    @Override
    public int hashCode() {
        return this.bot.hashCode() + this.top.hashCode()
                + this.alignX.hashCode() + this.alignY.hashCode()
                + (int) this.dx * 37 + (int) this.dy * 16;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayOffsetAlign(this.alignX, this.alignY,
                this.top, this.dx, this.dy, this.bot);
        i.pinhole = p;
        return i;
    }
}
