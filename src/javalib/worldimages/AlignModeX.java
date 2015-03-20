package javalib.worldimages;

public enum AlignModeX {
    LEFT, RIGHT, MIDDLE, CENTER, PINHOLE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static AlignModeX fromString(String name) {
        return AlignModeX.valueOf(name.toUpperCase());
    }
}
