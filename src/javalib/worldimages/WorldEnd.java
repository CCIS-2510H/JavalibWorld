package javalib.worldimages;

import javalib.worldcanvas.WorldScene;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent a pair of values <code>boolean</code> and
 * <code>{@link WorldScene WorldScene}</code>.
 * </p>
 * <p>
 * This is used to indicate when the world should end and allow for creating one
 * last image to display at the end.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public final class WorldEnd {

    /**
     * the indicator whether the world should end: true if the world should end,
     * false if the world goes on
     */
    public boolean worldEnds;

    /** the last scene to display when the world ends */
    public WorldScene lastScene;

    /**
     * The standard full constructor.
     * 
     * @param worldEnds
     *            -- true if the world should end, false if the world goes on
     * @param lastScene
     *            -- the last image to display when the world ends (ignored if
     *            the world goes on)
     */
    public WorldEnd(boolean worldEnds, WorldScene lastScene) {
        this.worldEnds = worldEnds;
        this.lastScene = lastScene;
    }
}