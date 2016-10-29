package javalib.worldimages;

import javalib.worldcanvas.WorldSceneBase;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent a pair of values <code>boolean</code> and
 * <code>{@link WorldSceneBase WorldScene}</code>.
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
    public WorldSceneBase lastScene;

    /**
     * The standard full constructor.
     * 
     * @param worldEnds
     *            -- true if the world should end, false if the world goes on
     * @param lastScene
     *            -- the last image to display when the world ends (ignored if
     *            the world goes on)
     */
    public WorldEnd(boolean worldEnds, WorldSceneBase lastScene) {
        this.worldEnds = worldEnds;
        this.lastScene = lastScene;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldEnd)) return false;
        WorldEnd end = (WorldEnd)obj;
        return this.worldEnds == end.worldEnds && this.lastScene.equals(end.lastScene);
    }

    @Override
    public int hashCode() {
        int hash = this.lastScene.hashCode();
        if (this.worldEnds)
            hash = hash * 2;
        return hash;
    }
}