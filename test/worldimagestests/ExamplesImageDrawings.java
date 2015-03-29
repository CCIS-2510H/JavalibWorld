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

    WorldImage ellipse = new RotateImage(new EllipseImage(60, 20,
            OutlineMode.OUTLINE, Color.BLUE), 75);
    WorldImage ellipseText = new TextImage(
            "RotateImage(EllipseImage(60, 20, \"outline\" Color.BLUE), 75)",
            Color.BLUE);

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

    WorldImage overlayXY = new OverlayOffsetImages(disc, -50, -30, rectangle);
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

    WorldImage whitePetal = new OverlayOffsetImages(new CircleImage(15,
            OutlineMode.SOLID, Color.WHITE), 60, 0, new OverlayOffsetImages(
            new TriangleImage(new Posn(0, 0), new Posn(60, -15), new Posn(60,
                    15), OutlineMode.SOLID, Color.WHITE), 30, 0,
            new CircleImage(75, OutlineMode.SOLID, new Color(0, 0, 255, 0))));
    WorldImage yellowPetal = new OverlayOffsetImages(new CircleImage(15,
            OutlineMode.SOLID, Color.YELLOW), 60, 0, new OverlayOffsetImages(
            new TriangleImage(new Posn(0, 0), new Posn(60, -15), new Posn(60,
                    15), OutlineMode.SOLID, Color.YELLOW), 30, 0,
            new CircleImage(75, OutlineMode.SOLID, new Color(0, 0, 255, 0))));
    WorldImage daisyImg = new OverlayImages(new RotateImage(whitePetal, 0),
            new OverlayImages(new RotateImage(yellowPetal, 60),
                    new OverlayImages(new RotateImage(whitePetal, 120),
                            new OverlayImages(
                                    new RotateImage(yellowPetal, 180),
                                    new OverlayImages(new RotateImage(
                                            whitePetal, 240), new RotateImage(
                                            yellowPetal, 300))))));
    WorldScene daisy = scene.placeImageXY(
            new RectangleImage(400, 400, OutlineMode.SOLID, Color.GRAY), 100,
            100).placeImageXY(new FrameImage(daisyImg.movePinhole(100, 100)),
            100, 100);

    WorldImage arrow = new BesideImage(new RectangleImage(10, 5, "outline",
            Color.BLACK), new TriangleImage(new Posn(0, -10), new Posn(0, 10),
            new Posn(10, 0), "outline", Color.BLACK));
    WorldImage pinhole = new CircleImage(2, "solid", Color.RED);
    WorldImage center = new CircleImage(2, "solid", Color.GREEN);
    WorldImage sq = new RectangleImage(20, 20, "solid", Color.GRAY);
    WorldImage ooa = new OverlayOffsetAlign("pinhole",
            "pinhole", sq.movePinhole(5, 25), 0, 0, new RectangleImage(100, 20,
                    "solid", Color.LIGHT_GRAY).movePinhole(-25, -5));

    WorldScene pinholes = drawCircles(generateCircles())
            .placeImageXY(new FrameImage(ooa), 400, 200)
            .placeImageXY(center, 400, 200)
            .placeImageXY(pinhole, ooa.pinhole.x + 400, ooa.pinhole.y + 200);

    WorldImage[] generateCircles() {
        WorldImage[] pinholeImages = new WorldImage[5];
        pinholeImages[0] = new CircleImage(20, "solid", Color.BLUE);
        pinholeImages[1] = pinholeImages[0].movePinhole(10, -10);
        pinholeImages[2] = new ShearedImage(pinholeImages[1], -0.5, 0.0);
        pinholeImages[3] = new RotateImage(pinholeImages[2], 90);
        pinholeImages[4] = new OverlayOffsetAlign("pinhole", "pinhole",
                pinholeImages[3], 0, 0, pinholeImages[2]);
        return pinholeImages;
    }

    WorldScene drawCircles(WorldImage[] circles) {
        WorldScene s = scene;
        int x = 100;
        int horizDist = 100;
        for (int i = 0; i < circles.length - 1; i++) {
            int y = 100 * (i + 1);
            s = s.placeImageXY(circles[i], x, y)
                    .placeImageXY(arrow, x + horizDist / 2, y)
                    .placeImageXY(circles[i + 1], x + horizDist, y)
                    .placeImageXY(center, x, y)
                    .placeImageXY(center, x + horizDist, y)
                    .placeImageXY(pinhole, x + circles[i].pinhole.x,
                            y + circles[i].pinhole.y)
                    .placeImageXY(pinhole,
                            x + horizDist + circles[i + 1].pinhole.x,
                            y + circles[i + 1].pinhole.y);
        }
        return s;
    }

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
        // boolean makeDrawing = c.show() && c.drawScene(e.combined);
        c = new WorldCanvas(800, 800);
        // boolean daisy = c.show() && c.drawScene(e.daisy);
        boolean pins = c.show() && c.drawScene(e.pinholes);
    }
}
