package javalib.impworld;

import javalib.worldimages.*;
import javalib.worldcanvas.*;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

/**
 * Copyright 2007, 2008 2009, 2012 Viera K. Proulx
 * This program is distributed under the terms of the
 * GNU Lesser General Public License (LGPL)
 */

/**
 * World for programming interactive games - with graphics, key events, mouse
 * events and a timer.
 *
 * @author Viera K. Proulx
 * @since November 15 2007, March 17 2008, October 19 2009, February 4 2012
 */
abstract public class World {

  /**
   * The canvas that displays the current world.
   *
   * (Note: not public, so that students can't directly manipulate it)
   */
  WorldCanvas theCanvas;

  /** true if 'bigBang' started the world and it did not end, did not stop */
  private transient boolean worldExists = false;

  /** the timer for this world */
  transient MyTimer mytime;

  /** timer events not processed when the mouse event is processed */
  transient boolean stopTimer = false;

  /** the key adapter for this world */
  private transient MyKeyAdapter ka;

  /** the mouse adapter for this world */
  private transient MyMouseAdapter ma;

  /** the window closing listener for this world */
  private transient WindowListener windowClosing;

  /** a blank image, to avoid <code>null</code> in the <code>lastWorld</code> */
  private transient WorldScene blankScene = new WorldScene(0, 0);

  /** the last world - if needed */
  public WorldEnd lastWorld = new WorldEnd(false, this.blankScene);

  private int width, height;

  /**
   * The default constructor. To start the world one must invoke the
   * <code>bigBang</code> method.
   */
  public World() {
  }

  // ///////////////////////////////////////////////////////////////////////
  // Methods for interacting with the World //
  // ///////////////////////////////////////////////////////////////////////

  /**
   * EFFECT:
   * <p>
   * Start the world by creating a canvas of the given size, creating and
   * adding the key and mouse adapters, and starting the timer at the given
   * speed.
   * </p>
   *
   * @param w
   *            the width of the <code>{@link WorldCanvas Canvas}</code>
   * @param h
   *            the height of the <code>{@link WorldCanvas Canvas}</code>
   * @param speed
   *            the speed at which the clock runs
   */
  public void bigBang(int w, int h, double speed) {
    if (this.worldExists) {
      System.out.println("Only one world can run at a time");
      return;
    }
    // throw runtime exceptions if w, h <= 0
    this.width = w;
    this.height = h;
    this.theCanvas = new WorldCanvas(w, h);
    this.blankScene = new WorldScene(w, h);
    this.lastWorld = new WorldEnd(false, this.blankScene);

    // if the user closes the Canvas window
    // it will only hide and can be reopened by invoking 'show'
    this.theCanvas.f
            .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    this.windowClosing = new MyWindowClosingListener(this);
    this.theCanvas.f.addWindowListener(this.windowClosing);

    // pause a bit so that two canvases do not compete when being opened
    // almost at the same time
    long start = System.currentTimeMillis();
    long tmp = System.currentTimeMillis();

    while (tmp - start < 1000) {
      tmp = System.currentTimeMillis();
    }

    // add the key listener to the frame for our canvas
    this.ka = new MyKeyAdapter(this);
    this.theCanvas.f.addKeyListener(this.ka);
    this.theCanvas.f.setFocusTraversalKeysEnabled(false);


    // add the mouse listener to the frame for our canvas
    this.ma = new MyMouseAdapter(this);
    this.theCanvas.f.addMouseListener(this.ma);
    this.theCanvas.f.addMouseMotionListener(this.ma);

    // make sure the canvas responds to events
    this.theCanvas.f.setFocusable(true);

    // finally, show the canvas and draw the initial world
    this.theCanvas.show();

    // draw the initial world
    this.worldExists = true;
    this.mytime = new MyTimer(this, speed);
    this.drawWorld("");

    // pause again after the Canvas is shown to make sure
    // all listeners and the timer are installed for theCanvas
    start = System.currentTimeMillis();
    tmp = System.currentTimeMillis();
    // System.out.println("Going to sleep again.");

    while (tmp - start < 1000) {
      tmp = System.currentTimeMillis();
    }

    // and add the timer -- start it if speed is greater than 0
    if (speed > 0.0)
      this.mytime.timer.start();

    this.drawWorld("");

    // print a header that specifies the current version of the World
    System.out.println(Versions.CURRENT_VERSION);
  }

  public WorldScene getEmptyScene() {
    return new WorldScene(this.width, this.height);
  }

  /**
   * EFFECT:
   * <p>
   * Start the world by creating a canvas of the given size, creating and
   * adding the key and mouse adapters, without running the the timer.
   * </p>
   *
   * @param w
   *            the width of the <code>{@link WorldCanvas Canvas}</code>
   * @param h
   *            the height of the <code>{@link WorldCanvas Canvas}</code>
   */
  public void bigBang(int w, int h) {
    this.bigBang(w, h, 0.0);
  }

  /**
   * EFFECT:
   * <p>
   * top the world, close all listeners and the timer, draw the last
   * </p>
   * <code>Scene</code>.
   */
  void stopWorld() {
    if (worldExists) {
      // remove listeners and set worldExists to false
      this.mytime.timer.stop();
      this.worldExists = false;
      this.mytime.stopTimer();
      this.theCanvas.f.removeKeyListener(this.ka);
      this.theCanvas.f.removeMouseListener(this.ma);
      System.out.println("The world stopped.");

      // draw the final scene of the world with the end of time message
      this.theCanvas.clear();
      this.theCanvas.drawScene(this.lastWorld.lastScene);
    }
  }

  /**
   * <p>
   * This method is invoked at each tick. It checks if the world should end
   * now.
   * </p>
   * <p>
   * The saved image will be shown when the world ends, otherwise it is
   * ignored.
   * </p>
   *
   * @return pair (true, last image) or (false, any image)
   */
  public WorldEnd worldEnds() {
    return new WorldEnd(false, this.makeScene());
  }

  /**
   * End the world interactions - leave the canvas open, show the image of the
   * last world with the given message
   *
   * @param s
   *            the message to display
   */
  public void endOfWorld(String s) {
    // set up the last world pair and finish as usual
    this.lastWorld = new WorldEnd(true, this.lastScene(s));
    this.stopWorld();
  }

  /**
   * The <code>onTick</code> method is invoked only if the world exists. To
   * test the method <code>onTick</code> we provide this method that will
   * invoke the <code>onTick</code> method for the testing purposes.
   */
  public void testOnTick() {
    // the world does not exist, but run the test for the end of the world
    // and handle the last sound effect as needed
    this.lastWorld = this.worldEnds();
    if (this.lastWorld.worldEnds) {
      this.stopWorld();
    }

    // now invoke the onTick method -
    // it will ignore the code similar to that above
    // but will handle the tickTunes manipulation
    this.processTick();
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the timer on each tick. Delegates to the user to
   * define a new state of the world, then resets the canvas and event
   * handlers for the new world to those currently used.
   * </p>
   */
  synchronized void processTick() {
    try {
      if (this.worldExists && !this.stopTimer) {
        this.lastWorld = this.worldEnds();
        if (this.lastWorld.worldEnds) {
          this.stopWorld();
        } else {
          this.onTick();
          if (this.lastWorld.worldEnds) {
            this.stopWorld();
          } else {
            this.drawWorld("");
          }
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the timer on each tick. Produces a
   * new <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   */
  public void onTick() {
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the key adapter on selected key events. Delegates
   * to the user to define a new state of the world, then resets the canvas
   * and event handlers for the new world to those currently used.
   * </p>
   */
  synchronized void processKeyReleased(String key) {
    try {
      if (this.worldExists) {
        this.onKeyReleased(key);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the key adapter on key-released
   * events. Updates this <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   */
  public void onKeyReleased(String key) {
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the key adapter on selected key events. Delegates
   * to the user to define a new state of the world, then resets the canvas
   * and event handlers for the new world to those currently used.
   * </p>
   */
  synchronized void processKeyEvent(String ke) {
    try {
      if (this.worldExists) {
        this.onKeyEvent(ke);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the key adapter on selected key
   * events. Updates this <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   */
  public void onKeyEvent(String s) {
  }

  private String buttonNameFor(int button) {
    switch (button) {
      case MouseEvent.BUTTON1:
        return "LeftButton";
      case MouseEvent.BUTTON2:
        return "MiddleButton";
      case MouseEvent.BUTTON3:
        return "RightButton";
      default:
        return "UnknownButton";
    }
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse clicked event. Delegates
   * to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when clicked
   */
  void processMouseClicked(Posn mouse, String button) {
    try {
      if (this.worldExists) {
        this.onMouseClicked(mouse, button);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * clicked. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when clicked
   */
  public void onMouseClicked(Posn mouse) {
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * clicked. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when clicked
   * @param buttonName which button was clicked
   */
  public void onMouseClicked(Posn mouse, String buttonName) {
    this.onMouseClicked(mouse);
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse entered event. Delegates
   * to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when entered
   */
  void processMouseEntered(Posn mouse) {

    try {
      if (this.worldExists) {
        this.onMouseEntered(mouse);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * entered. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when entered
   */
  public void onMouseEntered(Posn mouse) {
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse exited event. Delegates
   * to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when exited
   */
  void processMouseExited(Posn mouse) {

    try {
      if (this.worldExists) {
        this.onMouseExited(mouse);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * exited. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when exited
   */
  public void onMouseExited(Posn mouse) {
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse pressed event. Delegates
   * to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when pressed
   */
  void processMousePressed(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        this.onMousePressed(mouse, button);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * pressed. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when pressed
   */
  public void onMousePressed(Posn mouse) {
  }

  public void onMousePressed(Posn mouse, String buttonName) {
    this.onMousePressed(mouse);
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse released event.
   * Delegates to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when released
   */
  void processMouseReleased(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        this.onMouseReleased(mouse, button);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * released. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when released
   */
  public void onMouseReleased(Posn mouse) {
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * released. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when released
   * @param buttonName which button was pressed
   */

  public void onMouseReleased(Posn mouse, String buttonName) {
    this.onMouseReleased(mouse);
  }

  /**
   * EFFECT:
   * <p>
   * The method invoked by the mouse adapter on mouse moved event.
   * Delegates to the user to define a new state of the world.
   * </p>
   *
   * @param mouse
   *            the location of the mouse when moved
   */
  void processMouseMoved(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        this.onMouseMoved(mouse, button);
        if (!this.lastWorld.worldEnds)
          this.drawWorld("");
        else {
          // draw the last world
          this.theCanvas.drawScene(lastWorld.lastScene);
        }
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld("");
      // throw re;
      Runtime.getRuntime().halt(1);
    }
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * moved. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when moved
   */
  public void onMouseMoved(Posn mouse) {
  }

  /**
   * EFFECT:
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * moved. Update the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse
   *            the location of the mouse when moved
   * @param buttonName which button was pressed
   */

  public void onMouseMoved(Posn mouse, String buttonName) {
    this.onMouseMoved(mouse);
  }


  /**
   * EFFECT:
   * <p>
   * Invoke the user defined <code>makeImage</code> method, if this
   * <code>{@link World World}</code> has been initialized via
   * <code>bigBang</code> and did not stop or end, otherwise invoke the user
   * defined <code>lastImage</code> method
   * </p>
   */
  synchronized void drawWorld(String s) {
    if (this.worldExists) {
      this.theCanvas.clear();
      this.theCanvas.drawScene(this.makeScene());
    } else {
      this.theCanvas.clear();
      this.theCanvas.drawScene(this.lastScene(s));
    }
  }

  /**
   * <P>
   * User defined method to draw the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @return the image that represents this world at this moment
   */
  abstract public WorldScene makeScene();

  /**
   * <P>
   * User defined method to draw the <code>{@link World World}</code>.
   * </P>
   * <P>
   * Override this method in the game world class
   * </P>
   *
   * @return the image that represents the last world to be drawn
   */
  public WorldScene lastScene(String s) {
    return this.makeScene();
  }
}

/**
 * The window closing listener - it stops the timer when the frame is closed and
 * stops playing all tunes - in the tickTunes bucket and in the keyTunes bucket.
 *
 * @author Viera K. Proulx
 * @since 5 August 2010
 *
 */
class MyWindowClosingListener extends WindowAdapter {
  World w;

  MyWindowClosingListener(World w) {
    this.w = w;
  }

  public void windowClosing(WindowEvent we) {
    if (this.w != null && this.w.mytime != null)
      this.w.mytime.stopTimer();
  }
}

/**
 * The action listener for the timer events.
 *
 * @author Viera K. Proulx
 * @since August 2, 2007
 */
final class MyTimer {

  /**
   * the current <code>{@link World World}</code> that handles the timer
   * events
   */
  World currentWorld;

  /** the <code>Timer</code> that generates the time events */
  Timer timer;

  public boolean running = true;

  /** the timer speed */
  int speed;

  /**
   * Create the initial timer for the given <code>{@link World World}</code>
   * at the given <code>speed</code>.
   *
   * @param currentWorld
   *            the given <code>{@link World World}</code>
   * @param speed
   *            the given <code>speed</code>
   */
  MyTimer(World currentWorld, double speed) {
    this.currentWorld = currentWorld;
    this.timer = new Timer((new Double(speed * 1000)).intValue(),
            this.timerTasks);
    this.speed = (new Double(speed * 1000)).intValue();
  }

  /**
   * The callback for the timer events
   */
  ActionListener timerTasks = new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
      if (running)
        currentWorld.processTick();
    }
  };

  /**
   * A helper method to convert the <code>speed</code> given as a delay time
   * into milliseconds
   */
  void setSpeed() {
    this.timer.setDelay(this.speed);
  }

  void stopTimer() {
    this.running = false;
  }
}

/**
 * <p>
 * The implementation of callbacks for the key events.
 * </p>
 * <p>
 * Report all regular key presses and the four arrow keys
 * </p>
 * <p>
 * Ignore other key pressed events, key released events, special keys, etc.
 * </p>
 *
 * @author Viera K. Proulx
 * @since August 2, 2007
 */
final class MyKeyAdapter extends javalib.utils.AbstractKeyAdapter {
  /** the <code>KeyAdapter</code> that handles the key events */
  MyKeyAdapter(World currentWorld) {
    super(new OnKey(currentWorld), new OnReleased(currentWorld));
  }

  void resetWorld(final World currentWorld) {
    this.onKey = new OnKey(currentWorld);
    this.onReleased = new OnReleased(currentWorld);
  }

  static class OnKey implements javalib.utils.AbstractKeyAdapter.Consumer<String> {
    final World currentWorld;

    OnKey(World w) {
      this.currentWorld = w;
    }

    public void apply(String data) {
      currentWorld.processKeyEvent(data);
    }
  }

  static class OnReleased implements javalib.utils.AbstractKeyAdapter.Consumer<String> {
    final World currentWorld;

    OnReleased(World w) {
      this.currentWorld = w;
    }

    public void apply(String data) {
      currentWorld.processKeyReleased(data);
    }
  }
}

/**
 * <p>
 * The implementation of callbacks for the mouse events: mouse clicked, entered,
 * exited, pressed, and released.
 * </p>
 * <p>
 * We do not handle mouse motion events: mouse dragged and mouse moved.
 * </p>
 *
 * @author Viera K. Proulx
 * @since August 2, 2007
 */
final class MyMouseAdapter extends MouseAdapter {

  private String buttonNameFor(MouseEvent evt) {
    if (SwingUtilities.isLeftMouseButton(evt)) return "LeftButton";
    if (SwingUtilities.isMiddleMouseButton(evt)) return "MiddleButton";
    if (SwingUtilities.isRightMouseButton(evt)) return "RightButton";
    return "UnknownButton";
  }

  /**
   * the current <code>{@link World World}</code> that handles the mouse
   * events
   */
  World currentWorld;

  /** the mouse position recorded by the mouse event handler */
  Posn mousePosn;

  /**
   * Create the mouse listener for the given <code>{@link World World}</code>.
   *
   * @param currentWorld
   *            the given <code>{@link World World}</code>
   */
  MyMouseAdapter(World currentWorld) {
    this.currentWorld = currentWorld;
  }

  // ---------------------------------------------------------------------
  // the mouse event handlers

  /**
   * Adjust the reported mouse position to account for the top bar
   *
   * @param mousePosn
   *            the recorded mouse position
   * @return the actual mouse position
   */
  Posn adjustMousePosn(Posn mousePosn) {
    // .... use this to find the height of the top bar
    Insets ins = this.currentWorld.theCanvas.f.getInsets();
    mousePosn.y -= ins.top;
    mousePosn.x -= ins.left;
    return mousePosn;
  }

  /**
   * Invoked when the mouse has been clicked on a component.
   *
   * @param e
   *            the mouse event that invoked this callback
   */
  public void mouseClicked(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseClicked(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse enters a component.
   *
   * @param e
   *            the mouse event that invoked this callback
   */
  public void mouseEntered(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseEntered(adjustMousePosn(this.mousePosn));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse exits a component.
   *
   * @param e
   *            the mouse event that invoked this callback
   */
  public void mouseExited(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseExited(adjustMousePosn(this.mousePosn));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse button has been pressed on a component.
   *
   * @param e
   *            the mouse event that invoked this callback
   */
  public void mousePressed(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMousePressed(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when a mouse button has been released on a component.
   *
   * @param e
   *            the mouse event that invoked this callback
   */
  public void mouseReleased(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseReleased(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse is moved
   * @param e the mouse event that invoked this callback
   */
  public void mouseMoved(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseMoved(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }
}
