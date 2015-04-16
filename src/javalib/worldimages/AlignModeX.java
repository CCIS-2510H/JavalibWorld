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
        return AlignModeX.valueOf(name.toUpperCase());
    }
}
