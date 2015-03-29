package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The parent class for all images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012, April 25 2012
 */
public abstract class WorldImage {
    public Posn pinhole;

    /**
     * Every image has a pinhole (<code>Posn</code>) and a color (
     * <code>Color</code>). The color for the images derived from image files,
     * or constructed by a combination of several images is set to
     * <code>Color.white</code> and ignored in drawing the images.
     * 
     * @param pinhole
     *            the pinhole location for this image
     * @param color
     *            the color for this image
     */
    public WorldImage() {
        this(new Posn(0, 0));
    }

    // Ignore this for now
    protected WorldImage(Posn pinhole) {
        this.pinhole = pinhole;
    }

    public BoundingBox getBB() {
        return this.getBB(new AffineTransform());
    }

    protected abstract BoundingBox getBB(AffineTransform t);

    protected static Point2D transformPosn(AffineTransform t, Posn p) {
        return transformPosn(t, p.x, p.y);
    }

    protected static Point2D transformPosn(AffineTransform t, int x, int y) {
        Point2D point = new Point(x, y);
        return t.transform(point, null);
    }

    public abstract WorldImage movePinholeTo(Posn p);

    public WorldImage movePinhole(double dx, double dy) {
        return movePinholeTo(new Posn((int) Math.round(this.pinhole.x + dx),
                (int) Math.round(this.pinhole.y + dy)));
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    abstract public void draw(Graphics2D g);

    /**
     * <p>
     * A convenience method that allows us to combine several images into one on
     * top of this image without the need to explicitly construct
     * <code>{@link OverlayImages OverlayImages}</code>
     * </p>
     * <p>
     * The pinhole is placed in the middle of all overlayed images.
     * </p>
     * 
     * @param args
     *            an arbitrarily long list of
     *            <code>{@link WorldImage WorldImage}</code> to add to this
     *            image
     * @return the composite image
     **/
    public WorldImage overlayImages(WorldImage... args) {
        WorldImage image = this;

        // compute the length of the argument list
        int length = (args != null) ? args.length : 0;

        // add each of the images to this one
        // They should all align on their respective centers
        for (int i = 0; i < length; i++) {
            image = new OverlayImages(image, args[i]);
        }

        return image;
    }

    /**
     * Produce the width of this image
     * 
     * @return the width of this image
     */
    abstract public int getWidth();

    /**
     * Produce the height of this image
     * 
     * @return the height of this image
     */
    abstract public int getHeight();

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    abstract public String toIndentedString(String indent);

    /**
     * produce the <code>String</code> that represent the color without the
     * extra narrative.
     * 
     * @param color
     *            the given color
     * @return the rgb representation S <code>String</code>
     */
    protected static String colorString(String indent, Color color) {
        String result = color.toString();
        int start = result.indexOf('[');
        result = result.substring(start, result.length());
        return "\n" + indent + "this.color = " + result + ",";
    }

    protected static String classNameString(String indent, String className) {
        return "\n" + indent + "new " + className + "(";
    }

    public static void main(String[] argv) {
        System.out.println(colorString("  ", new Color(255, 255, 0, 50)));
    }

}