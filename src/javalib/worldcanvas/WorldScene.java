package javalib.worldcanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class WorldScene {
    public int width, height;
    private List<PlaceImage> imgs;

    public WorldScene(int width, int height) {
        this.width = width;
        this.height = height;
        this.imgs = new LinkedList<PlaceImage>();
        this.imgs.add(new PlaceImage(new RectangleImage(width, height,
                OutlineMode.OUTLINE, Color.black), width / 2, height / 2));
    }

    public WorldScene placeImageXY(WorldImage image, int x, int y) {
        imgs.add(new PlaceImage(image, x, y));
        return this;
    }

    protected void draw(Graphics2D g) {
        for (PlaceImage i : imgs) {
            i.img.drawAt(g, i.x, i.y);
        }
    }
}

class PlaceImage {
    WorldImage img;
    int x, y;

    PlaceImage(WorldImage i, int x, int y) {
        this.img = i;
        this.x = x;
        this.y = y;
    }
}
