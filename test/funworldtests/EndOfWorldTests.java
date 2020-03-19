package funworldtests;

import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.AboveAlignImage;
import javalib.worldimages.AlignModeX;
import javalib.worldimages.TextImage;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

class WorldWithEnding extends World {
  int value;
  int parent;
  int serial;
  static int SERIAL = 0;
  WorldWithEnding(int value, int parent) {
    this.value = value;
    this.parent = parent;
    this.serial = SERIAL++;
  }

  @Override
  public World onTick() {
    if (this.value == 20) return this.endOfWorld("20!");
    return new WorldWithEnding(this.value + 1, this.value);
  }

  @Override
  public World onKeyEvent(String s) {
    return new WorldWithEnding(-this.value, this.value).endOfWorld("Done");
  }

  @Override
  public WorldScene lastScene(String s) {
    return this.getEmptyScene()
            .placeImageXY(new TextImage(s, Color.BLACK), 50, 50)
            .placeImageXY(new AboveAlignImage(AlignModeX.LEFT,
                    new TextImage(String.format("This: %d", this.value), Color.RED),
                    new TextImage(String.format("Parent: %d", this.parent), Color.BLUE)), 50, 75);
  }

  @Override
  public WorldScene makeScene() {
    System.out.println(String.format("%d: In %d from %d, done? %s", this.serial, this.value, this.parent,
            this.hasWorldEnded() ? "yes" : "no"));
    StackTraceElement[] st = new RuntimeException().getStackTrace();
    for (int i = 0; i < st.length && i < 3; i++)
      System.out.println(st[i].toString());
    return this.getEmptyScene()
            .placeImageXY(new AboveAlignImage(AlignModeX.LEFT,
                    new TextImage(String.format("This: %d", this.value), Color.RED),
                    new TextImage(String.format("Parent: %d", this.parent), Color.BLUE)), 50, 30);
  }
}

class EndOfWorldTests {
  public static void main(String[] args) {
    new WorldWithEnding(0, 0).bigBang(100, 100, 0.5);
  }
}
