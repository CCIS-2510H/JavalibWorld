package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent an image that came from a file and is to be drawn by
 * the world when drawing on its <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public final class FromFileImage extends WorldImage {

    /** the file name for the image source */
    public String fileName;

    /**
     * the instance of the class that handles reading of files just once set to
     * be transient, so that it is not used in comparisons by tester lib
     */
    protected volatile ImageMaker imread;

    /**
     * A full constructor for this image created from the file input
     * 
     * @param fileName
     *            -- the file name for the image source
     */
    public FromFileImage(String fileName) {
        super(1);

        // determine how to read the file name
        // then read the image, or verify that it has been read already
        this.imread = new ImageMaker(fileName);

        // set the filename
        this.fileName = fileName;
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
    public void draw(Graphics2D g) {
        // Adjust the position of the frame
        g.translate(-(this.imread.width / 2.0), -(this.imread.height / 2.0));

        g.drawRenderedImage(this.imread.image, new AffineTransform());

        // Reset to original position
        g.translate((this.imread.width / 2.0), (this.imread.height / 2.0));
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.draw(g);
    }

    /**
     * Is this <code>FromFileImage</code> the same as that
     * <code>FromFileImage</code>?
     * 
     * @param that
     *            -- Image to compare against
     */
    public boolean same(FromFileImage that) {
        return this.fileName.equals(that.fileName);
    }

    @Override
    public double getWidth() {
        return this.imread.width;
    }

    @Override
    public double getHeight() {
        return this.imread.height;
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        double w = this.getWidth();
        double h = this.getHeight();
        Point2D tl = t.transform(new Point.Double(-w / 2, -h / 2), null);
        Point2D br = t.transform(new Point.Double(w / 2, h / 2), null);
        return new BoundingBox(tl.getX(), tl.getY(), br.getX(), br.getY());
    }
    
    /**
     * Produce a <code>String</code> representation of this from-file image
     */
    public String toString() {
        return className(this) + "this.fileName = \"" + this.fileName + "\")";
    }

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, this) + "this.fileName = \""
                + this.fileName + "\")";
    }

    /**
     * Is this <code>FromFileImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        return o instanceof FromFileImage && this.same((FromFileImage) o);
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.fileName.hashCode();
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new FromFileImage(this.fileName);
        i.pinhole = p;
        return i;
    }
}