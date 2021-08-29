package javalib.worldimages;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

/**
 * <p>Copyright 2015 Ben Lerner</p>
 * <p>This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)</p>
 */

/**
 * <p>
 * The parent class for all images drawn by the world when drawing on its
 * <code>Canvas</code>.
 * </p>
 * 
 * @author Eric Kelly
 * @author Ben Lerner
 * @since April 4 2015
 */
public abstract class WorldImage {
    
    /**
     * the pinhole of the image. When this image gets overlaid on top of another
     * image, they will be aligned on their respective pinholes (unless
     * otherwise specified)
     */
    public Posn pinhole;
    /**
     * This can't be a field on the object itself, or else the presence or absence of a cached
     * bounding box might affect the tester library deciding if two objects are the same or not
     */
    static WeakHashMap<WorldImage, BoundingBox> bbCache;
    
    /** this describes how deeply nested the image object is constructed */
    int depth;
    
    /** How deeply nested are the image objects in this image? */
    public int getImageNestingDepth() { return this.depth; }

    protected WorldImage(int depth) {
        this(new Posn(0, 0), depth);
    }

    /**
     * Every image has a pinhole (<code>Posn</code>)
     * 
     * @param pinhole
     *            -- the pinhole location for this image (using the internal
     *            coordinate system of the image. 0, 0 is the center of the
     *            image and the default pinhole location). By default it is the
     *            origin
     */
    protected WorldImage(Posn pinhole, int depth) {
        this.pinhole = pinhole;
        this.depth = depth;
        if (WorldImage.bbCache == null)
            WorldImage.bbCache = new WeakHashMap<WorldImage, BoundingBox>();
    }

    abstract int numKids();
    abstract WorldImage getKid(int i);
    abstract AffineTransform getTransform(int i);
    /**
     * Get the Bounding Box of the image
     * 
     * @return The tight bounding box of the image
     */
    public BoundingBox getBB() {
        BoundingBox ret = WorldImage.bbCache.get(this);
        if (ret == null) {
            ret = this.getBB(new AffineTransform());
            WorldImage.bbCache.put(this, ret);
        }
        return ret;
    }
    protected BoundingBox getBB(final AffineTransform tx) {
        try {
            if (tx.isIdentity() && WorldImage.bbCache.containsKey(this)) { 
                return this.getBB(); 
            }
            else { 
                return this.getBBHelp(tx); 
            }
        } catch (StackOverflowError e) {
            final WorldImage img = this;
            return (new Callable<BoundingBox>() {
                public BoundingBox call() {
                    return img.getBB(tx);
                }
            }).call();
        }
//        WorldImageLeavesIterator iter = new WorldImageLeavesIterator(this, tx);
//        BoundingBox acc = null;
//        while (iter.hasNext()) {
//            iter.next();
//            BoundingBox newBB = iter.curImg.getBBHelp(iter.curTx);
//            if (acc == null) { acc = new BoundingBox(newBB); }
//            else { acc.combineWith(newBB); }
//        }
//        return acc;
    }

    /**
     * Get the Bounding Box of the image, calculated by combining the operations
     * done on the image (represented by the passed in AffineTransform)
     * 
     * @param t
     *            -- Operations done to transform the image
     * @return The Bounding box of the image
     */
    protected abstract BoundingBox getBBHelp(AffineTransform t);

    /**
     * Transform a Posn by the operations as given by the AffineTransform
     * 
     * @param t
     *            -- Operations on the point
     * @param p
     *            -- The point to transform
     * @return A Point2D representing the transformation of <code>p</code> by
     *         <code>t</code>
     */
    protected static Point2D transformPosn(AffineTransform t, Posn p) {
        return transformPosn(t, p.x, p.y);
    }

    /**
     * Transform a DPosn by the operations as given by the AffineTransform
     * 
     * @param t
     *            -- Operations on the point
     * @param p
     *            -- The point to transform
     * @return A Point2D representing the transformation of <code>p</code> by
     *         <code>t</code>
     */
    protected static Point2D transformPosn(AffineTransform t, DPosn p) {
        return transformPosn(t, p.x, p.y);
    }

    /**
     * Transform x and y coordinates by the operations as given by the
     * AffineTransform
     * 
     * @param t
     *            -- Operations on the point
     * @param x
     *            -- The x coordinate to transform
     * @param y
     *            -- The y coordinate to transform
     * @return A Point2D representing the transformation of <code>x</code> and
     *         <code>y</code> by <code>t</code>
     */
    protected static Point2D transformPosn(AffineTransform t, double x, double y) {
        Point2D point = new Point2D.Double(x, y);
        return t.transform(point, null);
    }

    /**
     * Move the image's pinhole to the point represented by <code>p</code>,
     * given in the image's own coordinate system with its own center at the
     * origin
     * 
     * @param p
     *            -- The new location of the pinhole
     * @return a new image with an adjusted pinhole
     */
    public abstract WorldImage movePinholeTo(Posn p);

    /**
     * Move the image's pinhole by <code>dx</code> in the x direction and
     * <code>dy</code> in the y direction.
     * 
     * @param dx
     *            -- x direction pinhole movement
     * @param dy
     *            -- y direction pinhole movement
     * @return a new image with an adjusted pinhole
     */
    public WorldImage movePinhole(double dx, double dy) {
        return movePinholeTo(new Posn((int) Math.round(this.pinhole.x + dx),
                (int) Math.round(this.pinhole.y + dy)));
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            -- the provided <code>Graphics2D</code> context
     */
    abstract protected void drawStackUnsafe(Graphics2D g);
    
    abstract protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs);
    
    public final void draw(Graphics2D g) {
        if (this.depth < 1000) {
            this.drawStackUnsafe(g);
        } else {
            Stack<WorldImage> images = new Stack<WorldImage>();
            Stack<AffineTransform> txs = new Stack<AffineTransform>();
            AffineTransform initTx = g.getTransform();
            images.push(this);
            txs.push(initTx);
            while (!images.isEmpty()) {
                WorldImage nextI = images.pop();
                AffineTransform nextT = txs.pop();
                g.setTransform(nextT);
                nextI.drawStacksafe(g, images, txs);
            }
            g.setTransform(initTx);
        }
    }

    /**
     * Saves the current scene to a PNG file of the specified name
     * @param filename -- where to save the image
     * @return The filename, if the image was successfully saved, or an error message
     */
    public final String saveImage(String filename) {
        try {
            BufferedImage img = new BufferedImage((int)this.getWidth(), (int)this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setTransform(AffineTransform.getTranslateInstance(this.getWidth() / 2, this.getHeight() / 2));
            this.draw(g);
            if (ImageIO.write(img, "png", new File(filename))){
                return filename;
            }
            return "Could not save file";
        } catch (Exception e) {
            return "Error saving file: " + e.getMessage();
        }
    }

    /**
     * A helper method for the equals method below, this method implements extensional equality
     * via a worklist algorithm.  It collaborates with the abstract method
     * {@link WorldImage#equalsStacksafe(WorldImage, Stack, Stack)} below, which each image
     * class must implement.
     *
     * @param that   The image to be compared
     * @return Whether the two images are extensionally equal
     */
    protected final boolean equalsStacksafe(WorldImage that) {
        Stack<WorldImage> worklistThis = new Stack<WorldImage>();
        Stack<WorldImage> worklistThat = new Stack<WorldImage>();
        worklistThis.push(this);
        worklistThat.push(that);
        while (!worklistThis.empty()) {
            WorldImage one = worklistThis.pop();
            WorldImage two = worklistThat.pop();
            if (one == two) continue; // fast success path
            if (!(one.equalsStacksafe(two, worklistThis, worklistThat)))
                return false;
        }
        return true;
    }

    /**
     * This helper method implements the recursive part of extensional equality checking.
     * Each image class must check its local fields and push any contained images onto the worklists
     * to be checked later.  The two stacks must be manipulated in lockstep: it is cheaper to have
     * two stacks whose structures must be parallel than to have one stack where we allocate an
     * "ImagePair" object to keep the items in tandem, since there may be many such allocations.
     *
     * @param other The image to be compared
     * @param worklistThis The worklist onto which child images of {@code this} are pushed for later comparison
     * @param worklistThat The worklist onto which child images of {@code other} are pushed for later comparison
     * @return If the image types do not match, or the local fields are not equal, returns false.
     *         Otherwise, returns true.  Any recursive checking of subimages will be done
     *         by the {@link WorldImage#equalsStacksafe(WorldImage)} method.
     */
    protected abstract boolean equalsStacksafe(WorldImage other, Stack<WorldImage> worklistThis,
                                               Stack<WorldImage> worklistThat);

    /**
     * Provides extensional equality on WorldImages.  Each image subclass must override hashcode
     * to be compatible.
     *
     * This function is stack-safe: regardless of the depth of the image, it will not cause
     * a StackOverflow.  It relies on {@link WorldImage#equalsStacksafe(WorldImage)} by default.
     *
     * @param obj The object to be compared
     * @return Whether the object is extensionally equal to the current image
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof WorldImage) && this.equalsStacksafe((WorldImage)obj);
    }

    /**
     * <p>
     * A convenience method that allows us to combine several images into one on
     * top of this image without the need to explicitly construct
     * <code>{@link OverlayImage OverlayImages}</code>
     * </p>
     * <p>
     * The pinhole is placed in the middle of all overlayed images.
     * </p>
     * 
     * @param args
     *            -- an arbitrarily long list of
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
            image = new OverlayImage(image, args[i]);
        }

        return image;
    }

    /**
     * Produce the width of this image
     * 
     * @return the width of this image
     */
    abstract public double getWidth();

    /**
     * Produce the height of this image
     * 
     * @return the height of this image
     */
    abstract public double getHeight();

    @Override
    public String toString() {
        return this.toIndentedString(new StringBuilder(), "", 0).toString();
    }

    /**
     * Builds up a textual rendering of this image, with every line (except the first)
     * prefixed by the given {@code linePrefix}, and all indentations nest by {@code indent} spaces.
     * (The first line is not prefixed, since the client of this method might want to include the
     * results on an existing, partial line.)
     *
     * This method is stack-safe: it will use a constant amount of stack, unless a nested
     * object has a custom {@link Object#toString()} override that itself overflows the stack.
     *
     * @param sb The StringBuilder into which the results should be built
     * @param linePrefix A string to be prepended at the start of every new line of text
     * @param indent How many spaces each nested indentation should be
     * @return the given {@link StringBuilder}, with the rendering of this image appended to
     *          its original contents
     */
    public StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
        return ImagePrinter.makeString(this, sb, linePrefix, indent);
    }

    /**
     * A template helper method for {@link WorldImage#toIndentedString(StringBuilder, String, int)}
     * and {@link ImagePrinter#makeString(Object, StringBuilder, String, int)}.
     * Subclasses of {@link WorldImage} must implement this method, to produce output of the form
     *
     * <pre>
     *   new MyImageClass(this.easyField1 = value1, this.easyField2 = value2,
     *     this.recursiveField = recursiveValue2
     *   )
     * </pre>
     *
     * The "easy fields" above are primitive values, and can be rendered easily in constant stack
     * space; those values should be appended to the given {@link StringBuilder} by the subclass
     * directly.  The "recursive" fields above are objects that may contain arbitrary values, and
     * so cannot be rendered in constant stack space.  Instead, the subclass should push its
     * nested objects onto the supplied stack, as a {@link FieldsWLItem} containing
     * however many {@link ImageField} items are needed.  If the subclass needs to render fields on
     * multiple lines, then it should push them and any fields after them, even if they're "easy".
     * If no fields are needed, the subclass should append the closing ")" of the rendering;
     * otherwise, the {@link ImagePrinter#makeString(Object, StringBuilder, String, int)} method
     * will supply it once all nested values have been rendered.
     *
     * All implementations of this method <b>must</b> use constant stack space.
     *
     * @param sb The {@link StringBuilder} into which the rendered text should be appended
     * @param stack The {@link Stack} into which any recursive fields should be pushed,
     *              as {@link FieldsWLItem} collections
     * @return The given {@link StringBuilder}
     */
    protected abstract StringBuilder toIndentedStringHelp(StringBuilder sb, Stack<Object> stack);

    protected String simpleName() { return this.getClass().getSimpleName(); }
    /**
     * Produce the <code>String</code> that represents the color without the
     * extra narrative.
     * 
     * @param indent
     *            -- How much to indent the string
     * @param color
     *            -- the given color
     * @return the rgb representation S <code>String</code>
     */
    protected static String colorString(String indent, Color color) {
        return "\n" + indent + colorString(color);
    }

    /**
     * Produce the <code>String</code> that represents the color without the
     * extra narrative.
     * 
     * @param color
     *            -- the given color
     * @return the rgb representation S <code>String</code>
     */
    protected static String colorString(Color color) {
        String result = color.toString();
        int start = result.indexOf('[');
        result = result.substring(start, result.length());
        return "this.color = " + result;
    }

    protected static void boundsCheck(int x, int y, int width, int height) throws IndexOutOfBoundsException {
        if (x < 0 || x >= width)
            throw new IndexOutOfBoundsException(String.format("Specified x (%d) is not in range [0, %d)",
                    x, width));
        if (y < 0 || y >= height)
            throw new IndexOutOfBoundsException(String.format("Specified y (%d) is not in range [0, %d)",
                    y, height));

    }
}