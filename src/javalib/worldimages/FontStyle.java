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
        switch(name.toUpperCase()) {
            case "REGULAR": return REGULAR;
            case "BOLD": return BOLD;
            case "ITALIC": return ITALIC;
            case "BOLD_ITALIC": return BOLD_ITALIC;
            default: throw new IllegalArgumentException("Unknown FontStyle: " + name);
        }
    }
}
