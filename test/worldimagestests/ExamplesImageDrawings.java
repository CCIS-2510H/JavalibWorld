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
    public static WorldImage makeText(Posn pos, int size) {
        WorldImage hello = new TextImage("quickbrownfoxjumpedoveralazydog",
                size, 3, Color.blue);

        WorldImage helloRed = new OverlayImages(new RectangleImage(
                hello.getWidth(), hello.getHeight(), "solid", Color.red), hello);
        return new OverlayImages(helloRed, new CircleImage(2,
                OutlineMode.SOLID, Color.yellow));
    }

    // Images
    WorldScene scene = new WorldScene(800, 600);

    WorldImage circle = new CircleImage(10, OutlineMode.OUTLINE, Color.red);
    WorldImage circleText = new TextImage(
            "CircleImage(10, \"outline\", Color.red)", Color.red);

    WorldImage disc = new CircleImage(10, "solid", Color.red);
    WorldImage discText = new TextImage(
            "CircleImage(10, \"solid\", Color.red)", Color.red);

    WorldImage rectangle = new RectangleImage(60, 20, OutlineMode.SOLID,
            Color.orange);
    WorldImage rectangleText = new TextImage(
            "RectangleImage(60, 20, \"solid\", Color.orange)", Color.orange);

    WorldImage frame = new RectangleImage(60, 20, "outline", Color.black);
    WorldImage frameText = new TextImage(
            "RectangleImage(60, 20, \"outline\", Color.black)", Color.black);

    WorldImage line = new LineImage(new Posn(80, 10), Color.green);
    WorldImage lineText = new TextImage(
            "LineImage(new Posn(80, 10), Color.green)", Color.green);

    WorldImage triangle = new TriangleImage(new Posn(50, 0), new Posn(0, 40),
            new Posn(-50, 10), "solid", Color.cyan);
    WorldImage triangleText = new TextImage(
            "TriangleImage(new Posn(50, 0), new Posn(0, 40), new Posn(-50, 10),\n"
                    + "\"solid\", Color.cyan)", Color.cyan);

    WorldImage ellipse = new EllipseImage(60, 20, OutlineMode.OUTLINE,
            Color.blue);
    WorldImage ellipseText = new TextImage(
            "EllipseImage(60, 20, \"outline\" Color.blue)", Color.blue);

    WorldImage oval = new EllipseImage(60, 20, "solid", Color.yellow);
    WorldImage ovalText = new TextImage(
            "OvalImage(60, 20, \"solid\", Color.yellow)", Color.yellow);

    WorldImage hexagon = new HexagonImage(20.0, 0.72, "solid", Color.darkGray);
    WorldImage hexagonText = new TextImage(
            "HexagonImage(20.0, 0.72, \"solid\", Color.darkGray)", Color.darkGray);

    WorldScene combined = scene.placeImageXY(rectangle, 600, 330)
            .placeImageXY(rectangleText, 600, 300)
            .placeImageXY(frame, 600, 130).placeImageXY(frameText, 600, 100)
            .placeImageXY(circleText, 200, 20).placeImageXY(circle, 200, 60)
            .placeImageXY(discText, 200, 100).placeImageXY(disc, 200, 140)
            .placeImageXY(line, 200, 220).placeImageXY(lineText, 220, 180)
            .placeImageXY(triangle, 200, 300)
            .placeImageXY(triangleText, 250, 260)
            .placeImageXY(ellipse, 600, 60).placeImageXY(ellipseText, 600, 20)
            .placeImageXY(oval, 600, 220).placeImageXY(ovalText, 600, 180)
            .placeImageXY(hexagon, 200, 410)
            .placeImageXY(hexagonText, 200, 380);

    public void testAll(Tester t) {
        String[] args = new String[] {};
        ExamplesImageDrawings.main(args);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(800, 600);

        // WorldImage pic = ExamplesImageDrawings.makeText(new Posn(300, 400),
        // 15);

        ExamplesImageDrawings e = new ExamplesImageDrawings();

        // show several images in the canvas
        boolean makeDrawing = c.show() && c.drawScene(e.combined);
    }
}
