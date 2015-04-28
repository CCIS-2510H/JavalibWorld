package javalib.worldimages;

import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

abstract class WorldImageIterator implements Iterator<WorldImage> {
    Deque<WorldImage> worklist;
    Deque<AffineTransform> txs;
    Deque<Boolean> expanded;
    
    public WorldImage curImg;
    public AffineTransform curTx;
    
    private WorldImage root;
    private AffineTransform rootTx;
    
    public WorldImageIterator(WorldImage src) {
        this(src, new AffineTransform());
    }
    public WorldImageIterator(WorldImage src, AffineTransform init) {
        this.root = src;
        this.rootTx = init;
        this.reset();
    }
    public void reset() {
        this.expanded = new LinkedList<Boolean>();
        this.txs = new LinkedList<AffineTransform>();
        this.worklist = new LinkedList<WorldImage>();
        this.worklist.add(this.root);
        this.txs.add(this.rootTx);
        this.expanded.add(false);
    }

    abstract void expandChild(WorldImage kid);
    @Override
    public boolean hasNext() {
        return !this.worklist.isEmpty();
    }

    @Override
    public WorldImage next() {
        while (!this.expanded.peekFirst()) {
            this.expanded.removeFirst();
            this.expandChild(this.worklist.peek());
        }
        this.curImg = this.worklist.pop();
        this.curTx = this.txs.pop();
        this.expanded.pop();
        return this.curImg;
    }
}
