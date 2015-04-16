package javalib.worldcanvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public abstract class WorldSceneBase {
    public int width, height;

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
    protected ArrayList<PlaceImage> revImgs;

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
    }

    protected void draw(Graphics2D g) {
        this.revImagesIfNeeded();
        for (PlaceImage i : revImgs) {
            g.translate(i.x, i.y);
            i.img.draw(g);
            g.translate(-i.x, -i.y);
        }
    }

    private void revImagesIfNeeded() {
        if (this.revImgs == null) {
            this.revImgs = new ArrayList<PlaceImage>();
            for (PlaceImage i : this.imgs) {
                this.revImgs.add(i);
            }
        }
    }

    protected class PlaceImage {
        WorldImage img;
        int x, y;

        public PlaceImage(WorldImage i, int x, int y) {
            this.img = i;
            this.x = x;
            this.y = y;
        }
    }
}
