package lohr.michael.labyrinth.lanterna;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import lohr.michael.labyrinth.math.Position;
import lohr.michael.labyrinth.math.Size;
import lombok.val;

/**
 * Created by Michael Lohr on 10.01.2016.
 *
 * my custom screen implementation with a lot of shortcuts and usefull methods
 */
public class GameScreen extends Screen {

    public GameScreen(Terminal terminal) {
        super(terminal);
    }

    public GameScreen(Terminal terminal, TerminalSize terminalSize) {
        super(terminal, terminalSize);
    }

    public GameScreen(Terminal terminal, int terminalWidth, int terminalHeight) {
        super(terminal, terminalWidth, terminalHeight);
    }

    public Position getCursorPosition() {
        return Position.fromTerminalPosition(super.getCursorPosition());
    }

    // sets the absoloute position
    public void setCursor(Position position) {
        position.clamp(Position.ZERO, getSize().shrink(Size.ONE).toPosition());
        super.setCursorPosition(position);
    }

    public void setCursor(int x, int y) {
        setCursor(new Position(x, y));
    }

    // moves the cursor relative to its current position
    public void moveCursor(Position position) {
        setCursor(getCursorPosition().add(position));
    }

    public void moveCursor(int x, int y) {
        setCursor(new Position(x, y));
    }

    public void hideCursor() {
        super.setCursorPosition(null);
    }

    public void putString(String string) {
        this.putString(string, null);
    }

    public void putString(String string, TextStyle style) {
        val pos = getCursorPosition();

        if (style == null)
            style = new TextStyle();
        super.putString(pos.getX(), pos.getY(), string, style.getForeground(), style.getBackground(), style.getEffects());
    }

    public void putCharacter(char c) {
        this.putString(c + "");
    }

    public void putCharacter(char c, TextStyle style) {
        this.putString(c + "", style);
    }

    public Size getSize() {
        return Size.fromTerminalSize(getTerminalSize());
    }

    @Override
    public String toString() {
        return "GameScreen{" +
                "cursorPosition: " + getCursorPosition() +
                "size: " + getSize() +
                '}';
    }
}
