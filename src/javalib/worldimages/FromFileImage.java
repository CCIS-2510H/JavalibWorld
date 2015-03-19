package javalib.worldimages;

import java.awt.*;
import java.awt.geom.*;

/**
 * <p>Copyright 2012 Viera K. Proulx</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The class to represent an image that came from a .png file and is to be drawn
 * by the world when drawing on its <code>Canvas</code>.
 * </p>
 * 
 * @author Viera K. Proulx
 * @since February 4 2012
 */
public class FromFileImage extends WorldImage {

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
     * @param pinhole
     *            the pinhole location (the center) for this image
     * @param fileName
     *            the file name for the image source
     */
    public FromFileImage(String fileName) {
        super();

        // determine how to read the file name
        // then read the image, or verify that it has been read already
        this.imread = new ImageMaker(fileName);

        /*
         * this.pinhole.x = pinhole.x - (this.imread.width / 2); this.pinhole.y
         * = pinhole.y - (this.imread.height / 2);
         * 
         * 
         * System.out.println("Image width = " + this.imread.width);
         * System.out.println("Image height = " + this.imread.height);
         * 
         * System.out.println("pinhole.x = " + this.pinhole.x);
         * System.out.println("pinhole.y = " + this.pinhole.y);
         */

        // initialize the filename and the affine transform
        this.fileName = fileName;
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    public void draw(Graphics2D g) {
        g.translate(-this.imread.width / 2, -this.imread.height / 2);

        g.drawRenderedImage(this.imread.image, new AffineTransform());

        // Reset to original position
        g.translate(this.imread.width / 2, this.imread.height / 2);
    }

    /**
     * <p>
     * Provide a method for comparing two images constructed from image files to
     * be used by the <em>tester</em> library.
     * </p>
     * 
     * <p>
     * This requires the import of the tester library. The comparison involves
     * only the file names and the location of the pinholes.
     * </p>
     */
    public boolean same(FromFileImage that) {
        return this.fileName.equals(that.fileName);
    }

    /**
     * Produce the width of this image
     * 
     * @return the width of this image
     */
    public int getWidth() {
        return this.imread.width;
    }

    /**
     * Produce the height of this image
     * 
     * @return the height of this image
     */
    public int getHeight() {
        return this.imread.height;
    }

    /**
     * Produce a <code>String</code> representation of this from-file image
     */
    public String toString() {
        return "new FromFileImage(this.fileName = " + this.fileName + ")\n";
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
        return classNameString(indent, "FromFileImage") + indent
                + "this.fileName = " + this.fileName + ")\n";
    }

    /**
     * Is this <code>FromFileImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof FromFileImage) {
            FromFileImage that = (FromFileImage) o;
            return this.same(that);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.fileName.hashCode();
    }
}