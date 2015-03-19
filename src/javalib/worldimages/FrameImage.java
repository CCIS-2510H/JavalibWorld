package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public class FrameImage extends RectangleImage {
    public WorldImage img;

    public FrameImage(WorldImage img, Color color) {
        super(img.getWidth(), img.getHeight(), OutlineMode.OUTLINE, color);
        this.img = img;
    }

    public FrameImage(WorldImage img) {
        this(img, Color.black);
    }

    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    @Override
    public void draw(Graphics2D g) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // draw the object
        this.img.draw(g);
        // set the paint to the given color
        g.setPaint(this.color);
        g.draw(new Rectangle2D.Double(Math.ceil(-(this.width / 2.0)), Math
                .ceil(-(this.height / 2.0)), this.width, this.height));
        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return "new FrameImage(this.img = " + this.img.toString() + ")\n";
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
        return classNameString(indent, "FrameImage")
                + colorString(indent, this.color) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    /**
     * Is this <code>FrameImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof FrameImage) {
            FrameImage that = (FrameImage) o;
            return this.width == that.width && this.height == that.height
                    && this.color.equals(that.color)
                    && this.img.equals(that.img);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.color.hashCode() + this.width + this.height;
    }
}
