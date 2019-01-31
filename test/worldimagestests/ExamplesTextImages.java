package worldimagestests;

import javalib.worldimages.*;
import javalib.funworld.WorldScene;
import javalib.worldcanvas.*;
import java.awt.Color;
import tester.Tester;

class ExamplesTextImages {
  void testImageBug(Tester t) {
    WorldImage simTxtImg = new TextImage("ABCDE", 13.0, Color.BLACK);
    t.checkExpect(simTxtImg.getWidth(), 42.0);
    TextImage t1 = new TextImage("ABCDE", 13.0, FontStyle.BOLD, Color.BLACK);
    t.checkInexact(t1.getWidth(), 45.0, 0.00001);
    TextImage t2 = new TextImage("ABCDE", 13.0, FontStyle.BOLD_ITALIC, Color.BLACK);
    t.checkInexact(t2.getWidth(), 44.0, 0.00001);

    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    c.drawScene(s.placeImageXY(
            new ScaleImage(
                    new AboveAlignImage(AlignModeX.LEFT,
                      new FrameImage(simTxtImg, Color.RED),
                      new AboveAlignImage(AlignModeX.LEFT,
                              new FrameImage(t1, Color.GREEN),
                              new FrameImage(t2, Color.BLUE))),
                    5.0), 250, 250));
    c.show();

  }

  public static void main(String[] argv) {
    ExamplesTextImages e = new ExamplesTextImages();
    Tester.runReport(e, false, true, new tester.DefaultReporter(), 80);
  }
}