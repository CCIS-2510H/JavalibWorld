package javalib.worldimages;

public final class BesideImage extends BesideAlignImageBase {

    public BesideImage(WorldImage im1, WorldImage... ims) {
        super(AlignModeY.CENTER, im1, ims);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new BesideImage(this.im1, this.im2);
        i.pinhole = p;
        return i;
    }
}
