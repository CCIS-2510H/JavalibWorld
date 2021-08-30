package worldimagestests;

import javalib.funworld.World;
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

//    WorldImage rotatedEllipse =
//            new OverlayImage(
//                new FrameImage(
//                    new RotateImage(
//                            new VisiblePinholeImage(
//                        new WedgeImage(100, 120, "solid", Color.LIGHT_GRAY), Color.RED), 00)),
//                new RectangleImage(300, 300, "solid", Color.gray));
//
//    WorldScene combined = scene
//            .placeImageXY(rotatedEllipse, 200, 200);
//            .placeImageXY(new VisiblePinholeImage(circle.movePinhole(-10, -10)), 200, 100)
//            .placeImageXY(new FrameImage(croppedCircle), 200, 200)
//            .placeImageXY(new FrameImage(croppedEllipse), 100, 200)
//            .placeImageXY(new FrameImage(shearedCrop), 300, 200)
//            .placeImageXY(new ShearedImage(ellipse, 0.5, 0.25), 200, 300)
//            .placeImageXY(new FrameImage(croppedShear), 300, 300)
//            .placeImageXY(new FrameImage(new CropImage(0, 0, 80, 100, ellipse)), 350, 300)
//            .placeImageXY(new FrameImage(new OverlayImage(phantom, rectangle)),
//                    100, 100);

    public void testAll(Tester t) {
        String[] args = new String[] {};
        ExamplesCrop.main(args);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(WedgeWorld.SCALE * 400, WedgeWorld.SCALE * 400);

        ExamplesCrop e = new ExamplesCrop();

        // show several images in the canvas
        // boolean makeDrawing = c.show() && c.drawScene(e.combined);

        new WedgeWorld(60, 0, 1).bigBang(WedgeWorld.SCALE * 400, WedgeWorld.SCALE * 400, 0.1);
    }
}

class WedgeWorld extends World {
    int theta, start, speed;
    static final int SCALE = 2;

    WedgeWorld(int theta, int start, int speed) {
        this.theta = theta;
        this.start = start;
        this.speed = speed;
    }

    @Override
    public World onTick() {
        return new WedgeWorld(this.theta, (this.start + this.speed) % 360, this.speed);
    }

    @Override
    public World onKeyEvent(String s) {
        if (s.equals("w")) {
            return new WedgeWorld(this.theta + 5, this.start, this.speed);
        } else if (s.equals("n")) {
            return new WedgeWorld(this.theta - 5, this.start, this.speed);
        } else if (s.equals("f")) {
            return new WedgeWorld(this.theta, this.start, this.speed + 1);
        } else if (s.equals("s")) {
            return new WedgeWorld(this.theta, this.start, this.speed - 1);
        } else {
            return this;
        }
    }

    @Override
    public WorldScene makeScene() {
        WorldImage p;
        int x = (int)(100 * Math.cos(Math.toRadians(this.theta)));
        int y = -(int)(100 * Math.sin(Math.toRadians(this.theta)));
        if (theta <= -270) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, 100), new Posn(-100, 0), new Posn(0, -100), new Posn(x, y));
        } else if (theta <= -180) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, 100), new Posn(-100, 0), new Posn(x, y));
        } else if (theta <= -90) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, 100), new Posn(x, y));
        } else if (theta <= 90) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(x, y));
        } else if (theta <= 180) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, -100), new Posn(x, y));
        } else if (theta <= 270) {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, -100), new Posn(-100, 0), new Posn(x, y));
        } else {
            p = new PointPolygonImage("solid", Color.RED,
                    new Posn(0, 0), new Posn(100, 0), new Posn(0, -100), new Posn(-100, 0), new Posn(0, 100), new Posn(x, y));
        }
        p = p.movePinholeTo(new Posn(0, 0));
        WorldImage rotatedEllipse =
            new OverlayImage(
                new FrameImage(
                    new RotateImage(
                        new OverlayImage(
                            p,
                            new VisiblePinholeImage(
                                new WedgeImage(100, this.theta, "outline", Color.LIGHT_GRAY), Color.RED)),
                        this.start)),
                new RectangleImage(300, 300, "solid", Color.gray));
        return this.getEmptyScene().placeImageXY(new ScaleImage(rotatedEllipse, SCALE), SCALE * 200, SCALE * 200);
    }
}
