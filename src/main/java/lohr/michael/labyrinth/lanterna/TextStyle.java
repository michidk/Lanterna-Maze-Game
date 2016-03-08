package lohr.michael.labyrinth.lanterna;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Michael Lohr on 10.01.2016.
 *
 * stores the Terminal Style
 */
@Data
@NoArgsConstructor
//@XArgsConstructor - not available in the main lombok fork
@AllArgsConstructor
public class TextStyle {

    private Terminal.Color foreground = Terminal.Color.WHITE;
    private Terminal.Color background = Terminal.Color.BLACK;
    private ScreenCharacterStyle[] effects = new ScreenCharacterStyle[]{};

    public TextStyle(Terminal.Color foreground) {
        this.foreground = foreground;
    }

    public TextStyle(ScreenCharacterStyle[] effects) {
        this.effects = effects;
    }

    public TextStyle(ScreenCharacterStyle style) {
        this.effects = new ScreenCharacterStyle[]{style};
    }

    public TextStyle(Terminal.Color color, ScreenCharacterStyle style) {
        this.foreground = color;
        this.effects = new ScreenCharacterStyle[]{style};
    }
}
