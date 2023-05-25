package javalib.impworld;

import javalib.worldcanvas.WorldSceneBase;
import javalib.worldimages.WorldImage;

import java.util.Objects;

/**
 * This class represents an imperative empty scene
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

    /**
     * Place the <code>image</code> at (<code>x</code>, <code>y</code>)
     * 
     * @param image
     *            -- the image to place
     * @param x
     *            -- x coordinate of the image
     * @param y
     *            -- y coordinate of the image
     * @throws NullPointerException if image is null
     */
    public void placeImageXY(WorldImage image, int x, int y) {
        Objects.requireNonNull(image, "Image cannot be null");
        this.imgs = this.imgs.add(new PlaceImage(image, x, y));
        this.revImgs = null;
    }
}
