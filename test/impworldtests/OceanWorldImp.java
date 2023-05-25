package impworldtests;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.BesideImage;
import javalib.worldimages.EllipseImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.TriangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

import java.awt.Color;
import java.util.Random;

/**
 * Copyright 2012 Viera K. Proulx This program is distributed under the terms of
 * the GNU Lesser General Public License (LGPL)
 * 
 * A complete example of an interactive game, including the illustration of how
 * to run two concurrent worlds
 * 
 * @author Viera K. Proulx
 * @since 8 August 2012
 */

// the basic data used by the ocean world game
interface OceanWorldConstants {
    // a random number generator
    public Random rand = new Random();

    // the width of the world
    public int WIDTH = 400;

    // the height of the world
    public int HEIGHT = 400;

    // the color of the ocean
    public Color oceanColor = new Color(50, 150, 255);

    // the background image of the ocean
    public WorldImage oceanImage = new RectangleImage(WIDTH, HEIGHT,
            OutlineMode.SOLID, oceanColor);
}

// To represent a location (x,y) in graphics coordinates
// -- adding methods to the library Posn class
class CartPt implements OceanWorldConstants {

    int x, y;
    // teh standard constructor - invokes the one in the super class
    CartPt(int x, int y) {

        this.x = x;
        this.y = y;
    }

    // EFFECT:
    // Move this CartPt left i units -- or back to the right, if out of bounds
    void moveLeft(int i) {
        if (this.x - i < 0)
            this.x = WIDTH;
        else
            this.x = this.x - i;
    }

    // EFFECT:
    // Move this CartPt left i units -- or back to the right, if out of bounds
    // also move randomly up or down -2 to 2 pixel
    void moveLeftRandom(int i) {
        if (this.x - i < 0)
            this.x = WIDTH;
        else {
            // our tests did not catch that rand.nextInt(2)
            // did not provide all desired values
            this.x = this.x - i;
            this.y = this.y + rand.nextInt(5) - 2;
        }
        // NOTE: My tests found the error in this simple method:
        // I forgot to enclose the two statements in the else clause
        // in brackets, and so, the y coordinate was changing
        // regardless of whether the fish moved back to the start!!!
    }

    // Compute the distance from this point to the given one
    double distTo(CartPt that) {
        return Math.sqrt((this.x - that.x) * (this.x - that.x)
                + (this.y - that.y) * (this.y - that.y));
    }
}

// A Shark is on the left edge of the ocean, looking to eat some fish
class Shark implements OceanWorldConstants {
    int y; // the horizontal coordinate
    int health; // the hunger level, when zero, the shark dies

    // the standard complete constructor
    Shark(int y, int health) {
        this.y = y;
        this.health = health;
    }

    // produce the position of this shark
    CartPt sharkPos() {
        return new CartPt(20, this.y);
    }

    // EFFECT:
    // move the shark up or down, on up-down key press
    void move(String ke) {
        if (ke.equals("up"))
            this.y = this.y - 3;
        else if (ke.equals("down"))
            this.y = this.y + 3;
    }

    // produce the image of this shark at its position
    WorldImage sharkImage() {
        return new EllipseImage(this.health, this.health / 2,
                OutlineMode.SOLID, Color.LIGHT_GRAY);
    }

    // EFFECT:
    // make a shark hungrier than before
    void starve() {
        this.health = this.health - 1;
    }

    // is this shark dead?
    boolean isDead() {
        return this.health <= 0;
    }

    // EFFECT:
    // make the shark fatter after he eats the fish of the given size
    void eat(int size) {
        this.health = this.health + size;
    }
}

// A Fish swims in the ocean - providing nourishment for the shark
class Fish implements OceanWorldConstants {
    CartPt p; // the location of this fish
    int size; // the size of this fish
    Color color; // the color of the fish

    // the standard complete constructor
    Fish(CartPt p, int size, Color color) {
        this.p = p;
        this.size = size;
        this.color = color;
    }

    // EFFECT:
    // move the fish to the right side of the ocean
    // and reset its size
    void start(int y, int size) {
        this.p.x = WIDTH;
        this.p.y = y;
        this.size = size;
    }

    // EFFECT:
    // move the fish to the right side of the ocean
    // at the same y coordinate and of the same size as this one
    void reStart() {
        this.start(this.p.y, this.size);
    }

    // EFFECT:
    // fish swims from right to left towards the waiting shark
    void swim() {
        this.p.moveLeftRandom(5);
    }

    // Is this fish close to the given shark?
    boolean closeByHuh(Shark that) {
        return this.p.distTo(that.sharkPos()) < 30;
    }

    WorldImage fishImage() {
        return new BesideImage(new EllipseImage(this.size, this.size / 2,
                OutlineMode.SOLID, this.color), new TriangleImage(
                new Posn(0, 0), new Posn(this.size / 4, -this.size / 4),
                new Posn(this.size / 4, this.size / 4), "solid", this.color));
    }

    // produce the image of this fish at its position
    WorldScene drawImageOnScene(WorldScene scn) {
        scn.placeImageXY(this.fishImage(), this.p.x, this.p.y);
        return scn;
    }
}

// A School of Fish is one of:
// - new EmptySchool()
// - new ConsSchool(Fish, School)

// A school of (an arbitrary number of) fish.
interface School extends OceanWorldConstants {

    // produce the image of all fish in this school
    public WorldScene drawFishesOnScene(WorldScene scn);

    // EFFECT:
    // let all fish in this school swim ahead on tick
    public void swim();

    // Is any fish in this school close to the given shark?
    public boolean closeByHuh(Shark that);

    // EFFECT:
    // let the shark eat the first closest fish (is there is such)
    public void feedShark(Shark mac);

    // EFFECT:
    // replace the fish that the shark has eaten
    public void removeEaten(Shark mac);
}

// A school with no fish
class EmptySchool implements School, OceanWorldConstants {
    EmptySchool() {
    }

    // produce an empty blue dot in the sea of blue
    public WorldScene drawFishesOnScene(WorldScene scn) {
        return scn;
    }

    // EFFECT:
    // let all fish in this school swim ahead on tick
    public void swim() {
    }

    // Is any fish in this school close to the given shark?
    public boolean closeByHuh(Shark that) {
        return false;
    }

    // EFFECT:
    // let the shark eat the first closes fish (is there is such)
    public void feedShark(Shark mac) {
    }

    // EFFECT:
    // replace the fish that the shark has eaten
    public void removeEaten(Shark mac) {
    }
}

// A school with at least one fish
class ConsSchool implements School, OceanWorldConstants {
    Fish first;
    School rest;

    // the standard complete constructor
    ConsSchool(Fish first, School rest) {
        this.first = first;
        this.rest = rest;
    }

    // draw this school of fish
    public WorldScene drawFishesOnScene(WorldScene scn) {
        return this.first.drawImageOnScene(this.rest.drawFishesOnScene(scn));
    }

    // EFFECT:
    // let all fish in this school swim ahead on tick
    public void swim() {
        this.first.swim();
        this.rest.swim();
    }

    // Is any fish in this school close to the given shark?
    public boolean closeByHuh(Shark that) {
        return this.first.closeByHuh(that) || this.rest.closeByHuh(that);
    }

    // EFFECT:
    // let the shark eat the first closes fish (is there is such)
    public void feedShark(Shark mac) {
        if (this.first.closeByHuh(mac))
            mac.eat(this.first.size);
        else
            this.rest.feedShark(mac);
    }

    // EFFECT:
    // replace the fish that the shark has eaten
    public void removeEaten(Shark mac) {
        if (this.first.closeByHuh(mac))
            this.first.reStart();
        else
            this.rest.removeEaten(mac);
    }
}

// An Ocean has a shark and a fish swimming in it
class Ocean extends World implements OceanWorldConstants {
    Shark shark; // the hungry shark
    School fish; // the fish the shark will eat (maybe)

    Ocean(Shark shark, School fish) {
        this.shark = shark;
        this.fish = fish;
    }

    // EFFECT:
    // the fish swim from right to left on each tick
    public void onTick() {
        if (this.fish.closeByHuh(this.shark)) {
            this.fish.feedShark(this.shark);
            this.fish.removeEaten(this.shark);
        } else {
            this.shark.starve();
            this.fish.swim();
        }
    }

    // EFFECT:
    // the shark moves up or down as directed by the arrow key
    public void onKeyEvent(String ke) {
        this.shark.move(ke);
    }

    // produce the image of the fish and shark swimming in the sea of blue
    public WorldScene makeScene() {
        WorldScene scn = this.getEmptyScene();
        scn.placeImageXY(new RectangleImage(WIDTH, HEIGHT, OutlineMode.SOLID,
                new Color(50, 150, 255)), WIDTH / 2, HEIGHT / 2);
        this.fish.drawFishesOnScene(scn);
        scn.placeImageXY(this.shark.sharkImage(), 0, this.shark.y);
        return scn;
    }

    // the world ends when the shark starves to death
    public boolean shouldWorldEnd() {
        return this.shark.isDead();
    }

    @Override
    public WorldScene lastScene(String s) {
        WorldScene scn = this.makeScene();
        scn.placeImageXY(new TextImage("The shark died", Color.RED), 100,
                50);
        return scn;
    }

}

// Examples for the ocean game
class ExamplesOceanWorldImp implements OceanWorldConstants {
    ExamplesOceanWorldImp() {
    }

    CartPt p1 = new CartPt(WIDTH, HEIGHT / 2);
    CartPt p2 = new CartPt(WIDTH, HEIGHT / 2 - 50);
    CartPt p3 = new CartPt(WIDTH, HEIGHT / 2 + 50);
    CartPt p4 = new CartPt(WIDTH, HEIGHT / 2 + 100);
    CartPt pEnd = new CartPt(3, HEIGHT / 2);
    CartPt pStart = new CartPt(WIDTH, HEIGHT / 2);

    Fish f = new Fish(p1, 50, Color.RED);

    Fish f1 = new Fish(p1, 50, Color.RED);
    Fish f2 = new Fish(p2, 50, Color.GREEN);
    Fish f3 = new Fish(p3, 50, Color.BLUE);
    Fish f4 = new Fish(p4, 50, Color.YELLOW);

    Fish fCloseBy = new Fish(new CartPt(3, HEIGHT / 2 - 3), 20, this.f.color);

    School noFish = new EmptySchool();
    School allFish = new ConsSchool(this.f1, new ConsSchool(this.f2,
            new ConsSchool(this.f3, new ConsSchool(this.f4, this.noFish))));

    School allFish2 = new ConsSchool(this.f1, new ConsSchool(this.f2,
            new ConsSchool(this.f3, new ConsSchool(this.f4, this.noFish))));

    School feedSchool = new ConsSchool(this.f1, new ConsSchool(this.fCloseBy,
            new ConsSchool(this.f4, this.noFish)));

    School eatenSchool = new ConsSchool(this.f1, new ConsSchool(new Fish(
            new CartPt(WIDTH, HEIGHT / 2 - 3), 20, this.f.color),
            new ConsSchool(this.f4, this.noFish)));

    Shark s = new Shark(HEIGHT / 2, 200);
    Shark s2 = new Shark(HEIGHT / 2, 200);

    Ocean ocean = new Ocean(this.s, this.allFish);
    Ocean ocean1 = new Ocean(this.s, this.allFish);
    Ocean ocean2 = new Ocean(this.s2, this.allFish2);
    Ocean oldOcean = new Ocean(new Shark(HEIGHT / 2, 200), this.allFish);

    // reset all fish to their original positions
    private void reset() {
        this.p1 = new CartPt(WIDTH, HEIGHT / 2);
        this.p2 = new CartPt(WIDTH, HEIGHT / 2 - 50);
        this.p3 = new CartPt(WIDTH, HEIGHT / 2 + 50);
        this.p4 = new CartPt(WIDTH, HEIGHT / 2 + 100);
        this.pEnd = new CartPt(3, HEIGHT / 2);
        this.pStart = new CartPt(WIDTH, HEIGHT / 2);

        this.f = new Fish(p1, 50, Color.RED);

        this.f1 = new Fish(p1, 50, Color.RED);
        this.f2 = new Fish(p2, 50, Color.GREEN);
        this.f3 = new Fish(p3, 50, Color.BLUE);
        this.f4 = new Fish(p4, 50, Color.YELLOW);

        this.fCloseBy = new Fish(new CartPt(3, HEIGHT / 2 - 3), 20,
                this.f.color);

        this.allFish = new ConsSchool(this.f1, new ConsSchool(this.f2,
                new ConsSchool(this.f3, new ConsSchool(this.f4, this.noFish))));
        this.allFish2 = new ConsSchool(this.f1, new ConsSchool(this.f2,
                new ConsSchool(this.f3, new ConsSchool(this.f4, this.noFish))));
    }

    void resetShark() {
        this.s = new Shark(HEIGHT / 2, 200);
    }

    // test all methods in the class Shark
    public void testSharkMethods(Tester t) {
        t.checkExpect(this.s.sharkPos(), new CartPt(20, HEIGHT / 2));

        this.s.move("up");
        t.checkExpect(this.s, new Shark(HEIGHT / 2 - 3, 200));

        resetShark();
        this.s.move("down");
        t.checkExpect(this.s, new Shark(HEIGHT / 2 + 3, 200));

        resetShark();
        this.s.move("left");
        t.checkExpect(this.s, new Shark(HEIGHT / 2, 200));

        resetShark();
        this.s.starve();
        t.checkExpect(this.s, new Shark(HEIGHT / 2, 199));

        resetShark();
        t.checkExpect(this.s.isDead(), false);
        t.checkExpect(new Shark(30, 0).isDead(), true);

        resetShark();
        this.s.eat(20);
        t.checkExpect(this.s, new Shark(HEIGHT / 2, 220));
    }

    // test all methods in the class Fish
    public void testFishMethods(Tester t) {
        this.f.start(30, 50);
        t.checkExpect(this.f, new Fish(new CartPt(WIDTH, 30), 50, this.f.color));

        reset();
        Fish fishy = new Fish(new CartPt(2, 200), 50, this.f.color);
        fishy.swim();
        t.checkExpect(fishy, new Fish(new CartPt(WIDTH, 200), 50, this.f.color));

        t.checkExpect(this.f.closeByHuh(this.s), false);
        t.checkExpect(
                new Fish(new CartPt(19, 195), 30, f.color).closeByHuh(this.s),
                true);
    }

    // test all methods in the class CartPt
    public void testCartPtMethods(Tester t) {
        t.checkExpect(this.p1.distTo(this.pEnd), 397.0);

        this.p1.moveLeft(5);
        t.checkExpect(this.p1, new CartPt(395, HEIGHT / 2));

        this.pEnd.moveLeft(5);
        t.checkExpect(this.pEnd, this.pStart);
    }

    // test that the expected value is one of several possible values
    public void testRandomMethods(Tester t) {
        CartPt pt;
        Fish fishy;
        for (int i = 0; i < 100; i++) {
            pt = new CartPt(10, 20);
            pt.moveLeftRandom(5);
            t.checkRange(pt.y, 18, 23);
        }
        for (int i = 0; i < 100; i++) {
            fishy = new Fish(new CartPt(10, 20), 25, f.color);
            fishy.swim();
            t.checkOneOf(fishy, new Fish(new CartPt(5, 18), 25, f.color),
                    new Fish(new CartPt(5, 19), 25, f.color), new Fish(
                            new CartPt(5, 20), 25, f.color), new Fish(
                            new CartPt(5, 21), 25, f.color), new Fish(
                            new CartPt(5, 22), 25, f.color));
        }
    }

    // test the methods for the classes that implement the School interface
    public void testSchoolMethods(Tester t) {
        t.checkExpect(this.noFish.closeByHuh(this.s), false);
        t.checkExpect(this.allFish.closeByHuh(this.s), false);
        t.checkExpect(this.feedSchool.closeByHuh(this.s), true);

        Shark oldShark = new Shark(HEIGHT / 2, 200);
        resetShark();
        this.noFish.feedShark(this.s);
        t.checkExpect(this.s, oldShark);

        this.allFish.feedShark(this.s);
        t.checkExpect(this.s, oldShark);

        this.feedSchool.feedShark(this.s);
        t.checkExpect(this.s, new Shark(HEIGHT / 2, 220));

        resetShark();
        School oldNoFish = this.noFish;
        School oldAllFish = this.allFish;

        this.noFish.removeEaten(this.s);
        t.checkExpect(this.noFish, oldNoFish);

        this.allFish.removeEaten(this.s);
        t.checkExpect(this.allFish, oldAllFish);

        this.feedSchool.removeEaten(this.s);
        t.checkExpect(this.feedSchool, this.eatenSchool);
    }

    // test Ocean method onKeyEvent
    public void testOceanOnKeyEvent(Tester t) {
        reset();
        resetShark();
        this.ocean = new Ocean(this.s, this.allFish);
        this.ocean.onKeyEvent("x");
        t.checkExpect(this.ocean, new Ocean(new Shark(HEIGHT / 2, 200),
                new ConsSchool(new Fish(new CartPt(WIDTH, HEIGHT / 2), 50,
                        Color.RED), new ConsSchool(new Fish(new CartPt(WIDTH,
                        HEIGHT / 2 - 50), 50, Color.GREEN), new ConsSchool(
                        new Fish(new CartPt(WIDTH, HEIGHT / 2 + 50), 50,
                                Color.BLUE), new ConsSchool(new Fish(
                                new CartPt(WIDTH, HEIGHT / 2 + 100), 50,
                                Color.YELLOW), this.noFish))))));

        this.ocean.onKeyEvent("up");
        t.checkExpect(this.ocean, new Ocean(new Shark(HEIGHT / 2 - 3, 200),
                this.allFish));

        resetShark();
        this.ocean = new Ocean(this.s, this.allFish);
        this.ocean.onKeyEvent("down");
        t.checkExpect(this.ocean, new Ocean(new Shark(HEIGHT / 2 + 3, 200),
                this.allFish));
    }

    // test Ocean method onTick
    public void dontTestOceanOnTick(Tester t) {
        resetShark();
        reset();
        this.ocean = new Ocean(this.s, this.allFish);
        this.ocean.onTick();
        // There is randomness in how the fish swim, so this will rarely work
        t.checkExpect(this.ocean, new Ocean(new Shark(HEIGHT / 2, 199),
                new ConsSchool(new Fish(new CartPt(WIDTH - 5, HEIGHT / 2), 50,
                        Color.RED), new ConsSchool(new Fish(new CartPt(
                        WIDTH - 5, HEIGHT / 2 - 50), 50, Color.GREEN),
                        new ConsSchool(new Fish(new CartPt(WIDTH - 5,
                                HEIGHT / 2 + 50), 50, Color.BLUE),
                                new ConsSchool(new Fish(new CartPt(WIDTH - 3,
                                        HEIGHT / 2 + 100), 50, Color.YELLOW),
                                        this.noFish))))));
    }

    // test Ocean method onTick, shouldWorldEnd
    public void testOceanWorldEnds(Tester t) {
        t.checkExpect(this.ocean.shouldWorldEnd(), false);
        WorldScene scn = new Ocean(new Shark(HEIGHT / 2, 0), this.allFish)
                .makeScene();
        scn.placeImageXY(new TextImage("The shark died", Color.RED), 100, 50);
        Ocean test2 = (new Ocean(new Shark(HEIGHT / 2, 0), this.allFish));
        t.checkExpect(test2.shouldWorldEnd(), true);
        t.checkExpect(test2.lastScene(""), scn);
    }

    public void testRun(Tester t) {
        // run the game
        this.ocean.bigBang(400, 400, 0.07);
    }

    public static void main(String[] argv) {
        ExamplesOceanWorldImp e = new ExamplesOceanWorldImp();
        Tester.runReport(e, false, false, new tester.DefaultReporter(), 80);
        // you cannot invoke bigBang on the same object until the first one
        // stops
        // (we started it in the last test -- see above)
        e.ocean.bigBang(400, 400, 0.07);

        // uses the same data as this.ocean, runs in a strange way
        // the fish mirror the images in the other world
        // but the shark is controlled separately
        // and it keeps the score for each individually
        e.ocean1.bigBang(400, 400, 0.07);

        // uses a new copy of the shark and the fish
        // an acceptable use of concurrency
        e.ocean2.bigBang(400, 400, 0.07);
    }
}
