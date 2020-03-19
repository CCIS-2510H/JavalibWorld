package javalib.worldimages;

public enum LengthMode {
  /**
   * Polygons constructed with this mode interpret their
   * length as the radius of the surrounding circle
   */
  RADIUS,
  /**
   * Polygons constructed with this mode interpret their
   * length as the length of a single side
   */
  SIDE;


  @Override
  public String toString() {
    return this.name().toLowerCase();
  }

  /**
   *
   * @return The enum associated with the given <code>name</code>
   */
  public static LengthMode fromString(String name) {
    switch(name.toUpperCase()) {
      case "RADIUS": return RADIUS;
      case "SIDE": return SIDE;
      default: throw new IllegalArgumentException("Unknown LengthMode: " + name);
    }
  }
}
