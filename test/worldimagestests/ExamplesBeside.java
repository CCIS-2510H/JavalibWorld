package worldimagestests;

import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.BesideImage;
import javalib.worldimages.FrameImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.ShearedImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class ExamplesBeside {

    WorldImage hello = new TextImage("Hejlo ", 24, Color.BLACK);
    WorldImage world = new TextImage("fjords", 24, Color.BLACK);
    WorldImage hw = new BesideImage(hello, world);
    
    WorldImage framedHello = new FrameImage(hello, Color.GREEN);
    WorldImage framedWorld = new FrameImage(world, Color.GREEN);
    WorldImage hw2 = new BesideImage(framedHello, world);
    WorldImage hw3 = new BesideImage(hello, framedWorld);
    WorldImage hw4 = new BesideImage(framedHello, framedWorld);
    
    WorldImage rect1 = new RectangleImage(20, 40, "solid", Color.BLUE);
    WorldImage rect2 = new RectangleImage(40, 20, "solid", Color.RED);
    WorldImage rect = new BesideImage(rect1, rect2);
    
    WorldScene scn = new WorldScene(400, 500);
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(400, 500);

        ExamplesBeside e = new ExamplesBeside();
        
        e.scn.placeImageXY(new FrameImage(e.hw), 200, 100);
        e.scn.placeImageXY(new FrameImage(e.hw2), 200, 150);
        e.scn.placeImageXY(new FrameImage(e.hw3), 200, 200);
        e.scn.placeImageXY(new FrameImage(e.hw4), 200, 250);
        e.scn.placeImageXY(new FrameImage(e.rect), 200, 350);
        e.scn.placeImageXY(new FrameImage(new ShearedImage(new FrameImage(e.world), 0.5, -0.25)), 200, 450);

        // show several images in the canvas
        boolean makeDrawing = c.show() && c.drawScene(e.scn);
    }
}
