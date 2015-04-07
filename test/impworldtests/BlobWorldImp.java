package impworldtests;

import javalib.impworld.*;
import javalib.worldcanvas.WorldScene;
import javalib.worldimages.*;

import java.awt.Color;
import java.util.Random;

import tester.*;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * Class that represents a colored disk that moves around the Canvas
 */
class Blob {

    Posn center;
    int radius;
    Color col;

    /** The constructor */
    Blob(Posn center, int radius, Color col) {
        this.center = center;
        this.radius = radius;
        this.col = col;
    }

    /** produce the image of this blob at its current location and color */
    WorldImage blobImage() {
        return new CircleImage(this.radius, OutlineMode.OUTLINE, this.col);
    }

    /**
     * move this blob 20 pixels in the direction given by the ke or change its
     * color to Green, Red or Yellow
     */
    public void moveBlob(String ke) {
        if (ke.equals("right")) {
            this.center.x = this.center.x + 5;
        } else if (ke.equals("left")) {
            this.center.x = this.center.x - 5;
        } else if (ke.equals("up")) {
            this.center.y = this.center.y - 5;
        } else if (ke.equals("down")) {
            this.center.y = this.center.y + 5;
            ;
        }
        // change the color to Y, G, R
        else if (ke.equals("Y")) {
            this.col = Color.YELLOW;
        } else if (ke.equals("G")) {
            this.col = Color.GREEN;
        } else if (ke.equals("R")) {
            this.col = Color.RED;
        }
    }

    /** produce a new blob moved by a random distance < n pixels */
    void randomMove(int n) {
        this.center = new Posn(this.center.x + this.randomInt(n), this.center.y
                + this.randomInt(n));
    }

    /** helper method to generate a random number in the range -n to n */
    int randomInt(int n) {
        return -n + (new Random().nextInt(2 * n + 1));
    }

    /** is the blob outside the bounds given by the width and height */
    boolean outsideBounds(int width, int height) {
        return this.center.x < 0 || this.center.x > width || this.center.y < 0
                || this.center.y > height;
    }

    /** is the blob near the center of area given by the width and height */
    boolean nearCenter(int width, int height) {
        return this.center.x > width / 2 - 10 && this.center.x < width / 2 + 10
                && this.center.y > height / 2 - 10
                && this.center.y < height / 2 + 10;
    }
}

/** Represent the world of a Blob */
public class BlobWorldImp extends World {

    int width = 200;
    int height = 300;
    Blob blob;

    /** The constructor */
    public BlobWorldImp(Blob blob) {
        super();
        this.blob = blob;
    }

    /** Move the Blob when the player presses a key */
    public void onKeyEvent(String ke) {
        if (ke.equals("x"))
            this.endOfWorld("Goodbye");
        else
            this.blob.moveBlob(ke);
    }

    /**
     * On tick check whether the Blob is out of bounds, or fell into the black
     * hole in the middle. If all is well, move the Blob in a random direction.
     */
    public void onTick() {
        this.blob.randomMove(5);
    }

    /**
     * On mouse click move the blob to the mouse location, make the color red.
     */
    public void onMouseClicked(Posn loc) {
        this.blob.center = loc;
    }

    /**
     * The entire background image for this world It illustrates the use of most
     * of the <code>WorldImage</code> shapes
     */
    public WorldImage blackHole = new OverlayImage(new CircleImage(10,
            OutlineMode.SOLID, Color.BLACK), new RectangleImage(this.width,
            this.height, OutlineMode.SOLID, Color.BLUE));

    /**
     * produce the image of this world by adding the moving blob to the
     * background image
     */
    public WorldScene makeScene() {
        return this
                .getEmptyScene()
                .placeImageXY(this.blackHole, this.width / 2, this.height / 2)
                .placeImageXY(this.blob.blobImage(), this.blob.center.x,
                        this.blob.center.y);
    }

    /**
     * produce the image of this world by adding the given <code>String</code>
     * to the image with the blob at its point of demise
     */
    public WorldScene lastScene(String s) {
        return this.makeScene().placeImageXY(new TextImage(s, Color.red), 100,
                40);
    }

    /**
     * Check whether the Blob is out of bounds, or fell into the black hole in
     * the middle.
     */
    public WorldEnd worldEnds() {
        // if the blob is outside the canvas, stop
        if (this.blob.outsideBounds(this.width, this.height)) {
            return new WorldEnd(true,
                    this.lastScene("Blob is outside the bounds"));
        }
        // time ends is the blob falls into the black hole in the middle
        if (this.blob.nearCenter(this.width, this.height)) {
            return new WorldEnd(true, this.makeScene().placeImageXY(
                    new TextImage("Black hole ate the blob", 13, 3, Color.red),
                    100, 40));
        } else {
            return new WorldEnd(false, this.makeScene());
        }
    }

    // support for the regression tests
    public static BlobExamples examplesInstance = new BlobExamples();
}

class BlobExamples {

    // examples of data for the Blob class:
    Blob b1 = new Blob(new Posn(100, 100), 50, Color.RED);
    Blob b1left = new Blob(new Posn(95, 100), 50, Color.RED);
    Blob b1right = new Blob(new Posn(105, 100), 50, Color.RED);
    Blob b1up = new Blob(new Posn(100, 95), 50, Color.RED);
    Blob b1down = new Blob(new Posn(100, 105), 50, Color.RED);
    Blob b1G = new Blob(new Posn(100, 100), 50, Color.GREEN);
    Blob b1Y = new Blob(new Posn(100, 100), 50, Color.YELLOW);

    // examples of data for the BlobWorld class:
    BlobWorldImp b1w = new BlobWorldImp(this.b1);
    BlobWorldImp b1leftw = new BlobWorldImp(this.b1left);
    BlobWorldImp b1rightw = new BlobWorldImp(this.b1right);
    BlobWorldImp b1upw = new BlobWorldImp(this.b1up);
    BlobWorldImp b1downw = new BlobWorldImp(this.b1down);
    BlobWorldImp b1Gw = new BlobWorldImp(this.b1G);
    BlobWorldImp b1Yw = new BlobWorldImp(this.b1Y);
    BlobWorldImp b1mouse50x50w = new BlobWorldImp(new Blob(new Posn(50, 50),
            50, Color.RED));

    BlobWorldImp bwOutOfBounds = new BlobWorldImp(new Blob(new Posn(100, 350),
            50, Color.RED));

    BlobWorldImp bwInTheCenter = new BlobWorldImp(new Blob(new Posn(100, 150),
            50, Color.RED));

    void reset() {

        // examples of data for the Blob class:
        this.b1 = new Blob(new Posn(100, 100), 50, Color.RED);
        this.b1left = new Blob(new Posn(95, 100), 50, Color.RED);
        this.b1right = new Blob(new Posn(105, 100), 50, Color.RED);
        this.b1up = new Blob(new Posn(100, 95), 50, Color.RED);
        this.b1down = new Blob(new Posn(100, 105), 50, Color.RED);
        this.b1G = new Blob(new Posn(100, 100), 50, Color.GREEN);
        this.b1Y = new Blob(new Posn(100, 100), 50, Color.YELLOW);

        // examples of data for the BlobWorld class:
        this.b1w = new BlobWorldImp(this.b1);
        this.b1leftw = new BlobWorldImp(this.b1left);
        this.b1rightw = new BlobWorldImp(this.b1right);
        this.b1upw = new BlobWorldImp(this.b1up);
        this.b1downw = new BlobWorldImp(this.b1down);
        this.b1Gw = new BlobWorldImp(this.b1G);
        this.b1Yw = new BlobWorldImp(this.b1Y);
        this.b1mouse50x50w = new BlobWorldImp(new Blob(new Posn(50, 50), 50,
                Color.RED));

        this.bwOutOfBounds = new BlobWorldImp(new Blob(new Posn(100, 350), 50,
                Color.RED));

        this.bwInTheCenter = new BlobWorldImp(new Blob(new Posn(100, 150), 50,
                Color.RED));
    }

    /** test the method moveBlob in the Blob class */
    void testMoveBlob(Tester t) {
        this.reset();
        this.b1.moveBlob("left");
        t.checkExpect(this.b1, this.b1left, "test moveBolb - left " + "\n");

        this.reset();
        this.b1.moveBlob("right");
        t.checkExpect(this.b1, this.b1right, "test movelob - right " + "\n");

        this.reset();
        this.b1.moveBlob("up");
        t.checkExpect(this.b1, this.b1up, "test moveBlob - up " + "\n");

        this.reset();
        this.b1.moveBlob("down");
        t.checkExpect(this.b1, this.b1down, "test moveBlob - down " + "\n");

        this.reset();
        this.b1.moveBlob("G");
        t.checkExpect(this.b1, this.b1G, "test moveBlob - G " + "\n");

        this.reset();
        this.b1.moveBlob("Y");
        t.checkExpect(this.b1, this.b1Y, "test moveBlob - Y " + "\n");

        this.reset();
        this.b1G.moveBlob("R");
        t.checkExpect(this.b1G, this.b1, "test moveBlob - R " + "\n");
    }

    /** test the method onKeyEvent in the BlobWorld class */
    void testOnKeyEvent(Tester t) {

        this.reset();
        this.b1w.onKeyEvent("left");
        t.checkExpect(this.b1w, this.b1leftw, "test moveBolb - left " + "\n");

        this.reset();
        this.b1w.onKeyEvent("right");
        t.checkExpect(this.b1w, this.b1rightw, "test movelob - right " + "\n");

        this.reset();
        this.b1w.onKeyEvent("up");
        t.checkExpect(this.b1w, this.b1upw, "test moveBlob - up " + "\n");

        this.reset();
        this.b1w.onKeyEvent("down");
        t.checkExpect(this.b1w, this.b1downw, "test moveBlob - down " + "\n");

        this.reset();
        this.b1w.onKeyEvent("G");
        t.checkExpect(this.b1w, this.b1Gw, "test moveBlob - G " + "\n");

        this.reset();
        this.b1w.onKeyEvent("Y");
        t.checkExpect(this.b1w, this.b1Yw, "test moveBlob - Y " + "\n");

        this.reset();
        this.b1Gw.onKeyEvent("R");
        t.checkExpect(this.b1Gw, this.b1w, "test moveBlob - R " + "\n");

        this.reset();
        this.b1Gw.onKeyEvent("x");
        t.checkExpect(
                this.b1Gw.lastWorld,
                new WorldEnd(true, this.b1Gw.makeScene().placeImageXY(
                        new TextImage("Goodbye", Color.red), 100, 40)));
    }

    /** test the method outsideBounds in the Blob class */
    void testOutsideBounds(Tester t) {

        this.reset();
        t.checkExpect(this.b1.outsideBounds(60, 200), true,
                "test outsideBounds on the right");

        this.reset();
        t.checkExpect(this.b1.outsideBounds(100, 90), true,
                "test outsideBounds below");

        this.reset();
        t.checkExpect(new Blob(new Posn(-5, 100), 50, Color.RED).outsideBounds(
                100, 110), true, "test outsideBounds above");

        this.reset();
        t.checkExpect(new Blob(new Posn(80, -5), 50, Color.BLUE).outsideBounds(
                100, 90), true, "test outsideBounds on the left");

        this.reset();
        t.checkExpect(this.b1.outsideBounds(200, 400), false,
                "test outsideBounds - within bounds");
    }

    /** test the method onMOuseClicked in the BlobWorld class */
    void testOnMouseClicked(Tester t) {

        this.reset();
        this.b1w.onMouseClicked(new Posn(50, 50));
        t.checkExpect(this.b1w, this.b1mouse50x50w);
    }

    /** test the method nearCenter in the Blob class */
    void testNearCenter(Tester t) {

        this.reset();
        t.checkExpect(this.b1.nearCenter(200, 200), true,
                "test nearCenter - true");

        this.reset();
        t.checkExpect(this.b1.nearCenter(200, 100), false,
                "test nearCenter - false");
    }

    /** the method randomInt in the Blob class */
    void testRandomInt(Tester t) {

        this.reset();
        t.checkOneOf("test randomInt", this.b1.randomInt(3), -3, -2, -1, 0, 1,
                2, 3);

        this.reset();
        t.checkNoneOf("test randomInt", this.b1.randomInt(3), -5, -4, 4, 5);
    }

    /** test the method randomMove in the Blob class */
    void testRandomMove(Tester t) {

        this.reset();
        this.b1.randomMove(1);
        t.checkOneOf("test randomMove", this.b1, new Blob(new Posn(99, 99), 50,
                Color.RED), new Blob(new Posn(99, 100), 50, Color.RED),
                new Blob(new Posn(99, 101), 50, Color.RED), new Blob(new Posn(
                        100, 99), 50, Color.RED), new Blob(new Posn(100, 100),
                        50, Color.RED), new Blob(new Posn(100, 101), 50,
                        Color.RED), new Blob(new Posn(101, 99), 50, Color.RED),
                new Blob(new Posn(101, 100), 50, Color.RED), new Blob(new Posn(
                        101, 101), 50, Color.RED));
    }

    /** test the method onTick in the BlobWorld class */
    void testOnTick1(Tester t) {

        this.reset();
        boolean result = true;
        for (int i = 0; i < 20; i++) {
            this.reset();
            this.b1w.onTick();
            result = result && t.checkRange(this.b1w.blob.center.x, 95, 106)
                    && t.checkRange(this.b1w.blob.center.y, 95, 106);
        }
        t.checkExpect(result);
    }

    // test the method worldEnds for the class BlobWorld
    void testWorldEnds(Tester t) {

        this.reset();
        t.checkExpect(
                this.bwOutOfBounds.worldEnds(),
                new WorldEnd(true, this.bwOutOfBounds.makeScene().placeImageXY(
                        new TextImage("Blob is outside the bounds", Color.red),
                        100, 40)));

        this.reset();
        t.checkExpect(
                this.bwInTheCenter.worldEnds(),
                new WorldEnd(true, this.bwInTheCenter.makeScene().placeImageXY(
                        new TextImage("Black hole ate the blob", 13, 3,
                                Color.red), 100, 40)));

        this.reset();
        t.checkExpect(this.b1w.worldEnds(),
                new WorldEnd(false, this.b1w.makeScene()));
    }

    /** run the animation */
    BlobWorldImp w1 = new BlobWorldImp(new Blob(new Posn(100, 200), 20,
            Color.RED));
    BlobWorldImp w2 = new BlobWorldImp(new Blob(new Posn(100, 200), 20,
            Color.RED));
    BlobWorldImp w3 = new BlobWorldImp(new Blob(new Posn(100, 200), 20,
            Color.RED));

    // test that we can run three different animations concurrently
    // with the events directed to the correct version of the world
    public static void main(String[] argv) {

        // run the tests - showing only the failed test results
        BlobExamples be = new BlobExamples();
        Tester.runReport(be, false, false);

        // run the game
        BlobWorldImp w = new BlobWorldImp(new Blob(new Posn(150, 100), 20,
                Color.RED));
        w.bigBang(200, 300, 0.3);
    }

}