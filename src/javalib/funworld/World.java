package javalib.funworld;

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
   * the canvas that displays the current world
   */
  public WorldCanvas theCanvas;

  /**
   * true if 'bigBang' started the world and it did not end, did not stop
   */
  private transient boolean worldExists = false;

  /**
   * the timer for this world
   */
  transient MyTimer mytime;

  /**
   * timer events not processed when the mouse event is processed
   */
  transient boolean stopTimer = false;

  /**
   * the key adapter for this world
   */
  private transient MyKeyAdapter keyAdapter
          ;

  /**
   * the mouse adapter for this world
   */
  private transient MyMouseAdapter mouseAdapter;

  /**
   * the window closing listener for this world
   */
  private transient WindowListener windowClosing;

  private transient WorldScene blankScene = new WorldScene(0, 0);

  /**
   * Has the world ended?
   */
  private transient boolean worldEnded = false;
  public final boolean hasWorldEnded() {
    return this.worldEnded || this.shouldWorldEnd();
  }

  private transient WorldScene lastScene = null;

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
   * Start the world by creating a canvas of the given size, creating and
   * adding the key and mouse adapters, and starting the timer at the given
   * speed.
   *
   * @param width  the width of the <code>{@link WorldCanvas Canvas}</code>
   * @param height the height of the <code>{@link WorldCanvas Canvas}</code>
   * @param speed  the speed at which the clock runs
   * @return <code>true</code>
   */
  public boolean bigBang(int width, int height, double speed) {
    if (this.worldExists) {
      System.out.println("Only one world can run at a time");
      return true;
    }
    // throw runtime exceptions if width, height <= 0
    this.theCanvas = new WorldCanvas(width, height);
    this.blankScene = new WorldScene(width, height);
    this.worldEnded = false;

    // if the user closes the Canvas window
    // it will only hide and can be reopened by invoking 'show'
    this.theCanvas.frame
            .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    this.windowClosing = new MyWindowClosingListener(this);
    this.theCanvas.frame.addWindowListener(this.windowClosing);

    // pause a bit so that two canvases do not compete when being opened
    // almost at the same time
    long start = System.currentTimeMillis();
    long tmp = System.currentTimeMillis();

    while (tmp - start < 1000) {
      tmp = System.currentTimeMillis();
    }

    // add the key listener to the frame for our canvas
    this.keyAdapter = new MyKeyAdapter(this);
    this.theCanvas.frame.addKeyListener(this.keyAdapter);
    this.theCanvas.frame.setFocusTraversalKeysEnabled(false);

    // add the mouse listener to the frame for our canvas
    this.mouseAdapter = new MyMouseAdapter(this);
    this.theCanvas.frame.addMouseListener(this.mouseAdapter);
    this.theCanvas.frame.addMouseMotionListener(this.mouseAdapter);

    // make sure the canvas responds to events
    this.theCanvas.frame.setFocusable(true);

    // finally, show the canvas and draw the initial world
    this.theCanvas.show();

    // draw the initial world
    this.worldEnded = this.shouldWorldEnd();
    this.mytime = new MyTimer(this, speed);
    if (!this.worldEnded) {
      this.worldExists = true;
    }

    this.drawWorld();

    // pause again after the Canvas is shown to make sure
    // all listeners and the timer are installed for theCanvas
    start = System.currentTimeMillis();
    tmp = System.currentTimeMillis();

    while (tmp - start < 1000) {
      tmp = System.currentTimeMillis();
    }

    // and add the timer -- start it if speed is greater than 0
    if (speed > 0.0)
      this.mytime.timer.start();

    // print a header that specifies the current version of the World
    System.out.println(Versions.CURRENT_VERSION);

    return this.drawWorld();
  }

  public WorldScene getEmptyScene() {
    return this.blankScene;
  }

  /**
   * Start the world by creating a canvas of the given size, creating and
   * adding the key and mouse adapters, without running the the timer.
   *
   * @param w the width of the <code>{@link WorldCanvas Canvas}</code>
   * @param h the height of the <code>{@link WorldCanvas Canvas}</code>
   * @return <code>true</code>
   */
  public boolean bigBang(int w, int h) {
    return this.bigBang(w, h, 0.0);
  }

  /**
   * Stop the world, close all listeners and the timer, draw the last
   * <code>Scene</code>.
   */
  private void stopWorld(WorldScene toDraw) {
    if (worldExists) {
      // remove listeners and set worldExists to false
      this.mytime.timer.stop();
      this.worldExists = false;
      this.mytime.stopTimer();
      this.theCanvas.frame.removeKeyListener(this.keyAdapter);
      this.theCanvas.frame.removeMouseListener(this.mouseAdapter);
      System.out.println("The world stopped.");

      // draw the final scene of the world with the end of time message
      if (toDraw != null) {
        this.lastScene = toDraw;
        this.theCanvas.clear();
        this.theCanvas.drawScene(toDraw);
      }
    }
  }

  /**
   * <p>
   * This method is invoked at each tick. It checks if the world should end
   * now.
   * </p>
   */
  public boolean shouldWorldEnd() {
    return false;
  }

  /**
   * End the world interactions - leave the canvas open, show the image of the
   * last world with the given message
   *
   * @param s the message to display
   * @return <code>this</code> world
   */
  public World endOfWorld(String s) {
    // set up the last world pair and finish as usual
    this.worldEnded = true;
    this.stopWorld(this.getLastScene(s));

    return this;
  }

  /**
   * In the Funworld environment, it is tempting to write a handler that does something like
   * <pre>
   *   public World onBlah() {
   *     return this.makeOneLastChange().endOfWorld("done");
   *   }
   * </pre>
   * However, because worlds are intended to be immutable, there is an off-by-one-world glitch
   * here: calling <code>endOfWorld</code> will produce a new world that knows it's over,
   * but this current world does not, and its event handlers continue to fire.
   * Moreover, we need to draw the lastScene from the new world, without modifying this one.
   *
   * This helper routine will disable the current world and draw the given world's lastScene,
   * or else transfer control over to the given world.
   * @param w The world that either gains control from this one, or paints our last rendered scene.
   * @return
   */
  private World stopOrReset(World w, String why) {
    w.worldEnded = w.hasWorldEnded();
    if (w.worldEnded) {
      this.stopWorld(resetWorld(w).getLastScene(why));
      return w;
    } else {
      return resetWorld(w);
    }
  }


  /**
   * The method invoked by the timer on each tick. Delegates to the user to
   * define a new state of the world, then resets the canvas and event
   * handlers for the new world to those currently used.
   *
   * @return <code>{@link World World}</code> after the tick event
   */
  synchronized World processTick() {
    try {
      if (this.worldExists && !this.stopTimer) {
        if (this.worldEnded) {
          this.stopWorld(this.getLastScene("tick"));
        } else {
          World bw = this.onTick();
          return stopOrReset(bw, "tick");
        }
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the timer on each tick. Produces a
   * new <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @return <code>{@link World World}</code> that needs to have the canvas
   * and the event handlers initialized
   */
  public World onTick() {
    return this;
  }

  /**
   * The method invoked by the key adapter on selected key events. Delegates
   * to the user to define a new state of the world, then resets the canvas
   * and event handlers for the new world to those currently used.
   *
   * @return <code>{@link World World}</code> after the key event
   */
  synchronized World processKeyEvent(String key) {
    try {
      if (this.worldExists) {
        World bw = this.onKeyEvent(key);
        return stopOrReset(bw, "keyEvent");
      } else
        return this;

    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the key adapter on selected key
   * events. Produces a new <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @return <code>{@link World World}</code> that needs to have the canvas
   * and the event handlers initialized
   */
  public World onKeyEvent(String s) {
    return this;
  }

  /**
   * The method invoked by the key adapter on key-released events. Delegates
   * to the user to define a new state of the world, then resets the canvas
   * and event handlers for the new world to those currently used.
   *
   * @return <code>{@link World World}</code> after the key event
   */
  synchronized World processKeyReleased(String key) {
    try {
      if (this.worldExists) {
        World bw = this.onKeyReleased(key);
        return stopOrReset(bw, "keyReleased");
      } else
        return this;

    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the key adapter on key-released
   * events. Produces a new <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @return <code>{@link World World}</code> that needs to have the canvas
   * and the event handlers initialized
   */
  public World onKeyReleased(String key) {
    return this;
  }


  /**
   * The method invoked by the mouse adapter on mouse clicked event. Delegates
   * to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when clicked
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMouseClicked(Posn mouse, String button) {
    try {
      if (this.worldExists) {
        World bw = this.onMouseClicked(mouse, button);
        return stopOrReset(bw, "mouseClicked");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * clicked. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when clicked
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseClicked(Posn mouse) {
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * clicked. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse      the location of the mouse when clicked
   * @param buttonName which button was clicked
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseClicked(Posn mouse, String buttonName) {
    return this.onMouseClicked(mouse);
  }

  /**
   * The method invoked by the mouse adapter on mouse entered event. Delegates
   * to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when entered
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMouseEntered(Posn mouse) {

    try {
      if (this.worldExists) {
        World bw = this.onMouseEntered(mouse);
        return stopOrReset(bw, "mouseEntered");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }

    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * entered. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when entered
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseEntered(Posn mouse) {
    return this;
  }

  /**
   * The method invoked by the mouse adapter on mouse exited event. Delegates
   * to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when exited
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMouseExited(Posn mouse) {

    try {
      if (this.worldExists) {
        World bw = this.onMouseExited(mouse);
        return stopOrReset(bw, "mouseExited");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }

    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * exited. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when exited
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseExited(Posn mouse) {
    return this;
  }

  /**
   * The method invoked by the mouse adapter on mouse pressed event. Delegates
   * to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when pressed
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMousePressed(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        World bw = this.onMousePressed(mouse, button);
        return stopOrReset(bw, "mousePressed");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }

    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * pressed. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when pressed
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMousePressed(Posn mouse) {
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * pressed. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse      the location of the mouse when pressed
   * @param buttonName which button was pressed
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMousePressed(Posn mouse, String buttonName) {
    return this.onMousePressed(mouse);
  }

  /**
   * The method invoked by the mouse adapter on mouse released event.
   * Delegates to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when released
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMouseReleased(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        World bw = this.onMouseReleased(mouse, button);
        return stopOrReset(bw, "mouseReleased");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }

    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * released. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when released
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseReleased(Posn mouse) {
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * released. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse      the location of the mouse when released
   * @param buttonName which button was released
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseReleased(Posn mouse, String buttonName) {
    return this.onMouseReleased(mouse);
  }

  /**
   * The method invoked by the mouse adapter on mouse moved event.
   * Delegates to the user to define a new state of the world.
   *
   * @param mouse the location of the mouse when moved
   * @return <code>{@link World World}</code> after the mouse event
   */
  World processMouseMoved(Posn mouse, String button) {

    try {
      if (this.worldExists) {
        World bw = this.onMouseMoved(mouse, button);
        return stopOrReset(bw, "mouseMoved");
      } else
        return this;
    } catch (RuntimeException re) {
      re.printStackTrace();
      this.drawWorld();
      // throw re;
      Runtime.getRuntime().halt(1);
    }

    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * moved. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse the location of the mouse when moved
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseMoved(Posn mouse) {
    return this;
  }

  /**
   * <p>
   * User defined method to be invoked by the mouse adapter when a mouse is
   * moved. Update the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @param mouse      the location of the mouse when moved
   * @param buttonName which button was moved
   * @return <code>{@link World World}</code> after the mouse event
   */
  public World onMouseMoved(Posn mouse, String buttonName) {
    return this.onMouseMoved(mouse);
  }

  /**
   * Initialize the canvas, the event handlers, and the timer for the given
   * <code>{@link World World}</code> to the current settings.
   *
   * @param bw the new <code>{@link World World}</code> created by the user
   * @return the same <code>{@link World World}</code> with the canvas, the
   * event handlers, and the timer initialized to the current
   * settings.
   */
  private synchronized World resetWorld(World bw) {
    if (this.worldExists) {
      bw.blankScene = this.blankScene;
      bw.theCanvas = this.theCanvas;
      bw.worldExists = true;

      // with all the listeners
      bw.keyAdapter = this.keyAdapter;
      bw.mouseAdapter = this.mouseAdapter;
      bw.windowClosing = this.windowClosing;
      bw.keyAdapter.resetWorld(bw);
      bw.mouseAdapter.currentWorld = bw;

      // and the timer
      bw.mytime = this.mytime;
      bw.mytime.setSpeed();
      bw.mytime.currentWorld = bw;

      // draw the new world
      bw.drawWorld();
      return bw;
    } else {
      this.drawWorld();
      return this;
    }
  }

  /**
   * Invoke the user defined <code>makeImage</code> method, if this
   * <code>{@link World World}</code> has been initialized via
   * <code>bigBang</code> and did not stop or end, otherwise invoke the user
   * defined <code>lastImage</code> method,
   *
   * @return <code>true</code>
   */
  synchronized boolean drawWorld() {
    if (this.worldExists) {
      this.theCanvas.clear();
      this.theCanvas.drawScene(this.makeScene());
      return true;
    } else {
      if (this.getLastScene("") != null) {
        this.theCanvas.clear();
        this.theCanvas.drawScene(lastScene);
      }
      return true;
    }
  }

  /**
   * <p>
   * User defined method to draw the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @return the image that represents this world at this moment
   */
  abstract public WorldScene makeScene();

  /**
   * <p>
   * User defined method to draw the <code>{@link World World}</code>.
   * </P>
   * <p>
   * Override this method in the game world class
   * </P>
   *
   * @return the image that represents the last world to be drawn
   */
  public WorldScene lastScene(String s) {
    return null;
  }

  private WorldScene getLastScene(String s) {
    if (this.lastScene == null) {
      this.lastScene = this.lastScene(s);
    }
    return this.lastScene;
  }
}

/**
 * The window closing listener - it stops the timer when the frame is closed and
 * stops playing all tunes - in the tickTunes bucket and in the keyTunes bucket.
 *
 * @author Viera K. Proulx
 * @since 5 August 2010
 */
class MyWindowClosingListener extends WindowAdapter {
  World w;

  MyWindowClosingListener(World w) {
    this.w = w;
  }

  public void windowClosing(WindowEvent we) {
    if (this.w != null && this.w.mytime != null)
      this.w.mytime.stopTimer();

    /**
     * if (tunes.MusicBox.SYNTH_READY){ // clear the tick tune bucket
     * this.w.tickTunes.clearBucket(); // clear the key tune bucket
     * this.w.keyTunes.clearBucket(); }
     **/
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

  /**
   * the <code>Timer</code> that generates the time events
   */
  Timer timer;

  public boolean running = true;

  /**
   * the timer speed
   */
  final int speed;

  /**
   * Create the initial timer for the given <code>{@link World World}</code>
   * at the given <code>speed</code>.
   *
   * @param currentWorld the given <code>{@link World World}</code>
   * @param speed        the given <code>speed</code>
   */
  MyTimer(World currentWorld, double speed) {
    this.currentWorld = currentWorld;
    this.timer = new Timer((int)(speed * 1000),
            this.timerTasks);
    this.speed = (int)(speed * 1000);
  }

  /**
   * The callback for the timer events
   */
  ActionListener timerTasks = new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
      if (running)
        currentWorld = currentWorld.processTick();
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
  /**
   * the <code>KeyAdapter</code> that handles the key events
   */
  MyKeyAdapter(World currentWorld) {
    super(new OnKey(currentWorld), new OnReleased(currentWorld));
  }

  void resetWorld(final World currentWorld) {
    this.onKey = new OnKey(currentWorld);
    this.onReleased = new OnReleased(currentWorld);
  }

  static class OnKey implements javalib.utils.AbstractKeyAdapter.Consumer<String> {
    World currentWorld;

    OnKey(World w) {
      this.currentWorld = w;
    }

    public void apply(String data) {
      currentWorld = currentWorld.processKeyEvent(data);
    }
  }

  static class OnReleased implements javalib.utils.AbstractKeyAdapter.Consumer<String> {
    World currentWorld;

    OnReleased(World w) {
      this.currentWorld = w;
    }

    public void apply(String data) {
      currentWorld = currentWorld.processKeyReleased(data);
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

  /**
   * the mouse position recorded by the mouse event handler
   */
  Posn mousePosn;

  /**
   * Create the mouse listener for the given <code>{@link World World}</code>.
   *
   * @param currentWorld the given <code>{@link World World}</code>
   */
  MyMouseAdapter(World currentWorld) {
    this.currentWorld = currentWorld;
  }

  // ---------------------------------------------------------------------
  // the mouse event handlers

  /**
   * Adjust the reported mouse position to account for the top bar
   *
   * @param mousePosn the recorded mouse position
   * @return the actual mouse position
   */
  Posn adjustMousePosn(Posn mousePosn) {
    // .... use this to find the height of the top bar
    if (this.currentWorld == null || this.currentWorld.theCanvas == null || this.currentWorld.theCanvas.frame == null) {
      return mousePosn;
    }
    Insets ins = this.currentWorld.theCanvas.frame.getInsets();
    mousePosn.y -= ins.top;
    mousePosn.x -= ins.left;
    return mousePosn;
  }

  /**
   * Invoked when the mouse has been clicked on a component.
   *
   * @param e the mouse event that invoked this callback
   */
  public void mouseClicked(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld = this.currentWorld
            .processMouseClicked(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse enters a component.
   *
   * @param e the mouse event that invoked this callback
   */
  public void mouseEntered(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld = this.currentWorld
            .processMouseEntered(adjustMousePosn(this.mousePosn));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse exits a component.
   *
   * @param e the mouse event that invoked this callback
   */
  public void mouseExited(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld = this.currentWorld
            .processMouseExited(adjustMousePosn(this.mousePosn));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when the mouse button has been pressed on a component.
   *
   * @param e the mouse event that invoked this callback
   */
  public void mousePressed(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld = this.currentWorld
            .processMousePressed(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  /**
   * Invoked when a mouse button has been released on a component.
   *
   * @param e the mouse event that invoked this callback
   */
  public void mouseReleased(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld = this.currentWorld
            .processMouseReleased(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }

  public void mouseMoved(MouseEvent e) {
    this.currentWorld.stopTimer = true;
    this.mousePosn = new Posn(e.getX(), e.getY());
    this.currentWorld.processMouseMoved(adjustMousePosn(this.mousePosn), buttonNameFor(e));
    this.currentWorld.stopTimer = false;
  }
}
