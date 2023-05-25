package javalib.worldimages;

import java.util.Objects;

/**
 * An enum representing alignment states along the Y axis an overlaid WorldImage
 * can have
 * 
 * @author Eric Kelly
 * @since April 4, 2015
 * 
 */
public enum AlignModeY {
    BOTTOM, TOP, MIDDLE, PINHOLE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    /**
     * 
     * @return The enum associated with the given <code>name</code>
     */
    public static AlignModeY fromString(String name) {
        Objects.requireNonNull(name, "Vertical align mode name cannot be null");
        switch(name.toUpperCase()) {
            case "BOTTOM": return BOTTOM;
            case "TOP": return TOP;
            case "MIDDLE": return MIDDLE;
            case "PINHOLE": return PINHOLE;
            default: throw new IllegalArgumentException("Unknown AlignModeY: " + name);
        }
    }
}
