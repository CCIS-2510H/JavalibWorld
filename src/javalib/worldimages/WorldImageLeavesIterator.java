package javalib.worldimages;

import java.awt.geom.AffineTransform;

class WorldImageLeavesIterator extends WorldImageIterator {
    public WorldImageLeavesIterator(WorldImage src) {
        super(src);
    }
    WorldImageLeavesIterator(WorldImage src, AffineTransform tx) {
        super(src, tx);
    }
    @Override
    void expandChild(WorldImage kid) {
        if (kid.numKids() == 0) {
            this.expanded.push(true);
        } else {
            this.worklist.pop();
            AffineTransform tx = this.txs.pop();
            for (int i = kid.numKids() - 1; i >= 0; i--) {
                this.worklist.push(kid.getKid(i));
                this.expanded.push(false);
                AffineTransform newTx = new AffineTransform(tx);
                newTx.concatenate(kid.getTransform(i));
                this.txs.push(newTx);
            }
        }
    }
}
