package javalib.worldimages;

public enum FontStyle {
    REGULAR, BOLD, ITALIC, BOLD_ITALIC;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * 
     * @return The enum associated with the given <code>name</code>
     */
    public static FontStyle fromString(String name) {
        return FontStyle.valueOf(name.toUpperCase());
    }
}
