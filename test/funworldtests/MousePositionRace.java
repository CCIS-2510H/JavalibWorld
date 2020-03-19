package funworldtests;

import java.awt.Color;

import javalib.funworld.*;
import javalib.worldimages.CircleImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.TextImage;

/** By John Compitello and Adam Tobey */
public class MousePositionRace {


  public static void main(String[] args) {
    MyWorld w = new MyWorld(0);
    w.bigBang(400, 400, 1);
  }

}

class MyWorld extends World {

  int tick;
  final int MAX_TICK = 10;

  MyWorld(int tick) {
    this.tick = tick;
  }

  public World onTick() {
    if (this.tick < MAX_TICK) {
      return new MyWorld(this.tick + 1);
    } else {
      return this.endOfWorld("onTick " + this.tick);
    }
  }

  public WorldScene makeScene() {
    return this.getEmptyScene().placeImageXY(new CircleImage(20, OutlineMode.SOLID,
            Color.black), 30, 30).placeImageXY(new TextImage("" + this.tick, 24, Color.black), 200, 20);
  }

  public WorldScene lastScene(String m) {
    System.out.println("Last scene: " + m);
    return null;//this.getEmptyScene().placeImageXY(new CircleImage(20, OutlineMode.SOLID, Color.red), 30, 30);
  }

}