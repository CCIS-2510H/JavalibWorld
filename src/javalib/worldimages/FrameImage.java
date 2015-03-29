package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public final class FrameImage extends RectangleImageBase {
    public WorldImage img;

    public FrameImage(WorldImage img, Color color) {
        super((int) img.getBB().getWidth(), (int) img.getBB().getHeight(),
                OutlineMode.OUTLINE, color);
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
        Stroke oldStroke = g.getStroke();
        // draw the object
        this.img.draw(g);
        // set the paint to the given color
        g.setPaint(this.color);
        // Adjust the position of the frame
        //g.translate(this.img.pinhole.x, this.img.pinhole.y);
        // Draw the frame
        BoundingBox bb = this.img.getBB();
        g.draw(new Rectangle2D.Double(bb.tlx, bb.tly, bb.getWidth(), bb
                .getHeight()));
        // Reset the position of the frame
        //g.translate(-this.img.pinhole.x, -this.img.pinhole.y);
        // reset the original paint
        g.setPaint(oldPaint);
        g.setStroke(oldStroke);
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

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new FrameImage(this.img, this.color);
        i.pinhole = p;
        return i;
    }
}
