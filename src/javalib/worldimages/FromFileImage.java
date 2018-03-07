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
    protected void drawStackUnsafe(Graphics2D g) {
        // Adjust the position of the frame
        g.translate(-(this.imread.width / 2.0), -(this.imread.height / 2.0));

        g.drawRenderedImage(this.imread.image, new AffineTransform());

        // Reset to original position
        g.translate((this.imread.width / 2.0), (this.imread.height / 2.0));
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.drawStackUnsafe(g);
    }

    @Override
    public double getWidth() {
        return this.imread.width;
    }

    @Override
    public double getHeight() {
        return this.imread.height;
    }

    public Color getColorAt(int x, int y) {
        return this.imread.getColorPixel(x, y);
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        double w = this.getWidth();
        double h = this.getHeight();
        Point2D tl = t.transform(new Point.Double(-w / 2, -h / 2), null);
        Point2D br = t.transform(new Point.Double(w / 2, h / 2), null);
        return new BoundingBox(tl.getX(), tl.getY(), br.getX(), br.getY());
    }

    @Override
    protected StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack) {
        sb = sb.append("new ").append(this.simpleName()).append("(")
               .append("this.fileName = \"")
               .append(this.fileName.replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
        if (this.pinhole.x != 0 || this.pinhole.y != 0)
            stack.push(new FieldsWLItem(this.pinhole));
        else
            sb = sb.append(")");
        return sb;
    }

    @Override
    protected boolean equalsStacksafe(WorldImage other,
                                      Stack<WorldImage> worklistThis, Stack<WorldImage> worklistThat) {
        if (other instanceof FromFileImage) {
            FromFileImage that = (FromFileImage)other;
            return this.fileName.equals(that.fileName) && this.pinhole.equals(that.pinhole);
        }
        return false;
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