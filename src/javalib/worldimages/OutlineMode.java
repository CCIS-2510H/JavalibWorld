package javalib.worldimages;

public enum OutlineMode {
    SOLID, OUTLINE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static OutlineMode fromString(String name) {
        return OutlineMode.valueOf(name.toUpperCase());
    }
}
