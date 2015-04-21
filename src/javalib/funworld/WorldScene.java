package javalib.funworld;

import javalib.worldcanvas.WorldSceneBase;
import javalib.worldimages.WorldImage;

/**
 * This class represents a functional empty scene
 * 
 * @author eric
 * 
 */
public class WorldScene extends WorldSceneBase {

    /**
     * Create a new, empty WorldScene of size <code>width</code> and
     * <code>height</code>
     * 
     * @param width
     *            -- width of the scene
     * @param height
     *            -- height of the scene
     */
    public WorldScene(int width, int height) {
        super(width, height);
    }

    private WorldScene(int width, int height, IList<PlaceImage> imgs) {
        super(width, height, imgs);
    }

    /**
     * Place the <code>image</code> at (<code>x</code>, <code>y</code>)
     * 
     * @param image
     *            -- the image to place
     * @param x
     *            -- x coordinate of the image
     * @param y
     *            -- y coordinate of the image
     */
    public WorldScene placeImageXY(WorldImage image, int x, int y) {
        return new WorldScene(width, height, this.imgs.add(new PlaceImage(
                image, x, y)));
    }
}