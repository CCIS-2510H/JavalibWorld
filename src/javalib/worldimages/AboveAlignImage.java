package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

public class AboveAlignImage extends WorldImage {
    WorldImage im1, im2;
    AlignModeX mode;

    public AboveAlignImage(AlignModeX mode, WorldImage im1, WorldImage... ims) {
        super(new Posn(0, 0), Color.BLACK);
        this.mode = mode;
        this.im1 = im1;
        if (ims.length == 1) {
            im2 = ims[0];
        } else if (ims.length > 1) {
            WorldImage[] images = new WorldImage[ims.length - 1];
            System.arraycopy(ims, 1, images, 0, images.length);
            im2 = new AboveAlignImage(mode, ims[0], images);
        }
    }

    public AboveAlignImage(String mode, WorldImage im1, WorldImage... ims) {
        this(AlignModeX.fromString(mode), im1, ims);
    }

    @Override
    public void draw(Graphics2D g) {
        if (color == null)
            color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(color);

        // Save the old transform state
        AffineTransform old = g.getTransform();

        // draw the objects
        if (this.im2 == null) {
            this.im1.draw(g);
        } else {
            int x = xMoveDist();
            g.translate(0, -(this.im2.getHeight() / 2));
            this.im1.draw(g);
            g.translate(x, (this.im2.getHeight() / 2) + this.im1.getHeight() / 2);
            this.im2.draw(g);
        }

        // Reset the transformation matrix
        g.setTransform(old);

        // reset the original paint
        g.setPaint(oldPaint);
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
        }
        return 0;
    }

    @Override
    public WorldImage getMovedImage(int dx, int dy) {
        throw new UnsupportedOperationException(
                "getMovedImage not currently supported");
    }

    @Override
    public WorldImage getMovedTo(Posn p) {
        throw new UnsupportedOperationException(
                "getMovedTo not currently supported");
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
        return classNameString(indent, "AboveAlignImage")
                + pinholeString(indent, this.pinhole) + ")\n" + indent
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
    public boolean same(AboveAlignImage that) {
        return this.pinhole.x == that.pinhole.x
                && this.pinhole.y == that.pinhole.y
                && this.mode == that.mode
                && this.im1.equals(that.im1)
                && ((this.im2 == null && that.im2 == null) || (this.im2 != null
                        && that.im2 != null && this.im2.equals(that.im2)));
    }

    /**
     * Is this <code>FromFileImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof AboveAlignImage) {
            AboveAlignImage that = (AboveAlignImage) o;
            return this.same(that);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.pinhole.x + this.pinhole.y + this.color.hashCode()
                + this.mode.hashCode();
    }

}
