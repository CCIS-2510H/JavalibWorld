package worldimagestests;

import javalib.worldimages.*;
import tester.*;

import java.awt.*;

/**
 * Copyright 2012 Viera K. Proulx
 * This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)
 */

/**
 * Tests for the non-drawing methods in the classes that implement the
 * <code>WorldImage</code> class.
 * 
 * @author Viera K. Proulx
 * @since May 22 2012
 * 
 */
public class ExamplesImageMethods implements IExamples {
    ExamplesImageMethods() {
    }

    // support for the regression tests
    public static ExamplesImageMethods examplesInstance = new ExamplesImageMethods();

    // ------------ CircleImage class
    // ------------------------------------------//

    WorldImage circle1 = new CircleImage(4, OutlineMode.SOLID, Color.RED);
    WorldImage circle2 = new CircleImage(3, OutlineMode.SOLID, Color.RED);
    WorldImage circle3 = new CircleImage(4, OutlineMode.OUTLINE, Color.RED);
    WorldImage circle4 = new CircleImage(4, OutlineMode.OUTLINE, Color.BLUE);
    WorldImage circle5 = new CircleImage(4, OutlineMode.SOLID, Color.RED);

    // Tests for the CircleImage class
    void testCircleImage(Tester t) {
        t.checkExpect(this.circle1.toString(),
                "new CircleImage(this.radius = 4,\nthis.fill = solid,"
                        + "\nthis.color = [r=255,g=0,b=0])");
        t.checkExpect(this.circle1.toIndentedString(" "),
                "\n   new CircleImage(this.radius = 4,"
                        + "\n   this.fill = solid,"
                        + "\n   this.color = [r=255,g=0,b=0])");
        t.checkExpect(this.circle1, this.circle5, "Should be the same");
        t.checkExpect(!this.circle1.equals(this.circle2), "fails in radius");
        t.checkExpect(!this.circle1.equals(this.circle3), "fails in fill state");
        t.checkExpect(!this.circle1.equals(this.circle4), "fails in color");
        t.checkExpect(this.circle1.hashCode(), this.circle5.hashCode());

        t.checkInexact(this.circle1.getWidth(), 8.0, 0.001);
        t.checkInexact(this.circle1.getHeight(), 8.0, 0.001);
    }

    // ------------ EllipseImage class
    // -----------------------------------------//

    WorldImage ellipse1 = new EllipseImage(4, 5, OutlineMode.SOLID, Color.GREEN);
    WorldImage ellipse2 = new EllipseImage(4, 5, OutlineMode.SOLID, Color.GREEN);
    WorldImage ellipse3 = new EllipseImage(4, 6, OutlineMode.SOLID, Color.GREEN);
    WorldImage ellipse4 = new EllipseImage(5, 4, OutlineMode.SOLID, Color.GREEN);
    WorldImage ellipse5 = new EllipseImage(4, 5, OutlineMode.OUTLINE,
            Color.GREEN);
    WorldImage ellipse6 = new EllipseImage(4, 5, OutlineMode.SOLID, Color.BLUE);

    // Tests for the EllipseImage class
    void testEllipseImage(Tester t) {
        t.checkExpect(this.ellipse1.toString(),
                "new EllipseImage(this.width = 4, this.height = 5,"
                        + "\nthis.fill = solid,"
                        + "\nthis.color = [r=0,g=255,b=0])");
        t.checkExpect(this.ellipse1.toIndentedString(" "),
                "\n   new EllipseImage(this.width = 4, this.height = 5,"
                        + "\n   this.fill = solid,"
                        + "\n   this.color = [r=0,g=255,b=0])");
        t.checkExpect(this.ellipse1, this.ellipse2);
        t.checkExpect(!this.ellipse1.equals(this.ellipse3), "fails in height");
        t.checkExpect(!this.ellipse1.equals(this.ellipse4), "fails in width");
        t.checkExpect(!this.ellipse1.equals(this.ellipse5), "fails in fill");
        t.checkExpect(!this.ellipse1.equals(this.ellipse6), "fails in color");
        t.checkExpect(this.ellipse1.hashCode(), this.ellipse2.hashCode());

        t.checkExpect(!this.circle1.equals(this.ellipse1), "fails - different classes");
        t.checkExpect(!this.ellipse1.equals(this.circle1), "fails - different classes");

        t.checkInexact(this.ellipse6.getWidth(), 4.0, 0.001);
        t.checkInexact(this.ellipse6.getHeight(), 5.0, 0.001);
    }

    // ------------ FrameImage class -----------------------------------------//

    WorldImage frame1 = new FrameImage(circle1);
    WorldImage frame2 = new FrameImage(circle1);
    WorldImage frame3 = new FrameImage(circle4);

    // Tests for the FrameImage class
    void testFrameImage(Tester t) {
        t.checkExpect(this.frame1.toString(), "new FrameImage(this.img = "
                + this.circle1.toString()
                + ",\nthis.color = [r=0,g=0,b=0])");
        t.checkExpect(
                this.frame1.toIndentedString(" "),
                "\n   new FrameImage(this.img = "
                        + this.circle1.toIndentedString("   ")
                        + ",\n   this.color = [r=0,g=0,b=0])");
        t.checkExpect(this.frame1, this.frame2);
        t.checkExpect(!this.frame1.equals(this.frame3), "fails in equals");
        t.checkExpect(this.frame1.hashCode(), this.frame2.hashCode());

        t.checkExpect(!this.rectangle1.equals(this.frame1), "fails - different classes");
        t.checkExpect(!this.frame1.equals(this.rectangle1), "fails - different classes");

        t.checkInexact(this.frame1.getWidth(), 8.0, 0.001);
        t.checkInexact(this.frame1.getHeight(), 8.0, 0.001);
    }

    // ------------ RectangleImage class
    // ---------------------------------------//

    WorldImage rectangle1 = new RectangleImage(4, 5, OutlineMode.SOLID,
            Color.GREEN);
    WorldImage rectangle2 = new RectangleImage(4, 5, OutlineMode.SOLID,
            Color.GREEN);
    WorldImage rectangle3 = new RectangleImage(3, 5, OutlineMode.SOLID,
            Color.GREEN);
    WorldImage rectangle4 = new RectangleImage(4, 6, OutlineMode.SOLID,
            Color.GREEN);
    WorldImage rectangle5 = new RectangleImage(4, 5, OutlineMode.OUTLINE,
            Color.GREEN);
    WorldImage rectangle6 = new RectangleImage(4, 5, OutlineMode.SOLID,
            Color.BLUE);

    // Tests for the RectangleImage class
    void testRectangleImage(Tester t) {
        t.checkExpect(this.rectangle1.toString(),
                "new RectangleImage(this.width = 4, this.height = 5,"
                        + "\nthis.fill = solid,"
                        + "\nthis.color = [r=0,g=255,b=0])");
        t.checkExpect(this.rectangle1.toIndentedString(" "),
                "\n   new RectangleImage(this.width = 4, this.height = 5,"
                        + "\n   this.fill = solid,"
                        + "\n   this.color = [r=0,g=255,b=0])");
        t.checkExpect(this.rectangle1, this.rectangle2);
        t.checkExpect(!this.rectangle1.equals(this.rectangle3), "fails in width");
        t.checkExpect(!this.rectangle1.equals(this.rectangle4), "fails in height");
        t.checkExpect(!this.rectangle1.equals(this.rectangle5), "fails in fill");
        t.checkExpect(!this.rectangle1.equals(this.rectangle6), "fails in color");
        t.checkExpect(this.rectangle1.hashCode(), this.rectangle2.hashCode());

        t.checkExpect(!this.frame1.equals(this.rectangle1), "fails - different classes");
        t.checkExpect(!this.rectangle1.equals(this.frame1), "fails - different classes");

        t.checkInexact(this.rectangle1.getWidth(), 4.0, 0.001);
        t.checkInexact(this.rectangle1.getHeight(), 5.0, 0.001);
    }

    // ------------ LineImage class
    // --------------------------------------------//

    WorldImage line1 = new LineImage(new Posn(4, 5), Color.GREEN);
    WorldImage line2 = new LineImage(new Posn(4, 5), Color.GREEN);
    WorldImage line3 = new LineImage(new Posn(5, 5), Color.GREEN);
    WorldImage line4 = new LineImage(new Posn(4, 6), Color.GREEN);
    WorldImage line5 = new LineImage(new Posn(4, 5), Color.BLUE);

    // Tests for the LineImage class
    void testLineImage(Tester t) {
        t.checkExpect(
                this.line1.toString(),
                "new LineImage(this.endPoint = (4, 5),"
                        + "\nthis.color = [r=0,g=255,b=0])");
        t.checkExpect(
                this.line1.toIndentedString(" "),
                "\n   new LineImage(this.endPoint = (4, 5),"
                        + "\n   this.color = [r=0,g=255,b=0])");
        t.checkExpect(this.line1, this.line2);
        t.checkExpect(!this.line1.equals(this.line3), "fails in endPoint.x");
        t.checkExpect(!this.line1.equals(this.line4), "fails in endPoint.y");
        t.checkExpect(!this.line1.equals(this.line5), "fails in color");
        t.checkExpect(this.line1.hashCode(), this.line2.hashCode());

        t.checkExpect(!this.frame1.equals(this.line1), "fails - different classes");
        t.checkExpect(!this.line1.equals(this.frame1), "fails - different classes");

        t.checkInexact(this.line1.getWidth(), 4.0, 0.001);
        t.checkInexact(this.line1.getHeight(), 5.0, 0.001);
    }

    // ------------ TriangleImage class
    // ----------------------------------------//

    WorldImage triangle1 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle2 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle3 = new TriangleImage(new Posn(1, 3), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle4 = new TriangleImage(new Posn(2, 4), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle5 = new TriangleImage(new Posn(2, 3), new Posn(5, 5),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle6 = new TriangleImage(new Posn(2, 3), new Posn(4, 6),
            new Posn(6, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle7 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(7, 7), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle8 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(6, 8), OutlineMode.SOLID, Color.BLACK);
    WorldImage triangle9 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.OUTLINE, Color.BLACK);
    WorldImage triangle10 = new TriangleImage(new Posn(2, 3), new Posn(4, 5),
            new Posn(6, 7), OutlineMode.OUTLINE, Color.BLUE);

    // Tests for the TriangleImage class
    void testTriangleImage(Tester t) {
        t.checkExpect(this.triangle1.toString(),
                "new TriangleImage(this.p1 = (2, 3),\nthis.p2 = (4, 5),\nthis.p3 = (6, 7),"
                        + "\nthis.fill = solid,"
                        + "\nthis.color = [r=0,g=0,b=0])");
        t.checkExpect(this.triangle1.toIndentedString(" "),
                "\n   new TriangleImage(this.p1 = (2, 3),\n   this.p2 = (4, 5),"
                        + "\n   this.p3 = (6, 7),"
                        + "\n   this.fill = solid,"
                        + "\n   this.color = [r=0,g=0,b=0])");
        t.checkExpect(this.triangle1, this.triangle2);
        t.checkExpect(!this.triangle1.equals(this.triangle3), "fails in p1.x");
        t.checkExpect(!this.triangle1.equals(this.triangle4), "fails in p1.y");
        t.checkExpect(!this.triangle1.equals(this.triangle5), "fails in p2.x");
        t.checkExpect(!this.triangle1.equals(this.triangle6), "fails in p2.y");
        t.checkExpect(!this.triangle1.equals(this.triangle7), "fails in p3.x");
        t.checkExpect(!this.triangle1.equals(this.triangle8), "fails in p3.y");
        t.checkExpect(!this.triangle1.equals(this.triangle9), "fails in color");
        t.checkExpect(this.triangle1.hashCode(), this.triangle2.hashCode());

        t.checkExpect(!this.line1.equals(this.triangle1), "fails - different classes");
        t.checkExpect(!this.triangle1.equals(this.line1), "fails - different classes");

        t.checkInexact(this.triangle8.getWidth(), 4.0, 0.001);
        t.checkInexact(this.triangle8.getHeight(), 5.0, 0.001);
    }

    // ------------ TextImage class
    // --------------------------------------------//

    WorldImage text1 = new TextImage("hello", 6, FontStyle.ITALIC, Color.BLACK);
    WorldImage text2 = new TextImage("hello", 6, FontStyle.ITALIC, Color.BLACK);
    WorldImage text3 = new TextImage("hell", 6, FontStyle.ITALIC, Color.BLACK);
    WorldImage text4 = new TextImage("hello", 7, FontStyle.ITALIC, Color.BLACK);
    WorldImage text5 = new TextImage("hello", 6, FontStyle.BOLD, Color.BLACK);
    WorldImage text6 = new TextImage("hello", 6.0f, FontStyle.ITALIC, Color.BLACK);
    WorldImage text7 = new TextImage("hello", 6, FontStyle.ITALIC, Color.BLUE);

    // Tests for the TextImage class
    void testTextImage(Tester t) {
        char c = '"';
        t.checkExpect(this.text1.toString(), "new TextImage(this.text = " + c
                + "hello" + c + "," + "\nthis.size = 6.0, this.style = 2,"
                + "\nthis.color = [r=0,g=0,b=0])");
        t.checkExpect(this.text1.toIndentedString(" "),
                "\n   new TextImage(this.text = " + c + "hello" + c + ","
                        + "\n   this.size = 6.0, this.style = 2,"
                        + "\n   this.color = [r=0,g=0,b=0])");
        t.checkExpect(this.text1, this.text2);
        t.checkExpect(this.text1, this.text6);
        t.checkExpect(!this.text1.equals(this.text3), "fails in text");
        t.checkExpect(!this.text1.equals(this.text4), "fails in size");
        t.checkExpect(!this.text1.equals(this.text5), "fails in style");
        t.checkExpect(!this.text1.equals(this.text7), "fails in color");
        t.checkExpect(this.text1.hashCode(), this.text2.hashCode());
        t.checkExpect(this.text1.hashCode(), this.text6.hashCode());

        t.checkExpect(!this.line1.equals(this.text1), "fails - different classes");
        t.checkExpect(!this.text1.equals(this.line1), "fails - different classes");

        t.checkExpect(this.text1.getWidth(), 14);
        t.checkExpect(this.text1.getHeight(), 4);
    }

    // ------------ OverlayImage class
    // ----------------------------------------//

    OverlayImage overlay1 = new OverlayImage(this.circle1, this.rectangle1);
    OverlayImage overlay2 = new OverlayImage(this.circle1, this.rectangle2);
    OverlayImage overlay3 = new OverlayImage(this.circle1, this.overlay1);
    OverlayImage overlay4 = new OverlayImage(this.circle1, this.circle1);

    // Tests for the OverlayImage class
    void testOverlayImage(Tester t) {
        t.checkExpect(this.overlay1.toString(), "new OverlayImage(this.top = "
                + this.overlay1.top.toString() + "\nthis.bot = "
                + this.overlay1.bot.toString() + ")");
        t.checkExpect(this.overlay1.toIndentedString(" "),
                "\n   new OverlayImage(this.top = " + this.overlay1.top.toIndentedString("   ")
                        + ",\n   this.bot = " + this.overlay1.bot.toIndentedString("   ")
                        + ")");

        t.checkExpect(this.overlay1, this.overlay2);
        t.checkExpect(!this.overlay1.equals(this.overlay3), "fails in bot");
        t.checkExpect(!this.overlay1.equals(this.overlay4), "fails in top");
        t.checkExpect(this.overlay1.hashCode(), this.overlay2.hashCode());

        t.checkExpect(!this.line1.equals(this.overlay1), "fails - different classes");
        t.checkExpect(!this.overlay1.equals(this.rectangle1),
                "fails - different classes");

        t.checkInexact(this.overlay1.getWidth(), 8.0, 0.001);
        t.checkInexact(this.overlay1.getHeight(), 8.0, 0.001);
    }

    // ------------ OverlayImage class
    // ----------------------------------------//

    WorldImage overlayXY1 = new OverlayOffsetImage(this.circle1, 5, 3,
            this.rectangle1);
    WorldImage overlayXY2 = new OverlayOffsetImage(this.circle1, 5, 3,
            this.rectangle2);
    WorldImage overlayXY3 = new OverlayOffsetImage(this.circle1, 4, 3,
            this.rectangle1);
    WorldImage overlayXY4 = new OverlayOffsetImage(this.circle1, 5, 4,
            this.rectangle1);
    WorldImage overlayXY5 = new OverlayOffsetImage(this.circle1, 5, 3,
            this.ellipse1);
    WorldImage overlayXY6 = new OverlayOffsetImage(this.overlay1, 5, 3,
            this.rectangle1);

    // Tests for the OverlayOffsetImage class
    void testOverlayOffsetImage(Tester t) {
        t.checkExpect(
                this.overlayXY1.toString(),
                "new OverlayOffsetImage(this.top = " + this.circle1.toString() + ","
                        + "\nthis.dx = 5.0, this.dy = 3.0,"
                        + "\nthis.bot = " + this.rectangle1.toString() + ")");
        t.checkExpect(
                this.overlayXY1.toIndentedString(" "),
                "\n   new OverlayOffsetImage(this.top = " + this.circle1.toIndentedString("   ") + ","
                        + "\n   this.dx = 5.0, this.dy = 3.0,"
                        + "\n   this.bot = " + this.rectangle1.toIndentedString("   ") + ")");

        t.checkExpect(this.overlayXY1, this.overlayXY2);
        t.checkExpect(!this.overlayXY1.equals(this.overlayXY3), "fails in dx");
        t.checkExpect(!this.overlayXY1.equals(this.overlayXY4), "fails in dy");
        t.checkExpect(!this.overlayXY1.equals(this.overlayXY5), "fails in bot");
        t.checkExpect(!this.overlayXY1.equals(this.overlayXY6), "fails in top");
        t.checkExpect(this.overlayXY1.hashCode(), this.overlayXY2.hashCode());

        t.checkExpect(!this.line1.equals(this.overlayXY1), "fails - different classes");
        t.checkExpect(!this.overlayXY6.equals(this.ellipse1),
                "fails - different classes");

        t.checkInexact(this.overlayXY1.getWidth(), 11.0, 0.001);
        t.checkInexact(this.overlayXY1.getHeight(), 9.5, 0.001);
    }

    // ------------ FromFileImage class
    // ----------------------------------------//

    WorldImage fromFile1 = new FromFileImage("rubberduck.jpg");
    WorldImage fromFile2 = new FromFileImage("rubberduck.jpg");
    WorldImage fromFile3 = new FromFileImage("rubberduck2.jpg");

    // Tests for the FromFileImage class
    void testFromFileImage(Tester t) {
        t.checkExpect(this.fromFile1.toString(),
                "new FromFileImage(this.fileName = \"rubberduck.jpg\")");
        t.checkExpect(this.fromFile1.toIndentedString(" "),
                "\n   new FromFileImage(this.fileName = \"rubberduck.jpg\")");

        t.checkExpect(this.fromFile1, this.fromFile2);
        t.checkExpect(!this.fromFile1.equals(this.fromFile3));
        
        t.checkExpect(!this.fromFile1.equals(this.text1), "fails - different classes");
        t.checkExpect(!this.line2.equals(this.fromFile1), "fails - different classes");

        t.checkInexact(this.fromFile1.getWidth(), 134.0, 0.001);
        t.checkInexact(this.fromFile1.getHeight(), 134.0, 0.001);
    }

    // Run all tests - comment out those you want to skip
    public void tests(Tester t) {
        testCircleImage(t);
        testCircleImage(t);
        testEllipseImage(t);
        testFrameImage(t);
        testEllipseImage(t);
        testRectangleImage(t);
        testLineImage(t);
        testTriangleImage(t);
        testTextImage(t);
        testOverlayImage(t);
        testOverlayOffsetImage(t);
        testFromFileImage(t);
    }

    public static void main(String[] argv) {
        ExamplesImageMethods e = new ExamplesImageMethods();
        Tester.runReport(e, false, true);
    }
}