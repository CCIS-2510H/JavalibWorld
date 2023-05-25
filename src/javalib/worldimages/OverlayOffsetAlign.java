package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;
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

    private OverlayOffsetAlign(AlignModeX alignX, AlignModeY alignY,
                              WorldImage top, double dx, double dy, WorldImage bot,
                              Posn pinhole) {
        super(alignX, alignY, top, dx, dy, bot, pinhole);
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

    @Override
    public WorldImage movePinholeTo(Posn p) {
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new OverlayOffsetAlign(this.alignX, this.alignY,
                this.top, this.dx, this.dy, this.bot, p);
    }
}

abstract class OverlayOffsetAlignBase extends WorldImage {

    /** the bottom image for the combined image */
    public final WorldImage bot;

    /** the top image for the combined image */
    public final WorldImage top;

    /** how much the top and bottom images need to move relative to each other */
    protected final DPosn deltaTop, deltaBot;
    public final double dx, dy;

    /** The base alignments */
    public final AlignModeX alignX;
    public final AlignModeY alignY;

    public OverlayOffsetAlignBase(AlignModeX alignX, AlignModeY alignY,
            WorldImage top, double dx, double dy, WorldImage bot) {
        this(new TransientOverlayOffsetAlign(
                Objects.requireNonNull(alignX, "Horizontal align mode cannot be null"),
                Objects.requireNonNull(alignY, "Vertical align mode cannot be null"),
                Objects.requireNonNull(top, "Top image cannot be null"),
                dx, dy,
                Objects.requireNonNull(bot, "Bottom image cannot be null")));
    }
    OverlayOffsetAlignBase(AlignModeX alignX, AlignModeY alignY,
                                  WorldImage top, double dx, double dy, WorldImage bot,
                                  Posn pinhole) {
        this(new TransientOverlayOffsetAlign(
                Objects.requireNonNull(alignX, "Horizontal align mode cannot be null"),
                Objects.requireNonNull(alignY, "Vertical align mode cannot be null"),
                Objects.requireNonNull(top, "Top image cannot be null"),
                dx, dy,
                Objects.requireNonNull(bot, "Bottom image cannot be null")),
                pinhole);
    }
    OverlayOffsetAlignBase(TransientOverlayOffsetAlign img) {
        this(img, img.pinhole);
    }
    OverlayOffsetAlignBase(TransientOverlayOffsetAlign img, Posn pinhole) {
        super(pinhole, img.depth);
        this.bot = img.bot;
        this.top = img.top;
        this.deltaBot = img.deltaBot;
        this.deltaTop = img.deltaTop;
        this.dx = img.dx;
        this.dy = img.dy;
        this.alignX = img.alignX;
        this.alignY = img.alignY;
        this.hashCode = img.hashCode;
    }

    /**
     * This class is entirely an implementation detail, in order to make the computation of
     * the pinhole happen before the super() call in OverlayOffsetAlignBase's constructor.
     */
    private static final class TransientOverlayOffsetAlign {
        final Posn pinhole;
        final int depth;
        final WorldImage top, bot;
        final AlignModeX alignX;
        final AlignModeY alignY;
        final double dx, dy;
        final DPosn deltaTop, deltaBot;
        final int hashCode;

        private TransientOverlayOffsetAlign(AlignModeX alignX, AlignModeY alignY,
                                    WorldImage top, double dx, double dy, WorldImage bot) {
            this.depth = 1 + Math.max(top.depth, bot.depth);
            this.bot = bot;
            this.top = top;
            this.alignX = alignX;
            this.alignY = alignY;
            this.dx = dx;
            this.dy = dy;

            BoundingBox botBox = this.bot.getBB();
            BoundingBox topBox = this.top.getBB();
            double botDeltaX, botDeltaY, topDeltaX, topDeltaY;
            switch (alignX) {
                case LEFT: // move both images so their left sides are at x = 0;
                    botDeltaX = -botBox.getTlx();
                    topDeltaX = -topBox.getTlx();
                    break;
                case CENTER:
                    botDeltaX = -botBox.getCenterX();
                    topDeltaX = -topBox.getCenterX();
                    break;
                case PINHOLE:
                    botDeltaX = -bot.pinhole.x;
                    topDeltaX = -top.pinhole.x;
                    break;
                case RIGHT:
                    botDeltaX = -botBox.getBrx();
                    topDeltaX = -topBox.getBrx();
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected AlignModeX");
            }
            switch (alignY) {
                case TOP: // move both images so their left sides are at x = 0;
                    botDeltaY = -botBox.getTly();
                    topDeltaY = -topBox.getTly();
                    break;
                case MIDDLE:
                    botDeltaY = -botBox.getCenterY();
                    topDeltaY = -topBox.getCenterY();
                    break;
                case PINHOLE:
                    botDeltaY = -bot.pinhole.y;
                    topDeltaY = -top.pinhole.y;
                    break;
                case BOTTOM:
                    botDeltaY = -botBox.getBry();
                    topDeltaY = -topBox.getBry();
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected AlignModeY");
            }
            topDeltaX -= dx;
            topDeltaY -= dy;

            double minX = Math.min(botBox.getTlx() + botDeltaX, topBox.getTlx() + topDeltaX);
            double minY = Math.min(botBox.getTly() + botDeltaY, topBox.getTly() + topDeltaY);
            double maxX = Math.max(botBox.getBrx() + botDeltaX, topBox.getBrx() + topDeltaX);
            double maxY = Math.max(botBox.getBry() + botDeltaY, topBox.getBry() + topDeltaY);

            double centerX = (minX + maxX) / 2.0;
            double centerY = (minY + maxY) / 2.0;

//        topDeltaX -= centerX;
//        topDeltaY -= centerY;
//        botDeltaX -= centerX;
//        botDeltaY -= centerY;

            this.deltaBot = new DPosn(botDeltaX, botDeltaY);
            this.deltaTop = new DPosn(topDeltaX, topDeltaY);

            if (alignX == AlignModeX.PINHOLE && alignY == AlignModeY.PINHOLE && dx == 0 && dy == 0) {
                this.pinhole = new Posn((int) (top.pinhole.x + topDeltaX), (int) (top.pinhole.y + topDeltaY));
            } else {
                this.pinhole = new Posn((int) centerX, (int) centerY);
            }

            this.hashCode = this.bot.hashCode() + this.top.hashCode()
                    + this.alignX.hashCode() + this.alignY.hashCode()
                    + (int) this.dx * 37 + (int) this.dy * 16;
        }
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

    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        // Save the old transform state
        AffineTransform old = g.getTransform();

        // draw the two objects
        g.translate(this.deltaBot.x, this.deltaBot.y);
        this.bot.drawStackUnsafe(g);
        g.setTransform(old);
        g.translate(this.deltaTop.x, this.deltaTop.y);
        this.top.drawStackUnsafe(g);

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
                new FieldsWLItem(this.pinhole,
                        new ImageField("top", this.top),
                        new ImageField("dx", this.dx), new ImageField("dy", this.dy, true),
                        new ImageField("bot", this.bot)));
        return sb;
    }


    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (this.getClass().equals(other.getClass())) {
            OverlayOffsetAlignBase that = (OverlayOffsetAlignBase)other;
            if (this.alignX == that.alignX && this.alignY == that.alignY
                    && this.dx == that.dx && this.dy == that.dy
                    && this.pinhole.equals(that.pinhole)) {
                worklistThis.push(this.bot);
                worklistThat.push(that.bot);
                worklistThis.push(this.top);
                worklistThat.push(that.top);
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
}
