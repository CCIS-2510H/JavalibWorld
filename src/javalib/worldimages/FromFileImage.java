package javalib.worldimages;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private static final Map<String, BufferedImage> loadedImages = new HashMap<>();
    private static final Map<String, Long> modifiedTimes = new HashMap<>();

    /**
     * Construct the <code>BufferedImage</code> from the given file. The file
     * can be given as an URL, or a reference to local image file. If this image
     * has been loaded already, use the saved version.
     *
     * @param filename
     *            the file name for the desired image
     */
    private static String loadFromFile(String filename) {
        /** now we set up the image file for the user to process */
        try {
            File inputfile = new File(filename);
            String abs = inputfile.getCanonicalPath();
            long lastModTime = inputfile.lastModified();
            if (loadedImages.containsKey(abs) && modifiedTimes.containsKey(abs) &&
                    modifiedTimes.get(abs) >= lastModTime) {
                return abs;
            } else {
                BufferedImage imageSource = ImageIO.read(inputfile);
                ColorModel cmodel = imageSource.getColorModel();
                BufferedImage image = new BufferedImage(imageSource.getWidth(), imageSource.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                ColorConvertOp colorOp = new ColorConvertOp(cmodel.getColorSpace(),
                        image.getColorModel().getColorSpace(), null);
                colorOp.filter(imageSource, image);
                loadedImages.put(abs, image);
                modifiedTimes.put(abs, lastModTime);
                return abs;
            }
        } catch (IOException e) {
            System.out.println("Could not open the image file " + filename);
        }
        return null;
    }


    /** the file name for the image source */
    public final String fileName;

    /**
     * the instance of the class that handles reading of files just once set to
     * be transient, so that it is not used in comparisons by tester lib
     */
    private transient final BufferedImage image;

    private transient final long modifiedTime;

    /**
     * A full constructor for this image created from the file input
     * 
     * @param fileName
     *            -- the file name for the image source
     * @throws NullPointerException if fileName is null
     */
    public FromFileImage(String fileName) {
        this(fileName, DEFAULT_PINHOLE);
    }

    private FromFileImage(String fileName, Posn pinhole) {
        super(pinhole, 1);
        Objects.requireNonNull(fileName, "Filename cannot be null");

        String absName = loadFromFile(fileName);
        this.fileName = fileName;
        this.image = loadedImages.get(absName);
        this.modifiedTime = modifiedTimes.get(absName);
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
        g.translate(-(this.image.getWidth() / 2.0), -(this.image.getHeight() / 2.0));

        g.drawRenderedImage(this.image, new AffineTransform());

        // Reset to original position
        g.translate((this.image.getWidth() / 2.0), (this.image.getHeight() / 2.0));
    }
    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.drawStackUnsafe(g);
    }

    @Override
    public double getWidth() {
        return this.image.getWidth();
    }

    @Override
    public double getHeight() {
        return this.image.getHeight();
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
        WorldImage.boundsCheck(x, y, this.image.getWidth(), this.image.getHeight());
        int[] ans = new int[4];
        this.image.getRaster().getPixel(x, y, ans);
        return new Color(ans[0], ans[1], ans[2], ans[3]);
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
            return this.fileName.equals(that.fileName)
                    && this.modifiedTime == that.modifiedTime
                    && this.pinhole.equals(that.pinhole);
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
        Objects.requireNonNull(p, "Pinhole position cannot be null");
        return new FromFileImage(this.fileName, p);
    }
}