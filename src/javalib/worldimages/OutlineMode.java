package javalib.worldimages;

/**
 * An enum representing the type of outline/fill mode a WorldImage should have
 * 
 * @author Eric Kelly
 * @since April 4, 2015
 * 
 */
public enum OutlineMode {
    SOLID, OUTLINE;

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
