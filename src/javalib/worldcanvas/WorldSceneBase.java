package javalib.worldcanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

/**
 * Class representing the common functionality (drawing, width, height) of the
 * various types of <code>WorldScene</code>s
 * 
 * @author eric
 * 
 */
public abstract class WorldSceneBase {

    /** width of the scene */
    public int width;

    /** height of the scene */
    public int height;

    protected interface IList<T> extends Iterable<T> {
        Cons<T> add(T val);
    }

    protected class Empty<T> implements IList<T> {
        public Cons<T> add(T val) {
            return new Cons<T>(val, this);
        }

        public Iterator<T> iterator() {
            return new IListIterator<T>(this);
        }
    }

    protected class Cons<T> implements IList<T> {
        T first;
        IList<T> rest;

        Cons(T first, IList<T> rest) {
            this.first = first;
            this.rest = rest;
        }

        public Cons<T> add(T val) {
            return new Cons<T>(val, this);
        }

        public Iterator<T> iterator() {
            return new IListIterator<T>(this);
        }
    }

    protected class IListIterator<T> implements Iterator<T> {
        IList<T> source;

        IListIterator(IList<T> source) {
            this.source = source;
        }

        public boolean hasNext() {
            return this.source instanceof Cons;
        }

        public T next() {
            Cons<T> s = (Cons<T>) this.source;
            this.source = s.rest;
            return s.first;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    protected IList<PlaceImage> imgs;
    protected Deque<PlaceImage> revImgs;

    protected WorldSceneBase(int width, int height) {
        this.width = width;
        this.height = height;
        this.imgs = new Empty<PlaceImage>();
        this.imgs = this.imgs.add(new PlaceImage(new RectangleImage(width,
                height, OutlineMode.OUTLINE, Color.black), width / 2,
                height / 2));
        this.revImgs = null;
    }

    protected WorldSceneBase(int width, int height, IList<PlaceImage> imgs) {
        this.width = width;
        this.height = height;
        this.imgs = imgs;
        this.revImgs = null;
    }

    protected void draw(Graphics2D g) {
        this.revImagesIfNeeded();
        for (PlaceImage i : revImgs) {
            g.translate(i.x, i.y);
            i.img.drawStackless(g);
            g.translate(-i.x, -i.y);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldSceneBase)) return false;
        WorldSceneBase other = (WorldSceneBase)obj;

        if (this.width != other.width || this.height != other.height) return false;

        if (this.width == 0 || this.height == 0) return true;

        BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D1 = image1.createGraphics();
        this.draw(graphics2D1);
        BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D2 = image2.createGraphics();
        other.draw(graphics2D2);


        int[] pix1 = new int[width * height];
        int[] pix2 = new int[width * height];
        PixelGrabber pg1 = new PixelGrabber(image1, 0, 0, width, height, pix1, 0, width);
        PixelGrabber pg2 = new PixelGrabber(image2, 0, 0, width, height, pix2, 0, width);

        while (true) {
            try {
                pg1.grabPixels();
                pg2.grabPixels();
                return Arrays.equals(pix1, pix2);
            }
            catch (InterruptedException e) {
                break;
            }
        }
        return false;
    }

    private void revImagesIfNeeded() {
        if (this.revImgs == null) {
            revImgs = new ArrayDeque<PlaceImage>();
            for (PlaceImage i : this.imgs) {
                this.revImgs.push(i);
            }
        }
    }

    public StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
        revImagesIfNeeded();
        sb.append("new ").append(this.getClass().getSimpleName()).append("(){")
          .append("this.width = ").append(this.width).append(", ")
          .append("this.height = ").append(this.height);
        int count = 0;
        for (PlaceImage i : this.revImgs) {
            sb.append(",\n").append(linePrefix + "  ").append("[").append(count).append("] = PlaceImage(");
            i.toIndentedString(sb, linePrefix + "    ", indent);
            sb.append(")");
            count++;
        }
        sb.append("\n").append(linePrefix).append(")}");
        return sb;
    }
    protected class PlaceImage {
        WorldImage img;
        int x, y;

        public PlaceImage(WorldImage i, int x, int y) {
            this.img = i;
            this.x = x;
            this.y = y;
        }
        StringBuilder toIndentedString(StringBuilder sb, String linePrefix, int indent) {
            sb.append("this.x = ").append(this.x).append(", ");
            sb.append("this.y = ").append(this.x).append(",\n");
            sb.append(linePrefix);
            return this.img.toIndentedString(sb, linePrefix + "  ", indent);
        }
    }
}
