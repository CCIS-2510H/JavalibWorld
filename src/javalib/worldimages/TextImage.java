package javalib.worldimages;

import javalib.worldcanvas.CanvasPanel;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

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
    public double size;

    /** the color of the text */
    public Color color;

    /**
     * the style of the font
     */
    public FontStyle style = FontStyle.REGULAR;

    /** the width of the bounding box */
    public double width = 0;

    /** the height of the bounding box */
    public double height = 0;

    private double baselineDy = 0;

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
    public TextImage(String text, double size, FontStyle style, Color color) {
        super(1);
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
     *            -- the size of the font to use
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, double size, Color color) {
        this(text, size, FontStyle.REGULAR, color);
    }

    /**
     * A convenience constructor providing the default style (regular).
     * 
     * @param text
     *            -- the text to be shown
     * @param size
     *            -- the size of the font to use
     * @param color
     *            -- the color for this image
     */
    public TextImage(String text, int size, Color color) {
        this(text, size, FontStyle.REGULAR, color);
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
        this(text, 13, FontStyle.REGULAR, color);
    }
    @Override
    int numKids() {
        return 0;
    }
    @Override
    WorldImage getKid(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }
    @Override
    AffineTransform getTransform(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    protected void drawStackUnsafe(Graphics2D g) {
        if (this.text == null)
            this.text = "";
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint and font
        Paint oldPaint = g.getPaint();
        Font oldFont = g.getFont();

        // change the font style and size as given
        g.setFont(oldFont.deriveFont(this.style.ordinal(), (float) this.size));
        // set the paint to the given color
        g.setPaint(this.color);

        g.drawString(this.text, (int) -Math.round(this.width / 2),
                (int) -this.baselineDy);

        // reset the original paint and font
        g.setPaint(oldPaint);
        g.setFont(oldFont);
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
         this.drawStackUnsafe(g);
    }

    /**
     * Compute and set the width and the height for this text in the given style
     * and size
     */
    protected void setWidthHeight() {
        Rectangle2D bounds = getBoundingBox();

        this.width = (int) bounds.getWidth();
        this.height = (int) bounds.getHeight();
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        AffineTransform old = g.getTransform();
        g.setTransform(t);
        Rectangle2D bounds = getBoundingBox();
        g.setTransform(old);
        return new BoundingBox(bounds.getMinX(), bounds.getMinY(),
                bounds.getMaxX(), bounds.getMaxY());
    }
    
    /**
     * Get the bounding box of the text. Note: This is distinct and separate
     * from getBB. getBoundingBox is for internal TextImage use, whereas getBB
     * is for package consumption
     * 
     * @return
     */
    private Rectangle2D getBoundingBox() {
        Font newFont = font.deriveFont(this.style.ordinal(), (float)this.size);

        FontRenderContext frc = new FontRenderContext(null, true, true);//g.getFontRenderContext();
        TextLayout layout = new TextLayout(this.text, newFont, frc);


        double width = layout.getBounds().getWidth();
        double height = layout.getAscent() + layout.getDescent();

        this.baselineDy = height / 2.0 - layout.getAscent();

        Rectangle2D ans = new Rectangle2D.Double(-width / 2.0, -height / 2.0,
                width, height);
        return ans;
/*
        AffineTransform t = g.getTransform();
        Point2D topLeft = new Point2D.Double(ans.getMinX(), ans.getMinY());
        Point2D botRight = new Point2D.Double(ans.getMaxX(), ans.getMaxY());
        Point2D topRight = new Point2D.Double(ans.getMaxX(), ans.getMinY());
        Point2D botLeft = new Point2D.Double(ans.getMinX(), ans.getMaxY());
        t.transform(topLeft, topLeft);
        t.transform(topRight, topRight);
        t.transform(botRight, botRight);
        t.transform(botLeft, botLeft);
        double minX = Math.min(Math.min(topLeft.getX(), topRight.getX()),
                Math.min(botLeft.getX(), botRight.getX()));
        double minY = Math.min(Math.min(topLeft.getY(), topRight.getY()),
                Math.min(botLeft.getY(), botRight.getY()));
        double maxX = Math.max(Math.max(topLeft.getX(), topRight.getX()),
                Math.max(botLeft.getX(), botRight.getX()));
        double maxY = Math.max(Math.max(topLeft.getY(), topRight.getY()),
                Math.max(botLeft.getY(), botRight.getY()));
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        */
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.text = \"")
               .append(this.text.replace("\\", "\\\\").replace("\"", "\\\""))
               .append("\",");
        stack.push(
                new FieldsWLItem(this.pinhole,
                        new ImageField("size", this.size),
                        new ImageField("style", this.style, true),
                        new ImageField("color", this.color)));
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof TextImage) {
            TextImage that = (TextImage)other;
            return this.size == that.size && this.style == that.style
                    && this.text.equals(that.text) && this.color.equals(that.color)
                    && this.pinhole.equals(that.pinhole);
        }
        return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + (int) this.size + this.style.hashCode()
                + this.text.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new TextImage(this.text, this.size, this.style,
                this.color);
        i.pinhole = p;
        return i;
    }
}