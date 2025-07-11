package javalib.worldcanvas;

import javalib.impworld.WorldScene;
import javalib.worldimages.CircleImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Copyright 2007, 2008, 2009, 2012 Viera K. Proulx
 * This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)
 */

/**
 * Functional Canvas - allows the drawing of shapes, lines, and text in the
 * window of the given size, window closing and re-opening.
 * 
 * @author Viera K. Proulx
 * @since July 12 2007, August 2, 2007, October 19, 2009
 */
public class WorldCanvas {

    /** records the number of canvases currently open */
    protected static int WINDOWS_OPEN = 0;

    /** the frame that holds the canvas */
    public transient JFrame frame;

    /** the panel that allows us to paint graphics */
    public transient CanvasPanel panel;

    /** the width of the panel */
    protected int width;

    /** the height of the panel */
    protected int height;

    /**
     * <P>
     * Construct a new frame with the
     * <CODE>{@link CanvasPanel CanvasPanel} panel</CODE> as its component.
     * <P>
     * 
     * @param width
     *            the width of the panel
     * @param height
     *            the height of the panel
     * @param title
     *            the title of the panel
     */
    public WorldCanvas(final int width, final int height, final String title) {
        this.width = width;
        this.height = height;

        // Label the frame as "Canvas" and set up the layout
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // End the application when the last window closes
        frame.addWindowListener(winapt);

        // if the user closes the Canvas window
        // it will only hide and can be reopened by invoking 'show'
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        // set up the panel and the graphics
        panel = new CanvasPanel(width, height);
        panel.addNotify();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().setMinimumSize(new Dimension(width, height));

        frame.pack();
        Graphics g = panel.getGraphics();
        frame.update(g);
        frame.setVisible(false);
    }

    /**
     * A WindowAdapter that allows us to close a window and re-open, provided
     * there is at least one open window. The program ends when all windows have
     * been closed.
     */
    protected transient WindowAdapter winapt = new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            // System.out.println("hiding the window");
            WINDOWS_OPEN = WINDOWS_OPEN - 1;
            // closeCanvas();
            panel.clearPanel();

            // diagnostics showing the state of the WINDOWS_OPEN constant
            // System.out.println(WINDOWS_OPEN + " open windows.");

            if (WINDOWS_OPEN == 0)
                System.exit(0);
        }
    };

    /**
     * Create a new canvas with the default title "Canvas"
     * 
     * @param width
     *            the width of the canvas
     * @param height
     *            the height of the canvas
     */
    public WorldCanvas(int width, int height) {
        this(width, height, "Canvas");
    }

    /**
     * <p>
     * Returns a <code>Graphics2D</code> object that permits painting to the
     * internal buffered image for this canvas.
     * </p>
     * 
     * <p>
     * The user should always use this object to paint to the buffer and thus
     * indirectly modify this buffered panel.
     * </p>
     * 
     * <p>
     * To make painting changes to the buffer visible, the
     * <code>repaint()</code> method must explicitly be called. This allows a
     * number of painting operations to be done prior to screen repaint.
     * </p>
     */
    public final Graphics2D getBufferGraphics() {
        return panel.getBufferGraphics();
    }

    /**
     * Retrieves the color of the requested pixel of this image
     *
     * @param x - the column of the desired pixel
     * @param y - the row of the desired pixel
     * @return the {@link Color} of the desired pixel
     * @throws IndexOutOfBoundsException if (x, y) is out of bounds
     */
    public Color getColorAt(int x, int y) throws IndexOutOfBoundsException {
        if (x < 0 || x >= this.width)
            throw new IndexOutOfBoundsException(String.format("Specified x (%d) is not in range [0, %d)",
                    x, this.width));
        if (y < 0 || y >= this.height)
            throw new IndexOutOfBoundsException(String.format("Specified y (%d) is not in range [0, %d)",
                    y, this.height));
        int[] ans = new int[4];
        panel.getBuffer().getRaster().getPixel(x, y, ans);
        return new Color(ans[0], ans[1], ans[2], ans[3]);
    }

    // ///////////////////////////////////////////////////////////////////////
    // Methods for drawing and erasing shapes and text //
    // ///////////////////////////////////////////////////////////////////////

    public boolean drawScene(WorldSceneBase scene) {
        if (frame.getWidth() != scene.width || frame.getHeight() != scene.height) {
            frame.getContentPane().setMinimumSize(
                    new Dimension(scene.width, scene.height));
        }
        ((CanvasPanel) panel).drawScene(scene);
        return true;
    }

    public void printCurrentFont() {
        ((CanvasPanel) panel).getFont();
    }

    // ///////////////////////////////////////////////////////////////////////
    // Methods for showing and hiding the canvas //
    // ///////////////////////////////////////////////////////////////////////

    /**
     * Show the window with the canvas cleared
     * 
     * @return <code>true</code> if successfully opened, or opened already
     */
    public boolean show() {
        // check if the widow is already open
        if (!frame.isVisible()) {

            // account for the open window, make it appear
            WINDOWS_OPEN = WINDOWS_OPEN + 1;
            frame.setVisible(true);
            // redraw the background
            return true;
        } else {
            // do nothing if the window is open already
            // System.out.println("The window is shown already");
            return true;
        }
    }

    /**
     * Close the window - if it is currently open, do nothing otherwise
     * 
     * @return <code>true</code> if successfully closed, or closed already
     */
    public boolean close() {
        if (frame.isVisible()) {
            WINDOWS_OPEN = WINDOWS_OPEN - 1;
            frame.setVisible(false);
            panel.clearPanel();
        }
        return true;
    }

    /**
     * Clear the canvas before painting the next scene
     */
    public void clear() {
        this.panel.clearPanel();
    }

    /**
     * Helper method to display a message and await RETURN before proceeding
     * 
     * @param message
     *            the message to display
     */
    private static void nextStep(String message) {
        try {
            System.out.println(message);
            System.out.println("Press RETURN");
            // no input needed - the default input is a valid integer
            System.in.read();
        } catch (IOException e) {
            System.out.println("Next step");
        }
    }

    /**
     * Produce a <code>String</code> representation of this Canvas
     */
    public String toString() {
        return "new Canvas(" + this.width + ", " + this.height + ")";
    }

    /**
     * Produce an indented <code>String</code> representation of this Canvas
     * 
     * @param indent
     *            the desired indentation: ignored, because we only produce one
     *            line of text.
     */
    public String toIndentedString(String indent) {
        return "new Canvas(" + this.width + ", " + this.height + ")";
    }

    /**
     * Self test for the Canvas class
     * 
     * @param argv
     */
    public static void main(String[] argv) {
        nextStep("Canvas with default name is constructed");
        WorldCanvas sm1 = new WorldCanvas(200, 200);

        nextStep("To show the canvas ... ");
        sm1.show();

        WorldScene scene1 = new WorldScene(200, 200);

        nextStep("Canvas shown - should be blank - add red and blue disk");
        scene1.placeImageXY(new CircleImage(20, "outline", Color.red), 50, 50);
        scene1.placeImageXY(new CircleImage(20, "outline", Color.blue), 150, 50);
        sm1.drawScene(scene1);

        nextStep("Show the canvas again - it should not do anything");
        sm1.show();

        nextStep("Draw a green disk");
        scene1.placeImageXY(new CircleImage(50, "outline", Color.green), 50,
                150);
        sm1.drawScene(scene1);

        nextStep("Close the Canvas");
        sm1.close();

        nextStep("Show the canvas again - it should be cleared");
        sm1.show();

        nextStep("Paint one disks on the canvas");

        WorldScene scene2 = new WorldScene(200, 200);
        scene2.placeImageXY(new CircleImage(25, "outline", Color.black), 50,
                150);
        sm1.drawScene(scene2);

        nextStep("Construct a second canvas with the name Smiley");
        WorldCanvas sm2 = new WorldCanvas(200, 200, "Smiley");

        nextStep("Show the second canvas");
        sm2.show();

        nextStep("Paint two disks on the Smiley canvas");
        WorldScene scene3 = new WorldScene(200, 200);

        scene3.placeImageXY(new CircleImage(20, "outline", Color.red), 50, 50);
        scene3.placeImageXY(new CircleImage(50, "outline", Color.blue), 150, 50);
        sm2.drawScene(scene3);

        nextStep("Manually close the 'Canvas' window"
                + "and see if we can bring it back to life");
        sm1.show();

        nextStep("The first canvas should be shown - cleared");
        WorldScene scene4 = new WorldScene(200, 200);
        scene4.placeImageXY(new CircleImage(30, "outline", Color.red), 50, 50);
        scene4.placeImageXY(new CircleImage(30, "outline", Color.blue), 150, 50);
        scene4.placeImageXY(new CircleImage(30, "outline", Color.green), 50,
                150);
        sm1.drawScene(scene4);

        nextStep("The first canvas has three disks drawn");

        System.out.println("Close both canvas windows to end the program");
    }
}
