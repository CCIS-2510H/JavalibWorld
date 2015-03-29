package javalib.worldimages;

public final class AboveImage extends AboveAlignImageBase {

    public AboveImage(WorldImage im1, WorldImage... ims) {
        super(AlignModeX.CENTER, im1, ims);
    }

    @Override
    public WorldImage movePinholeTo(Posn p) {
        WorldImage i = new AboveImage(this.im1, this.im2);
        i.pinhole = p;
        return i;
    }
}
