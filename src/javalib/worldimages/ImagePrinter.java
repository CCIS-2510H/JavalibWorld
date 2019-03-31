package javalib.worldimages;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

class ImageField {
  String name;
  Object value;
  boolean noNewlineBefore;
  ImageField(String name, Object value) { this(name, value, false); }
  ImageField(String name, Object value, boolean noNewlineBefore) {
    this.name = name; this.value = value; this.noNewlineBefore = noNewlineBefore;
  }
}
class FieldsWLItem implements Iterator<ImageField> {
  final Posn pinhole;
  final ImageField[] fields;
  int cur;

  FieldsWLItem(Posn pinhole, ImageField... fields) {
    this.pinhole = pinhole;
    this.fields = fields;
  }

  boolean first() { return cur == 0; }

  @Override
  public boolean hasNext() {
    return cur <= fields.length;
  }

  @Override
  public ImageField next() {
    if (this.cur == 0) {
      cur++;
      if (this.pinhole != null && (this.pinhole.x != 0 || this.pinhole.y != 0))
        return new ImageField("pinhole", this.pinhole);
    }
    return fields[cur++ - 1];
  }

  @Override
  public void remove() {

  }

  void skipToEnd() { this.cur = fields.length + 1; }
}


/**
 * Created by blerner on 3/18/17.
 */
class ImagePrinter {
  private static Map<Integer, String> indentations = new HashMap<Integer, String>();
  private static String indentation(int INDENT) {
    String ans = indentations.get(INDENT);
    if (ans == null) {
      char[] spaces = new char[INDENT];
      Arrays.fill(spaces, ' ');
      ans = new String(spaces);
      indentations.put(INDENT, ans);
    }
    return ans;
  }
  private static boolean isWrapperClass(String name) {
    return name.equals("java.lang.Integer") || name.equals("java.lang.Long")
            || name.equals("java.lang.Short") || name.equals("java.math.BigInteger")
            || name.equals("java.math.BigDecimal") || name.equals("java.lang.Float")
            || name.equals("java.lang.Double") || name.equals("java.lang.Byte")
            || name.equals("java.lang.Boolean") || name.equals("java.lang.Character");
  }
  private static <T> String makePrimitiveStrings(String className, T value) {
    StringBuilder result = new StringBuilder();

    if (className.equals("java.lang.Short")) {
      return result + value.toString() + "S";
    }
    else if (className.equals("java.lang.Long")) {
      return result + value.toString() + "L";
    }
    else if (className.equals("java.lang.Float")) {
      return result + value.toString() + "F";
    }
    else if (className.equals("java.math.BigInteger")) {
      return result + value.toString() + "BigInteger";
    }
    else if (className.equals("java.math.BigDecimal")) {
      return result + value.toString() + "BigDecimal";
    }
    else
			/*
			 * (className.equals("java.lang.Integer") ||
			 * className.equals("java.lang.Double") ||
			 * className.equals("java.lang.Character") ||
			 * className.equals("java.lang.Byte") ||
			 * className.equals("java.lang.Boolean"))
			 */ {
      return result + value.toString();
    }
  }

  static StringBuilder makeString(Object obj, StringBuilder sb, String linePrefix, int indent) {

    int INDENT = 0;
    Stack<Object> worklist = new Stack<Object>();
    int valueCount = 0;

    worklist.push(obj);
    while (!worklist.empty()) {
      valueCount++;
      obj = worklist.peek();
      if (worklist.size() > 100 || valueCount > 1000) {
        if (obj instanceof FieldsWLItem) {
          ((FieldsWLItem) obj).skipToEnd();
        } else {
          // For the constant-size cases, show them anyway
          if (obj == null) {
            sb = sb.append("null");
          }
          else if (obj instanceof java.lang.String) {
            sb = sb.append("\"").append(((String) obj).replace("\\", "\\\\").replace("\"", "\\\""))
                   .append("\"");
          }
          else if (obj instanceof java.util.Random) {
            sb = sb.append("new Random()");
          }
          else if (obj instanceof java.awt.Color) {
            sb = formatColor((Color) obj, sb);
          }
          else if (obj instanceof java.lang.Enum) {
            Enum<?> e = (Enum<?>) obj;
            sb = sb.append(e.getDeclaringClass().getName().replace('$', '.')).append(".")
                   .append(e.name());
          }
          else if (obj.getClass().isPrimitive() || isWrapperClass(obj.getClass().getName())) {
            sb = sb.append(makePrimitiveStrings(obj.getClass().getName(), obj));
          }
          else {
            if (valueCount > 1000) {
              sb = sb.append("<truncated; too many objects to print>");
            } else {
              sb = sb.append("<truncated; objects are too deeply nested to print>");
            }
          }
          worklist.pop();
          continue;
        }
      }
      if (obj == null) {
        sb = sb.append("null");
        worklist.pop();
      }
      else if (obj instanceof java.lang.String) {
        sb = sb.append("\"").append(((String) obj).replace("\\", "\\\\").replace("\"", "\\\""))
               .append("\"");
        worklist.pop();
      }
      else if (obj instanceof java.util.Random) {
        sb = sb.append("new Random()");
        worklist.pop();
      }
      else if (obj instanceof java.awt.Color) {
        sb = formatColor((Color) obj, sb);
        worklist.pop();
      }
      else if (obj instanceof Posn) {
        sb = sb.append(((Posn)obj).coords());
        worklist.pop();
      }
      else if (obj instanceof java.lang.Enum) {
        Enum<?> e = (Enum<?>) obj;
        sb = sb.append(e.getDeclaringClass().getSimpleName().replace('$', '.')).append(".")
               .append(e.name());
        worklist.pop();
      }
      // if the object is of primitive data type
      // or an instance of a wrapper class - use default toString method
      else if (obj.getClass().isPrimitive() || isWrapperClass(obj.getClass().getName())) {
        sb = sb.append(makePrimitiveStrings(obj.getClass().getName(), obj));
        worklist.pop();
        continue;
      }
      else if (obj instanceof WorldImage) {
        WorldImage img = (WorldImage)obj;
        INDENT += indent;
        worklist.pop();
        sb = img.toIndentedStringHelp(sb, worklist);
      }
      else if (obj instanceof FieldsWLItem) {
        FieldsWLItem fieldsWLItem = (FieldsWLItem)obj;
        if (fieldsWLItem.hasNext()) {
          if (!fieldsWLItem.first()) { sb = sb.append(","); }
          ImageField f = fieldsWLItem.next();
          if (f.noNewlineBefore) {
            sb = sb.append(" ");
          } else {
            sb = sb.append("\n").append(linePrefix).append(indentation(INDENT));
          }
          sb = sb.append("this.").append(f.name).append(" = ");
          worklist.push(f.value);
        } else {
          INDENT -= indent;
          sb = sb.append(")"); // no newlines here; keep things terse
          worklist.pop();
        }
      }
      else {
        sb = sb.append("Unknown object: ").append(obj.getClass().getName())
               .append("\n").append(obj.toString());
        worklist.pop();
      }
    }
    return sb;
  }

  private static StringBuilder formatColor(Color obj, StringBuilder sb) {
    Color c = obj;
    sb = sb.append("[r=").append(c.getRed())
            .append(",g=").append(c.getGreen())
            .append(",b=").append(c.getBlue());
    if (c.getAlpha() < 255)
      sb = sb.append(",a=").append(c.getAlpha());
    sb = sb.append("]");
    return sb;
  }

}
