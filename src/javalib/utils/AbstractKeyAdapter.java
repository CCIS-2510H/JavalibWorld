package javalib.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

import static java.awt.event.KeyEvent.*;

/**
 * Created by blerner on 2/13/17.
 */
public class AbstractKeyAdapter extends KeyAdapter {
  public interface Consumer<T> {
    void apply(T data);
  }
  protected Consumer<String> onKey;
  protected Consumer<String> onReleased;

  public AbstractKeyAdapter(Consumer<String> onKey, Consumer<String> onReleased) {
    this.onKey = onKey;
    this.onReleased = onReleased;
  }

  /**
   * <p>
   * Handle the key typed event from the canvas.
   * </p>
   * <p>
   * This is where we get the letter keys.
   * </p>
   *
   * @param e
   *            the <code>KeyEvent</code> that caused the callback
   */
  public void keyTyped(KeyEvent e) {
      //this.onKey.apply("" + e.getKeyChar());
  }

  /**
   * <p>
   * Handle the key pressed event from the canvas.
   * </p>
   * <p>
   * This is where we get the arrow keys.
   * </p>
   *
   * @param e
   *            the <code>KeyEvent</code> that caused the callback
   */
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() != VK_UNDEFINED)
      this.onKey.apply(this.getNamedKey(e));
  }

  /**
   * <p>
   * Handle the key released event from the canvas.
   * </p>
   * <p>
   * This is where we get the arrow keys.
   * </p>
   *
   * @param e
   *            the <code>KeyEvent</code> that caused the callback
   */
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() != VK_UNDEFINED)
      this.onReleased.apply(this.getNamedKey(e));
  }

  String getNamedKey(KeyEvent e) {
    return getLocationPrefix(e) + getKeyName(e);
  }
  String getKeyName(KeyEvent e) {
    Locale.setDefault(Locale.ROOT);
    switch (e.getKeyChar()) {
      case CHAR_UNDEFINED:
        return getKeyText(e.getKeyCode()).toLowerCase().replace(' ', '-');
      case VK_TAB:
        return "tab";
      case VK_ENTER:
        return "enter";
      case VK_DELETE:
        return "delete";
      case VK_ESCAPE:
        return "escape";
      case VK_BACK_SPACE:
        return "backspace";
      default:
        return "" + e.getKeyChar();
    }
  }
  String getLocationPrefix(KeyEvent e) {
    switch (e.getKeyLocation()) {
      case KEY_LOCATION_NUMPAD: return "numpad-";
      case KEY_LOCATION_RIGHT: return "right-";
      default: return "";
    }
  }

  // Copied from KeyEvent.getKeyText, so as to deliberately de-localize the strings
  // On Mac, these are undesirably localized to unicode characters
  public static String getKeyText(int keyCode) {
    if (keyCode >= VK_0 && keyCode <= VK_9 || keyCode >= VK_A && keyCode <= VK_Z) {
      return String.valueOf((char)keyCode);
    }

    switch(keyCode) {
      case VK_ENTER: return "Enter";
      case VK_BACK_SPACE: return "Backspace";
      case VK_TAB: return "Tab";
      case VK_CANCEL: return "Cancel";
      case VK_CLEAR: return "Clear";
      case VK_COMPOSE: return "Compose";
      case VK_PAUSE: return "Pause";
      case VK_CAPS_LOCK: return "Caps Lock";
      case VK_ESCAPE: return "Escape";
      case VK_SPACE: return "Space";
      case VK_PAGE_UP: return "Page Up";
      case VK_PAGE_DOWN: return "Page Down";
      case VK_END: return "End";
      case VK_HOME: return "Home";
      case VK_LEFT: return "Left";
      case VK_UP: return "Up";
      case VK_RIGHT: return "Right";
      case VK_DOWN: return "Down";
      case VK_BEGIN: return "Begin";

      // modifiers
      case VK_SHIFT: return "Shift";
      case VK_CONTROL: return "Control";
      case VK_ALT: return "Alt";
      case VK_META: return "Meta";
      case VK_ALT_GRAPH: return "Alt Graph";

      // punctuation
      case VK_COMMA: return "Comma";
      case VK_PERIOD: return "Period";
      case VK_SLASH: return "Slash";
      case VK_SEMICOLON: return "Semicolon";
      case VK_EQUALS: return "Equals";
      case VK_OPEN_BRACKET: return "Open Bracket";
      case VK_BACK_SLASH: return "Back Slash";
      case VK_CLOSE_BRACKET: return "Close Bracket";

      // numpad numeric keys handled below
      case VK_MULTIPLY: return "NumPad *";
      case VK_ADD: return "NumPad +";
      case VK_SEPARATOR: return "NumPad ,";
      case VK_SUBTRACT: return "NumPad -";
      case VK_DECIMAL: return "NumPad .";
      case VK_DIVIDE: return "NumPad /";
      case VK_DELETE: return "Delete";
      case VK_NUM_LOCK: return "Num Lock";
      case VK_SCROLL_LOCK: return "Scroll Lock";

      case VK_WINDOWS: return "Windows";
      case VK_CONTEXT_MENU: return "Context Menu";

      case VK_F1: return "F1";
      case VK_F2: return "F2";
      case VK_F3: return "F3";
      case VK_F4: return "F4";
      case VK_F5: return "F5";
      case VK_F6: return "F6";
      case VK_F7: return "F7";
      case VK_F8: return "F8";
      case VK_F9: return "F9";
      case VK_F10: return "F10";
      case VK_F11: return "F11";
      case VK_F12: return "F12";
      case VK_F13: return "F13";
      case VK_F14: return "F14";
      case VK_F15: return "F15";
      case VK_F16: return "F16";
      case VK_F17: return "F17";
      case VK_F18: return "F18";
      case VK_F19: return "F19";
      case VK_F20: return "F20";
      case VK_F21: return "F21";
      case VK_F22: return "F22";
      case VK_F23: return "F23";
      case VK_F24: return "F24";

      case VK_PRINTSCREEN: return "Print Screen";
      case VK_INSERT: return "Insert";
      case VK_HELP: return "Help";
      case VK_BACK_QUOTE: return "Back Quote";
      case VK_QUOTE: return "Quote";

      case VK_KP_UP: return "Up";
      case VK_KP_DOWN: return "Down";
      case VK_KP_LEFT: return "Left";
      case VK_KP_RIGHT: return "Right";

      case VK_DEAD_GRAVE: return "Dead Grave";
      case VK_DEAD_ACUTE: return "Dead Acute";
      case VK_DEAD_CIRCUMFLEX: return "Dead Circumflex";
      case VK_DEAD_TILDE: return "Dead Tilde";
      case VK_DEAD_MACRON: return "Dead Macron";
      case VK_DEAD_BREVE: return "Dead Breve";
      case VK_DEAD_ABOVEDOT: return "Dead Above Dot";
      case VK_DEAD_DIAERESIS: return "Dead Diaeresis";
      case VK_DEAD_ABOVERING: return "Dead Above Ring";
      case VK_DEAD_DOUBLEACUTE: return "Dead Double Acute";
      case VK_DEAD_CARON: return "Dead Caron";
      case VK_DEAD_CEDILLA: return "Dead Cedilla";
      case VK_DEAD_OGONEK: return "Dead Ogonek";
      case VK_DEAD_IOTA: return "Dead Iota";
      case VK_DEAD_VOICED_SOUND: return "Dead Voiced Sound";
      case VK_DEAD_SEMIVOICED_SOUND: return "Dead Semivoiced Sound";

      case VK_AMPERSAND: return "Ampersand";
      case VK_ASTERISK: return "Asterisk";
      case VK_QUOTEDBL: return "Double Quote";
      case VK_LESS: return "Less";
      case VK_GREATER: return "Greater";
      case VK_BRACELEFT: return "Left Brace";
      case VK_BRACERIGHT: return "Right Brace";
      case VK_AT: return "At";
      case VK_COLON: return "Colon";
      case VK_CIRCUMFLEX: return "Circumflex";
      case VK_DOLLAR: return "Dollar";
      case VK_EURO_SIGN: return "Euro";
      case VK_EXCLAMATION_MARK: return "Exclamation Mark";
      case VK_INVERTED_EXCLAMATION_MARK: return "Inverted Exclamation Mark";
      case VK_LEFT_PARENTHESIS: return "Left Parenthesis";
      case VK_NUMBER_SIGN: return "Number Sign";
      case VK_MINUS: return "Minus";
      case VK_PLUS: return "Plus";
      case VK_RIGHT_PARENTHESIS: return "Right Parenthesis";
      case VK_UNDERSCORE: return "Underscore";

      case VK_FINAL: return "Final";
      case VK_CONVERT: return "Convert";
      case VK_NONCONVERT: return "No Convert";
      case VK_ACCEPT: return "Accept";
      case VK_MODECHANGE: return "Mode Change";
      case VK_KANA: return "Kana";
      case VK_KANJI: return "Kanji";
      case VK_ALPHANUMERIC: return "Alphanumeric";
      case VK_KATAKANA: return "Katakana";
      case VK_HIRAGANA: return "Hiragana";
      case VK_FULL_WIDTH: return "Full-Width";
      case VK_HALF_WIDTH: return "Half-Width";
      case VK_ROMAN_CHARACTERS: return "Roman Characters";
      case VK_ALL_CANDIDATES: return "All Candidates";
      case VK_PREVIOUS_CANDIDATE: return "Previous Candidate";
      case VK_CODE_INPUT: return "Code Input";
      case VK_JAPANESE_KATAKANA: return "Japanese Katakana";
      case VK_JAPANESE_HIRAGANA: return "Japanese Hiragana";
      case VK_JAPANESE_ROMAN: return "Japanese Roman";
      case VK_KANA_LOCK: return "Kana Lock";
      case VK_INPUT_METHOD_ON_OFF: return "Input Method On/Off";

      case VK_AGAIN: return "Again";
      case VK_UNDO: return "Undo";
      case VK_COPY: return "Copy";
      case VK_PASTE: return "Paste";
      case VK_CUT: return "Cut";
      case VK_FIND: return "Find";
      case VK_PROPS: return "Props";
      case VK_STOP: return "Stop";
    }

    if (keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9) {
      String numpad = "NumPad";
      char c = (char)(keyCode - VK_NUMPAD0 + '0');
      return numpad + "-" + c;
    }

    if ((keyCode & 0x01000000) != 0) {
      return String.valueOf((char)(keyCode ^ 0x01000000 ));
    }
    String unknown = "Unknown";
    return unknown + " keyCode: 0x" + Integer.toString(keyCode, 16);
  }
}
