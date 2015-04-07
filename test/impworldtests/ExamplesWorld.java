package impworldtests;

import javalib.impworld.*;
import javalib.worldcanvas.WorldScene;
import javalib.worldimages.*;

import java.awt.Color;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * A very simple example of a world
 * 
 * @author Viera K. Proulx
 * @since 5 February 2012
 */
public class ExamplesWorld extends World {
    Posn pos;
    boolean worldEnd = false;
    int width;

    ExamplesWorld(Posn pos, boolean worldEnd, int width) {
        this.pos = pos;
        this.worldEnd = worldEnd;
        this.width = width;
    }

    ExamplesWorld(Posn pos, int width) {
        this(pos, false, width);
    }

    // just a red ball moving across the screen
    public WorldScene makeScene() {
        return this.getEmptyScene().placeImageXY(
                new CircleImage(30, OutlineMode.SOLID, Color.RED), this.pos.x,
                this.pos.y);
    }

    // test all kinds of actions using key events:
    // left and right keys move the ball in the given direction
    // up key returns the ball to the left edge
    // "E" ends the world
    // "s" invokes endOfTime method
    // "q" throws a runtime exception and quits
    public void onKeyEvent(String ke) {
        System.out.println("Key event " + ke);
        if (ke.equals("right"))
            this.pos.x = this.pos.x;
        if (ke.equals("left"))
            this.pos.x = this.pos.x - 20;
        if (ke.equals("up"))
            this.pos.x = 0;
        if (ke.equals("E"))
            this.worldEnd = true;
        if (ke.equals("q"))
            throw new RuntimeException("We quit!!!");
    }

    /**
     * Check whether the World ended.
     */
    public WorldEnd worldEnds() {
        // if the blob is outside the canvas, stop
        if (this.worldEnd)
            return new WorldEnd(true, this.makeScene()
                    .placeImageXY(
                            new TextImage("End of the World!!", 13, Color.red),
                            100, 40));
        else
            return new WorldEnd(false, this.makeScene());
    }

    // at each tick print the current position of the ball
    public void onTick() {
        System.out.println("Tick -- pos = (" + this.pos.x + ", " + this.pos.y
                + ")");
        this.pos.x = (this.pos.x + 2) % this.width;
    }

    // at each mouse click print the location of the mouse click
    public void onMouseClicked(Posn loc) {
        System.out.println("Click -- pos = (" + loc.x + ", " + loc.y + ")");
        this.pos.x = loc.x;
        this.pos.y = loc.y;
    }

    // run two worlds concurrently - the one in focus responds to mouse and key
    // events, but the clock ticks regardless
    public static void main(String[] argv) {
        ExamplesWorld ew = new ExamplesWorld(new Posn(50, 60), 200);
        ExamplesWorld ew2 = new ExamplesWorld(new Posn(50, 20), 400);

        // runs two worlds: switch between by clicking on the window top bar
        // output coordinates report on the window currently in focus
        // to see that mouse click works correctly, comment out one of the
        // worlds
        ew.bigBang(200, 300, 0.1);
        ew2.bigBang(400, 200, 0.3);

    }

}