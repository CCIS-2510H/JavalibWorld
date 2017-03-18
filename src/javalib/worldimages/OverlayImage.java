package javalib.worldimages;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

import java.util.Stack;

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
     *            -- the bottom image for the combined image
     * @param top
     *            -- the bottom image for the combined image
     */
    public OverlayImage(WorldImage top, WorldImage bot) {
        super(AlignModeX.PINHOLE, AlignModeY.PINHOLE, top, 0, 0, bot);
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(");
        stack.push(
                new FieldsWLItem(
                        new ImageField("top", this.top),
                        new ImageField("bot", this.bot)));
        return sb;
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