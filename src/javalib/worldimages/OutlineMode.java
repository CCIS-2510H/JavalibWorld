package javalib.worldimages;

/**
 * An enum representing the type of outline/fill mode a WorldImage should have
 * 
 * @author Eric Kelly
 * @since April 4, 2015
 * 
 */
public enum OutlineMode {
    /**
     * Images drawn with this mode will be fully filled
     */
    SOLID,
    /**
     * Images drawn with this mode will be drawn solely as an outline
     */
    OUTLINE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * 
     * @return The enum associated with the given <code>name</code>
     */
    public static OutlineMode fromString(String name) {
        return OutlineMode.valueOf(name.toUpperCase());
    }
}
