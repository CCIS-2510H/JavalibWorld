package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public final class AboveAlignImage extends AboveAlignImageBase {
    
    public AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage... ims) {
        super(mode, im1, ims);
    }

    public AboveAlignImage(String mode, WorldImage im1, WorldImage... ims) {
        super(mode, im1, ims);
    }
}

class AboveAlignImageBase extends WorldImage {
    WorldImage im1, im2;
    AlignModeX mode;

    public AboveAlignImageBase(AlignModeX mode, WorldImage im1,
            WorldImage... ims) {
        super();
        this.mode = mode;
        this.im1 = im1;
        if (ims.length == 1) {
            im2 = ims[0];
        } else if (ims.length > 1) {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            im2 = new AboveAlignImageBase(mode, ims[0], images);
        }
    }

    public AboveAlignImageBase(String mode, WorldImage im1, WorldImage... ims) {
        this(AlignModeX.fromString(mode), im1, ims);
    }

    @Override
    protected BoundingBox getBB(AffineTransform t) {
        AffineTransform temp = new AffineTransform(t);
        temp.translate(0, -this.im2.getHeight() / 2);
        BoundingBox bb1 = this.im1.getBB(temp);
        temp.translate(xMoveDist(),
                (this.im1.getHeight() + this.im2.getHeight()) / 2);
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
            int x = xMoveDist();
            g.translate(0, -(this.im2.getHeight() / 2));
            this.im1.draw(g);
            g.translate(x, (this.im2.getHeight() / 2)
                    + (this.im1.getHeight() / 2));
            this.im2.draw(g);
        }

        // Reset the transformation matrix
        g.setTransform(old);
    }

    private int xMoveDist() {
        if (this.mode != AlignModeX.CENTER && this.mode != AlignModeX.MIDDLE) {
            int w1 = this.im1.getWidth();
            int w2 = this.im2.getWidth();
            if (this.mode == AlignModeX.LEFT) {
                return (w2 - w1) / 2;
            } else if (this.mode == AlignModeX.RIGHT) {
                return (w1 - w2) / 2;
            }
        } else if (this.mode == AlignModeX.PINHOLE) {
            return (this.im2.pinhole.x - this.im1.pinhole.x) / 2;
        }
        return 0;
    }

    @Override
    public int getWidth() {
        int w = this.im1.getWidth();
        if (this.im2 != null) {
            w = Math.max(w, this.im2.getWidth());
        }
        return w;
    }

    @Override
    public int getHeight() {
        int h = this.im1.getHeight();
        if (this.im2 != null) {
            h += this.im2.getHeight();
        }
        return h;
    }

    @Override
    public String toIndentedString(String indent) {
        indent = indent + "  ";
        return classNameString(indent, "AboveAlignImage") + indent
                + "this.mode = " + this.mode + ")\n";
    }

    /**
     * <p>
     * Provide a method for comparing two images constructed from
     * BesideAlignImages.
     * </p>
     */
    public boolean same(AboveAlignImageBase that) {
        return this.mode == that.mode
                && this.im1.equals(that.im1)
                && ((this.im2 == null && that.im2 == null) || (this.im2 != null
                        && that.im2 != null && this.im2.equals(that.im2)));
    }

    /**
     * Is this <code>FromFileImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof AboveAlignImageBase) {
            AboveAlignImageBase that = (AboveAlignImageBase) o;
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

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new AboveAlignImage(this.mode, this.im1, this.im2);
        i.pinhole = p;
        return i;
    }

}
