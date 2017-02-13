import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;

import tester.Tester;
import funworldtests.*;
import impworldtests.*;
import worldimagestests.ExamplesImageDrawings;
import worldimagestests.ExamplesImageMethods;

class MyWorld extends javalib.funworld.World {
    @Override
    public javalib.funworld.World onTick() {
        return this.endOfWorld("hello");
    }

    @Override
    public javalib.funworld.WorldScene makeScene() {
        return this.getEmptyScene();
    }

    @Override
    public javalib.funworld.WorldScene lastScene(String s) {
        return this.getEmptyScene().placeImageXY(new javalib.worldimages.TextImage(s, Color.RED), 250, 250);
    }
}

class MyWorld2 extends javalib.impworld.World {
    @Override
    public void onTick() {
        this.endOfWorld("hello");
    }

    @Override
    public javalib.impworld.WorldScene makeScene() {
        return this.getEmptyScene();
    }

    @Override
    public javalib.impworld.WorldScene lastScene(String s) {
        javalib.impworld.WorldScene scene = this.getEmptyScene();
        scene.placeImageXY(new javalib.worldimages.TextImage(s, Color.RED), 250, 250);
        return scene;
    }
}

public class WorldTests {

    static int n;

    static Object blobExamples = BlobWorldFun.examplesInstance;
    static Object tickyTackExamples = TickyTackFun.examplesInstance;

    static Object[] examples = new Object[] {

    BlobWorldFun.examplesInstance, TickyTackFun.examplesInstance,
            BlobWorldImp.examplesInstance, TickyTackImp.examplesInstance,
            ExamplesImageDrawings.examplesInstance,
            ExamplesImageMethods.examplesInstance };

    /**
     * Run the tests in the given <code>Examples</code> class.
     * 
     * @param obj
     *            an instance of the <code>Examples</code> class
     */
    public static void runTests(Object obj) {
        String className = obj.getClass().getName();
        try {
            System.out
                    .println("\n**********************"
                            + "\n* Tests for the class: " + className
                            + "\n**********************"
                            + "\nPress return to start:\n");
            n = System.in.read();

            Tester.runReport(obj, false, false, new tester.DefaultReporter(), 80);
        } catch (IOException e) {
            System.out.println("IO error when running tests for " + className
                    + "\n " + e.getMessage());
        }
    }

    /** main: an alternative way of starting the world and running the tests */
    public static void main(String[] argv) {
/*
        for (int i = 0; i < Array.getLength(examples); i++) {
            runTests(examples[i]);
        }
        */
runTests(new WorldTests());
        // runTests(blobExamples);
        // runTests(tickyTackExamples);
    }

    boolean testWorldEnd(Tester t) {
        new MyWorld().bigBang(500, 500, 0.5);
        return true;
    }
}