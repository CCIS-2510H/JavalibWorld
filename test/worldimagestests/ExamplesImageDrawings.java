package worldimagestests;

import javalib.worldcanvas.WorldCanvas;
import javalib.worldcanvas.WorldScene;
import javalib.worldimages.*;

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
public class ExamplesImageDrawings {

    public ExamplesImageDrawings() {
    }

    // support for the regression tests
    public static ExamplesImageDrawings examplesInstance = new ExamplesImageDrawings();

    // a text inside a red rectangle with a yellow dot in its pinhole location
    public static WorldImage makeText(int size) {
        WorldImage hello = new TextImage("quickbrownfoxjumpedoveralazydog",
                size, 3, Color.BLUE);

        WorldImage helloRed = new OverlayImages(new RectangleImage(
                hello.getWidth(), hello.getHeight(), "solid", Color.RED), hello);
        return new OverlayImages(helloRed, new CircleImage(2,
                OutlineMode.SOLID, Color.YELLOW));
    }

    // Images
    WorldScene scene = new WorldScene(800, 800);

    WorldImage circle = new CircleImage(10, OutlineMode.OUTLINE, Color.RED);
    WorldImage circleText = new TextImage(
            "CircleImage(10, \"outline\", Color.RED)", Color.RED);

    WorldImage disc = new CircleImage(10, "solid", Color.RED);
    WorldImage discText = new TextImage(
            "CircleImage(10, \"solid\", Color.RED)", Color.RED);

    WorldImage rectangle = new RectangleImage(60, 20, OutlineMode.SOLID,
            Color.ORANGE);
    WorldImage rectangleText = new TextImage(
            "RectangleImage(60, 20, \"solid\", Color.ORANGE)", Color.ORANGE);

    WorldImage frame = new RectangleImage(60, 20, "outline", Color.BLACK);
    WorldImage frameText = new TextImage(
            "RectangleImage(60, 20, \"outline\", Color.BLACK)", Color.BLACK);

    WorldImage line = new LineImage(new Posn(80, 30), Color.GREEN);
    WorldImage lineText = new TextImage(
            "LineImage(new Posn(80, 30), Color.GREEN)", Color.GREEN);

    WorldImage triangle = new TriangleImage(new Posn(50, 0), new Posn(0, 40),
            new Posn(-50, 10), "solid", Color.CYAN);
    WorldImage triangleText = new TextImage(
            "TriangleImage(new Posn(50, 0), new Posn(0, 40), new Posn(-50, 10),\n"
                    + "\"solid\", Color.CYAN)", Color.CYAN);

    WorldImage ellipse = new RotateImage(new EllipseImage(60, 20, OutlineMode.OUTLINE,
            Color.BLUE), 75);
    WorldImage ellipseText = new TextImage(
            "RotateImage(EllipseImage(60, 20, \"outline\" Color.BLUE), 75)", Color.BLUE);

    WorldImage oval = new EllipseImage(60, 20, "solid", Color.YELLOW);
    WorldImage ovalText = new TextImage(
            "OvalImage(60, 20, \"solid\", Color.YELLOW)", Color.YELLOW);

    WorldImage hexagon = new HexagonImage(20.0, "solid", Color.PINK);
    WorldImage hexagonText = new TextImage(
            "HexagonImage(20.0, \"solid\", Color.PINK)", Color.PINK);

    WorldImage polygonText = new TextImage(
            "RegularPolyImage(20.0, <# sides>, \"outline\", Color.DARK_GRAY)",
            Color.DARK_GRAY);
    WorldImage polygon1 = new RegularPolyImage(20.0, 3, "outline",
            Color.DARK_GRAY);
    WorldImage polygon2 = new RegularPolyImage(20.0, 4, "outline",
            Color.DARK_GRAY);
    WorldImage polygon3 = new RegularPolyImage(20.0, 5, "outline",
            Color.DARK_GRAY);
    WorldImage polygon4 = new RegularPolyImage(20.0, 6, "outline",
            Color.DARK_GRAY);

    WorldImage overlay = new OverlayImages(disc, new OverlayImages(oval,
            rectangle));
    WorldImage overlayText = new TextImage(
            "OverlayImages(disc, OverlayImages(oval, rectangle))", Color.BLACK);

    WorldImage overlayXY = new OverlayImagesXY(disc, rectangle, -50, -30);
    WorldImage overlayXYText = new TextImage(
            "OverlayImagesXY(disc, rectangle, -50, -30)", Color.BLACK);

    WorldImage rotate35 = new RotateImage(new FrameImage(overlayXY), 35);
    WorldImage rotate35Text = new TextImage("RotateImage(overlayXY, 35)",
            Color.BLACK);

    WorldImage scale2 = new ScaleImage(overlay, 2);
    WorldImage scale2Text = new TextImage("ScaleImage(overlay, 2)", Color.BLACK);

    WorldImage scale2Y = new ScaleImageXY(overlay, 1, 2);
    WorldImage scale2YText = new TextImage("ScaleImageXY(overlay, 1, 2)",
            Color.BLACK);

    WorldImage sheared = new ShearedImage(overlay, -0.5, -0.5);
    WorldImage shearedText = new TextImage("ShearedImage(overlay, -0.5, -0.5)",
            Color.BLACK);

    WorldImage duck = new ScaleImage(new FromFileImage("rubberduck.jpg"), 0.5);
    WorldImage duckText = new TextImage("FromFileImage(\"rubberduck.jpg\")",
            Color.BLACK);

    WorldImage allTransforms = new FrameImage(new ScaleImage(new ShearedImage(
            rotate35, 0.5, 0), 0.9), Color.RED);
    WorldImage allTransforms2 = new ScaleImage(new ShearedImage(polygon3, 0.5,
            0), 0.9);

    WorldImage beside = new BesideAlignImage("top", new EllipseImage(20, 70,
            "solid", Color.blue), new EllipseImage(20, 50, "outline",
            Color.blue), new EllipseImage(20, 30, "solid", Color.blue),
            new EllipseImage(20, 10, "outline", Color.blue));

    WorldImage above = new AboveAlignImage("right", new EllipseImage(70, 20,
            "solid", Color.LIGHT_GRAY), new EllipseImage(50, 20, "outline",
            Color.LIGHT_GRAY), new EllipseImage(30, 20, "solid",
            Color.LIGHT_GRAY), new EllipseImage(10, 20, "outline",
            Color.LIGHT_GRAY));

    WorldScene combined = scene
            .placeImageXY(new FrameImage(rectangle), 600, 330)
            .placeImageXY(rectangleText, 600, 300)
            .placeImageXY(frame, 600, 130).placeImageXY(frameText, 600, 100)
            .placeImageXY(circleText, 200, 20)
            .placeImageXY(new FrameImage(circle), 200, 60)
            .placeImageXY(discText, 200, 100)
            .placeImageXY(new FrameImage(disc), 200, 140)
            .placeImageXY(new FrameImage(line), 200, 220)
            .placeImageXY(lineText, 220, 180)
            .placeImageXY(new FrameImage(triangle), 200, 300)
            .placeImageXY(triangleText, 250, 260)
            .placeImageXY(new FrameImage(ellipse), 600, 60)
            .placeImageXY(ellipseText, 600, 20)
            .placeImageXY(new FrameImage(oval), 600, 220)
            .placeImageXY(ovalText, 600, 180)
            .placeImageXY(new FrameImage(hexagon), 200, 400)
            .placeImageXY(hexagonText, 200, 370)
            .placeImageXY(polygonText, 200, 450)
            .placeImageXY(overlayText, 600, 380)
            .placeImageXY(new FrameImage(overlayXY), 600, 470)
            .placeImageXY(overlayXYText, 600, 430)
            .placeImageXY(new FrameImage(polygon1), 150, 480)
            .placeImageXY(new FrameImage(polygon2), 200, 480)
            .placeImageXY(new FrameImage(polygon3), 250, 480)
            .placeImageXY(new FrameImage(polygon4), 300, 480)
            .placeImageXY(new FrameImage(overlay), 600, 400)
            .placeImageXY(new FrameImage(rotate35), 600, 580)
            .placeImageXY(rotate35Text, 600, 520)
            .placeImageXY(scale2Text, 125, 530)
            .placeImageXY(new FrameImage(scale2), 125, 560)
            .placeImageXY(scale2YText, 325, 530)
            .placeImageXY(new FrameImage(scale2Y), 325, 560)
            .placeImageXY(shearedText, 600, 650)
            .placeImageXY(new FrameImage(sheared), 600, 690)
            .placeImageXY(new FrameImage(duck), 200, 660)
            .placeImageXY(duckText, 200, 610)
            .placeImageXY(new FrameImage(beside), 400, 100)
            .placeImageXY(new FrameImage(above), 400, 200)
            .placeImageXY(new FrameImage(allTransforms), 400, 700)
            .placeImageXY(new FrameImage(allTransforms2), 400, 400);

    public void testAll(Tester t) {
        String[] args = new String[] {};
        ExamplesImageDrawings.main(args);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(800, 800);

        // WorldImage pic = ExamplesImageDrawings.makeText(15);

        ExamplesImageDrawings e = new ExamplesImageDrawings();

        // show several images in the canvas
        boolean makeDrawing = c.show() && c.drawScene(e.combined);
    }
}
