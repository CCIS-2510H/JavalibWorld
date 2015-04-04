package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;

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
        super();

        // determine how to read the file name
        // then read the image, or verify that it has been read already
        this.imread = new ImageMaker(fileName);

        // set the filename
        this.fileName = fileName;
    }

    @Override
    public void draw(Graphics2D g) {
        // Adjust the position of the frame
        g.translate(-(this.imread.width / 2), -(this.imread.height / 2));

        g.drawRenderedImage(this.imread.image, new AffineTransform());

        // Reset to original position
        g.translate((this.imread.width / 2), (this.imread.height / 2));
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
    public int getWidth() {
        return this.imread.width;
    }

    @Override
    public int getHeight() {
        return this.imread.height;
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        Point2D tl = t.transform(
                new Point(-this.getWidth() / 2, -this.getHeight() / 2), null);
        Point2D br = t.transform(
                new Point(this.getWidth() / 2, this.getHeight() / 2), null);
        return new BoundingBox((int) tl.getX(), (int) tl.getY(),
                (int) br.getX(), (int) br.getY());
    }

    /**
     * Produce a <code>String</code> representation of this from-file image
     */
    public String toString() {
        return className(this) + "this.fileName = " + this.fileName
                + "this.width = " + this.getWidth() + ", " + "this.height = "
                + this.getHeight() + ")\n";
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
        return classNameString(indent, this) + "this.fileName = "
                + this.fileName + ",\n" + indent + "this.width = "
                + this.getWidth() + ",\n" + indent + "this.height = "
                + this.getHeight() + ")\n";
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