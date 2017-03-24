package worldimagestests;

import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldcanvas.*;
import javalib.worldimages.*;

public class ExamplesOverlay {

    WorldImage rect1 = new RectangleImage(20, 40, "solid", new Color(0, 0, 255,
            80));
    WorldImage rect2 = new RectangleImage(40, 20, "solid", new Color(255, 0, 0,
            50)).movePinhole(-20, -10);

    WorldImage overlay1 = new OverlayOffsetAlign(AlignModeX.LEFT,
            AlignModeY.TOP, rect1, 10, 5, rect2);
    WorldImage text1 = new TextImage(
            "new OverlayOffsetAlign(\"left\", \"top\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay2 = new OverlayOffsetAlign(AlignModeX.LEFT,
            AlignModeY.BOTTOM, rect1, 10, 5, rect2);
    WorldImage text2 = new TextImage(
            "new OverlayOffsetAlign(\"left\", \"bottom\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay3 = new OverlayOffsetAlign(AlignModeX.LEFT,
            AlignModeY.MIDDLE, rect1, 10, 5, rect2);
    WorldImage text3 = new TextImage(
            "new OverlayOffsetAlign(\"left\", \"middle\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay4 = new OverlayOffsetAlign(AlignModeX.LEFT,
            AlignModeY.PINHOLE, rect1, 10, 5, rect2);
    WorldImage text4 = new TextImage(
            "new OverlayOffsetAlign(\"left\", \"pinhole\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay5 = new OverlayOffsetAlign(AlignModeX.RIGHT,
            AlignModeY.TOP, rect1, 10, 5, rect2);
    WorldImage text5 = new TextImage(
            "new OverlayOffsetAlign(\"right\", \"top\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay6 = new OverlayOffsetAlign(AlignModeX.RIGHT,
            AlignModeY.BOTTOM, rect1, 10, 5, rect2);
    WorldImage text6 = new TextImage(
            "new OverlayOffsetAlign(\"right\", \"bottom\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay7 = new OverlayOffsetAlign(AlignModeX.RIGHT,
            AlignModeY.MIDDLE, rect1, 10, 5, rect2);
    WorldImage text7 = new TextImage(
            "new OverlayOffsetAlign(\"right\", \"middle\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay8 = new OverlayOffsetAlign(AlignModeX.RIGHT,
            AlignModeY.PINHOLE, rect1, 10, 5, rect2);
    WorldImage text8 = new TextImage(
            "new OverlayOffsetAlign(\"right\", \"pinhole\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay9 = new OverlayOffsetAlign(AlignModeX.CENTER,
            AlignModeY.TOP, rect1, 10, 5, rect2);
    WorldImage text9 = new TextImage(
            "new OverlayOffsetAlign(\"center\", \"top\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay10 = new OverlayOffsetAlign(AlignModeX.CENTER,
            AlignModeY.BOTTOM, rect1, 10, 5, rect2);
    WorldImage text10 = new TextImage(
            "new OverlayOffsetAlign(\"center\", \"bottom\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay11 = new OverlayOffsetAlign(AlignModeX.CENTER,
            AlignModeY.MIDDLE, rect1, 10, 5, rect2);
    WorldImage text11 = new TextImage(
            "new OverlayOffsetAlign(\"center\", \"middle\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay12 = new OverlayOffsetAlign(AlignModeX.CENTER,
            AlignModeY.PINHOLE, rect1, 10, 5, rect2);
    WorldImage text12 = new TextImage(
            "new OverlayOffsetAlign(\"center\", \"pinhole\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay13 = new OverlayOffsetAlign(AlignModeX.PINHOLE,
            AlignModeY.TOP, rect1, 10, 5, rect2);
    WorldImage text13 = new TextImage(
            "new OverlayOffsetAlign(\"pinhole\", \"top\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay14 = new OverlayOffsetAlign(AlignModeX.PINHOLE,
            AlignModeY.BOTTOM, rect1, 10, 5, rect2);
    WorldImage text14 = new TextImage(
            "new OverlayOffsetAlign(\"pinhole\", \"bottom\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay15 = new OverlayOffsetAlign(AlignModeX.PINHOLE,
            AlignModeY.MIDDLE, rect1, 10, 5, rect2);
    WorldImage text15 = new TextImage(
            "new OverlayOffsetAlign(\"pinhole\", \"middle\", rect1, 10, 5, rect2)",
            Color.BLACK);
    WorldImage overlay16 = new OverlayOffsetAlign(AlignModeX.PINHOLE,
            AlignModeY.PINHOLE, rect1, 10, 5, rect2);
    WorldImage text16 = new TextImage(
            "new OverlayOffsetAlign(\"pinhole\", \"pinhole\", rect1, 10, 5, rect2)",
            Color.BLACK);

    WorldScene squares = makeSquares();

    static WorldScene makeSquares() {
        WorldImage img = new RectangleImage(640, 640, OutlineMode.SOLID, Color.GREEN);
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                img = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, new RectangleImage(10, 10, OutlineMode.SOLID, new Color(255, x * 3 + 63, y * 3 + 63)), -10 * x, -10 * y, img);
            }
        }
        WorldScene s = new WorldScene(640, 640);
        s.placeImageXY(img, 320, 320);
        return s;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(600, 850);

        ExamplesOverlay e = new ExamplesOverlay();
        WorldScene scn = new WorldScene(600, 850);
        scn.placeImageXY(new FrameImage(e.overlay1), 50, 50);
        scn.placeImageXY(new FrameImage(e.overlay2), 50, 100);
        scn.placeImageXY(new FrameImage(e.overlay3), 50, 150);
        scn.placeImageXY(new FrameImage(e.overlay4), 50, 200);
        scn.placeImageXY(new FrameImage(e.overlay5), 50, 250);
        scn.placeImageXY(new FrameImage(e.overlay6), 50, 300);
        scn.placeImageXY(new FrameImage(e.overlay7), 50, 350);
        scn.placeImageXY(new FrameImage(e.overlay8), 50, 400);
        scn.placeImageXY(new FrameImage(e.overlay9), 50, 450);
        scn.placeImageXY(new FrameImage(e.overlay10), 50, 500);
        scn.placeImageXY(new FrameImage(e.overlay11), 50, 550);
        scn.placeImageXY(new FrameImage(e.overlay12), 50, 600);
        scn.placeImageXY(new FrameImage(e.overlay13), 50, 650);
        scn.placeImageXY(new FrameImage(e.overlay14), 50, 700);
        scn.placeImageXY(new FrameImage(e.overlay15), 50, 750);
        scn.placeImageXY(new FrameImage(e.overlay16), 50, 800);
        scn.placeImageXY(new TextImage("rect1 = blue, rect2 = red. Red pinhole in upper left corner", Color.BLACK), 300, 20);
        scn.placeImageXY(e.text1, 300, 50);
        scn.placeImageXY(e.text2, 300, 100);
        scn.placeImageXY(e.text3, 300, 150);
        scn.placeImageXY(e.text4, 300, 200);
        scn.placeImageXY(e.text5, 300, 250);
        scn.placeImageXY(e.text6, 300, 300);
        scn.placeImageXY(e.text7, 300, 350);
        scn.placeImageXY(e.text8, 300, 400);
        scn.placeImageXY(e.text9, 300, 450);
        scn.placeImageXY(e.text10, 300, 500);
        scn.placeImageXY(e.text11, 300, 550);
        scn.placeImageXY(e.text12, 300, 600);
        scn.placeImageXY(e.text13, 300, 650);
        scn.placeImageXY(e.text14, 300, 700);
        scn.placeImageXY(e.text15, 300, 750);
        scn.placeImageXY(e.text16, 300, 800);

        // show several images in the canvas
        boolean makeDrawing = c.show() && c.drawScene(scn);

        c = new WorldCanvas(640, 640);
        c.show();
        scn = makeSquares();
        c.drawScene(scn);
        c = new WorldCanvas(1280, 1280);
        c.show();
        scn = new WorldScene(1280, 1280);
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                scn.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, new Color(x * 3 + 63, 255, y * 3 + 63)).movePinhole(-10, -10), 20 * x, 20 * y);
            }
        }
        c.drawScene(scn);
    }
}
