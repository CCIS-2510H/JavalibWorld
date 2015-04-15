package javalib.worldcanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public abstract class WorldSceneBase {
    public int width, height;
    protected List<PlaceImage> imgs;

    protected WorldSceneBase(int width, int height) {
        this.width = width;
        this.height = height;
        this.imgs = new LinkedList<PlaceImage>();
        this.imgs.add(new PlaceImage(new RectangleImage(width, height,
                OutlineMode.OUTLINE, Color.black), width / 2, height / 2));
    }

    protected WorldSceneBase(int width, int height, List<PlaceImage> imgs) {
        this.width = width;
        this.height = height;
        this.imgs = imgs;
    }

    protected void draw(Graphics2D g) {
        for (PlaceImage i : imgs) {
            g.translate(i.x, i.y);
            i.img.draw(g);
            g.translate(-i.x, -i.y);
        }
    }
    
    protected class PlaceImage {
        WorldImage img;
        int x, y;

        public PlaceImage(WorldImage i, int x, int y) {
            this.img = i;
            this.x = x;
            this.y = y;
        }
    }
}
