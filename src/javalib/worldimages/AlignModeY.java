package javalib.worldimages;

public enum AlignModeY {
    BOTTOM, TOP, MIDDLE, CENTER, PINHOLE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static AlignModeY fromString(String name) {
        return AlignModeY.valueOf(name.toUpperCase());
    }
}
