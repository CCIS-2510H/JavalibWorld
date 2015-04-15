package javalib.worldimages;

import javalib.worldcanvas.CanvasPanel;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent text as an image to be drawn by the world when drawing
 * on its <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4, 2015
 */
public final class TextImage extends WorldImage {

    /** the text to be shown */
    public String text;

    /** the size of the font to use: the default here is 13 */
    public float size;

    /** the color of the text */
    public Color color;

    /**
     * the style of the font: 0 = regular, 1 = bold, 2 = italic, 3 = italic bold
     */
    public int style = 0;

    /** the width of the bounding box */
    public int width = 0;

    /** the height of the bounding box */
    public int height = 0;

    /**
     * <p>
     * the desired alignment for this text: 0 = left, 1 = center, 2 = right
     * </p>
     * <em>not yet implemented - currently the text is centered</em>
     */
    public int alignment = 1;

    /** the Canvas for defining the font for this text image */
    public static CanvasPanel c = new CanvasPanel(600, 600);

    /** the graphics context where the text is to be shown */
    protected static Graphics2D g = c.getBufferGraphics();

    /** the current default font in our graphics context */
    protected static Font font = g.getFont();

    /**
     * A full constructor for this text image.
     * 
     * @param text
     *            -- the text to be shown
     * @param size
     *            -- the size of the font to use (the default is 13)
     * @param style
     *            -- the style of the font: (regular, bold, italic, italic/bold)
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, float size, int style, Color color) {
        super();
        // bad things happen if we want to display a null String
        // or a String of length 0
        if (text == null || text.equals(""))
            text = " ";
        this.text = text;
        this.size = size;
        this.style = style;
        this.color = color;
        this.setWidthHeight();
    }

    /**
     * A convenience constructor providing the default style (regular).
     * 
     * @param text
     *            -- the text to be shown
     * @param size
     *            -- the size of the font to use (the default is 13)
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, float size, Color color) {
        this(text, size, 0, color);
    }

    /**
     * A convenience constructor providing the default style (regular).
     * 
     * @param text
     *            -- the text to be shown
     * @param size
     *            -- the size of the font to use (the default is 13)
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, int size, Color color) {
        this(text, size, 0, color);
    }

    /**
     * A convenience constructor providing the default style (regular).
     * 
     * @param text
     *            -- the text to be shown
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, Color color) {
        this(text, 13, 0, color);
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.text == null)
            this.text = "";
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint and font
        Paint oldPaint = g.getPaint();
        Font oldFont = g.getFont();

        // change the font style and size as given
        g.setFont(oldFont.deriveFont(this.style, this.size));
        // set the paint to the given color
        g.setPaint(this.color);

        if (alignment == 1) {
            // draw the object
            g.drawString(this.text, -this.width / 2, this.height / 4);
        }

        // reset the original paint and font
        g.setPaint(oldPaint);
        g.setFont(oldFont);
    }

    /**
     * Compute and set the width and the height for this text in the given style
     * and size
     */
    protected void setWidthHeight() {
        AffineTransform t = g.getTransform();
        Rectangle2D bounds = getBoundingBox();
        g.setTransform(t);

        this.width = (int) bounds.getWidth();
        this.height = (int) bounds.getHeight();

    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform old = g.getTransform();
        g.setTransform(t);
        Rectangle2D bounds = getBoundingBox();
        g.setTransform(old);
        return new BoundingBox((int) bounds.getMinX(), (int) bounds.getMinY(),
                (int) bounds.getMaxX(), (int) bounds.getMaxY());
    }

    /**
     * Get the bounding box of the text. Note: This is distinct and separate
     * from getBB. getBoundingBox is for internal TextImage use, whereas getBB
     * is for package consumption
     * 
     * @return
     */
    private Rectangle2D getBoundingBox() {
        // change the font style and size as given
        g.setFont(font.deriveFont(this.style, this.size));
        // now get this new font
        Font newFont = g.getFont();

        FontRenderContext frc = g.getFontRenderContext();

        Point2D loc = new Point2D.Double(300, 300);

        TextLayout layout = new TextLayout(text, newFont, frc);
        layout.draw(g, (float) loc.getX(), (float) loc.getY());

        g.setFont(font);

        Rectangle2D bounds = layout.getBounds();
        return bounds;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Produce a <code>String</code> representation of this text image
     */
    public String toString() {
        char c = '"';
        return className(this) + "this.text = " + c + this.text + c
                + ",\nthis.size = " + this.size + ", this.style = "
                + this.style + ",\n" + colorString(this.color) + ")";
    }

    @Override
    public String toIndentedString(String indent) {
        char c = '"';
        indent = indent + "  ";
        return classNameString(indent, this) + "this.text = " + c + this.text
                + c + ",\n" + indent + "this.size = " + this.size
                + ", this.style = " + this.style + ","
                + colorString(indent, this.color) + ")";
    }

    /**
     * Is this <code>TextImage</code> same as that <code>TextImage</code>?
     */
    public boolean same(TextImage that) {
        return this.size == that.size && this.style == that.style
                && this.alignment == that.alignment
                && this.text.equals(that.text) && this.color.equals(that.color);
    }

    /**
     * Is this <code>TextImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof TextImage && this.same((TextImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + (int) this.size + this.style
                + this.alignment + this.text.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new TextImage(this.text, this.size, this.style,
                this.color);
        i.pinhole = p;
        return i;
    }
}