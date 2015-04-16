package javalib.impworld;

import javalib.worldcanvas.WorldSceneBase;
import javalib.worldimages.WorldImage;

public class WorldScene extends WorldSceneBase {

    public WorldScene(int width, int height) {
        super(width, height);
    }

    public void placeImageXY(WorldImage image, int x, int y) {
        this.imgs = this.imgs.add(new PlaceImage(image, x, y));
        this.revImgs = null;
    }
}
