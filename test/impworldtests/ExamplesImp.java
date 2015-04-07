package impworldtests;

import java.awt.Color;

import javalib.worldcanvas.*;
import javalib.worldimages.*;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * Simple example of drawing a text, constructing images from the files and
 * seeing the drawings in the Canvas
 * 
 * @author Viera K. Proulx
 * @since 5 February 2012
 */
public class ExamplesImp {

    public ExamplesImp() {
    }

    // a text inside a red rectangle with a small black line
    public static WorldScene makeImage(WorldScene scene, Posn pos) {
        return scene
                .placeImageXY(
                        new RectangleImage(60, 20, OutlineMode.SOLID, Color.RED),
                        pos.x, pos.y)
                .placeImageXY(new TextImage("hello", 12, 0, Color.BLUE), pos.x,
                        pos.y)
                .placeImageXY(new LineImage(new Posn(5, 5), Color.BLACK),
                        pos.x, pos.y);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(600, 600);
        WorldScene s = new WorldScene(600, 600);

        WorldScene pic = ExamplesImp.makeImage(s, new Posn(300, 100));

        // show several images in the canvas
        boolean makeDrawing = c.show()
                && c.drawScene(ExamplesImp.makeImage(
                        s.placeImageXY(
                                new LineImage(new Posn(200, 200), Color.RED),
                                500, 500)
                                .placeImageXY(
                                        new FromFileImage(
                                                "Images/green-fish.png"), 100,
                                        100)
                                .placeImageXY(
                                        new FromFileImage(
                                                "Images/pink-fish.png"), 200,
                                        250)
                                .placeImageXY(
                                        new FromFileImage("Images/shark.png"),
                                        350, 400)
                                .placeImageXY(
                                        new CircleImage(5, OutlineMode.SOLID,
                                                Color.BLACK), 100, 100),
                        new Posn(300, 100)));

        pic = ExamplesImp.makeImage(s, new Posn(200, 100));
        boolean makeAnotherDrawing = c.drawScene(pic);

        // pic.movePinhole(0, 100);
        // boolean makeAnotherDrawing2 = c.drawImage(pic);

        WorldImage triangle = new TriangleImage(new Posn(20, 50), new Posn(60,
                80), new Posn(40, 90), OutlineMode.SOLID, Color.GREEN);

        // WorldImage blueline = new LineImage(new Posn(200, 300), new Posn(300,
        // 200), Color.BLUE);
        //
        // WorldCanvas c2 = new WorldCanvas(600, 600);
        // boolean makeDrawing3 = c2.show() && c2.drawImage(pic)
        // && c2.drawImage(triangle) && c2.drawImage(blueline);
        //
        // triangle.movePinhole(0, 80);
        // blueline.moveTo(new Posn(100, 100));
        // boolean drawTriangle2 = c2.drawImage(triangle)
        // && c2.drawImage(blueline);
        //
        // blueline.movePinhole(50, 70);
        // boolean drawMovedline = c2.drawImage(blueline);

    }
}