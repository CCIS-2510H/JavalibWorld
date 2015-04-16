package javalib.funworld;

import javalib.worldcanvas.WorldSceneBase;
import javalib.worldimages.WorldImage;

public class WorldScene extends WorldSceneBase {

    public WorldScene(int width, int height) {
        super(width, height);
    }

    private WorldScene(int width, int height, IList<PlaceImage> imgs) {
        super(width, height, imgs);
    }

    public WorldScene placeImageXY(WorldImage image, int x, int y) {
        return new WorldScene(width, height, this.imgs.add(new PlaceImage(image, x, y)));
    }
}