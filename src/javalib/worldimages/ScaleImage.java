package javalib.worldimages;

public final class ScaleImage extends ScaleImageXYBase {

    public ScaleImage(WorldImage img, double scale) {
        super(img, scale, scale);
    }
    
    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new ScaleImage(this.img, this.scaleX);
        i.pinhole = p;
        return i;
    }

}
