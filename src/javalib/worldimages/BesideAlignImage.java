package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public final class BesideAlignImage extends BesideAlignImageBase {
    
    public BesideAlignImage(AlignModeY mode, WorldImage im1, WorldImage... ims) {
        super(mode, im1, ims);
    }
    
    public BesideAlignImage(String mode, WorldImage im1, WorldImage... ims) {
        super(mode, im1, ims);
    }
}

class BesideAlignImageBase extends WorldImage {
    WorldImage im1, im2;
    AlignModeY mode;

    public BesideAlignImageBase(AlignModeY mode, WorldImage im1, WorldImage... ims) {
        super();
        this.mode = mode;
        this.im1 = im1;
        if (ims.length == 1) {
            im2 = ims[0];
        } else if (ims.length > 1) {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            im2 = new BesideAlignImageBase(mode, ims[0], images);
        }
    }

    public BesideAlignImageBase(String mode, WorldImage im1, WorldImage... ims) {
        this(AlignModeY.fromString(mode), im1, ims);
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform temp = new AffineTransform(t);
        temp.translate(-this.im2.getWidth() / 2, 0);
        BoundingBox bb1 = this.im1.getBB(temp);
        temp.translate((this.im1.getWidth() + this.im2.getWidth()) / 2, yMoveDist());
        return bb1.combine(this.im2.getBB(temp));
    }
    
    @Override
    public void draw(Graphics2D g) {
        // Save the old transform state
        AffineTransform old = g.getTransform();

        // draw the objects
        if (this.im2 == null) {
            this.im1.draw(g);
        } else {
            int y = yMoveDist();
            g.translate(-(this.im2.getWidth() / 2), 0);
            this.im1.draw(g);
            g.translate((this.im2.getWidth() / 2) + this.im1.getWidth() / 2, y);
            this.im2.draw(g);
        }

        // Reset the transformation matrix
        g.setTransform(old);
    }

    private int yMoveDist() {
        if (this.mode != AlignModeY.CENTER && this.mode != AlignModeY.MIDDLE) {
            int h1 = this.im1.getHeight();
            int h2 = this.im2.getHeight();
            if (this.mode == AlignModeY.TOP) {
                return (h2 - h1) / 2;
            } else if (this.mode == AlignModeY.BOTTOM) {
                return (h1 - h2) / 2;
            }
        }
        return 0;
    }

    @Override
    public int getWidth() {
        int w = this.im1.getWidth();
        if (this.im2 != null) {
            w += this.im2.getWidth();
        }
        return w;
    }

    @Override
    public int getHeight() {
        int h = this.im1.getHeight();
        if (this.im2 != null) {
            h = Math.max(h, this.im2.getHeight());
        }
        return h;
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, "BesideAlignImage") + indent
                + "this.mode = " + this.mode + ")\n";
    }

    /**
     * <p>
     * Provide a method for comparing two images constructed from
     * BesideAlignImages to be used by the <em>tester</em> library.
     * </p>
     * 
     * <p>
     * This requires the import of the tester library.
     * </p>
     */
    public boolean same(BesideAlignImageBase that) {
        return this.mode == that.mode
                && this.im1.equals(that.im1)
                && ((this.im2 == null && that.im2 == null) || (this.im2 != null
                        && that.im2 != null && this.im2.equals(that.im2)));
    }

    /**
     * Is this <code>FromFileImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof BesideAlignImageBase) {
            BesideAlignImageBase that = (BesideAlignImageBase) o;
            return this.same(that);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.mode.hashCode();
    }

}
