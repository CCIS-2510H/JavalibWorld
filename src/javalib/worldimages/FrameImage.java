package javalib.worldimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public class FrameImage extends RectangleImage {
    public WorldImage img;

    protected FrameImage(Posn pinhole, WorldImage img, Color color) {
        super(pinhole, img.getWidth(), img.getHeight(), OutlineMode.OUTLINE, color);
        this.img = img;
    }
    
    public FrameImage(WorldImage img) {
        this(new Posn(0, 0), img, Color.black);
    }
    
    public FrameImage(WorldImage img, Color c) {
        this(new Posn(0, 0), img, c);
    }
    
    /**
     * Draw this image in the provided <code>Graphics2D</code> context.
     * 
     * @param g
     *            the provided <code>Graphics2D</code> context
     */
    @Override
    public void drawAt(Graphics2D g, int x, int y) {
        if (this.width <= 0)
            return;
        if (this.height <= 0)
            return;
        if (this.color == null)
            this.color = new Color(0, 0, 0);

        // save the current paint
        Paint oldPaint = g.getPaint();
        // set the paint to the given color
        g.setPaint(this.color);
        // draw the object
        this.img.drawAt(g, x, y);
        g.draw(new Rectangle2D.Double(x - this.width / 2, y - this.height
                / 2, this.width, this.height));
        // reset the original paint
        g.setPaint(oldPaint);
    }

    /**
     * Produce a <code>String</code> representation of this rectangle image
     */
    public String toString() {
        return "new FrameImage(this.pinhole = (" + this.pinhole.x + ", "
                + this.pinhole.y + "), \nthis.color = " + this.color.toString()
                + "\nthis.width = " + width + ", this.height = " + height
                + ")\n";
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
                + pinholeString(indent, this.pinhole)
                + colorString(indent, this.color) + "\n" + indent
                + "this.width = " + width + ", this.height = " + height + ")\n";
    }

    /**
     * Is this <code>FrameeImage</code> same as the given object?
     */
    public boolean equals(Object o) {
        if (o instanceof FrameImage) {
            FrameImage that = (FrameImage) o;
            return this.pinhole.x == that.pinhole.x
                    && this.pinhole.y == that.pinhole.y
                    && this.width == that.width && this.height == that.height
                    && this.color.equals(that.color)
                    && this.img.equals(that.img);
        } else
            return false;
    }

    /**
     * The hashCode to match the equals method
     */
    public int hashCode() {
        return this.pinhole.x + this.pinhole.y + this.color.hashCode()
                + this.width + this.height;
    }
}
