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

import java.util.Stack;

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

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("top", this.top),
                        new ImageField("dx", this.dx), new ImageField("dy", this.dy, true),
                        new ImageField("bot", this.bot)));
        return sb;
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new OverlayOffsetImage(this.top, this.dx, this.dy,
                this.bot);
        i.pinhole = p;
        return i;
    }
}