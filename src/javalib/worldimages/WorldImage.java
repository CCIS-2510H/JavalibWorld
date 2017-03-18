package javalib.worldimages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
     * given in the image's own coordinate system with it's own center at the
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
    abstract public void draw(Graphics2D g);
    
    abstract protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs);
    
    public void drawStackless(Graphics2D g) {
        if (this.depth < 6000) {
            this.draw(g);
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

    protected static class ImagePair {
        WorldImage one, two;
        ImagePair(WorldImage one, WorldImage two) {
            this.one = one;
            this.two = two;
        }
    }

    /**
     * A helper method for the equals method below, this method implements extensional equality
     * via a worklist.  It collaborates with the abstract method
     * {@link WorldImage#equalsStacksafe(WorldImage, Stack)} below, which each image
     * class must implement.
     *
     * @param other   The image to be compared
     * @return Whether the two images are extensionally equal
     */
    protected final boolean equalsStacksafe(WorldImage other) {
        Stack<ImagePair> imagePairs = new Stack<ImagePair>();
        imagePairs.push(new ImagePair(this, other));
        while (!imagePairs.empty()) {
            ImagePair p = imagePairs.pop();
            if (p.one == p.two) continue; // fast success path
            if (!(p.one.equalsStacksafe(p.two, imagePairs)))
                return false;
        }
        return true;
    }

    /**
     * This helper method implements the recursive part of extensional equality checking.
     * Each image class must check its local fields and push any contained images onto the worklist
     * to be checked later.
     *
     * @param other The image to be compared
     * @param worklist The worklist onto which child images are pushed for later comparison
     * @return If the image types do not match, or the local fields are not equal, returns false.
     *         Otherwise, returns true.  Any recursive checking of subimages will be done
     *         by the {@link WorldImage#equalsStacksafe(WorldImage)} method.
     */
    protected abstract boolean equalsStacksafe(WorldImage other, Stack<ImagePair> worklist);

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

    /**
     * Produce a <code>String</code> that represents this image, indented by the
     * given <code>indent</code>
     * 
     * @param indent
     *            -- the given prefix representing the desired indentation
     * @return the <code>String</code> representation of this image
     */
    abstract public String toIndentedString(String indent);

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

    /**
     * Produce the <code>String</code> that represents the class name.
     * 
     * <p>
     * Helper method for {@link #toIndentedString(String)}.
     * </p>
     * 
     * @param indent
     *            -- the indent amount of the string
     * @param o
     *            -- the object that needs its class printed out
     * @return the string representation of the class indented by the proper
     *         amount
     */
    protected static String classNameString(String indent, WorldImage o) {
        return "\n" + indent + className(o);
    }

    /**
     * Produce the <code>String</code> that represents the class name.
     * 
     * @param o
     *            -- the object that needs its class printed out
     * @return the string representation of the class name
     */
    protected static String className(WorldImage o) {
        return "new " + o.getClass().getSimpleName() + "(";
    }
}