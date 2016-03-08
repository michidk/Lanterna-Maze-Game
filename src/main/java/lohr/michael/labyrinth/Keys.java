package lohr.michael.labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.lanterna.TextStyle;

/**
 * Created by Michael Lohr on 19.01.2016.
 *
 * a enum containing the key types and their color.
 * you can have multiple keys with different colors, but i never implemented colored doors (which can only be opened by a key with the same color)
 */
public enum Keys {
    GOLD,
    RED,
    BLUE;

    // returns the color of the keytpye
    public TextStyle getColor() {
        switch (this) {
            case RED:
                return new TextStyle(Terminal.Color.RED);
            case BLUE:
                return new TextStyle(Terminal.Color.CYAN);
            default:
                return new TextStyle(Terminal.Color.YELLOW);
        }
    }
}
