package funworldtests;

import java.awt.Color;

import tester.*;
import javalib.funworld.*;
import javalib.worldimages.*;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * @author Viera K. Proulx
 * @since 5 February 2012
 */

// A class to display one of the ticky-tacky houses on the hillside.
class House {
    Posn loc; // of the SW corner of the house base
    int width; // the width of the house
    int height; // the height of the house
    Color color; // the color of the house
    Person person;

    House(Posn loc, int width, int height, Color color) {
        this.loc = loc;
        this.width = width;
        this.height = height;
        this.color = color;
        this.person = new Person(this.width / 2, this.height / 2, this.color);
    }

    // make the image of this house
    WorldImage houseImage() {
        WorldImage house = new RectangleImage(this.width, this.height, "solid",
                this.color);
        house = new AboveImage(new TriangleImage(new Posn(0, 0), new Posn(
                this.width / 2, -this.height / 2), new Posn(this.width, 0),
                "solid", Color.RED), house);
        house = new OverlayOffsetAlign("center", "bottom", new RectangleImage(
                this.width / 2, (int) Math.round(this.height / 2.0), "solid",
                Color.GRAY), 0, 0, house);
        house = new OverlayOffsetAlign("center", "bottom",
                this.person.personImage(), 0, 0, house);
        return house;
    }

    int getX() {
        return this.loc.x + (this.width / 2);
    }

    int getY() {
        return this.loc.y - (int) ((this.height + (this.height / 2.0)) / 2.0);
    }
}

class Person {
    int width;
    int height;
    Color color;

    Person(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    // make the image of this person
    WorldImage personImage() {
        WorldImage leftLeg = new LineImage(new Posn(this.width / 2,
                -this.height / 2), Color.BLACK);
        WorldImage rightLeg = new LineImage(new Posn(-this.width / 2,
                -this.height / 2), Color.BLACK);
        WorldImage arms = new LineImage(new Posn(this.width, 0), Color.BLACK);
        WorldImage head = new CircleImage(this.height / 4 - 1, "outline",
                Color.BLACK);
        WorldImage legs = new BesideImage(leftLeg, rightLeg);
        return new AboveImage(head, arms, legs);
    }
}

// a class to add some trees to the ticky-tack world
class Tree {
    Posn loc; // the left base of the trunk
    int trunkHeight; // the height of the trunk
    int width; // the width of the crown of the tree
    int height; // the height of the crown of the tree

    Tree(Posn loc, int trunkHeight, int width, int height) {
        this.loc = loc;
        this.trunkHeight = trunkHeight;
        this.width = width;
        this.height = height;
    }

    // make the image of this tree
    WorldImage treeImage() {
        return new AboveImage(new EllipseImage(this.width, this.height,
                "solid", Color.GREEN), new RectangleImage(this.width / 4,
                this.trunkHeight, "solid", new Color(0x84, 0x3c, 0x24)));
    }

    int getX() {
        return loc.x + (this.width / 8);
    }

    int getY() {
        return loc.y - ((this.trunkHeight + this.height) / 2);
    }
}

// a white cloud floating over the ticky-tack houses
class Cloud {
    Posn loc;
    int width;
    int height;

    Cloud(Posn loc, int width, int height) {
        this.loc = loc;
        this.width = width;
        this.height = height;
    }

    // move this cloud east with the wind
    Cloud move(int dx) {
        return new Cloud(new Posn(this.loc.x + dx, this.loc.y), this.width,
                this.height);
    }

    // when cloud moves off the canvas, move it back to the west
    Cloud moveInBounds(int dx, int hbound) {
        if (this.loc.x + dx > hbound)
            return new Cloud(new Posn(dx, this.loc.y), this.width, this.height);
        else
            return this.move(dx);
    }

    // make the image of this cloud
    WorldImage cloudImage() {
        return new OverlayOffsetImage(new EllipseImage(this.width / 2,
                this.height / 2, "solid", Color.WHITE), -this.width / 2,
                this.height / 3, new OverlayOffsetImage(new EllipseImage(
                        this.width, this.height, "solid", Color.WHITE),
                        -this.width / 4, -this.height / 2, new EllipseImage(
                                this.width / 2, this.height / 2, "solid",
                                Color.WHITE)));
    }
}

// the sun above this pretty world
class Sun {
    int size; // limited to less than 30

    Sun(int size) {
        this.size = size % 30;
    }

    // make the image of this sun - somewhat transparent
    WorldImage sunImage() {
        return new CircleImage(this.size, "solid", new Color(255, 255, 0, 230));
    }
}

/** Class that represents little houses with clouds above */
public class TickyTackFun extends World {
    House h1 = new House(new Posn(0, 300), 60, 80, Color.red);
    House h2 = new House(new Posn(60, 300), 60, 40, Color.green);
    House h3 = new House(new Posn(120, 300), 80, 60, Color.pink);
    House h4 = new House(new Posn(200, 300), 70, 50, Color.cyan);
    House h5 = new House(new Posn(270, 300), 90, 70, Color.yellow);
    House h6 = new House(new Posn(360, 300), 80, 60, Color.magenta);
    House h7 = new House(new Posn(440, 300), 90, 70, Color.orange);

    Tree t1 = new Tree(new Posn(550, 300), 40, 50, 80);
    Tree t2 = new Tree(new Posn(580, 300), 20, 30, 30);

    Cloud cloud;

    Sun sun = new Sun(20);

    int width, height;

    TickyTackFun(Cloud cloud, Sun sun, int width, int height) {
        this.cloud = cloud;
        this.sun = sun;
        this.width = width;
        this.height = height;
    }

    // move the cloud, making sure there is always a cloud in the sky
    public World onTick() {
        return new TickyTackFun(this.cloud.moveInBounds(4, 600), this.sun,
                this.width, this.height);
    }

    // the space bar makes the sun grow till max, then shrink and grow again
    public World onKeyEvent(String ke) {
        if (ke.equals(" "))
            return new TickyTackFun(this.cloud, new Sun(this.sun.size + 3),
                    this.width, this.height);

        else if (ke.equals("x")) {
            return this.endOfWorld("Have a nice Day!");
        } else
            return this;
    }

    // move the cloud to the location of the mouse click - if it is high enough
    public World onMouseClicked(Posn p) {
        if (p.y < 100)
            return new TickyTackFun(new Cloud(p, this.cloud.width,
                    this.cloud.height), this.sun, this.width, this.height);
        else
            return this;
    }

    // say goodbye to the sun, when it gets to be big enough again
    public WorldEnd worldEnds() {
        if (10 < sun.size && sun.size < 20)
            return new WorldEnd(true, this.makeScene().placeImageXY(
                    new TextImage("Goodbye sun!", 15, FontStyle.BOLD_ITALIC, Color.RED), 150, 80));
        else
            return new WorldEnd(false, this.makeScene());
    }

    @Override
    public WorldScene makeScene() {
        return this
                .getEmptyScene()
                .placeImageXY(
                        new RectangleImage(width, height, "solid", Color.BLUE),
                        width / 2, height / 2)
                .placeImageXY(this.h1.houseImage(), this.h1.getX(),
                        this.h1.getY())
                .placeImageXY(this.h2.houseImage(), this.h2.getX(),
                        this.h2.getY())
                .placeImageXY(this.h3.houseImage(), this.h3.getX(),
                        this.h3.getY())
                .placeImageXY(this.h4.houseImage(), this.h4.getX(),
                        this.h4.getY())
                .placeImageXY(this.h5.houseImage(), this.h5.getX(),
                        this.h5.getY())
                .placeImageXY(this.h6.houseImage(), this.h6.getX(),
                        this.h6.getY())
                .placeImageXY(this.h7.houseImage(), this.h7.getX(),
                        this.h7.getY())
                .placeImageXY(this.t1.treeImage(), this.t1.getX(),
                        this.t1.getY())
                .placeImageXY(this.t2.treeImage(), this.t2.getX(),
                        this.t2.getY())
                .placeImageXY(this.cloud.cloudImage(), this.cloud.loc.x,
                        this.cloud.loc.y)
                .placeImageXY(this.sun.sunImage(), 50, 50);
    }

    @Override
    public WorldScene lastScene(String s) {
        return this.makeScene().placeImageXY(
                new TextImage(s, 15, FontStyle.BOLD_ITALIC, Color.RED), 150, 80);
    }

    // support for the regression tests
    public static ExamplesTickyTack examplesInstance = new ExamplesTickyTack();

}

class ExamplesTickyTack {
    ExamplesTickyTack() {
    }

    House h1 = new House(new Posn(0, 300), 60, 80, Color.red);
    House h2 = new House(new Posn(60, 300), 60, 40, Color.green);
    House h3 = new House(new Posn(120, 300), 80, 60, Color.pink);
    House h4 = new House(new Posn(200, 300), 70, 50, Color.cyan);
    House h5 = new House(new Posn(270, 300), 90, 70, Color.yellow);
    House h6 = new House(new Posn(360, 300), 80, 60, Color.magenta);
    House h7 = new House(new Posn(440, 300), 90, 70, Color.orange);

    Tree t1 = new Tree(new Posn(550, 300), 40, 50, 80);
    Tree t2 = new Tree(new Posn(580, 300), 20, 30, 30);

    Cloud cloud = new Cloud(new Posn(200, 100), 90, 60);

    Sun sun = new Sun(25);

    TickyTackFun tworld = new TickyTackFun(this.cloud, new Sun(25), 600, 300);

    // test the method move in the class Cloud
    boolean testMove(Tester t) {
        return t.checkExpect(this.cloud.move(5), new Cloud(new Posn(205, 100),
                90, 60))
                && t.checkExpect(this.cloud.move(5), new Cloud(new Posn(205,
                        100), 90, 60));
    }

    // test the method moveInBounds in the class Cloud
    boolean testMoveInBounds(Tester t) {
        return t.checkExpect(this.cloud.moveInBounds(5, 300), new Cloud(
                new Posn(205, 100), 90, 60))
                && t.checkExpect(this.cloud.moveInBounds(5, 200), new Cloud(
                        new Posn(5, 100), 90, 60));
    }

    // test the constructor for the sun
    boolean testSunConstructor(Tester t) {
        return t.checkExpect(new Sun(35), new Sun(5));
    }

    // test the method onTick for the ticky-tack world
    boolean testOnTick(Tester t) {
        return t.checkExpect(this.tworld.onTick(), new TickyTackFun(new Cloud(
                new Posn(204, 100), 90, 60), this.sun, 600, 300));
    }

    // test the method onKeyEvent for the ticky-tack world
    boolean testOnKeyEvent(Tester t) {
        return t.checkExpect(this.tworld.onKeyEvent(" "), new TickyTackFun(
                this.cloud, new Sun(28), 600, 300))
                && t.checkExpect(new TickyTackFun(this.cloud, new Sun(28), 600,
                        300).onKeyEvent(" "), new TickyTackFun(this.cloud,
                        new Sun(1), 600, 300))
                && t.checkExpect(this.tworld.onKeyEvent("x"), this.tworld);
    }

    // test the method onMouseClicked in the class Cloud
    boolean testOnMouseClicked(Tester t) {
        return t.checkExpect(this.tworld.onMouseClicked(new Posn(150, 80)),
                new TickyTackFun(new Cloud(new Posn(150, 80), 90, 60),
                        this.sun, 600, 300))
                && t.checkExpect(
                        this.tworld.onMouseClicked(new Posn(150, 180)),
                        this.tworld);
    }

    public static void main(String[] argv) {
        ExamplesTickyTack ett = new ExamplesTickyTack();

        Tester.runReport(ett, false, false, new tester.DefaultReporter(), 80);

        TickyTackFun tworld = new TickyTackFun(ett.cloud, ett.sun, 600, 300);
        tworld.bigBang(600, 300, 0.1);
    }

}