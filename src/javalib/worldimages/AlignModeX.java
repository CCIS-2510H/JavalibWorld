package javalib.worldimages;

/**
 * An enum representing alignment states along the X axis an overlaid WorldImage
 * can have
 * 
 * @author Eric Kelly
 * @since April 4, 2015
 * 
 */
public enum AlignModeX {
    LEFT, RIGHT, CENTER, PINHOLE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * 
     * @return The enum associated with the given <code>name</code>
     */
    public static AlignModeX fromString(String name) {
        switch(name.toUpperCase()) {
            case "LEFT": return LEFT;
            case "RIGHT": return RIGHT;
            case "CENTER": return CENTER;
            case "PINHOLE": return PINHOLE;
            default: throw new IllegalArgumentException("Unknown AlignModeX: " + name);
        }
    }
}
