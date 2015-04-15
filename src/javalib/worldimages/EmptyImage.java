package javalib.worldimages;

import java.awt.Color;

public class EmptyImage extends RectangleImageBase {
    public EmptyImage() {
        super(0, 0, "solid", new Color(0, 0, 0, 0));
    }

    public String toIndentedString(String indent) {
        indent += "  ";
        return classNameString(indent, this) + ")";
    }

    public String toString() {
        return className(this) + ")";
    }

    public boolean equals(Object o) {
        return o instanceof EmptyImage;
    }
}
