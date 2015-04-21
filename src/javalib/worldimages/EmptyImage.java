package javalib.worldimages;

import java.awt.Color;

/**
 * This class represents an empty image
 * 
 * @author eric
 * 
 */
public class EmptyImage extends RectangleImageBase {

    /**
     * Empty image constructor
     */
    public EmptyImage() {
        super(0, 0, "solid", new Color(0, 0, 0, 0));
    }

    @Override
    public String toIndentedString(String indent) {
        indent += "  ";
        return classNameString(indent, this) + ")";
    }

    @Override
    public String toString() {
        return className(this) + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EmptyImage;
    }
}
