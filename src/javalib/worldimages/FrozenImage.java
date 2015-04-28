package javalib.worldimages;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Stack;

public final class FrozenImage extends WorldImage {
    BufferedImage img;
    public FrozenImage(WorldImage img) {
        super(1);
        this.img = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = this.img.createGraphics();
        g.translate(img.getWidth() / 2, img.getHeight() / 2);
        img.drawStackless(g);
        g.dispose();
        this.pinhole = img.pinhole;
    }
    private FrozenImage(BufferedImage img, Posn pinhole) {
        super(pinhole, 1);
        this.img = img;
    }
    @Override
    int numKids() {
        return 0;
    }

    @Override
    WorldImage getKid(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    AffineTransform getTransform(int i) {
        throw new IllegalArgumentException("No such kid " + i);
    }

    @Override
    protected BoundingBox getBBHelp(AffineTransform t) {
        return new BoundingBox(WorldImage.transformPosn(t, 0, 0),
            WorldImage.transformPosn(t, this.getWidth(), this.getHeight()));
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        return new FrozenImage(this.img, p);
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(this.img, AffineTransform.getTranslateInstance(- this.getWidth() / 2, - this.getHeight() / 2), null);
    }

    @Override
    protected void drawStacksafe(Graphics2D g, Stack<WorldImage> images, Stack<AffineTransform> txs) {
        this.draw(g);
    }

    @Override
    public double getWidth() {
        return this.img.getWidth();
    }

    @Override
    public double getHeight() {
        return this.img.getHeight();
    }

    @Override
    public String toIndentedString(String indent) {
        return "new BufferedImage()";
    }

}
