package javalib.funworld;

import java.util.LinkedList;
import java.util.List;

import javalib.worldcanvas.WorldSceneBase;
import javalib.worldimages.WorldImage;

public class WorldScene extends WorldSceneBase {

    public WorldScene(int width, int height) {
        super(width, height);
    }

    private WorldScene(int width, int height, List<PlaceImage> imgs) {
        super(width, height, imgs);
    }

    public WorldScene placeImageXY(WorldImage image, int x, int y) {
        List<PlaceImage> newImgs = new LinkedList<PlaceImage>(imgs);
        newImgs.add(new PlaceImage(image, x, y));
        return new WorldScene(width, height, newImgs);
    }
}