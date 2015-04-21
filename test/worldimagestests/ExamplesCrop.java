package worldimagestests;

import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;
import javalib.funworld.WorldScene;

import java.awt.*;

import tester.Tester;

/**
 * Copyright 2012 Viera K. Proulx
 * This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)
 */

/**
 * A complete set of images displayed in the Canvas
 * 
 * @author Viera K. Proulx
 * @since 5 February 2012
 */
public class ExamplesCrop {

    public ExamplesCrop() {
    }

    // support for the regression tests
    public static ExamplesCrop examplesInstance = new ExamplesCrop();

    // Images
    WorldScene scene = new WorldScene(400, 400);

    WorldImage circle = new CircleImage(40, OutlineMode.SOLID, Color.RED);
    WorldImage ellipse = new EllipseImage(80, 120, "solid", Color.BLUE);
    WorldImage rectangle = new RectangleImage(60, 20, OutlineMode.SOLID,
            Color.ORANGE);

    // (crop 0 0 40 40 (circle 40 "solid" "chocolate"))
    WorldImage croppedCircle = new CropImage(0, 0, 40, 40, circle);
    // (crop 40 60 40 60 (ellipse 80 120 "solid" "dodgerblue"))
    WorldImage croppedEllipse = new CropImage(40, 60, 40, 60, ellipse);

    WorldImage shearedCrop = new ShearedImage(new FrameImage(croppedEllipse,
            Color.RED), 0.5, 0.25);
    WorldImage croppedShear = new CropImage(60, 40, 60, 40, new ShearedImage(
            ellipse, 0.5, 0.25));

    WorldImage phantom = new PhantomImage(new LineImage(new Posn(10, 50),
            Color.BLACK));

    WorldScene combined = scene
            .placeImageXY(new FrameImage(croppedCircle), 200, 200)
            .placeImageXY(new FrameImage(croppedEllipse), 100, 200)
            .placeImageXY(new FrameImage(shearedCrop), 300, 200)
            .placeImageXY(new ShearedImage(ellipse, 0.5, 0.25), 200, 300)
            .placeImageXY(new FrameImage(croppedShear), 300, 300)
            .placeImageXY(new FrameImage(new OverlayImage(phantom, rectangle)),
                    100, 100);

    public void testAll(Tester t) {
        String[] args = new String[] {};
        ExamplesCrop.main(args);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(400, 400);

        ExamplesCrop e = new ExamplesCrop();

        // show several images in the canvas
        boolean makeDrawing = c.show() && c.drawScene(e.combined);
    }
}
