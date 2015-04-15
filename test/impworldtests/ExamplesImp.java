package impworldtests;

import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldcanvas.*;
import javalib.worldimages.*;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * Simple example of drawing a text and seeing the drawings in the Canvas
 * 
 * @author Viera K. Proulx
 * @since 5 February 2012
 */
public class ExamplesImp {

    public ExamplesImp() {
    }

    // a text inside a red rectangle with a small black line
    public static WorldScene makeImage(WorldScene scene, Posn pos) {
        scene.placeImageXY(new RectangleImage(60, 20, OutlineMode.SOLID,
                Color.RED), pos.x, pos.y);
        scene.placeImageXY(new TextImage("hello", 12, 0, Color.BLUE), pos.x,
                pos.y);
        scene.placeImageXY(new LineImage(new Posn(5, 5), Color.BLACK), pos.x,
                pos.y + 5);
        return scene;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(600, 600);
        WorldScene s = new WorldScene(600, 600);

        // show several images in the canvas
        boolean makeDrawing = c.show()
                && c.drawScene(ExamplesImp.makeImage(s, new Posn(300, 100)));

        WorldScene pic = ExamplesImp.makeImage(s, new Posn(200, 100));
        boolean makeAnotherDrawing = c.drawScene(pic);

    }
}