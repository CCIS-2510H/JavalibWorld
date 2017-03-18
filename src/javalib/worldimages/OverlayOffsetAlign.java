package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

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
    protected DPosn deltaTop, deltaBot;
    public double dx, dy;

    /** The base alignments */
    public AlignModeX alignX;
    public AlignModeY alignY;

    public OverlayOffsetAlignBase(AlignModeX alignX, AlignModeY alignY,
            WorldImage top, double dx, double dy, WorldImage bot) {
        super(1 + Math.max(top.depth, bot.depth));
        this.bot = bot;
        this.top = top;
        this.alignX = alignX;
        this.alignY = alignY;
        this.dx = dx;
        this.dy = dy;

        // Calculate proper center point
        double botWidth = this.bot.getWidth();
        double topWidth = this.top.getWidth();
        double botHeight = this.bot.getHeight();
        double topHeight = this.top.getHeight();

        double rightX = Math.max((botWidth / 2.0) + xBotMoveDist(),
                (topWidth / 2.0) + xTopMoveDist());
        double leftX = Math.min((-botWidth / 2.0) + xBotMoveDist(),
                (-topWidth / 2.0) + xTopMoveDist());
        double bottomY = Math.max((botHeight / 2.0) + yBotMoveDist(),
                (topHeight / 2.0) + yTopMoveDist());
        double topY = Math.min((-botHeight / 2.0) + yBotMoveDist(),
                (-topHeight / 2.0) + yTopMoveDist());

        // yBotMoveDist(), yTopMoveDist(), xBotMoveDist(), and
        // xTopMoveDist() position the two images relative to
        // each other, but not correctly positioned at the origin,
        // which is why centerX and centerY exist
        double centerX = (rightX + leftX) / 2.0;
        double centerY = (bottomY + topY) / 2.0;

        double botDeltaY = -centerY + yBotMoveDist();
        double topDeltaY = -centerY + yTopMoveDist();
        double botDeltaX = -centerX + xBotMoveDist();
        double topDeltaX = -centerX + xTopMoveDist();

        this.deltaBot = new DPosn(botDeltaX, botDeltaY);
        this.deltaTop = new DPosn(topDeltaX, topDeltaY);

        if (alignY == AlignModeY.PINHOLE && alignX == AlignModeX.PINHOLE
                && dx == 0 && dy == 0) {
            // Set the pinhole
            this.pinhole = new Posn((int) -Math.round(centerX),
                    (int) -Math.round(centerY));
        }
        
        this.hashCode = this.bot.hashCode() + this.top.hashCode()
            + this.alignX.hashCode() + this.alignY.hashCode()
            + (int) this.dx * 37 + (int) this.dy * 16;
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        AffineTransform temp = new AffineTransform(t);
        temp.translate(this.deltaBot.x, this.deltaBot.y);
        BoundingBox botBox = this.bot.getBB(temp);
        temp.setTransform(t);
        temp.translate(this.deltaTop.x, this.deltaTop.y);
        BoundingBox topBox = this.top.getBB(temp);
        return botBox.combine(topBox);
    }
    @Override
    int numKids() {
        return 2;
    }
    @Override
    WorldImage getKid(int i) {
        if (i == 0) { return this.bot; }
        if (i == 1) { return this.top; }
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        if (i == 0) { return AffineTransform.getTranslateInstance(this.deltaBot.x, this.deltaBot.y); }
        if (i == 1) { return AffineTransform.getTranslateInstance(this.deltaTop.x, this.deltaTop.y); }
        throw new IllegalArgumentException("No such kid " + i);
    }
    
    /**
     * How much should bottom image move in the Y direction relative to the top
     * image?
     * 
     * @return y move distance
     */
    private double yBotMoveDist() {
        double moveDist = 0;
        if (this.alignY == AlignModeY.TOP || this.alignY == AlignModeY.BOTTOM) {
            double h1 = this.top.getHeight();
            double h2 = this.bot.getHeight();
            if (this.alignY == AlignModeY.TOP) {
                moveDist = (h2 - h1) / 2.0;
            } else if (this.alignY == AlignModeY.BOTTOM) {
                moveDist = (h1 - h2) / 2.0;
            }
        } else if (this.alignY == AlignModeY.PINHOLE) {
            moveDist = -this.bot.pinhole.y;
        }
        moveDist += dy;
        return moveDist;
    }

    /**
     * How much should top image move in the Y direction relative to the bottom
     * image?
     * 
     * @return y move distance
     */
    private double yTopMoveDist() {
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
    private double xBotMoveDist() {
        double moveDist = 0;
        if (this.alignX == AlignModeX.LEFT || this.alignX == AlignModeX.RIGHT) {
            double w1 = this.top.getWidth();
            double w2 = this.bot.getWidth();
            if (this.alignX == AlignModeX.LEFT) {
                moveDist = (w2 - w1) / 2.0;
            } else if (this.alignX == AlignModeX.RIGHT) {
                moveDist = (w1 - w2) / 2.0;
            }
        } else if (this.alignX == AlignModeX.PINHOLE) {
            moveDist = -this.bot.pinhole.x;
        }
        moveDist += dx;
        return moveDist;
    }

    /**
     * How much should top image move in the X direction relative to the bottom
     * image?
     * 
     * @return x move distance
     */
    private double xTopMoveDist() {
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
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        AffineTransform cur;
        cur = g.getTransform();
        cur.translate(this.deltaTop.x, this.deltaTop.y);
        txs.push(cur);
        images.push(this.top);
        cur = g.getTransform();
        cur.translate(this.deltaBot.x, this.deltaBot.y);
        txs.push(cur);
        images.push(this.bot);
    }

    @Override
    public double getWidth() {
        return this.getBB().getWidth();
    }

    @Override
    public double getHeight() {
        return this.getBB().getHeight();
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.alignX = ").append(this.alignX).append(", ")
               .append("this.alignY = ").append(this.alignY).append(",");
        stack.push(
                new FieldsWLItem(
                        new ImageField("top", this.top),
                        new ImageField("dx", this.dx), new ImageField("dy", this.dy, true),
                        new ImageField("bot", this.bot)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist) {
        if (this.getClass().equals(other.getClass())) {
            OverlayOffsetAlignBase that = (OverlayOffsetAlignBase)other;
            if (this.alignX == that.alignX && this.alignY == that.alignY
                    && this.dx == that.dx && this.dy == that.dy) {
                worklist.push(new ImagePair(this.bot, that.bot));
                worklist.push(new ImagePair(this.top, that.top));
                return true;
            }
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    private int hashCode;

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayOffsetAlign(this.alignX, this.alignY,
                this.top, this.dx, this.dy, this.bot);
        i.pinhole = p;
        return i;
    }
}
