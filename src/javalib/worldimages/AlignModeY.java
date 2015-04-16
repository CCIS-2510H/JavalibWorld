package javalib.worldimages;

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
        return AlignModeY.valueOf(name.toUpperCase());
    }
}
